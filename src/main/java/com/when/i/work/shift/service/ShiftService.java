package com.when.i.work.shift.service;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiftService {

    @Autowired
    private ShiftRepository repository;

    public Shift getShiftById(String id) {
        return this.repository.findShiftById(id);
    }

    public void createShift(Shift shift) {
        List<Shift> userShifts = this.repository.findAllByUserId(shift.getUserId());
        this.repository.save(shift);
    }
}
