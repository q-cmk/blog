package com.wang.blog.web;


import com.wang.blog.service.BlogService;
import com.wang.blog.service.CommentService;
import com.wang.blog.service.TagService;
import com.wang.blog.service.TypeService;
import com.wang.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
    @Autowired
    RedisUtil redisUtil;

    /**
     * 博客首页
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String index(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , Model model){
        //分页查询
        model.addAttribute("page",blogService.listBlog(pageable));
        //分类查询
        model.addAttribute("types",typeService.listTypeTop(2));
        //标签查询
        model.addAttribute("tags",tagService.listTagTop(2));
        //最新推荐
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(2));
        return "index";
    }

    /**
     * 全局搜索
     * @param pageable
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }

    /**
     * 博客详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        model.addAttribute("comments",commentService.listCommentByBlogId(id));
        return "blog";
    }


}
