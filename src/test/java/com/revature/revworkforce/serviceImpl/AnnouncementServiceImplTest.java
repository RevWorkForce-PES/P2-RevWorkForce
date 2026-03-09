package com.revature.revworkforce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.revature.revworkforce.dto.AnnouncementDTO;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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

    private Employee employee;
    private Announcement announcement;
    private AnnouncementDTO dto;

    @BeforeEach
    void setup() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("Aishwarya");

        announcement = new Announcement();
        announcement.setAnnouncementId(1L);
        announcement.setTitle("Test Announcement");
        announcement.setMessage("Test Message");
        announcement.setAnnouncementType("INFO");
        announcement.setExpiryDate(LocalDate.now().plusDays(10));
        announcement.setIsActive('Y');
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setCreatedBy(employee);

        dto = new AnnouncementDTO();
        dto.setTitle("Test Announcement");
        dto.setContent("Test Message");
        dto.setType("INFO");
        dto.setExpiryDate(LocalDate.now().plusDays(10));
    }

    // ====================================================
    // CREATE ANNOUNCEMENT
    // ====================================================
    @Test
    void convertToDTO_Success() {

        AnnouncementDTO result = announcementService.convertToDTO(announcement);

        assertNotNull(result);
        assertEquals(announcement.getTitle(), result.getTitle());
        assertEquals(employee.getEmployeeId(), result.getCreatedBy());
        assertEquals(employee.getFullName(), result.getCreatorName());
    }
    @Test
    void createAnnouncement_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(announcement);

        AnnouncementDTO result = announcementService.createAnnouncement(dto, "EMP001");

        assertNotNull(result);
        assertEquals("Test Announcement", result.getTitle());

        verify(notificationService).createAnnouncementForAll(any());
        verify(auditService).createAuditLog(any(), any(), any(), any(), any(), any(), any(), any());
    }

    // ====================================================
    // UPDATE ANNOUNCEMENT
    // ====================================================

    @Test
    void updateAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        when(announcementRepository.save(any()))
                .thenReturn(announcement);

        AnnouncementDTO result = announcementService.updateAnnouncement(1L, dto);

        assertNotNull(result);
        verify(announcementRepository).save(any());
    }

    @Test
    void updateAnnouncement_NotFound() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> announcementService.updateAnnouncement(1L, dto));
    }

    // ====================================================
    // GET ANNOUNCEMENT
    // ====================================================

    @Test
    void getAnnouncementById_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        AnnouncementDTO result = announcementService.getAnnouncementById(1L);

        assertNotNull(result);
        assertEquals("Test Announcement", result.getTitle());
    }

    // ====================================================
    // GET ALL ANNOUNCEMENTS
    // ====================================================

    @Test
    void getAllAnnouncements_Success() {

        when(announcementRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(announcement));

        List<Announcement> result = announcementService.getAllAnnouncements();

        assertEquals(1, result.size());
    }

    // ====================================================
    // ACTIVE ANNOUNCEMENTS
    // ====================================================

    @Test
    void getActiveAnnouncements_Success() {

        when(announcementRepository.findActiveAnnouncements('Y', LocalDate.now()))
                .thenReturn(List.of(announcement));

        List<AnnouncementDTO> result = announcementService.getActiveAnnouncements();

        assertEquals(1, result.size());
    }

    // ====================================================
    // RECENT ANNOUNCEMENTS
    // ====================================================

    @Test
    void getRecentAnnouncements_Success() {

        when(announcementRepository.findActiveAnnouncements('Y', LocalDate.now()))
                .thenReturn(List.of(announcement));

        List<AnnouncementDTO> result = announcementService.getRecentAnnouncements(1);

        assertEquals(1, result.size());
    }

    // ====================================================
    // DEACTIVATE ANNOUNCEMENT
    // ====================================================

    @Test
    void deactivateAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        announcementService.deactivateAnnouncement(1L);

        verify(announcementRepository).save(any());
        verify(auditService).createAuditLog(any(), any(), any(), any(), any(), any(), any(), any());
    }

    // ====================================================
    // DELETE ANNOUNCEMENT
    // ====================================================

    @Test
    void deleteAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        announcementService.deleteAnnouncement(1L);

        verify(announcementRepository).delete(announcement);
    }

    // ====================================================
    // CONVERT TO DTO
    // ====================================================

   

    // ====================================================
    // EXPIRED ANNOUNCEMENTS
    // ====================================================

    @Test
    void deactivateExpiredAnnouncements_Success() {

        announcementService.deactivateExpiredAnnouncements();

        verify(announcementRepository).deactivateExpired(LocalDate.now());
    }

}