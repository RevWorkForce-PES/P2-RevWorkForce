package com.revature.revworkforce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


import com.revature.revworkforce.dto.HolidayDTO;

public interface HolidayService {

    public boolean isHoliday(LocalDate date);

    public Set<LocalDate> getHolidayDatesInRange(LocalDate startDate,
                                                 LocalDate endDate);
    HolidayDTO createHoliday(HolidayDTO dto);

    HolidayDTO updateHoliday(Long holidayId, HolidayDTO dto);

    HolidayDTO getHolidayById(Long holidayId);

    List<HolidayDTO> getAllHolidays();

    void deleteHoliday(Long holidayId);

    void deactivateHoliday(Long holidayId);
}