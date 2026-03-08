package com.revature.revworkforce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.revworkforce.model.LeaveType;
import com.revature.revworkforce.repository.LeaveTypeRepository;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    // Get all leave types
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    // Get leave type by id
    public LeaveType getLeaveTypeById(Long id) {
        return leaveTypeRepository.findById(id).orElse(null);
    }

    // Save or update
    public LeaveType saveLeaveType(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    // Delete
    public void deleteLeaveType(Long id) {
        leaveTypeRepository.deleteById(id);
    }
}