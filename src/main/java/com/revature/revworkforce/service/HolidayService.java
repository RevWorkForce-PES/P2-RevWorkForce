package com.revature.revworkforce.service;

import java.time.LocalDate;
import java.util.Set;

public interface HolidayService {

    public boolean isHoliday(LocalDate date);

    public Set<LocalDate> getHolidayDatesInRange(LocalDate startDate,
                                                 LocalDate endDate);
}