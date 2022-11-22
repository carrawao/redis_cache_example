package com.redis.rosebowl.controller;

//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.redis.rosebowl.entity.RedisCacheClient;
import com.redis.rosebowl.entity.RoseBowl;
import com.redis.rosebowl.service.RoseBowlService;

@CrossOrigin("*")
@RestController 
public class RoseBowlController {
    /**
     * Instantiate Redis Client for the whole application
     */
    RedisCacheClient redisClient = new RedisCacheClient();

    /**
     * Time-To-Live (TTL) for Redis keys
     * Unit: seconds
     */
    public static final int TTL = 20;


    /*redisConnection.close();
    redisClient.shutdown(); */

	@Autowired
    private RoseBowlService rosebowlService;

	@PostMapping("/api/rosebowl")
    public RoseBowl saveRoseBowl(@Valid @RequestBody RoseBowl rosebowl) {
        return rosebowlService.saveRoseBowl(rosebowl);
    }

    // Read operation
    @GetMapping("/api/rosebowl")
    public List<RoseBowl> fetchRoseBowlRecords() {
        List<RoseBowl> roseBowlRecords = new ArrayList<RoseBowl>();
        long amountOfRecords = redisClient.redisConnection.sync().scard("tableEntries");

        if (amountOfRecords > 0) { // cache hit
            for (int i = 0; i < amountOfRecords; i++) {
                Map<String, String> hashRecord = redisClient.redisConnection.sync().hgetall("hash:" + (2020 - i));
                RoseBowl record = new RoseBowl(Long.parseLong(hashRecord.get("date")), hashRecord.get("winner"), hashRecord.get("opponent"));
                roseBowlRecords.add(record);
            }
        } else { // get result from MySQL
            roseBowlRecords = rosebowlService.fetchRoseBowlRecords();

            for (int i = 0; i < roseBowlRecords.size(); i++) {
                long date = roseBowlRecords.get(i).getDate();
                String winner = roseBowlRecords.get(i).getWinner();
                String opponent = roseBowlRecords.get(i).getOpponent();

                redisClient.redisConnection.sync().sadd("tableEntries", Long.toString(date));
                redisClient.redisConnection.sync().hset("hash:" + date, "date", Long.toString(date));
                redisClient.redisConnection.sync().hset("hash:" + date, "winner", winner);
                redisClient.redisConnection.sync().hset("hash:" + date, "opponent", opponent);
                redisClient.redisConnection.sync().expire("tableEntries", TTL);
                redisClient.redisConnection.sync().expire("hash:" + date, TTL);
            }
        }
        return roseBowlRecords;
    }

    @GetMapping("/api/rosebowl/{date}")
    public RoseBowl fetchRoseBowlByDate(@PathVariable("date") Long date) throws SQLException {
        /* Redis and MySQL results */
        RoseBowl result = null;

        BigDecimal redisTotalTime = new BigDecimal(0);
        BigDecimal mysqlTotalTime = new BigDecimal(0);

        long redisStartTime = System.nanoTime();
        //String redisResult = redisClient.redisConnection.sync().get(Long.toString(date));
        Map<String, String> redisResult =  redisClient.redisConnection.sync().hgetall("hash:" + date);
        long redisEndTime = System.nanoTime() - redisStartTime;

        if (redisResult.size() > 0) { // cache hit
            System.out.println("==== Result from Redis cache ====");
            redisTotalTime = redisTotalTime.add(new BigDecimal(redisEndTime));
            System.out.println("==== It took " + redisTotalTime.divide(new BigDecimal(1_000_000)) + " ms ====");
            
            //JSONObject jsonObject = new JSONObject(redisResult);
            //result = new RoseBowl(Long.parseLong(jsonObject.getString("date")), jsonObject.getString("winner"), jsonObject.getString("opponent"));
            result = new RoseBowl(Long.parseLong(redisResult.get("date")), redisResult.get("winner"), redisResult.get("opponent"));
        } else { // get result from MySQL
            long mysqlStartTime = System.nanoTime();
            result = rosebowlService.fetchRoseBowlByDate(date);
            long mysqlEndTime = System.nanoTime() - mysqlStartTime;

            System.out.println("==== Result directly from MySQl ====");
            mysqlTotalTime = mysqlTotalTime.add(new BigDecimal(mysqlEndTime));
            System.out.println("==== It took " + mysqlTotalTime.divide(new BigDecimal(1_000_000)) + " ms ====");

            //redisClient.redisConnection.sync().set(Long.toString(date), result.toString());

            redisClient.redisConnection.sync().hset("hash:" + date, "date", Long.toString(date));
            redisClient.redisConnection.sync().hset("hash:" + date, "winner", result.getWinner());
            redisClient.redisConnection.sync().hset("hash:" + date, "opponent", result.getOpponent());
            redisClient.redisConnection.sync().expire("hash:" + date, TTL);
        }

        //redisClient.redisConnection.sync().expire(Long.toString(date), TTL);
        return result;
    }
 
    // Update operation
    @Modifying(clearAutomatically = true)
    @Transactional
    @PutMapping(path = "/api/rosebowl/{date}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RoseBowl updateRoseBowl(@PathVariable("date") Long date, @RequestBody RoseBowl rosebowl) {
        RoseBowl result = null;
        if (redisClient.redisConnection.sync().hexists("hash:" + date , "date")) { // cache hit (update redis hash)
            redisClient.redisConnection.sync().hset("hash:" + date, "winner", rosebowl.getWinner());
            redisClient.redisConnection.sync().hset("hash:" + date, "opponent", rosebowl.getOpponent());
            redisClient.redisConnection.sync().scard("tableEntries");
            result = rosebowl;
        }
        // update row in MySQL
        System.out.println("Testing body request PUT ===== " + rosebowl.toString());
        result = rosebowlService.updateRoseBowl(date, rosebowl);
        return result;
    }
 
    // Delete operation
    @DeleteMapping("/api/rosebowl/{date}")
    public String deleteRoseBowlById(@PathVariable("date") Long date) {
        rosebowlService.deleteRoseBowlByDate(date);
        return "Deleted Successfully";
    }

}