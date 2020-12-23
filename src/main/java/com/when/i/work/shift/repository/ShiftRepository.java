package com.when.i.work.shift.repository;

import com.when.i.work.shift.dto.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShiftRepository extends MongoRepository<Shift, String> {
    Shift findShiftById(@Param("id") String id);

    List<Shift> findAllByUserId(@Param("userId") String userId);


}
