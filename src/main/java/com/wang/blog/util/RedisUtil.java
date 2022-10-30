package com.wang.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wqy
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public  Boolean set(String key, String value, Integer viewNum) {

        try {
            redisTemplate.opsForZSet().add(key,value,Double.valueOf(viewNum.toString()));

            return true;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置
     * @param key
     * @param value
     */
    public  void set(String key, Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 给一个指定的key值附加过期时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public Set<DefaultTypedTuple> zsReverseRangeWithScores(String key){
        Set set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);

        return set;

    }



}
