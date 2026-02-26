package com.revature.revworkforce.service;

import com.revature.revworkforce.model.LeaveApplication;
import java.time.LocalDate;

public interface LeaveApplicationService {

    LeaveApplication applyForLeave(String employeeId, Long leaveTypeId, LocalDate startDate, LocalDate endDate,
            String reason);

}
