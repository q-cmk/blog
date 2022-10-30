//package com.wang.blog.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//
///*
//
// */
//@Aspect//作用是把当前类标识为一个切面供容器读取
//@Component//通用的注解，可标注任意类为 Spring 组件。如果一个 Bean 不知道属于哪个层，可以使用@Component 注解标注。
//public class LogAspect {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /*
//    日志的执行顺序
//        Before
//        切点程序
//        AfterReturning
//        after
//     */
//    @Pointcut("execution(* com.wang.blog.web.*.*(..))")//声明它为切面
//    public void log() {
//
//    }
//
//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint) {
//        //springmvc中，可以通过RequestContextHolder的静态方法getRequestAttributes()获取attributes,再通过attributes获取request
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        //通过request获取url和ip
//        String url = request.getRequestURL().toString();
//        String ip = request.getRemoteAddr();
//        //通过joinPoint获取
//        //JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.
//        //Signature getSignature();
//        // 获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
//        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
//        //获取传入目标方法的参数对象
//        Object[] args = joinPoint.getArgs();
//        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
//        logger.info("Request:{}", requestLog);
//    }
//
//    @After("log()")
//    public void doAfter() {
//        logger.info("---------doAfter-----------");
//    }
//
//    @AfterReturning(returning = "result", pointcut = "log()")
//    public void doAfterReturn(Object result) {
//        logger.info("Result:()",result);
//    }
//
//    private class  RequestLog {
//        private String url;
//        private String ip;
//        private String classMethod;
//        private Object[] args;
//
//        public RequestLog(String url, String ip, String classMethod, Object[] args) {
//            this.url = url;
//            this.ip = ip;
//            this.classMethod = classMethod;
//            this.args = args;
//        }
//
//        @Override
//        public String toString() {
//            return "RequestLog{" +
//                    "url='" + url + '\'' +
//                    ", ip='" + ip + '\'' +
//                    ", classMethod='" + classMethod + '\'' +
//                    ", args=" + Arrays.toString(args) +
//                    '}';
//        }
//    }
//
//}
