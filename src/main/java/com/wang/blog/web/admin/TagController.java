package com.wang.blog.web.admin;

import com.wang.blog.pojo.Tag;
import com.wang.blog.service.TagService;
import com.wang.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @RequestMapping("/tags")
    public String tags(@PageableDefault(size = 2,sort={"id"},direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog , Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    /**
     * 接收增加标签的请求
     * @param model 传递参数 空tag对象
     * @return 返回至标签新增页
     */
    @GetMapping("/tags/input")
    public String toInputPage(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    /**
     * 接收修改标签的请求
     * @param id    通过id查出Tag对象
     * @param model 传递参数，tag对象
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String toEditPage(@PathVariable Long id,Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }
    @PostMapping("/tags/input")
    public String input(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1!=null){
            result.rejectValue("name","nameError","不能新增重复代码");
        }
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if(t==null){
            attributes.addFlashAttribute("message", "操作失败");
        }else{
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/tags";

    }

    /**
     *
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}/input")
    public String edit(@Valid Tag tag, BindingResult result,Long id, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1!=null){
            result.rejectValue("name","nameError","修改代码不能重复");
        }
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id,tag);
        if(t==null){
            attributes.addFlashAttribute("message", "操作失败");
        }else{
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/tags";

    }
}
