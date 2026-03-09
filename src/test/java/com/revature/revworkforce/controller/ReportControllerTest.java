package com.revature.revworkforce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.revworkforce.dto.ReportDTO;
import com.revature.revworkforce.service.ReportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    // ==============================
    // Dashboard Statistics
    // ==============================

    @Test
    void getDashboardStatistics_ShouldReturnReport() throws Exception {

        when(reportService.getDashboardStatistics())
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports"))
                .andExpect(status().isOk());

        verify(reportService).getDashboardStatistics();
    }

    // ==============================
    // Employee Report
    // ==============================

    @Test
    void getEmployeeReport_ShouldReturnReport() throws Exception {

        when(reportService.getEmployeeReport())
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/employees"))
                .andExpect(status().isOk());

        verify(reportService).getEmployeeReport();
    }

    // ==============================
    // Leave Report
    // ==============================

    @Test
    void getLeaveReport_ShouldReturnReport() throws Exception {

        when(reportService.getLeaveReport(2026))
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/leaves")
                .param("year", "2026"))
                .andExpect(status().isOk());

        verify(reportService).getLeaveReport(2026);
    }

    // ==============================
    // Department Report
    // ==============================

    @Test
    void getDepartmentReport_ShouldReturnReport() throws Exception {

        when(reportService.getDepartmentReport(1L))
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/departments")
                .param("departmentId", "1"))
                .andExpect(status().isOk());

        verify(reportService).getDepartmentReport(1L);
    }

    // ==============================
    // Performance Report
    // ==============================

    @Test
    void getPerformanceReport_ShouldReturnReport() throws Exception {

        when(reportService.getPerformanceReport(2026))
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/performance")
                .param("year", "2026"))
                .andExpect(status().isOk());

        verify(reportService).getPerformanceReport(2026);
    }

    // ==============================
    // Goal Report
    // ==============================

    @Test
    void getGoalReport_ShouldReturnReport() throws Exception {

        when(reportService.getGoalReport())
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/goals"))
                .andExpect(status().isOk());

        verify(reportService).getGoalReport();
    }

    // ==============================
    // Attendance Report
    // ==============================

    @Test
    void getAttendanceReport_ShouldReturnReport() throws Exception {

        when(reportService.getAttendanceReport(2026, 3))
                .thenReturn(new ReportDTO());

        mockMvc.perform(get("/admin/reports/attendance")
                .param("year", "2026")
                .param("month", "3"))
                .andExpect(status().isOk());

        verify(reportService).getAttendanceReport(2026, 3);
    }
}