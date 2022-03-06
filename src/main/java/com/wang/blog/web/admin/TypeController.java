package com.wang.blog.web.admin;

import com.wang.blog.pojo.Type;
import com.wang.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 4,sort = {"id"},direction = Sort.Direction.ASC)
                                Pageable pageable, Model modle){
        modle.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }
    /*
    接收请求，返回一个新增页面
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }
    /*
    接收请求，返回一个修改页面
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";

    }
    @PostMapping("/types/add")
    public String add(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        //先判定无重复才能判空
        //判重
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        //判空
        if (result.hasErrors()){
            return "admin/types-input";
        }

        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message","提交操作错误");
        }else{
            attributes.addFlashAttribute("message", "提交操作成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String update(@Valid Type type,BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        //先判定无重复才能判空
        //判重
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","不能重复修改");
        }
        //判空
        if (result.hasErrors()){
            return "admin/types-input";
        }

        Type t = typeService.updateType(id,type);
        if (t == null) {
            attributes.addFlashAttribute("message","修改操作错误");
        }else{
            attributes.addFlashAttribute("message", "修改操作成功");
        }
        return "redirect:/admin/types";
    }
    /*
    删除
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除操作成功");
        return "redirect:/admin/types";
    }
}
