package com.zzsc.infod.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class RightsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse response, Object o) throws Exception {
        response.setCharacterEncoding("utf-8");
        req.setAttribute("currUrl",req.getRequestURL());

            HttpSession session=req.getSession(false);
            if (session==null||session.getAttribute("user") == null) {

                //一系列处理后发现session已经失效
                if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有x-requested-with
                     PrintWriter out = response.getWriter();
                    out.print("登录已失效，请重新登录!");//session失效
                     out.flush();
                    return false;
                }else{
                  response.sendRedirect("/index.html");
                    return  false;
                }
            }
       // }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    ServletContext servletContext ;
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {


        if (servletContext==null)
            servletContext = httpServletRequest.getServletContext();

    }
}
