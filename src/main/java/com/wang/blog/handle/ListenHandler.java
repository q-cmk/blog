package com.wang.blog.handle;

import com.wang.blog.pojo.Blog;
import com.wang.blog.service.BlogService;
import com.wang.blog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

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

}
