package com.revature.revworkforce.service;

import com.revature.revworkforce.model.LeaveType;
import com.revature.revworkforce.repository.LeaveTypeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveTypeServiceTest {

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @InjectMocks
    private LeaveTypeService leaveTypeService;

    private LeaveType leaveType;

    @BeforeEach
    void setup() {
        leaveType = new LeaveType();
        leaveType.setLeaveTypeId(1L);
        leaveType.setLeaveName("Sick Leave");
    }

    // -----------------------------
    // getAllLeaveTypes
    // -----------------------------
    @Test
    void getAllLeaveTypes_ShouldReturnList() {

        when(leaveTypeRepository.findAll())
                .thenReturn(List.of(leaveType));

        List<LeaveType> result = leaveTypeService.getAllLeaveTypes();

        assertEquals(1, result.size());
        verify(leaveTypeRepository).findAll();
    }

    // -----------------------------
    // getLeaveTypeById
    // -----------------------------
    @Test
    void getLeaveTypeById_ShouldReturnLeaveType() {

        when(leaveTypeRepository.findById(1L))
                .thenReturn(Optional.of(leaveType));

        LeaveType result = leaveTypeService.getLeaveTypeById(1L);

        assertNotNull(result);
        assertEquals("Sick Leave", result.getLeaveName());
    }

    // -----------------------------
    // saveLeaveType
    // -----------------------------
    @Test
    void saveLeaveType_ShouldSaveLeaveType() {

        when(leaveTypeRepository.save(leaveType))
                .thenReturn(leaveType);

        LeaveType result = leaveTypeService.saveLeaveType(leaveType);

        assertEquals("Sick Leave", result.getLeaveName());
        verify(leaveTypeRepository).save(leaveType);
    }

    // -----------------------------
    // deleteLeaveType
    // -----------------------------
    @Test
    void deleteLeaveType_ShouldDeleteLeaveType() {

        leaveTypeService.deleteLeaveType(1L);

        verify(leaveTypeRepository).deleteById(1L);
    }
}