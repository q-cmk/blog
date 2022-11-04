package com.wang.blog.util;

import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Set;


/**
 * @author wqy
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 数据存入redis
     * @param key
     * @param value
     * @param viewNum
     * @return
     */
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
     * 从redis中取出数据
     * @param key
     * @return
     */
    public Set<DefaultTypedTuple> zsReverseRangeWithScores(String key){
        Set set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
        return set;
    }



}
