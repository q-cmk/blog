package com.wang.blog.service;

import com.wang.blog.pojo.Blog;
import com.wang.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);
    Blog getAndConvert(Long id);
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Page<Blog> listBlog(Pageable pageable);
    List<Blog> listRecommendBlogTop(Integer size);
    //根据tagId查询，返回一个分页列表
    Page<Blog> listBlog(Long tagId,Pageable pageable);
    Page<Blog> listBlog(String query,Pageable pageable);
    //查询年份对应的博客列表
    Map<String,List<Blog>> archiveBlog();

    Long countBlog();
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
}
