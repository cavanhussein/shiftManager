package com.when.i.work.shift.service;

import com.when.i.work.shift.dto.Shift;
import com.when.i.work.shift.dto.ShiftUpdateDto;
import com.when.i.work.shift.exception.BadRequestException;
import com.when.i.work.shift.exception.ConflictingShiftException;
import com.when.i.work.shift.repository.ShiftRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository repository;

    /** Return a list of shifts. Filtering with a startTime and endTime is supported.
     * @param stringStartTime
     * @param stringEndTime
     * @return List of shifts.
     * @throws BadRequestException
     */
    public List<Shift> getShifts(String stringStartTime, String stringEndTime) {
        //
        if ((stringStartTime == null || stringStartTime.isEmpty())
                && (stringEndTime == null || stringEndTime.isEmpty())) {
            return repository.findAll();
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date startTime = formatter.parse(stringStartTime);
            Date endTime = formatter.parse(stringEndTime);
            return repository.findOverlappingShifts(
                    startTime, endTime, Sort.by("startTime"));
        } catch (ParseException e) {
            throw new BadRequestException("StartTime and endTime must be of the form: yyyy-MM-dd'T'HH:mm:ssZ");
        }
    }

    /**
     * Return a shift filtered on a given ID.
     * @param id
     * @return The queried shift with given ID. Return null if the shift does not exist.
     */
    public Shift getShiftById(String id) {
        return repository.findShiftById(id);
    }

    /**
     * Attempt to create a shift for a given user.
     * @param shift
     * @return The created shift.
     */
    public Shift createShift(Shift shift) {
        validatePostFields(shift);
        validateStartEndTimeBoundaries(shift.getStartTime(), shift.getEndTime());
        ensureNoOverlappingShifts(shift.getUserId(), shift.getStartTime(), shift.getEndTime());
        return repository.save(shift);

    }

    /**
     * Attempt to delete a shift with a given ID.
     * @param id
     */
    public void deleteShift(String id) {
        repository.deleteById(id);
    }

    /**
     * Update an existing shift's startTime and/or endTime by querying with the given shift ID.
     * @param shiftUpdate
     * @param id
     * @return
     */
    public Shift patchShift(ShiftUpdateDto shiftUpdate, String id) {
        validateShiftUpdateFields(shiftUpdate);
        validateStartEndTimeBoundaries(shiftUpdate.getStartTime(), shiftUpdate.getEndTime());
        Shift shiftToUpdate = repository.findShiftById(id);
        ensureNoOverlappingShiftsForPatch(
                id, shiftToUpdate.getUserId(), shiftUpdate.getStartTime(), shiftUpdate.getEndTime());
        if (shiftUpdate.getStartTime() != null) {
            shiftToUpdate.setStartTime(shiftUpdate.getStartTime());
        }
        if (shiftUpdate.getEndTime() != null) {
            shiftToUpdate.setEndTime(shiftUpdate.getEndTime());
        }
        return repository.save(shiftToUpdate);
    }

    /**
     * Ensure the given shift's userId, startTime, and endTime are not null/empty.
     * @param shift
     */
    private void validatePostFields(Shift shift) {
        if (shift.getUserId() == null || shift.getUserId().isEmpty()
                || shift.getStartTime() == null || shift.getEndTime() == null) {
            throw new BadRequestException("userId, startTime, and endTime must be defined when creating a Shift.");
        }
    }

    /**
     * Ensure the given shift update object's startTime and endTime are not null.
     * @param shiftUpdateDto
     */
    private void validateShiftUpdateFields(ShiftUpdateDto shiftUpdateDto) {
        if (shiftUpdateDto.getStartTime() == null || shiftUpdateDto.getEndTime() == null) {
            throw new BadRequestException("startTime, and endTime must be defined when updating a Shift.");
        }
    }

    /**
     * Ensure the given startTime is earlier than the given endTime.
     * @param startTime
     * @param endTime
     */
    private void validateStartEndTimeBoundaries(Date startTime, Date endTime) {
        if (startTime.getTime() >= endTime.getTime()) {
            throw new BadRequestException("startTime is greater than or equal to endTime.");
        }
    }

    /**
     * Determine if there are any existing shifts for the given user that conflict the given startTime and endTime.
     * @param userId
     * @param startTime
     * @param endTime
     */
    private void ensureNoOverlappingShifts(String userId, Date startTime, Date endTime) {
        if (repository.findOverlappingShiftWithUserId(userId, startTime, endTime).size() > 0) {
            throw new ConflictingShiftException(startTime, endTime);
        }
    }

    /**
     * Determine if there are existing shifts excluding shift with shiftId
     * for the given user that conflict the given startTime and endTime.
     * @param shiftId
     * @param userId
     * @param startTime
     * @param endTime
     */
    private void ensureNoOverlappingShiftsForPatch(String shiftId, String userId, Date startTime, Date endTime) {
        List<Shift> overlappingShifts = repository.findOverlappingShiftWithUserId(userId, startTime, endTime);
        if ((overlappingShifts.size() == 1 && !overlappingShifts.get(0).getId().equals(shiftId))
            || overlappingShifts.size() > 1) {
            throw new ConflictingShiftException(startTime, endTime);
        }
    }
}
