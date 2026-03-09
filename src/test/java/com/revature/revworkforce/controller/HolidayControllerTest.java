//package com.revature.revworkforce.controller;
//
//import com.revature.revworkforce.dto.HolidayDTO;
//import com.revature.revworkforce.service.HolidayService;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HolidayController.class)
//class HolidayControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private HolidayService holidayService;
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void getAllHolidays_ReturnsOk() throws Exception {
//
//        when(holidayService.getAllHolidays()).thenReturn(List.of());
//
//        mockMvc.perform(get("/admin/holidays"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void showAddForm_ReturnsOk() throws Exception {
//
//        mockMvc.perform(get("/admin/holidays/add"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void saveHoliday_RedirectsToConfig() throws Exception {
//
//        when(holidayService.createHoliday(any(HolidayDTO.class))).thenReturn(null);
//
//        mockMvc.perform(post("/admin/holidays/add")
//                .with(csrf())
//                .param("holidayName","New Year")
//                .param("holidayDate","2026-01-01")
//                .param("holidayType","NATIONAL")
//                .param("description","New year holiday")
//                .param("active","true"))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void showEditForm_ReturnsOk() throws Exception {
//
//        when(holidayService.getHolidayById(1L)).thenReturn(new HolidayDTO());
//
//        mockMvc.perform(get("/admin/holidays/edit/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void updateHoliday_RedirectsToConfig() throws Exception {
//
//        when(holidayService.updateHoliday(anyLong(), any(HolidayDTO.class))).thenReturn(null);
//
//        mockMvc.perform(post("/admin/holidays/edit/1")
//                .with(csrf())
//                .param("holidayName","Updated Holiday")
//                .param("holidayDate","2026-01-01")
//                .param("holidayType","NATIONAL")
//                .param("description","Updated")
//                .param("active","true"))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void deleteHoliday_RedirectsToConfig() throws Exception {
//
//        doNothing().when(holidayService).deleteHoliday(1L);
//
//        mockMvc.perform(post("/admin/holidays/delete/1")
//                .with(csrf()))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    @WithMockUser(roles="ADMIN")
//    void deleteByYear_RedirectsToConfig() throws Exception {
//
//        doNothing().when(holidayService).deleteHolidaysByYear(2026);
//
//        mockMvc.perform(post("/admin/holidays/delete-year")
//                .with(csrf())
//                .param("year","2026"))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    @WithMockUser(username="EMP001",roles="EMPLOYEE")
//    void viewHolidays_ReturnsOk() throws Exception {
//
//        when(holidayService.getAllActiveHolidays()).thenReturn(List.of());
//
//        mockMvc.perform(get("/employee/holidays"))
//                .andExpect(status().isOk());
//    }
//}