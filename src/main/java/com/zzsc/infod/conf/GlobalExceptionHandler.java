package com.zzsc.infod.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class GlobalExceptionHandler {


    private Logger logger =   LoggerFactory.getLogger(this.getClass());

    //处理AOP 里的文件锁定分析异常
    @ExceptionHandler(ExceptionFileLocked.class)
    //@ResponseBody
    public String  handle(ExceptionFileLocked e, HttpServletRequest req, HttpServletResponse res){

        String info=null;
        if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有x-requested-with
            //这种情况说明是ajax的方式
            info=e.getMessage();
            PrintWriter out = null;
            try {
                out = res.getWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            out.print(info);
            out.flush();
            out.close();
        }else{
            info=e.getMessage()+"<a href=\"javascript:window.history.go(-1);\">点击返回</a>";
            res.setContentType("text/html;charset=utf-8");
            PrintWriter out = null;
            try {
                out = res.getWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            out.print(info);
            out.flush();
            out.close();
        }

        logger.info("全局异常捕获-ExceptionFileLocked  message="+e.getMessage());


        return null;
    }
}
