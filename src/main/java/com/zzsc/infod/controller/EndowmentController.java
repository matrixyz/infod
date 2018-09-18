package com.zzsc.infod.controller;
import com.zzsc.infod.service.EndowmentService;
import com.zzsc.infod.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.BindingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.zzsc.infod.util.UuidUtil;
@Controller
@EnableAutoConfiguration
@RequestMapping("/Endowment")
public class EndowmentController {
    @Autowired
    private EndowmentService EndowmentService;

    @RequestMapping(value="",method= RequestMethod.GET )
    public Endowment get(Model model,EndowmentDto endowment ){
        Endowment endowmentTemp = EndowmentService.getCondition(endowment) ;
        if (  endowmentTemp !=null){
            return  endowmentTemp;
        }

        return null;
    }

    @RequestMapping(value="/list",method= RequestMethod.GET )
    public String getList(Model model, EndowmentDto endowmentDto  ){

        if (endowmentDto.getPage() == null||"".equals(endowmentDto.getPage())) {
            endowmentDto.setPage("1");
        }



        List<EndowmentDto> list=EndowmentService.list(endowmentDto);


        model.addAttribute("EndowmentList",list);
        model.addAttribute("queryParams",endowmentDto);
        return "adm/Endowment-list";
    }

    @RequestMapping(value="/addPrepared",method= RequestMethod.GET )
    public String addPrepared(Model model,EndowmentDto endowmentDto ){
       // endowmentDto.setEid(UuidUtil.get16UUID());

        model.addAttribute("submitType", "POST");
        model.addAttribute("EndowmentDto", endowmentDto);
        return "adm/Endowment-form";
    }

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.POST )
    public String post(@RequestBody EndowmentDto endowment ){
        int res=EndowmentService.insert(endowment);
        if (res>0){
            return "添加信息成功!";
        }else {
            return "添加信息失败!";
        }
    }
    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.PUT )
    public String put(@RequestBody EndowmentDto endowment ){
        int res=EndowmentService.update(endowment);
        if (res>0){
        return "修改信息成功!";
        }else {
        return "修改信息失败!";
        }
    }



    public static void main(String argv[]){
        SpringApplication.run(EndowmentController.class,argv);
    }

}
