package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HolidayStatisticsDTOTest {

    @Test
    void testGettersAndSetters() {
        HolidayStatisticsDTO stats = new HolidayStatisticsDTO();

        stats.setTotal(100);
        stats.setNationalCount(50);
        stats.setRegionalCount(30);
        stats.setOptionalCount(20);

        assertEquals(100, stats.getTotal());
        assertEquals(50, stats.getNationalCount());
        assertEquals(30, stats.getRegionalCount());
        assertEquals(20, stats.getOptionalCount());
    }
}