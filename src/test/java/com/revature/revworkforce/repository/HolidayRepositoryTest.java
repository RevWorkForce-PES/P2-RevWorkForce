package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Holiday;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HolidayRepositoryTest {

    @Mock
    private HolidayRepository holidayRepository;

    // ------------------------------------------------
    // FIND BY HOLIDAY DATE
    // ------------------------------------------------

    @Test
    void findByHolidayDate_ReturnsHoliday() {

        Holiday holiday = new Holiday();
        holiday.setHolidayName("New Year");
        holiday.setHolidayDate(LocalDate.of(2026,1,1));
        holiday.setIsActive('Y');

        when(holidayRepository.findByHolidayDate(LocalDate.of(2026,1,1)))
                .thenReturn(Optional.of(holiday));

        Optional<Holiday> result =
                holidayRepository.findByHolidayDate(LocalDate.of(2026,1,1));

        assertThat(result).isPresent();
        assertThat(result.get().getHolidayName()).isEqualTo("New Year");
    }

    // ------------------------------------------------
    // FIND BY HOLIDAY DATE NOT FOUND
    // ------------------------------------------------

    @Test
    void findByHolidayDate_NotFound() {

        when(holidayRepository.findByHolidayDate(LocalDate.of(2030,1,1)))
                .thenReturn(Optional.empty());

        Optional<Holiday> result =
                holidayRepository.findByHolidayDate(LocalDate.of(2030,1,1));

        assertThat(result).isEmpty();
    }

    // ------------------------------------------------
    // FIND ALL ORDERED BY DATE
    // ------------------------------------------------

    @Test
    void findAllByOrderByHolidayDateAsc_ReturnsList() {

        Holiday h1 = new Holiday();
        h1.setHolidayName("Republic Day");

        Holiday h2 = new Holiday();
        h2.setHolidayName("Independence Day");

        when(holidayRepository.findAllByOrderByHolidayDateAsc())
                .thenReturn(List.of(h1, h2));

        List<Holiday> result =
                holidayRepository.findAllByOrderByHolidayDateAsc();

        assertThat(result).hasSize(2);
    }

    // ------------------------------------------------
    // EXISTS BY DATE AND ACTIVE
    // ------------------------------------------------

    @Test
    void existsByHolidayDateAndIsActive_ReturnsTrue() {

        when(holidayRepository.existsByHolidayDateAndIsActive(
                LocalDate.of(2026,5,10),'Y'))
                .thenReturn(true);

        boolean result =
                holidayRepository.existsByHolidayDateAndIsActive(
                        LocalDate.of(2026,5,10),'Y');

        assertThat(result).isTrue();
    }
}