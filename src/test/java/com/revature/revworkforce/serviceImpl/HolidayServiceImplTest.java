package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.enums.HolidayType;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.repository.HolidayRepository;
import com.revature.revworkforce.service.impl.HolidayServiceImpl;
import com.revature.revworkforce.service.AuditService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

        @Mock
        private HolidayRepository holidayRepository;

        @Mock
        private AuditService auditService;

        @InjectMocks
        private HolidayServiceImpl holidayService;

        private Holiday holiday;

        @BeforeEach
        void setUp() {

                holiday = new Holiday();
                holiday.setHolidayId(1L);
                holiday.setHolidayName("New Year");
                holiday.setHolidayDate(LocalDate.of(2030, 1, 1));
                holiday.setHolidayType(HolidayType.NATIONAL);
                holiday.setDescription("New year holiday");
                holiday.setIsActive('Y');
        }

        @Test
        void isHoliday_ReturnsTrue() {

                // Arrange
                LocalDate date = LocalDate.of(2030, 1, 1);

                when(holidayRepository.existsByHolidayDateAndIsActive(date, 'Y'))
                                .thenReturn(true);

                // Act
                boolean result = holidayService.isHoliday(date);

                // Assert
                assertTrue(result);
                verify(holidayRepository).existsByHolidayDateAndIsActive(date, 'Y');
        }

        @Test
        void getHolidaysInRange_ReturnsHolidayDates() {

                // Arrange
                when(holidayRepository.findByHolidayDateBetweenAndIsActive(
                                any(), any(), eq('Y')))
                                .thenReturn(List.of(holiday));

                // Act
                Set<LocalDate> result = holidayService.getHolidaysInRange(
                                LocalDate.of(2030, 1, 1),
                                LocalDate.of(2030, 12, 31));

                // Assert
                assertEquals(1, result.size());
                assertTrue(result.contains(LocalDate.of(2030, 1, 1)));
        }

        @Test
        void createHoliday_Success() {

                // Arrange
                HolidayDTO dto = new HolidayDTO();
                dto.setHolidayName("Republic Day");
                dto.setHolidayDate(LocalDate.of(2030, 1, 23)); // weekday
                dto.setType(HolidayType.NATIONAL);
                dto.setDescription("Holiday");

                when(holidayRepository.findByHolidayDate(any()))
                                .thenReturn(Optional.empty());

                when(holidayRepository.save(any(Holiday.class)))
                                .thenReturn(holiday);

                // Act
                HolidayDTO result = holidayService.createHoliday(dto);

                // Assert
                assertNotNull(result);
                verify(holidayRepository).save(any(Holiday.class));
        }

        @Test
        void updateHoliday_Success() {

                // Arrange
                HolidayDTO dto = new HolidayDTO();
                dto.setHolidayName("Updated Holiday");
                dto.setHolidayDate(LocalDate.of(2030, 1, 23)); // weekday
                dto.setType(HolidayType.NATIONAL);
                dto.setDescription("Updated");

                when(holidayRepository.findById(1L))
                                .thenReturn(Optional.of(holiday));

                when(holidayRepository.findByHolidayDate(any()))
                                .thenReturn(Optional.empty());

                when(holidayRepository.save(any()))
                                .thenReturn(holiday);

                // Act
                HolidayDTO result = holidayService.updateHoliday(1L, dto);

                // Assert
                assertNotNull(result);
                verify(holidayRepository).save(any(Holiday.class));
        }

        @Test
        void getHolidayById_ReturnsHoliday() {

                // Arrange
                when(holidayRepository.findById(1L))
                                .thenReturn(Optional.of(holiday));

                // Act
                HolidayDTO dto = holidayService.getHolidayById(1L);

                // Assert
                assertEquals("New Year", dto.getHolidayName());
        }

        @Test
        void getAllHolidays_ReturnsList() {

                // Arrange
                when(holidayRepository.findByIsActiveOrderByHolidayDateAsc('Y'))
                                .thenReturn(List.of(holiday));

                // Act
                List<HolidayDTO> result = holidayService.getAllHolidays();

                // Assert
                assertEquals(1, result.size());
        }

        @Test
        void deleteHoliday_Success() {

                // Arrange
                when(holidayRepository.existsById(1L)).thenReturn(true);

                // Act
                holidayService.deleteHoliday(1L);

                // Assert
                verify(holidayRepository).deleteById(1L);
        }

        @Test
        void deleteHoliday_NotFound_ThrowsException() {

                // Arrange
                when(holidayRepository.existsById(1L)).thenReturn(false);

                // Act & Assert
                assertThrows(IllegalArgumentException.class,
                                () -> holidayService.deleteHoliday(1L));
        }

        @Test
        void deactivateHoliday_Success() {

                // Arrange
                when(holidayRepository.findById(1L))
                                .thenReturn(Optional.of(holiday));

                // Act
                holidayService.deactivateHoliday(1L);

                // Assert
                verify(holidayRepository).save(holiday);
        }

        @Test
        void getUpcomingHolidays_ReturnsList() {

                // Arrange
                when(holidayRepository
                                .findByHolidayDateAfterAndIsActiveOrderByHolidayDateAsc(
                                                any(), eq('Y')))
                                .thenReturn(List.of(holiday));

                // Act
                List<HolidayDTO> result = holidayService.getUpcomingHolidays();

                // Assert
                assertEquals(1, result.size());
        }

        @Test
        void countHolidaysInRange_ReturnsCount() {

                // Arrange
                when(holidayRepository.countByHolidayDateBetweenAndIsActive(
                                any(), any(), eq('Y')))
                                .thenReturn(2L);

                // Act
                long count = holidayService.countHolidaysInRange(
                                LocalDate.of(2030, 1, 1),
                                LocalDate.of(2030, 12, 31));

                // Assert
                assertEquals(2, count);
        }

        @Test
        void getAllActiveHolidays_ReturnsList() {

                // Arrange
                when(holidayRepository.findByIsActiveOrderByHolidayDateAsc('Y'))
                                .thenReturn(List.of(holiday));

                // Act
                List<HolidayDTO> result = holidayService.getAllActiveHolidays();

                // Assert
                assertEquals(1, result.size());
        }
}