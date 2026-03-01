package com.revature.revworkforce.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.dto.HolidayStatisticsDTO;
import com.revature.revworkforce.enums.HolidayType;
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
        return holidayRepository
                .existsByHolidayDateAndIsActive(date, 'Y');
    }

    @Override
    public Set<LocalDate> getHolidaysInRange(LocalDate startDate,
                                             LocalDate endDate) {

        return holidayRepository
                .findByHolidayDateBetweenAndIsActive(
                        startDate,
                        endDate,
                        'Y'
                )
                .stream()
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
    @Override
    public List<HolidayDTO> getHolidaysByYear(int year) {

        return holidayRepository.findAll()
                .stream()
                .filter(h -> h.getIsActive() == 'Y')
                .filter(h -> h.getHolidayDate().getYear() == year)
                .sorted((h1, h2) ->
                        h1.getHolidayDate().compareTo(h2.getHolidayDate()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

@Override
public List<HolidayDTO> getUpcomingHolidays() {

    return holidayRepository
            .findByHolidayDateAfterAndIsActiveOrderByHolidayDateAsc(
                    LocalDate.now(),
                    'Y'
            )
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
}
@Override
public long countHolidaysInRange(LocalDate startDate,
                                  LocalDate endDate) {

    return holidayRepository
            .countByHolidayDateBetweenAndIsActive(
                    startDate,
                    endDate,
                    'Y'
            );
}
@Override
public int importHolidays(List<HolidayDTO> holidays) {

    int successCount = 0;

    for (HolidayDTO dto : holidays) {
        try {

            LocalDate date = dto.getHolidayDate();

            // Skip null date
            if (date == null) continue;

            // Skip weekend
            if (DateUtil.isWeekend(date)) continue;

            // Skip duplicate
            boolean exists =
                    holidayRepository.existsByHolidayDateAndIsActive(date, 'Y');

            if (exists) continue;

            Holiday holiday = mapToEntity(dto);
            holiday.setIsActive('Y');

            holidayRepository.save(holiday);
            successCount++;

        } catch (Exception e) {
            // Continue on error (do nothing)
        }
    }

    return successCount;
}
@Override
public void deleteHolidaysByYear(int year) {

    LocalDate start = LocalDate.of(year, 1, 1);
    LocalDate end   = LocalDate.of(year, 12, 31);

    List<Holiday> holidays =
            holidayRepository.findByHolidayDateBetweenAndIsActive(
                    start,
                    end,
                    'Y'
            );

    for (Holiday holiday : holidays) {
        holiday.setIsActive('N'); // Soft delete
    }

    holidayRepository.saveAll(holidays);
}
@Override
public HolidayStatisticsDTO getHolidayStatistics() {

    List<Holiday> activeHolidays =
            holidayRepository.findByIsActiveOrderByHolidayDateAsc('Y');

    HolidayStatisticsDTO stats = new HolidayStatisticsDTO();

    stats.setTotal(activeHolidays.size());

    stats.setNationalCount(
            activeHolidays.stream()
                    .filter(h -> h.getHolidayType() == HolidayType.NATIONAL)
                    .count()
    );

    stats.setRegionalCount(
            activeHolidays.stream()
                    .filter(h -> h.getHolidayType() == HolidayType.REGIONAL)
                    .count()
    );

    stats.setOptionalCount(
            activeHolidays.stream()
                    .filter(h -> h.getHolidayType() == HolidayType.COMPANY_SPECIFIC)
                    .count()
    );

    return stats;
}private HolidayDTO convertToDTO(Holiday holiday) {

    return HolidayDTO.builder()
            .holidayId(holiday.getHolidayId())
            .holidayName(holiday.getHolidayName())
            .holidayDate(holiday.getHolidayDate())
            .type(holiday.getHolidayType())
            .description(holiday.getDescription())
            .isActive(holiday.getIsActive() == 'Y')
            .build();
}
@Override
public List<HolidayDTO> getAllActiveHolidays() {

    return holidayRepository
            .findByIsActiveOrderByHolidayDateAsc('Y')
            .stream()
            .map(this::convertToDTO)
            .toList();
}
}

