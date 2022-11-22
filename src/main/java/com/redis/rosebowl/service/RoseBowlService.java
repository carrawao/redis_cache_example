package com.redis.rosebowl.service;

import com.redis.rosebowl.entity.RoseBowl;
import java.util.List;

public interface RoseBowlService {
    RoseBowl saveRoseBowl(RoseBowl rosebowl);
 
    // Read operation
    List<RoseBowl> fetchRoseBowlRecords();

    RoseBowl fetchRoseBowlByDate(Long date);
 
    // Update operation
    RoseBowl updateRoseBowl(Long date, RoseBowl rosebowl);
 
    // Delete operation
    void deleteRoseBowlByDate(Long date);
}
