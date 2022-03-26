package com.wang.blog.web;

import com.wang.blog.pojo.Comment;
import com.wang.blog.pojo.User;
import com.wang.blog.service.BlogService;
import com.wang.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/*

 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog::commentList";
    }
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        //获取blog id
        Long id = comment.getBlog().getId();
        //给comment set blog
        comment.setBlog(blogService.getBlog(id));
        //获取登录用户对象
        User user = (User) session.getAttribute("user");
        //判断是否为管理员登录
        if(user!=null){
            //设置管理员头像
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
//            comment.setNickname(user.getNickname());
        }else{
            comment.setAvatar(avatar);
        }

        //保存comment
        commentService.saveComment(comment);
        //重定向回请求"/comments/{blogId}"
        return "redirect:/comments/"+comment.getBlog().getId();
    }
}
