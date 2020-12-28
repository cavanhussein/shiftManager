package com.when.i.work.shift.repository;

import com.when.i.work.shift.dto.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ShiftRepository extends MongoRepository<Shift, String> {
    Shift findShiftById(@Param("id") String id);

    List<Shift> findAllByUserId(@Param("userId") String userId);

    @Query("{ $and: ["
            + "{ 'userId': ?0 },"
            + "{$or: ["
            + "{ $and: [{ 'startTime': { $lt: ?1 } }, { 'endTime': { $gt: ?1 } } ]},"
            + "{ $and: [{ 'startTime': { $lt: ?2 } }, { 'endTime': { $gt: ?2 } } ]}"
            + "]}"
            + "]}")
    List<Shift> findOverlappingShift(String userId, Date startTime, Date endTime);

}
