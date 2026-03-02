package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.exception.*;
import com.revature.revworkforce.model.*;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.util.DateUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayService holidayService;
    private final NotificationService notificationService;
    @Value("${app.leave.max-continuous-days}")
    private int maxContinuousLeaveDays;
    public LeaveServiceImpl(
            LeaveApplicationRepository leaveApplicationRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            LeaveTypeRepository leaveTypeRepository,
            EmployeeRepository employeeRepository,
            HolidayService holidayService,
            NotificationService notificationService
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.holidayService = holidayService;
        this.notificationService = notificationService;
    }

    // =========================================================
    // APPLY LEAVE
    // =========================================================

    @Override
    public LeaveApplication applyLeave(LeaveApplicationDTO dto, String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", employeeId));

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ValidationException("End date cannot be before start date");
        }

        validateLeaveOverlap(employee, dto.getStartDate(), dto.getEndDate());

        int workingDays = calculateWorkingDays(dto.getStartDate(), dto.getEndDate());

        LeaveType leaveType = leaveTypeRepository.findByLeaveCode(dto.getLeaveType())
                .orElseThrow(() ->
                        new ResourceNotFoundException("LeaveType", "leaveCode", dto.getLeaveType()));

        LeaveBalance balance =
                getOrCreateLeaveBalance(employee, leaveType, dto.getStartDate().getYear());

        validateLeaveBalance(balance, workingDays);

        LeaveApplication application = new LeaveApplication(
                employee,
                leaveType,
                dto.getStartDate(),
                dto.getEndDate(),
                workingDays,
                dto.getReason()
        );

        return leaveApplicationRepository.save(application);
    }

    // =========================================================
    // CANCEL LEAVE
    // =========================================================

    @Override
    public void cancelLeave(Long applicationId, String employeeId) {

        LeaveApplication application = getLeaveById(applicationId);

        if (!application.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You cannot cancel this leave");
        }

        if (application.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Leave",
                    application.getStatus().name(),
                    "CANCELLED"
            );
        }

        application.setStatus(LeaveStatus.CANCELLED);
        leaveApplicationRepository.save(application);
    }

    // =========================================================
    // GET LEAVE
    // =========================================================

    @Override
    public LeaveApplication getLeaveById(Long applicationId) {
        return leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("LeaveApplication", "applicationId", applicationId));
    }

    @Override
    public List<LeaveApplication> getEmployeeLeaveHistory(String employeeId) {
        return leaveApplicationRepository
                .findByEmployee_EmployeeIdOrderByAppliedOnDesc(employeeId);
    }

    // =========================================================
    // APPROVE
    // =========================================================
    @Override
    public LeaveApplication approveLeave(Long applicationId,
                                         String approverId,
                                         String comments) {

        LeaveApplication application = getLeaveById(applicationId);

        if (application.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Leave",
                    application.getStatus().name(),
                    "APPROVED"
            );
        }

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", approverId));

        boolean isAdmin = approver.hasRole("ADMIN");

        if (!isAdmin) {
            if (application.getEmployee().getManager() == null ||
                !application.getEmployee().getManager().getEmployeeId()
                        .equals(approver.getEmployeeId())) {

                throw new UnauthorizedException("Not authorized to approve this leave");
            }
        }

        LeaveBalance balance = getOrCreateLeaveBalance(
                application.getEmployee(),
                application.getLeaveType(),
                application.getStartDate().getYear()
        );

        validateLeaveBalance(balance, application.getTotalDays());

        balance.setUsed(balance.getUsed() + application.getTotalDays());
        balance.setBalance(balance.getBalance() - application.getTotalDays());

        application.setStatus(LeaveStatus.APPROVED);
        application.setApprovedBy(approver);
        application.setApprovedOn(LocalDateTime.now());
        application.setComments(comments);

        leaveBalanceRepository.save(balance);
        LeaveApplication saved = leaveApplicationRepository.save(application);

        // 🔔 CREATE NOTIFICATION
        notificationService.createNotification(
                application.getEmployee(),
                NotificationType.LEAVE,
                "Leave Approved",
                "Your leave from " +
                        application.getStartDate() +
                        " to " +
                        application.getEndDate() +
                        " has been approved.",
                NotificationPriority.NORMAL
        );

        return saved;
    }

    // =========================================================
    // REJECT
    // =========================================================

    @Override
    public LeaveApplication rejectLeave(Long applicationId,
                                        String approverId,
                                        String rejectionReason) {

        LeaveApplication application = getLeaveById(applicationId);

        if (application.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Leave",
                    application.getStatus().name(),
                    "REJECTED"
            );
        }

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", approverId));

        boolean isAdmin = approver.hasRole("ADMIN");

        if (!isAdmin) {
            if (application.getEmployee().getManager() == null ||
                !application.getEmployee().getManager().getEmployeeId()
                        .equals(approver.getEmployeeId())) {

                throw new UnauthorizedException("Not authorized to reject this leave");
            }
        }

        application.setStatus(LeaveStatus.REJECTED);
        application.setApprovedBy(approver);
        application.setApprovedOn(LocalDateTime.now());
        application.setRejectionReason(rejectionReason);

        LeaveApplication saved = leaveApplicationRepository.save(application);

     // 🔔 CREATE NOTIFICATION
     notificationService.createNotification(
             application.getEmployee(),
             NotificationType.LEAVE,
             "Leave Rejected",
             "Your leave from " +
                     application.getStartDate() +
                     " to " +
                     application.getEndDate() +
                     " has been rejected. Reason: " +
                     rejectionReason,
             NotificationPriority.NORMAL
     );

     return saved;
    }
    // =========================================================
    // BALANCES
    // =========================================================

    @Override
    public List<LeaveBalanceDTO> getLeaveBalances(String employeeId, Integer year) {

        return leaveBalanceRepository
                .findByEmployee_EmployeeIdAndYear(employeeId, year)
                .stream()
                .map(lb -> {
                    LeaveBalanceDTO dto = new LeaveBalanceDTO();

                    dto.setLeaveType(lb.getLeaveType().getLeaveCode());

                    // ✅ ADD THIS SWITCH HERE (INSIDE METHOD)
                    switch (lb.getLeaveType().getLeaveCode()) {
                        case "CL":
                            dto.setLeaveTypeName("Casual Leave");
                            break;
                        case "SL":
                            dto.setLeaveTypeName("Sick Leave");
                            break;
                        case "PL":
                            dto.setLeaveTypeName("Privilege Leave");
                            break;
                        case "PRIV":
                            dto.setLeaveTypeName("Privilege Leave (Extended)");
                            break;
                        default:
                            dto.setLeaveTypeName(lb.getLeaveType().getLeaveCode());
                    }

                    dto.setTotalAllocated(lb.getTotalAllocated());
                    dto.setUsedLeaves(lb.getUsed());
                    dto.setRemainingBalance(lb.getBalance());
                    dto.setYear(lb.getYear());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void initializeLeaveBalances(Employee employee) {

        List<LeaveType> types = leaveTypeRepository.findAll();

        for (LeaveType type : types) {
            getOrCreateLeaveBalance(employee, type, LocalDate.now().getYear());
        }
    }

    @Override
    public LeaveBalance getOrCreateLeaveBalance(Employee employee,
                                                LeaveType type,
                                                Integer year) {

        return leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(employee, type, year)
                .orElseGet(() -> {

                    LeaveBalance balance = new LeaveBalance(
                            employee,
                            type,
                            year,
                            type.getDefaultDays(),
                            0
                    );

                    return leaveBalanceRepository.save(balance);
                });
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    @Override
    public void validateLeaveOverlap(Employee employee,
                                     LocalDate startDate,
                                     LocalDate endDate) {

        List<LeaveApplication> overlaps =
                leaveApplicationRepository.findOverlappingLeaves(
                        employee.getEmployeeId(),
                        startDate,
                        endDate,
                        List.of(LeaveStatus.REJECTED, LeaveStatus.CANCELLED)
                );

        if (!overlaps.isEmpty()) {
            LeaveApplication existing = overlaps.get(0);

            throw new LeaveOverlapException(
                    startDate,
                    endDate,
                    existing.getStartDate(),
                    existing.getEndDate()
            );
        }
    }

    @Override
    public int calculateWorkingDays(LocalDate startDate,
                                    LocalDate endDate) {

        Set<LocalDate> holidays =
        		holidayService.getHolidaysInRange(startDate, endDate);

        return DateUtil.calculateWorkingDays(startDate, endDate, holidays);
    }

    @Override
    public void validateContinuousLeaveLimit(Employee employee,
                                             LocalDate startDate,
                                             LocalDate endDate) {

        int days = calculateWorkingDays(startDate, endDate);

        if (days > maxContinuousLeaveDays) {
            throw new ValidationException(
                "Continuous leave cannot exceed " + maxContinuousLeaveDays + " working days"
            );
        }
    }

    @Override
    public void validateLeaveBalance(LeaveBalance leaveBalance,
                                     int requestedDays) {

        if (leaveBalance.getBalance() < requestedDays) {
            throw new InsufficientLeaveBalanceException(
                    leaveBalance.getLeaveType().getLeaveCode(),
                    requestedDays,
                    leaveBalance.getBalance()
            );
        }
    }

    @Override
    public List<LeaveApplication> getPendingLeavesForManager(String managerId) {
        return leaveApplicationRepository
                .findPendingLeavesByManagerId(managerId, LeaveStatus.PENDING);
    }

    @Override
    public List<LeaveApplication> getTeamLeaves(String managerId) {
        return leaveApplicationRepository
                .findTeamLeavesByManagerId(managerId);
    }
    
}