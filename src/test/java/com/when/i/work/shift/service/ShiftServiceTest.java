package com.when.i.work.shift.service;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @InjectMocks
    ShiftService shiftService;

    @Mock
    private ShiftRepository shiftRepository;

    @Test
    void getShifts_ok() throws ParseException {

        List<Shift> shiftList = Collections.singletonList(generateShift(
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
    void getShiftById_ok() throws ParseException {
        String shiftId = "12345";
        Shift shift = generateShift(
                "12345", "testUser", "2020-12-24@7:00:00", "2020-12-24@8:00:00");
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(shift);
        Shift getShiftByIdReturn = shiftService.getShiftById(shiftId);
        assertNotNull(getShiftByIdReturn);
        assertEquals(getShiftByIdReturn.getId(), shiftId);
    }

    @Test
    void getShiftById_noMatch() {
        String shiftId = "nonExistingId";
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(null);
        Shift getShiftReturn = shiftService.getShiftById(shiftId);
        assertNull(getShiftReturn);
    }

    @Test
    void createShift_ok() throws ParseException {
        String shiftId = "12345";
        Shift shift = generateShift(
                "12345", "testUser", "2020-12-24@7:00:00", "2020-12-24@8:00:00");
        Mockito.when(shiftRepository.save(shift)).thenReturn(shift);
        Shift createShiftReturn = shiftService.createShift(shift);
        assertNotNull(createShiftReturn);
        assertEquals(createShiftReturn.getId(), shiftId);
    }

    @Test
    void createShift_malformedStartEndTimes() throws ParseException {
        Shift shift = generateShift(
                "12345", "testUser", "2020-12-24@10:00:00", "2020-12-24@8:00:00");
       assertThrows(RuntimeException.class, () -> shiftService.createShift(shift));
    }

    @Test
    void createShift_overlappingShifts() throws ParseException {
        String stringStartTime = "2020-12-24@6:00:00";
        String stringEndTime = "2020-12-24@8:00:00";
        String userId = "testUser";
        String shiftId = "12345";
        Shift shift = generateShift(
                shiftId, userId, stringStartTime, stringEndTime);
        Mockito.when(shiftRepository.findOverlappingShift(
                shift.getUserId(), shift.getStartTime(),
                shift.getEndTime())).thenReturn(Collections.singletonList(shift));
        assertThrows(RuntimeException.class, () -> shiftService.createShift(shift));
    }

    @Test
    void patchShift_ok() throws ParseException {
        String stringStartTime = "2020-12-24@6:00:00";
        String stringEndTime = "2020-12-24@8:00:00";
        String userId = "testUser";
        String shiftId = "12345";
        Shift shift = generateShift(
                shiftId, userId, stringStartTime, stringEndTime);
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                stringStartTime, stringEndTime);
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(shift);
        Mockito.when(shiftRepository.findOverlappingShift(
                shift.getUserId(), shift.getStartTime(), shift.getEndTime())).thenReturn(new LinkedList<>());
        Mockito.when(shiftRepository.save(any())).thenReturn(shift);
        shiftService.patchShift(shiftUpdateDto, shiftId);
    }

    @Test
    void patchShift_malformedStartEndTimes() throws ParseException {
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                "2020-12-24@10:00:00", "2020-12-24@8:00:00");
        assertThrows(RuntimeException.class, () -> shiftService.patchShift(shiftUpdateDto, "12345"));
    }

    @Test
    void patchShift_overlappingShifts() throws ParseException {
        String stringStartTime = "2020-12-24@6:00:00";
        String stringEndTime = "2020-12-24@8:00:00";
        String userId = "testUser";
        String shiftId = "12345";
        Shift shift = generateShift(
                shiftId, userId, stringStartTime, stringEndTime);
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                stringStartTime, stringEndTime);
        Mockito.when(shiftRepository.findShiftById(shiftId)).thenReturn(shift);
        Mockito.when(shiftRepository.findOverlappingShift(
                shift.getUserId(), shift.getStartTime(),
                shift.getEndTime())).thenReturn(Collections.singletonList(shift));
        assertThrows(RuntimeException.class, () -> shiftService.patchShift(shiftUpdateDto, userId));
    }

    private Shift generateShift(String id, String userId, String stringStartTime, String stringEndTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
        Date startTime = formatter.parse(stringStartTime);
        Date endTime = formatter.parse(stringEndTime);
        return new Shift(id, startTime, endTime, userId);
    }

    private ShiftUpdateDto generateShiftUpdateDto(String stringStartTime, String stringEndTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
        Date startTime = formatter.parse(stringStartTime);
        Date endTime = formatter.parse(stringEndTime);
        return new ShiftUpdateDto(startTime, endTime);
    }
}