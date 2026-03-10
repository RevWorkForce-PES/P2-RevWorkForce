package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AnnouncementRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.impl.AnnouncementServiceImpl;
import com.revature.revworkforce.service.AuditService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.revature.revworkforce.security.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // CREATE ANNOUNCEMENT
    // =========================

    @Test
    void createAnnouncement_Success() {

        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("System Maintenance");
        dto.setMessage("Server restart tonight");
        dto.setPriority(Priority.HIGH);

        Employee emp = new Employee();
        emp.setEmployeeId("EMP001");

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(emp));

        Announcement saved = new Announcement();
        saved.setAnnouncementId(1L);
        saved.setTitle("System Maintenance");
        saved.setCreatedBy(emp);

        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(saved);

        AnnouncementDTO result =
                announcementService.createAnnouncement(dto, "EMP001");

        assertNotNull(result);
        verify(notificationService).createAnnouncementForAll(any());
        verify(auditService).createAuditLog(
                anyString(),
                eq("ANNOUNCEMENT_CREATED"),
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                any());
    }

    // =========================
    // EMPLOYEE NOT FOUND
    // =========================

    @Test
    void createAnnouncement_EmployeeNotFound() {

        AnnouncementDTO dto = new AnnouncementDTO();

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> announcementService.createAnnouncement(dto, "EMP001"));
    }

    // =========================
    // GET ANNOUNCEMENT
    // =========================

    @Test
    void getAnnouncementById_Success() {

        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(1L);
        announcement.setTitle("Test");

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        AnnouncementDTO result =
                announcementService.getAnnouncementById(1L);

        assertEquals("Test", result.getTitle());
    }

    // =========================
    // UPDATE ANNOUNCEMENT
    // =========================

    @Test
    void updateAnnouncement_Success() {

        Announcement existing = new Announcement();
        existing.setAnnouncementId(1L);
        
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Updated");

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(announcementRepository.save(any()))
                .thenReturn(existing);

        try (MockedStatic<SecurityUtils> security =
                mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername)
                    .thenReturn("ADMIN");

            announcementService.updateAnnouncement(1L, dto);

            verify(announcementRepository).save(any());
            verify(auditService).createAuditLog(
                    anyString(),
                    eq("ANNOUNCEMENT_UPDATED"),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any());
        }
    }

    // =========================
    // GET ACTIVE ANNOUNCEMENTS
    // =========================

    @Test
    void getActiveAnnouncements_ReturnList() {

        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(1L);

        when(announcementRepository.findActiveAnnouncements(any(), any()))
                .thenReturn(List.of(announcement));

        List<AnnouncementDTO> result =
                announcementService.getActiveAnnouncements();

        assertEquals(1, result.size());
    }

    // =========================
    // DEACTIVATE ANNOUNCEMENT
    // =========================

    @Test
    void deactivateAnnouncement_Success() {

        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(1L);
        announcement.setIsActive('Y');

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        try (MockedStatic<SecurityUtils> security =
                mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername)
                    .thenReturn("ADMIN");

            announcementService.deactivateAnnouncement(1L);

            verify(announcementRepository).save(announcement);
        }
    }

    // =========================
    // DELETE ANNOUNCEMENT
    // =========================

    @Test
    void deleteAnnouncement_Success() {

        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(1L);

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        try (MockedStatic<SecurityUtils> security =
                mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername)
                    .thenReturn("ADMIN");

            announcementService.deleteAnnouncement(1L);

            verify(announcementRepository).delete(announcement);
        }
    }

    // =========================
    // SCHEDULED TASK
    // =========================

    @Test
    void deactivateExpiredAnnouncements_ShouldCallRepository() {

        announcementService.deactivateExpiredAnnouncements();

        verify(announcementRepository)
                .deactivateExpired(any(LocalDate.class));
    }
}