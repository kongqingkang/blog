package com.kqk.blog.aspect;

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
 * @auhtor kqk
 * @date 2019/11/11 0011 - 18:55
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());//日志记录器

    @Pointcut("execution(* com.kqk.blog.web.*.*(..))")
    public void log() {
    }//切面

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request:" + requestLog);
    }

    @After("log()")
    public void doAfter() {
//        logger.info("-----doAfter------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturning(Object result) {
        logger.info("Result:" + "{" + result + "}");
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String className;
        private Object[] args;

        public RequestLog(String url, String ip, String className, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.className = className;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", className='" + className + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
