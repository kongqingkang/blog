package com.kqk.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auhtor kqk
 * @date 2019/11/12 0012 - 21:56
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    //Interceptor(拦截器)
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
