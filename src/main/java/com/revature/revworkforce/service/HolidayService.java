package com.revature.revworkforce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.revature.revworkforce.dto.HolidayDTO;
import com.revature.revworkforce.dto.HolidayStatisticsDTO;

public interface HolidayService {
	 boolean isHoliday(LocalDate date);

	    Set<LocalDate> getHolidaysInRange(LocalDate startDate, LocalDate endDate);

	    List<HolidayDTO> getHolidaysByYear(int year);

	    List<HolidayDTO> getUpcomingHolidays();

	    long countHolidaysInRange(LocalDate startDate, LocalDate endDate);


    HolidayDTO createHoliday(HolidayDTO dto);

    HolidayDTO updateHoliday(Long holidayId, HolidayDTO dto);

    HolidayDTO getHolidayById(Long holidayId);

    List<HolidayDTO> getAllHolidays();

    void deleteHoliday(Long holidayId);

    void deactivateHoliday(Long holidayId);
  
    int importHolidays(List<HolidayDTO> holidays);
    void deleteHolidaysByYear(int year);
    HolidayStatisticsDTO getHolidayStatistics();
    List<HolidayDTO> getAllActiveHolidays();
}
