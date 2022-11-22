package com.redis.rosebowl.entity;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisCacheClient 
{
    public StatefulRedisConnection<String, String> redisConnection;

    public RedisCacheClient() {
        /**
         * Get Redis connection
         */
        RedisURI redisUri = RedisURI.Builder.redis("localhost", 6379).withPassword("noSQL".toCharArray()).build();
        
        /* Create Redis client and connect to server */
        RedisClient redisClient = RedisClient.create(redisUri);
        redisConnection = redisClient.connect();
        System.out.println("Connected to Redis successfully");

        /* Check whether server is running or not */
        System.out.println("Redis server is running: " + redisConnection.sync().ping());
    }
}
