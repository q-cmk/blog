package com.wang.blog.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice//注解定义全局异常处理类
// 该注解用于拦截所有标注@Controller的控制器
public class ControllerExceptionHandler {

    //获取当前类的日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
    该方法会拦截所有的exception
    ModelAndView 返回一个页面
    request 可以得到出错页面的路径
    e 传递一个异常对象
     */
    @ExceptionHandler(Exception.class)//注解声明异常处理方法
    public ModelAndView exceptionHander(HttpServletRequest request, Exception e) throws Exception {

        //如果注解返回的状态码存在，就把这个异常抛出，交由springboot来处理。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        //在日志中输出url和异常 {}起占位作用
        logger.error("Request URL:{},Exception:{}", request, e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("exception", e);
        mav.setViewName("error/error");
        return mav;
    }

}
