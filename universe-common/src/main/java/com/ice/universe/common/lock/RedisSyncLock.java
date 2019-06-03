package com.ice.universe.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RedisSyncLock
 * @Description TODO redis分布式锁
 * @Author liubin
 * @Date 2019/5/5 3:52 PM
 **/
@Component
public class RedisSyncLock {

    private static final String OK = "OK";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSyncLock.class);
    /**
     * 释放锁的lua脚本
     */
    private static final String UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then     return redis.call(\"del\",KEYS[1]) else     return 0 end ";

    @Autowired
    private RedisTemplate redisTemplate;

    public void lock(String key, String value, int lockSeconds) {
        try {
            while (!tryLock(key, value, lockSeconds)) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
        }
    }

    public boolean tryLock(String key, String value, int lockSeconds) {
        RedisCallback<Boolean> action = redisConnection -> {
            JedisCommands jedisCommands = (JedisCommands) redisConnection.getNativeConnection();
            return OK.equals(jedisCommands.set(key, value, "NX", "EX", lockSeconds));
        };
        try {
            return (boolean) redisTemplate.execute(action);
        } catch (Exception e) {
            LOGGER.error("REDIS LOCK FAILED FOR KEY {},VALUE{}", key, value, e);
        }
        return false;
    }

    public boolean tryLock(String key, String value, int lockSeconds, int tryTimes) throws InterruptedException {
        for(int i = 0;i<tryTimes;i++){
            if(tryLock(key,value,lockSeconds)){
                return true;
            }
            //休眠100毫秒，再尝试
            Thread.sleep(100);
        }
        return false;
    }

    public Boolean unlock(String key, String value) {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> args = new ArrayList<>();
        args.add(value);
        try {
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            RedisCallback<Long> callback = (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                if (nativeConnection instanceof JedisCluster) {
                    return (Long)((JedisCluster)nativeConnection).eval(UNLOCK_LUA, keys, args);
                } else if (nativeConnection instanceof Jedis) {
                    return (Long)((Jedis)nativeConnection).eval(UNLOCK_LUA, keys, args);
                } else {
                    // 模式匹配不上
                }

                return 0L;
            };

            Long result = (Long) redisTemplate.execute(callback);

            return result != null && result > 0;
        } catch (Exception e) {
            LOGGER.error("RELEASE LOCK OCCURED AN EXCEPTION", e);
        }
        return false;

    }
}
