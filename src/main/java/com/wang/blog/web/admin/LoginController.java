package com.wang.blog.web.admin;

import com.wang.blog.pojo.User;
import com.wang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;
    /*
    接收请求，返回登录页
     */
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }
    /*
    用户登录
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user!=null){
           user.setPassword(null);
           session.setAttribute("user",user);
           return "admin/index";
        }else {
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/admin";
        }

    }
    /*
    用户登出
     */
    @GetMapping("/logout")
    public String logout( HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
