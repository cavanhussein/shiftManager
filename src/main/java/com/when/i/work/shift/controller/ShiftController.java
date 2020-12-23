package com.when.i.work.shift.controller;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShiftController {

    @Autowired
    private ShiftService shiftservice;

    @GetMapping("/api/shift")
    public void getShifts() {
        System.out.println("test");
    }

    @GetMapping("/api/shift/{id}")
    public Shift getShiftById(@PathVariable String id) {
        return this.shiftservice.getShiftById(id);
    }

    @PostMapping(path = "/api/shift", consumes = "application/json", produces = "application/json")
    public void addShift(@RequestBody Shift shift) {
        this.shiftservice.createShift(shift);
    }
}
