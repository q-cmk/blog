package com.wang.blog.service;

import com.wang.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    //通过博客id查找到评论列表
    List<Comment> listCommentByBlogId(Long blogId);
    //保存评论
    Comment saveComment(Comment comment);
}
