package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.service.HolidayService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    // ------------------------------------------------
    // GET ALL HOLIDAYS
    // ------------------------------------------------

    @Test
    void getAllHolidays_ReturnsList() {

        HolidayDTO holiday = new HolidayDTO();
        holiday.setHolidayName("New Year");

        when(holidayService.getAllHolidays())
                .thenReturn(List.of(holiday));

        List<HolidayDTO> result =
                holidayService.getAllHolidays();

        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // GET ACTIVE HOLIDAYS
    // ------------------------------------------------

    @Test
    void getAllActiveHolidays_ReturnsList() {

        HolidayDTO holiday = new HolidayDTO();
        holiday.setHolidayName("Republic Day");

        when(holidayService.getAllActiveHolidays())
                .thenReturn(List.of(holiday));

        List<HolidayDTO> result =
                holidayService.getAllActiveHolidays();

        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // CREATE HOLIDAY
    // ------------------------------------------------

    @Test
    void createHoliday_ReturnsHoliday() {

        HolidayDTO holiday = new HolidayDTO();
        holiday.setHolidayName("Test Holiday");

        when(holidayService.createHoliday(any(HolidayDTO.class)))
                .thenReturn(holiday);

        HolidayDTO result =
                holidayService.createHoliday(holiday);

        assertThat(result).isNotNull();
    }

    // ------------------------------------------------
    // UPDATE HOLIDAY
    // ------------------------------------------------

    @Test
    void updateHoliday_ReturnsHoliday() {

        HolidayDTO holiday = new HolidayDTO();
        holiday.setHolidayName("Updated Holiday");

        when(holidayService.updateHoliday(eq(1L), any(HolidayDTO.class)))
                .thenReturn(holiday);

        HolidayDTO result =
                holidayService.updateHoliday(1L, holiday);

        assertThat(result).isNotNull();
    }

    // ------------------------------------------------
    // DELETE HOLIDAY
    // ------------------------------------------------

    @Test
    void deleteHoliday_Success() {

        doNothing().when(holidayService).deleteHoliday(1L);

        holidayService.deleteHoliday(1L);

        verify(holidayService).deleteHoliday(1L);
    }

    // ------------------------------------------------
    // DELETE HOLIDAYS BY YEAR
    // ------------------------------------------------

    @Test
    void deleteHolidaysByYear_Success() {

        doNothing().when(holidayService).deleteHolidaysByYear(2026);

        holidayService.deleteHolidaysByYear(2026);

        verify(holidayService).deleteHolidaysByYear(2026);
    }

}