package com.wang.blog.web.admin;

import com.wang.blog.pojo.Blog;
import com.wang.blog.pojo.User;
import com.wang.blog.service.BlogService;
import com.wang.blog.service.TagService;
import com.wang.blog.service.TypeService;
import com.wang.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 分页展示博客页面
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog , Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }

    /**
     * 根据标题、分类、是否推荐搜索博客
     * @param pageable 通过@PageableDefault 把pageable注入这个控制器的方法，里面包含从前段传递的page的值，用于返回相应的分页
     * @param blog  BlogQUery的对象，包含标题、分类、是否推荐
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String Search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog ,Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs::blogList";
    }

    /**
     *
     * @param model 传递type、tag、blog等对象
     * @return 跳转至博客新增页面
     */
    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("blog",new Blog());
        model.addAttribute("tags",tagService.listTag());
        return INPUT;
    }

    private void setTagAndType(Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types", typeService.listType());
    }

    /**
     * 跳转至博客修改页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String toEditPage(@PathVariable Long id,Model model){
        setTagAndType(model);
        Blog blog = blogService.getBlog(id);
        //把在blog的LIst<Tag>提取至ids中。
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    /**
     * 新增博客
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogs/input")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else{
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message","提交操作错误");
        }else{
            attributes.addFlashAttribute("message", "提交操作成功");
        }
        return REDIRECT_LIST;
    }

    /**
     * 删除blog
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除操作成功");
        return REDIRECT_LIST;
    }
}
