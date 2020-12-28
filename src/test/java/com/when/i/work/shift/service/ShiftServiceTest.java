package com.when.i.work.shift.service;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.repository.ShiftRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @InjectMocks
    ShiftService shiftService;

    @Mock
    private ShiftRepository shiftRepository;

    @Test
    void getShifts() throws ParseException {

        List<Shift> shiftList = Arrays.asList(generateShift(
                "1111", "testUser", "2020-12-24@7:00:00", "2020-12-24@8:00:00"));
        Mockito.when(shiftRepository.findAll()).thenReturn(shiftList);
        List<Shift> getShiftsList = shiftService.getShifts();
        assertEquals(getShiftsList.size(), 1);
        assertEquals(getShiftsList.get(0).getId(), "1111");
    }

    @Test
    void getShifts_multipleShifts() throws ParseException {

        List<Shift> shiftList = Arrays.asList(
                generateShift("1111", "testUser", "2020-12-24@7:00:00", "2020-12-24@8:00:00"),
                generateShift("12345", "testUser2", "2020-12-24@8:00:00", "2020-12-25@00:00:00")
        );
        Mockito.when(shiftRepository.findAll()).thenReturn(shiftList);
        List<Shift> getShiftsList = shiftService.getShifts();
        assertEquals(getShiftsList.size(), 2);
    }

    @Test
    void getShiftById() throws ParseException {
        String shiftId = "12345";
        Shift shift = generateShift(
                "12345", "testUser", "2020-12-24@7:00:00", "2020-12-24@8:00:00");
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(shift);
        Shift getShift = shiftService.getShiftById(shiftId);
        assertNotNull(getShift);
        assertEquals(getShift.getId(), shiftId);
    }

    @Test
    void getShiftById_noMatch() {
        String shiftId = "nonExistingId";
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(null);
        Shift getShift = shiftService.getShiftById(shiftId);
        assertNull(getShift);
    }

    @Test
    void createShift() {

    }

    @Test
    void deleteShift() {
    }

    @Test
    void patchShift() {
    }

    private Shift generateShift(String id, String userId, String stringStartTime, String stringEndTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
        Date startTime = formatter.parse(stringStartTime);
        Date endTime = formatter.parse(stringEndTime);
        return new Shift(id, startTime, endTime, userId);
    }
}