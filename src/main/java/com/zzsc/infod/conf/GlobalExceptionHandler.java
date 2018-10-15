package com.zzsc.infod.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {


    private Logger logger =   LoggerFactory.getLogger(this.getClass());

    //处理AOP 里的文件锁定分析异常
    @ExceptionHandler(ExceptionFileLocked.class)
    @ResponseBody
    public String  handle(ExceptionFileLocked e){
        logger.info("全局异常捕获-ExceptionFileLocked  message="+e.getMessage());
        return e.getMessage(); //自己需要实现的异常处理
    }
}
