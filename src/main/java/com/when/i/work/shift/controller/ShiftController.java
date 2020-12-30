package com.when.i.work.shift.controller;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
import com.when.i.work.shift.service.ShiftService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @GetMapping(path = "/api/shift", produces = "application/json")
    public ResponseEntity<List<Shift>> getShifts(
            @Param("startTime") String startTime, @Param("endTime") String endTime) {
        return new ResponseEntity<>(shiftService.getShifts(startTime, endTime), HttpStatus.OK);
    }

    @GetMapping(path = "/api/shift/{id}", produces = "application/json")
    public ResponseEntity<Shift> getShiftById(@PathVariable String id) {
        return new ResponseEntity<>(shiftService.getShiftById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/api/shift", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Shift> addShift(@RequestBody Shift shift) {
        return new ResponseEntity<>(shiftService.createShift(shift), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/shift/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteShift(@PathVariable String id) {
        shiftService.deleteShift(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/api/shift/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Shift> patchShift(@PathVariable("id") String id, @RequestBody ShiftUpdateDto shiftUpdate) {
        return new ResponseEntity<>(shiftService.patchShift(shiftUpdate, id), HttpStatus.OK);
    }
}
