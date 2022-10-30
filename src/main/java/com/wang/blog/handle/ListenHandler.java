package com.wang.blog.handle;

import com.wang.blog.pojo.Blog;
import com.wang.blog.service.BlogService;
import com.wang.blog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wqy
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ListenHandler {
    @Autowired
    private BlogService blogService;

    @Resource
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ListenHandler(){
        System.out.println("数据开始初始化");
    }
    @PostConstruct
    public void init() throws Exception {
        this.logger.info("数据库 初始化开始");
        //将数据库中的数据写入redis
        List<Blog> blogs = blogService.queryAllBlog();

        blogs.forEach(blog -> {
            //将浏览量写入redis
            redisUtil.set("viewNum",
                    blog.getId().toString(),
                    blog.getViews());
        });
        this.logger.info("已写入redis");
    }
    @PreDestroy
    public void afterDestroy(){
        System.out.println("关闭======================");
        //将Redis数据一次性写入数据库
        Set<DefaultTypedTuple> viewNum = redisUtil.zsReverseRangeWithScores("viewNum");
        this.writeNum(viewNum,"viewNum");
        this.logger.info("redis数据存入数据库完毕");
        System.out.println("系统关闭===========redis->数据库更新完毕===========");
    }
    public void writeNum(Set<DefaultTypedTuple> set,String fieldName){
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
