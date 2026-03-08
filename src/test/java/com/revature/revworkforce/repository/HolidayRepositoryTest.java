//package com.revature.revworkforce.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import com.revature.revworkforce.model.Holiday;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//class HolidayRepositoryTest {
//
//    @Autowired
//    private HolidayRepository holidayRepository;
//
//    @Test
//    @DisplayName("findByHolidayDate should return holiday")
//    void findByHolidayDate_ReturnsHoliday() {
//
//        Holiday holiday = new Holiday();
//        holiday.setHolidayName("New Year");
//        holiday.setHolidayDate(LocalDate.of(2026,1,1));
//        holiday.setIsActive('Y');
//
//        holidayRepository.save(holiday);
//
//        Optional<Holiday> result =
//                holidayRepository.findByHolidayDate(LocalDate.of(2026,1,1));
//
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    void findAllByOrderByHolidayDateAsc_ReturnsList() {
//
//        Holiday h1 = new Holiday();
//        h1.setHolidayName("Republic Day");
//        h1.setHolidayDate(LocalDate.of(2026,1,26));
//        h1.setIsActive('Y');
//
//        Holiday h2 = new Holiday();
//        h2.setHolidayName("Independence Day");
//        h2.setHolidayDate(LocalDate.of(2026,8,15));
//        h2.setIsActive('Y');
//
//        holidayRepository.save(h1);
//        holidayRepository.save(h2);
//
//        List<Holiday> list =
//                holidayRepository.findAllByOrderByHolidayDateAsc();
//
//        assertThat(list).hasSize(2);
//    }
//
//    @Test
//    void existsByHolidayDateAndIsActive_ReturnsTrue() {
//
//        Holiday holiday = new Holiday();
//        holiday.setHolidayName("Test Holiday");
//        holiday.setHolidayDate(LocalDate.of(2026,5,10));
//        holiday.setIsActive('Y');
//
//        holidayRepository.save(holiday);
//
//        boolean exists =
//                holidayRepository.existsByHolidayDateAndIsActive(
//                        LocalDate.of(2026,5,10),'Y');
//
//        assertThat(exists).isTrue();
//    }
//}