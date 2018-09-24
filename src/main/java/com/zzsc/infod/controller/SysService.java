package com.zzsc.infod.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/SysService")
public class SysService {
    @RequestMapping(value="/loginOut",method= RequestMethod.GET )
    public String login(HttpServletRequest request){
        request.getSession().invalidate();

        return "redirect:/index.html";

    }
}
