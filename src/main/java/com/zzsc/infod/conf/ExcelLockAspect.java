package com.zzsc.infod.conf;

import com.zzsc.infod.constant.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ExcelLockAspect {

    private Logger logger =   LoggerFactory.getLogger(this.getClass());

    @Autowired
    ServletContext applications;

    @Pointcut("execution(public * com.zzsc.infod.controller.MedicalAnalyseController.analyseVallage(..))")
    public void fileLock(){}

    @Before("fileLock()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        //ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //HttpServletRequest request = attributes.getRequest();



    }
    @Around("fileLock()")
    public Object around(ProceedingJoinPoint pjp) throws   ExceptionFileLocked{


        logger.info("上传"+Constant.medicalVallageUploadLock+"开始!=================================" );

        logger.info("方法环绕start.....");

            Object ret = null;
            // TODO: 此处为自定义验证逻辑，符合条件则继续执行，否则终止方法的执行
            Object lockObj=applications.getAttribute(Constant.medicalVallageUploadLock);
            if(lockObj!=null){
                if(String.valueOf(lockObj).equals(Constant.fileLocked)){
                    applications.setAttribute(Constant.medicalVallageUploadLock,Constant.fileLocked);
                    throw new ExceptionFileLocked(Constant.medicalVallageUploadLock+Constant.fileLocked);
                }else{
                    try {
                        // 方法 正常继续执行
                        ret= pjp.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }else{
                applications.setAttribute(Constant.medicalVallageUploadLock,Constant.fileLocked);

                try {
                    // 方法 执行
                    ret= pjp.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            return ret;

    }
    @AfterReturning(returning = "ret", pointcut = "fileLock()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("上传"+Constant.medicalVallageUploadLock+"结束!=================================");
        applications.setAttribute(Constant.medicalVallageUploadLock,Constant.fileUnlocked);

    }
}
