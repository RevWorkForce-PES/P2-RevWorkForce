package com.revature.revworkforce.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.repository.HolidayRepository;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.util.DateUtil;

@Service
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByHolidayDateAndIsActive(date, 'Y');
    }

    @Override
    public Set<LocalDate> getHolidayDatesInRange(LocalDate startDate,
                                                 LocalDate endDate) {

        return holidayRepository
                .findByDateRange(startDate, endDate)
                .stream()
                .filter(h -> h.getIsActive() == 'Y')
                .map(Holiday::getHolidayDate)
                .collect(Collectors.toSet());
    }
    private void validateDuplicateHoliday(LocalDate holidayDate) {

        boolean exists =
                holidayRepository.existsByHolidayDateAndIsActive(holidayDate, 'Y');

        if (exists) {
            throw new DuplicateResourceException(
                    "Holiday",
                    "holidayDate",
                    holidayDate
            );
        }
    }

    // ===========================
    // CREATE
    // ===========================

    @Override
    public HolidayDTO createHoliday(HolidayDTO dto) {

        validateBusinessRules(dto.getHolidayDate(), null);

        Holiday holiday = mapToEntity(dto);
        holiday.setIsActive('Y');

        Holiday saved = holidayRepository.save(holiday);
        return mapToDTO(saved);
    }

    // ===========================
    // UPDATE
    // ===========================

    @Override
    public HolidayDTO updateHoliday(Long holidayId, HolidayDTO dto) {

        Holiday existing = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException("Holiday not found"));

        validateBusinessRules(dto.getHolidayDate(), holidayId);

        existing.setHolidayName(dto.getHolidayName());
        existing.setHolidayDate(dto.getHolidayDate());
        existing.setHolidayType(dto.getType());
        existing.setDescription(dto.getDescription());

        Holiday updated = holidayRepository.save(existing);
        return mapToDTO(updated);
    }

    // ===========================
    // READ
    // ===========================

    @Override
    public HolidayDTO getHolidayById(Long holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException("Holiday not found"));

        return mapToDTO(holiday);
    }

    @Override
    public List<HolidayDTO> getAllHolidays() {
        return holidayRepository.findAllByOrderByHolidayDateAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // DELETE (Hard)
    // ===========================

    @Override
    public void deleteHoliday(Long holidayId) {
        if (!holidayRepository.existsById(holidayId)) {
            throw new IllegalArgumentException("Holiday not found");
        }
        holidayRepository.deleteById(holidayId);
    }

    // ===========================
    // SOFT DELETE
    // ===========================

    @Override
    public void deactivateHoliday(Long holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException("Holiday not found"));

        holiday.setIsActive('N');
        holidayRepository.save(holiday);
    }

    // ===========================
    // BUSINESS VALIDATIONS
    // ===========================

    private void validateBusinessRules(LocalDate date, Long currentId) {

        if (date == null) {
            throw new IllegalArgumentException("Holiday date is required");
        }

        // Prevent past date
        if (DateUtil.isPastDate(date)) {
            throw new IllegalArgumentException("Cannot create holiday in the past");
        }

        // Prevent weekend
        if (DateUtil.isWeekend(date)) {
            throw new IllegalArgumentException("Holiday cannot be on weekend");
        }

        // Prevent duplicate date
        holidayRepository.findByHolidayDate(date)
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getHolidayId().equals(currentId)) {
                        throw new IllegalArgumentException("Holiday already exists on this date");
                    }
                });
    }

    // ===========================
    // MAPPING
    // ===========================

    private HolidayDTO mapToDTO(Holiday holiday) {

        HolidayDTO dto = new HolidayDTO();
        dto.setHolidayId(holiday.getHolidayId());
        dto.setHolidayName(holiday.getHolidayName());
        dto.setHolidayDate(holiday.getHolidayDate());
        dto.setType(holiday.getHolidayType());
        dto.setDescription(holiday.getDescription());
        dto.setIsActive(holiday.getIsActive() == 'Y');
        dto.setCreatedAt(holiday.getCreatedAt());
        dto.setUpdatedAt(holiday.getUpdatedAt());

        return dto;
    }

    private Holiday mapToEntity(HolidayDTO dto) {

        Holiday holiday = new Holiday();
        holiday.setHolidayName(dto.getHolidayName());
        holiday.setHolidayDate(dto.getHolidayDate());
        holiday.setHolidayType(dto.getType());
        holiday.setDescription(dto.getDescription());

        return holiday;
    }
}

