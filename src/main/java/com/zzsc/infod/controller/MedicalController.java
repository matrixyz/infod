package com.zzsc.infod.controller;
import com.zzsc.infod.service.MedicalService;
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
@RequestMapping("/Medical")
public class MedicalController {
    @Autowired
    private MedicalService MedicalService;


    @RequestMapping(value="",method= RequestMethod.GET )
    public Medical get(Model model,MedicalDto medical ){
        Medical medicalTemp = MedicalService.getCondition(medical) ;
        if (  medicalTemp !=null){
            return  medicalTemp;
        }

        return null;
    }


    @RequestMapping(value="/list",method= RequestMethod.GET )
    public String getList( MedicalDto medicalDto  ){

        if (medicalDto.getPage() == null||"".equals(medicalDto.getPage())) {
            medicalDto.setPage("1");
        }
        List<MedicalDto> list=MedicalService.list(medicalDto);
        return "adm/Medical-list";
    }

    @RequestMapping(value="/addPrepared",method= RequestMethod.GET )
    public String addPrepared(Model model,MedicalDto medicalDto ){

        model.addAttribute("submitType", "POST");
        model.addAttribute("MedicalDto", medicalDto);
        return "adm/Medical-form";
    }

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.POST )
    public String post(@RequestBody MedicalDto medical ){
        int res=MedicalService.insert(medical);
        if (res>0){
            return "添加信息成功!";
        }else {
            return "添加信息失败!";
        }
    }
    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.PUT )
    public String put(@RequestBody MedicalDto medical ){
        int res=MedicalService.update(medical);
        if (res>0){
        return "修改信息成功!";
        }else {
        return "修改信息失败!";
        }
    }



    public static void main(String argv[]){
        SpringApplication.run(MedicalController.class,argv);
    }

}
