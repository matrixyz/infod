package com.zzsc.infod.controller;

 
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.SysUser;
import com.zzsc.infod.util.MD5Util;
import com.zzsc.infod.util.PropertiesUtil;
import com.zzsc.infod.util.StringUtil;
import com.zzsc.infod.util.VerifyCodeUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

@Controller
@EnableAutoConfiguration
@RequestMapping("/PubService")
public class PubServiceController {



    @Autowired
    ServletContext applications;

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.POST )
    public String post(@RequestBody SysUser crmSysUser, HttpServletRequest request ) throws UnsupportedEncodingException {
       // SysUser SysUserNew=SysUserMapper.selectReg(crmSysUser);
       SysUser SysUserNew=null;
        if (SysUserNew!=null){
            return "手机号["+crmSysUser.getUserPhone()+"]已存在，注册失败 !";
        }
        crmSysUser.setUserId(crmSysUser.getUserPhone()+"");
        crmSysUser.setUserPwd(MD5Util.getEncryption(crmSysUser.getUserPwd()));
        crmSysUser.setUserRoleId("cust_mgr");
        //int res=SysUserMapper.insert(crmSysUser);
        if (2>0){
            //PublicInfoResetService.resetInfo(request.getServletContext());
            return "注册成功，请登录!";
        }else {
            return "注册失败!";
        }
    }
    @ResponseBody
    @RequestMapping(value="users",method= RequestMethod.GET )
    public List<String> getUsers(   ) throws UnsupportedEncodingException {
        Properties p =PropertiesUtil.getCommonCgf("user");
        applications.setAttribute("usersKeyPwd",PropertiesUtil.getValuesAll(p));
        return PropertiesUtil.getValuesByKeyPatten(p,"name");

    }

    @ResponseBody
    @RequestMapping(value="users",method= RequestMethod.PUT )
    public  String  SetUsers(@RequestBody SysUser sysUser, HttpServletRequest request ) throws UnsupportedEncodingException {
        Properties p =PropertiesUtil.getCommonCgf("user");
        HttpSession session = request.getSession(true);
        Object u=session.getAttribute("user");
        if(u!=null){
            SysUser ux=(SysUser)u;
            PropertiesUtil.setSomeValue(ux.getUserName(), sysUser.getUserPwd(), p);
            return Constant.SUCCESS;
        }


        return Constant.ERR;

    }

    @RequestMapping(value="getUserspwdForm",method= RequestMethod.GET )
    public  String  getUserspwdForm( ) throws  Exception {

        return "adm/chg-pwd-form";

    }

    @ResponseBody
    @RequestMapping(value="/login",method= RequestMethod.GET )
    public String login(HttpServletRequest request, @RequestParam(name="userPhone",required = true) String userPhone,
                        @RequestParam(name="userPwd",required = true) String password,
                        @RequestParam(name="check",required = true) String check){
        int res=0;
        if (check == null) {
            return "err_check";
        }
        check=check.toUpperCase();
        HttpSession session = request.getSession(true);
        if (session.getAttribute("rand")==null||session.getAttribute("rand").equals(check)==false) {
            return "err_check";
        }
        Object obj=applications.getAttribute("usersKeyPwd");
        List<String> keypwds=(List<String>)obj;

        if ( keypwds.contains(userPhone+password) ){
            SysUser u=new SysUser();
            u.setUserName(userPhone);
            session.setAttribute("user",u);
            if(u.getUserName().equals("管理员"))
            return "success";
            else
                return "success1";
        }else {

            return "fail";
        }

    }


    @RequestMapping("/check")
    public void getCheckCode( HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //生成随机字串
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        //存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute("rand", verifyCode );
        //生成图片
        int w = 200, h = 80;
        VerifyCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode );

    }



}
