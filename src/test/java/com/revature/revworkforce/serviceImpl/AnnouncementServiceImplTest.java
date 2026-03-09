package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AnnouncementRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.impl.AnnouncementServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    private Announcement announcement;
    private AnnouncementDTO announcementDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");

        announcement = new Announcement();
        announcement.setAnnouncementId(1L);
        announcement.setTitle("Test Title");
        announcement.setMessage("Test Message");
        announcement.setAnnouncementType("INFO");
        announcement.setIsActive('Y');
        announcement.setCreatedBy(employee);

        announcementDTO = new AnnouncementDTO();
        announcementDTO.setTitle("Test Title");
        announcementDTO.setMessage("Test Message");
        announcementDTO.setType("INFO");
    }

    @Test
    void createAnnouncement_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(announcement);

        AnnouncementDTO result =
                announcementService.createAnnouncement(announcementDTO, "EMP001");

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");

        verify(announcementRepository).save(any(Announcement.class));
        verify(notificationService).createAnnouncementForAll(any(Announcement.class));
        verify(auditService).createAuditLog(eq("EMP001"),
                eq("ANNOUNCEMENT_CREATED"),
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void createAnnouncement_EmployeeNotFound() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> announcementService.createAnnouncement(announcementDTO, "EMP001"));
    }

    @Test
    void updateAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(announcement);

        AnnouncementDTO result =
                announcementService.updateAnnouncement(1L, announcementDTO);

        assertThat(result).isNotNull();

        verify(announcementRepository).save(any(Announcement.class));
        verify(auditService).createAuditLog(any(),
                eq("ANNOUNCEMENT_UPDATED"),
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void updateAnnouncement_NotFound() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> announcementService.updateAnnouncement(1L, announcementDTO));
    }

    @Test
    void getAnnouncementById_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        AnnouncementDTO result =
                announcementService.getAnnouncementById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getAllAnnouncements_Success() {

        when(announcementRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(announcement));

        List<Announcement> result =
                announcementService.getAllAnnouncements();

        assertThat(result).hasSize(1);
    }

    @Test
    void getActiveAnnouncements_Success() {

        when(announcementRepository.findActiveAnnouncements(eq('Y'), any(LocalDate.class)))
                .thenReturn(Arrays.asList(announcement));

        List<AnnouncementDTO> result =
                announcementService.getActiveAnnouncements();

        assertThat(result).hasSize(1);
    }

    @Test
    void deactivateAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        announcementService.deactivateAnnouncement(1L);

        assertThat(announcement.getIsActive()).isEqualTo('N');
        verify(announcementRepository).save(announcement);
    }

    @Test
    void deleteAnnouncement_Success() {

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(announcement));

        announcementService.deleteAnnouncement(1L);

        verify(announcementRepository).delete(announcement);
    }

    @Test
    void deactivateExpiredAnnouncements_Success() {

        announcementService.deactivateExpiredAnnouncements();

        verify(announcementRepository).deactivateExpired(any(LocalDate.class));
    }
}