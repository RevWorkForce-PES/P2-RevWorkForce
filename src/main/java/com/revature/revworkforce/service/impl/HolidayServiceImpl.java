package com.revature.revworkforce.service.impl;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.repository.HolidayRepository;
import com.revature.revworkforce.service.HolidayService;

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
}

