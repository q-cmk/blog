package com.wang.blog.handle;

import com.wang.blog.pojo.Blog;
import com.wang.blog.service.BlogService;
import com.wang.blog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 定时任务
 * @author wqy
 */
@Component
public class TimeTask {
    @Autowired
    BlogService blogService;

    @Autowired
    RedisUtil redisUtil;

    /**
     *获取当前类的日志对象
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestionView(){
        System.out.println("定时任务执行======================");
        //将Redis数据一次性写入数据库
        Set<DefaultTypedTuple> viewNum = redisUtil.zsReverseRangeWithScores("viewNum");
        this.writeNum(viewNum,"viewNum");
        this.logger.info("redis数据存入数据库完毕");
        System.out.println("定时任务执行完毕===========redis->数据库更新完毕===========");
    }
    public void writeNum(Set<DefaultTypedTuple> set, String fieldName){
        set.forEach(item->{
            DefaultTypedTuple dt= (DefaultTypedTuple)item;
            Long id = Long.valueOf((String) dt.getValue());
            Integer num = dt.getScore().intValue();

            Blog blog = blogService.getBlog(id);
            blog.setViews(num);
            blogService.updateBlog(id, blog);
            this.logger.info(fieldName+"更新完毕");
        });
    }
}
