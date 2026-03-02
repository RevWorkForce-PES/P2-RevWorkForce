package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.dto.HolidayStatisticsDTO;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.service.HolidayService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HolidayController {

	private final HolidayService holidayService;

	// ===============================
	// ADMIN ENDPOINTS
	// ===============================

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/holidays")
	public String getAllHolidays(Model model) {
	    model.addAttribute("holidays", holidayService.getAllHolidays());
	    return "admin/holidays/list";  // matches list.html
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/holidays/add")
	public String showAddForm(Model model) {
	    model.addAttribute("holiday", new HolidayDTO());
	    return "admin/holidays/form";   // matches form.html
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/holidays/add")
	public String createHoliday(@Valid @ModelAttribute("holiday") HolidayDTO dto) {
	    holidayService.createHoliday(dto);
	    return "redirect:/admin/holidays";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/holidays/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
	    model.addAttribute("holiday", holidayService.getHolidayById(id));
	    return "admin/holidays/form";   // reuse same form.html
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/holidays/edit/{id}")
	public String updateHoliday(@PathVariable Long id,
	                            @Valid @ModelAttribute("holiday") HolidayDTO dto) {
	    holidayService.updateHoliday(id, dto);
	    return "redirect:/admin/holidays";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/holidays/delete/{id}")
	public String deleteHoliday(@PathVariable Long id) {
	    holidayService.deleteHoliday(id);
	    return "redirect:/admin/holidays";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/holidays/delete-year")
	public String deleteByYear(@RequestParam int year) {
	    holidayService.deleteHolidaysByYear(year);
	    return "redirect:/admin/holidays";
	}

	// ===============================
	// EMPLOYEE ENDPOINT
	// ===============================

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/employee/holidays")
	public String viewHolidays(Model model) {

	    model.addAttribute("holidays",
	            holidayService.getAllActiveHolidays());

	    return "employee/holidays/calendar";   // ✅ CORRECT
	}
	
}