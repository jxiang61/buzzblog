package com.xjz.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//if user is not logged in, block it and redirect to /admin
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            if(request.getRequestURI().equals("/admin/sign-up")) {
                response.sendRedirect("/admin/sign-up");
                return false;
            }


            response.sendRedirect("/admin");
            return false;
        }

        return true;
    }
}
