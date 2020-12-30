package com.when.i.work.shift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
import com.when.i.work.shift.exception.BadRequestException;
import com.when.i.work.shift.exception.ConflictingShiftException;
import com.when.i.work.shift.service.ShiftService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ShiftController.class)
@RunWith(SpringRunner.class)
class ShiftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShiftService shiftService;

    @Test
    void getShifts_ok() throws Exception {
        List<Shift> shiftList = Collections.singletonList(generateShift(
                "1111", "testUser", "2020-12-24T7:00:00-0600", "2020-12-24T8:00:00-0600"));
        when(shiftService.getShifts(null, null)).thenReturn(shiftList);
        this.mockMvc.perform(get("/api/shift"))
                .andExpect(status().isOk());
    }

    @Test
    void getShiftById_ok() throws Exception {
        Shift shift = generateShift(
                "1111", "testUser", "2020-12-24T7:00:00-0600", "2020-12-24T8:00:00-0600");
        when(shiftService.getShiftById("1111")).thenReturn(shift);
        this.mockMvc.perform(get("/api/shift/1111"))
                .andExpect(status().isOk());
    }

    @Test
    void addShift_ok() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Shift shift = generateShift(
                "1111", "testUser", "2020-12-24T7:00:00-0600", "2020-12-24T8:00:00-0600");
        String jsonShift = mapper.writeValueAsString(shift);
        when(shiftService.createShift(shift)).thenReturn(shift);
        this.mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON).content(jsonShift))
                .andExpect(status().isOk());
    }

    @Test
    void addShift_missingEndTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Shift shift = new Shift();
        shift.setId("1111");
        shift.setUserId("testUser");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        shift.setStartTime(formatter.parse("2020-12-24T7:00:00-0600"));
        when(shiftService.createShift(any())).thenThrow(new BadRequestException("Test"));
        String jsonShift = mapper.writeValueAsString(shift);
        this.mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON).content(jsonShift))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShift_largerStartTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Shift shift = generateShift(
                "1111", "testUser", "2020-12-24T10:00:00-0600", "2020-12-24T8:00:00-0600");
        String jsonShift = mapper.writeValueAsString(shift);
        when(shiftService.createShift(any())).thenThrow(
                new BadRequestException("StartTime greater than endTime."));
        this.mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON).content(jsonShift))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShift_conflictingShift() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Shift shift = generateShift(
                "1111", "testUser", "2020-12-24T7:00:00-0600", "2020-12-24T8:00:00-0600");
        String jsonShift = mapper.writeValueAsString(shift);
        when(shiftService.createShift(shift)).thenThrow(
                new ConflictingShiftException(shift.getStartTime(), shift.getEndTime()));
        this.mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON).content(jsonShift))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShift_ok() throws Exception {
        doNothing().when(shiftService).deleteShift("1111");
        this.mockMvc.perform(delete("/api/shift/1111")).andExpect(status().isNoContent());
    }

    @Test
    void patchShift_ok() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String stringStartTime = "2020-12-24T6:00:00-0600";
        String stringEndTime = "2020-12-24T8:00:00-0600";
        String userId = "testUser";
        String shiftId = "12345";
        Shift shift = generateShift(
                shiftId, userId, stringStartTime, stringEndTime);
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                stringStartTime, stringEndTime);
        String jsonShiftUpdate = mapper.writeValueAsString(shiftUpdateDto);
        when(shiftService.patchShift(shiftUpdateDto, shiftId)).thenReturn(shift);
        this.mockMvc.perform(patch(String.format("/api/shift/%s", shiftId))
                .contentType(MediaType.APPLICATION_JSON).content(jsonShiftUpdate))
                .andExpect(status().isOk());
    }

    @Test
    void patchShift_missingEndtime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String stringStartTime = "2020-12-24T6:00:00-0600";
        String shiftId = "12345";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date startTime = formatter.parse(stringStartTime);
        ShiftUpdateDto shiftUpdateDto = new ShiftUpdateDto();
        shiftUpdateDto.setStartTime(startTime);
        String jsonShiftUpdate = mapper.writeValueAsString(shiftUpdateDto);
        when(shiftService.patchShift(any(), any())).thenThrow(new BadRequestException("Missing endTime"));
        this.mockMvc.perform(patch(String.format("/api/shift/%s", shiftId))
                .contentType(MediaType.APPLICATION_JSON).content(jsonShiftUpdate))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchShift_largerStartTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String stringStartTime = "2020-12-24T6:00:00-0600";
        String stringEndTime = "2020-12-24T8:00:00-0600";
        String shiftId = "12345";
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                stringStartTime, stringEndTime);
        String jsonShiftUpdate = mapper.writeValueAsString(shiftUpdateDto);
        when(shiftService.patchShift(any(), any())).thenThrow(new BadRequestException("StartTime greater than endTime."));
        this.mockMvc.perform(patch(String.format("/api/shift/%s", shiftId))
                .contentType(MediaType.APPLICATION_JSON).content(jsonShiftUpdate))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchShift_conflictingShift() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String stringStartTime = "2020-12-24T6:00:00-0600";
        String stringEndTime = "2020-12-24T8:00:00-0600";
        String userId = "testUser";
        String shiftId = "12345";
        Shift shift = generateShift(
                shiftId, userId, stringStartTime, stringEndTime);
        ShiftUpdateDto shiftUpdateDto = generateShiftUpdateDto(
                stringStartTime, stringEndTime);
        String jsonShiftUpdate = mapper.writeValueAsString(shiftUpdateDto);
        when(shiftService.patchShift(any(), any())).thenThrow(
                new ConflictingShiftException(shift.getStartTime(), shift.getEndTime()));
        this.mockMvc.perform(patch(String.format("/api/shift/%s", shiftId))
                .contentType(MediaType.APPLICATION_JSON).content(jsonShiftUpdate))
                .andExpect(status().isBadRequest());
    }

    private Shift generateShift(String id, String userId, String stringStartTime, String stringEndTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date startTime = formatter.parse(stringStartTime);
        Date endTime = formatter.parse(stringEndTime);
        return new Shift(id, startTime, endTime, userId);
    }

    private ShiftUpdateDto generateShiftUpdateDto(String stringStartTime, String stringEndTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date startTime = formatter.parse(stringStartTime);
        Date endTime = formatter.parse(stringEndTime);
        return new ShiftUpdateDto(startTime, endTime);
    }
}