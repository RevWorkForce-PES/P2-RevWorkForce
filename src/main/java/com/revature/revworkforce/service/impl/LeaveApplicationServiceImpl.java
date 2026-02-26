package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.exception.InsufficientLeaveBalanceException;
import com.revature.revworkforce.exception.LeaveOverlapException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveApplication;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveApplicationRepository;
import com.revature.revworkforce.repository.LeaveBalanceRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public LeaveApplicationServiceImpl(LeaveApplicationRepository leaveApplicationRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    @Override
    public LeaveApplication applyForLeave(String employeeId, Long leaveTypeId, LocalDate startDate, LocalDate endDate,
            String reason) {

        // 1. Basic Validations
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        // 2. Fetch Entities
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveType", "id", leaveTypeId.toString()));

        // 3. Calculate Days
        // NOTE: In a real system, we'd skip weekends and holidays using a Holiday
        // schedule
        int requestedDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 4. Overlap Check
        List<LeaveStatus> excludedStatuses = Arrays.asList(LeaveStatus.REJECTED, LeaveStatus.CANCELLED);
        List<LeaveApplication> overlappingLeaves = leaveApplicationRepository.findOverlappingLeaves(
                employeeId, startDate, endDate, excludedStatuses);

//        if (!overlappingLeaves.isEmpty()) {
//            LeaveApplication firstOverlap = overlappingLeaves.get(0);
//            throw new LeaveOverlapException(
//                    startDate,
//                    endDate,
//                    firstOverlap.getStartDate(),
//                    firstOverlap.getEndDate());
//        }
//
//        // 5. Balance Check
//        int currentYear = startDate.getYear();
//        Optional<LeaveBalance> optBalance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(
//                employee, leaveType, currentYear);
//
//        if (optBalance.isPresent()) {
//            LeaveBalance balance = optBalance.get();
//            if (balance.getBalance() < requestedDays) {
//                // Throw our specific custom exception!
//                throw new InsufficientLeaveBalanceException(
//                        leaveType.getLeaveCode(),
//                        requestedDays,
//                        balance.getBalance());
//            }
//
//            // Deduct balance
//            balance.setUsed(balance.getUsed() + requestedDays);
//            balance.setBalance(balance.getBalance() - requestedDays);
//            leaveBalanceRepository.save(balance);
//
//        } else {
//            // Let's assume they don't have this leave type allocated this year at all.
//            throw new InsufficientLeaveBalanceException(
//                    leaveType.getLeaveCode(),
//                    requestedDays,
//                    0);
//        }

        // 6. Save and Return Application
        LeaveApplication application = new LeaveApplication(
                employee,
                leaveType,
                startDate,
                endDate,
                requestedDays,
                reason);

        return leaveApplicationRepository.save(application);
    }
}
