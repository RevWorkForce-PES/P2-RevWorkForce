package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.LeaveType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LeaveTypeRepositoryTest {

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    // ------------------------------------------------
    // FIND BY LEAVE CODE
    // ------------------------------------------------

    @Test
    void findByLeaveCode_ReturnsLeaveType() {

        // Arrange
        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveCode("CL");

        when(leaveTypeRepository.findByLeaveCode("CL"))
                .thenReturn(Optional.of(leaveType));

        // Act
        Optional<LeaveType> result =
                leaveTypeRepository.findByLeaveCode("CL");

        // Assert
        assertThat(result).isPresent();
    }

    // ------------------------------------------------
    // FIND BY LEAVE NAME
    // ------------------------------------------------

    @Test
    void findByLeaveName_ReturnsLeaveType() {

        // Arrange
        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveName("Casual Leave");

        when(leaveTypeRepository.findByLeaveName("Casual Leave"))
                .thenReturn(Optional.of(leaveType));

        // Act
        Optional<LeaveType> result =
                leaveTypeRepository.findByLeaveName("Casual Leave");

        // Assert
        assertThat(result).isPresent();
    }

    // ------------------------------------------------
    // FIND BY ACTIVE STATUS
    // ------------------------------------------------

    @Test
    void findByIsActive_ReturnsLeaveTypes() {

        // Arrange
        LeaveType leaveType = new LeaveType();
        leaveType.setIsActive('Y');

        when(leaveTypeRepository.findByIsActive('Y'))
                .thenReturn(List.of(leaveType));

        // Act
        List<LeaveType> result =
                leaveTypeRepository.findByIsActive('Y');

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // EXISTS BY LEAVE CODE
    // ------------------------------------------------

    @Test
    void existsByLeaveCode_ReturnsTrue() {

        // Arrange
        when(leaveTypeRepository.existsByLeaveCode("CL"))
                .thenReturn(true);

        // Act
        boolean result =
                leaveTypeRepository.existsByLeaveCode("CL");

        // Assert
        assertThat(result).isTrue();
    }

    // ------------------------------------------------
    // FIND ALL ORDERED BY NAME
    // ------------------------------------------------

    @Test
    void findAllByOrderByLeaveNameAsc_ReturnsLeaveTypes() {

        // Arrange
        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveName("Casual Leave");

        when(leaveTypeRepository.findAllByOrderByLeaveNameAsc())
                .thenReturn(List.of(leaveType));

        // Act
        List<LeaveType> result =
                leaveTypeRepository.findAllByOrderByLeaveNameAsc();

        // Assert
        assertThat(result).isNotEmpty();
    }
}