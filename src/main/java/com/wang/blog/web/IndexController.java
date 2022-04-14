package com.wang.blog.web;

import com.wang.blog.service.BlogService;
import com.wang.blog.service.CommentService;
import com.wang.blog.service.TagService;
import com.wang.blog.service.TypeService;
import com.wang.blog.vo.BlogQuery;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@Controller
public class IndexController {
    @Autowired
    public TypeService typeService;
    @Autowired
    public BlogService blogService;
    @Autowired
    public TagService tagService;

    @Autowired
    public CommentService commentService;

    /**
     * 博客首页
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String index(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(2));
        model.addAttribute("tags",tagService.listTagTop(2));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(2));
        return "index";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        model.addAttribute("comments",commentService.listCommentByBlogId(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
