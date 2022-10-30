package com.wang.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author wqy
 */
@Component
@Aspect
public class LogAspect1 {
   private final Logger logger= LoggerFactory.getLogger(this.getClass());

   @Pointcut("execution(* com.wang.blog.web.*.*(..))")
   public void log(){
   }

   /**
    * 前置方法，在目标方法执行前执行
    * @param joinPoint  封装了代理方法信息的对象
    */
   @Before("log()")
   public void doBefore(JoinPoint joinPoint){
      logger.info("-------------doBefore--------------");
      //使用web包中的RequestContextHolder获取到requst属性
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      HttpServletRequest request = requestAttributes.getRequest();
      //通过request请求得到url和远程ip
      String url = request.getRequestURL().toString();
      String ip = request.getRemoteAddr();

      //通过joinPoint获取到代理对象中的方法名
      String methodName = joinPoint.getSignature().getName();
      //获取代理对象的参数列表
      Object[] args = joinPoint.getArgs();

      //生成日志对象
      ResultLog resultLog = new ResultLog(url, ip, methodName,args);
      logger.info("Request{}:",resultLog);
   }

   @After("log()")
   public void doAfter(){
      logger.info("--------------doAfter-------------");
   }

   //pointcut("log()")用于指定目标方法
   //returning接收目标方法返回值，设为下面方法的形参
   //此处将result的类型声明为Object，意味着对目标方法的返回值不加限制。
   @AfterReturning(returning="result",pointcut="log()")
   public void doAfterReturn(Object result){
      logger.info("Result:()",result);
   }

   private class ResultLog{
      private String url;
      private String ip;
      private String classMethod;
      private Object[] args;

      public ResultLog() {
      }

      public ResultLog(String url, String ip, String classMethod, Object[] args) {
         this.url = url;
         this.ip = ip;
         this.classMethod = classMethod;
         this.args = args;
      }

      @Override
      public String toString() {
         return "ResultLog{" +
                 "url='" + url + '\'' +
                 ", ip='" + ip + '\'' +
                 ", classMethod='" + classMethod + '\'' +
                 ", args=" + Arrays.toString(args) +
                 '}';
      }
   }



}
