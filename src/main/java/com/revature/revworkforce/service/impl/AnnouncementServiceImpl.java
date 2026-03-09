package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AnnouncementRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository,
            EmployeeRepository employeeRepository,
            NotificationService notificationService,
            AuditService auditService) {
        this.announcementRepository = announcementRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Override
    public AnnouncementDTO createAnnouncement(AnnouncementDTO dto, String createdBy) {
        Employee creator = employeeRepository.findById(createdBy)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + createdBy));

        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setMessage(dto.getMessage());
        announcement.setAnnouncementType(dto.getType() != null ? dto.getType() : "INFO");
        announcement.setPriority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM);
        announcement.setPublishDate(dto.getPublishDate() != null ? dto.getPublishDate() : LocalDate.now());
        announcement.setExpiryDate(dto.getExpiryDate());
        announcement.setIsActive('Y');
        // createdAt and updatedAt are set by JPA @PrePersist
        announcement.setCreatedBy(creator);

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Broadcast notification to all employees
        notificationService.createAnnouncementForAll(savedAnnouncement);

        // Audit Logging
        auditService.createAuditLog(
                createdBy,
                "ANNOUNCEMENT_CREATED",
                "ANNOUNCEMENTS",
                String.valueOf(savedAnnouncement.getAnnouncementId()),
                null,
                savedAnnouncement.getTitle(),
                null,
                null);

        return convertToDTO(savedAnnouncement);
    }

    @Override
    public AnnouncementDTO updateAnnouncement(Long announcementId, AnnouncementDTO dto) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));

        announcement.setTitle(dto.getTitle());
        announcement.setMessage(dto.getMessage());
        if (dto.getType() != null) {
            announcement.setAnnouncementType(dto.getType());
        }
        if (dto.getPriority() != null) {
            announcement.setPriority(dto.getPriority());
        }
        if (dto.getPublishDate() != null) {
            announcement.setPublishDate(dto.getPublishDate());
        }
        announcement.setExpiryDate(dto.getExpiryDate());
        if (dto.getIsActive() != null) {
            announcement.setIsActive(dto.getIsActive());
        }

        Announcement updatedAnnouncement = announcementRepository.save(announcement);

        // Audit Logging
        auditService.createAuditLog(
                SecurityUtils.getCurrentUsername(),
                "ANNOUNCEMENT_UPDATED",
                "ANNOUNCEMENTS",
                announcementId.toString(),
                null,
                updatedAnnouncement.getTitle(),
                null,
                null);

        return convertToDTO(updatedAnnouncement);
    }

    @Override
    public AnnouncementDTO getAnnouncementById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        return convertToDTO(announcement);
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllWithCreator();
    }
    

    @Override
    public List<AnnouncementDTO> getActiveAnnouncements() {
        List<Announcement> activeAnnouncements = announcementRepository.findActiveAnnouncements('Y', LocalDate.now());
        return activeAnnouncements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementDTO> getRecentAnnouncements(int limit) {
        return getActiveAnnouncements().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        announcement.setIsActive('N');
        announcementRepository.save(announcement);

        // Audit Logging
        auditService.createAuditLog(
                SecurityUtils.getCurrentUsername(),
                "ANNOUNCEMENT_DEACTIVATED",
                "ANNOUNCEMENTS",
                announcementId.toString(),
                "Y",
                "N",
                null,
                null);
    }

    @Override
    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        announcementRepository.delete(announcement);

        // Audit Logging
        auditService.createAuditLog(
                SecurityUtils.getCurrentUsername(),
                "ANNOUNCEMENT_DELETED",
                "ANNOUNCEMENTS",
                announcementId.toString(),
                null,
                "Hard deleted announcement: " + announcement.getTitle(),
                null,
                null);
    }
    

    @Override
    @Scheduled(cron = "0 0 1 * * ?") // Runs daily at 1:00 AM
    public void deactivateExpiredAnnouncements() {
        announcementRepository.deactivateExpired(LocalDate.now());
    }

    @Override
    public AnnouncementDTO convertToDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getAnnouncementId());
        dto.setTitle(announcement.getTitle());
        dto.setMessage(announcement.getMessage());
        dto.setType(announcement.getAnnouncementType());
        dto.setPriority(announcement.getPriority());
        dto.setPublishDate(announcement.getPublishDate());
        dto.setExpiryDate(announcement.getExpiryDate());
        dto.setIsActive(announcement.getIsActive());
        dto.setCreatedAt(announcement.getCreatedAt());
        dto.setUpdatedAt(announcement.getUpdatedAt());

        if (announcement.getCreatedBy() != null) {
            dto.setCreatedBy(announcement.getCreatedBy().getEmployeeId());
            dto.setCreatorName(announcement.getCreatedBy().getFullName());
        }

        return dto;
    }

    @Override
    public List<AnnouncementDTO> getAllAnnouncementsAsDTO() {
        return getAllAnnouncements().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
