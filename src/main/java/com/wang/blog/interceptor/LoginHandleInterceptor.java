package com.wang.blog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user==null){
            request.setAttribute("message","未登录,没有访问权限");
            request.getRequestDispatcher("/admin").forward(request, response);
            return false;
        }

        return true;
    }
}
