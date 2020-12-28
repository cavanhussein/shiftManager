package com.when.i.work.shift.controller;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
import com.when.i.work.shift.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @GetMapping("/api/shift")
    public List<Shift> getShifts() {
        return shiftService.getShifts();
    }

    @GetMapping("/api/shift/{id}")
    public Shift getShiftById(@PathVariable String id) {
        return shiftService.getShiftById(id);
    }

    @PostMapping(path = "/api/shift", consumes = "application/json", produces = "application/json")
    public Shift addShift(@RequestBody Shift shift) {
        return shiftService.createShift(shift);
    }

    @DeleteMapping(path = "/api/shift{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShift(@PathVariable String id) {
        shiftService.deleteShift(id);
    }

    @PatchMapping(path = "/api/shift/{id}")
    public Shift patchShift(@PathVariable("id") String id, @RequestBody ShiftUpdateDto shiftUpdate) {
        return shiftService.patchShift(shiftUpdate, id);
    }
}
