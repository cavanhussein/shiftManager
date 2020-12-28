package com.when.i.work.shift.service;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
import com.when.i.work.shift.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository repository;

    public List<Shift> getShifts() {
        return this.repository.findAll();
    }

    public Shift getShiftById(String id) {
        return this.repository.findShiftById(id);
    }

    public void createShift(Shift shift) {
        validateStartEndTime(shift.getStartTime(), shift.getEndTime());
        ensureNoOverlappingShifts(shift.getUserId(), shift.getStartTime(), shift.getEndTime());
        this.repository.save(shift);
    }

    public void deleteShift(String id) {
        this.repository.deleteById(id);
    }

    public Shift patchShift(ShiftUpdateDto shiftUpdate, String id) {
        validateStartEndTime(shiftUpdate.getStartTime(), shiftUpdate.getEndTime());
        Shift shiftToUpdate = this.repository.findShiftById(id);
        ensureNoOverlappingShifts(shiftToUpdate.getUserId(), shiftUpdate.getStartTime(), shiftUpdate.getEndTime());
        shiftToUpdate.setStartTime(shiftUpdate.getStartTime());
        shiftToUpdate.setEndTime(shiftUpdate.getEndTime());
        return this.repository.save(shiftToUpdate);
    }

    private void validateStartEndTime(Date startTime, Date endTime) {
        if (startTime.getTime() > endTime.getTime()) {
            //TODO: Use better exception.
            throw new RuntimeException();
        }
    }

    private void ensureNoOverlappingShifts(String userId, Date startTime, Date endTime) {
        if (this.repository.findOverlappingShift(userId, startTime, endTime).size() > 0) {
            throw new RuntimeException();
        }
    }
}
