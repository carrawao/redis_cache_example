package com.redis.rosebowl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redis.rosebowl.entity.RoseBowl;

@Repository
public interface RoseBowlRepository extends JpaRepository<RoseBowl, Long> {
    @Query(value="SELECT * FROM rose_bowl", nativeQuery=true)
    List<RoseBowl> fetchRoseBowlRecords();

    @Query(value="SELECT * FROM rose_bowl WHERE date=:date", nativeQuery=true)
    RoseBowl fetchRoseBowlByDate(@Param("date") Long date);
}
