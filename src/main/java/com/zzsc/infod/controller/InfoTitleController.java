package com.zzsc.infod.controller;
import com.zzsc.infod.service.InfoTitleService;
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
@RequestMapping("/InfoTitle")
public class InfoTitleController {
    @Autowired
    private InfoTitleService InfoTitleService;

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.GET )
    public InfoTitle get(Model model,InfoTitleDto infoTitle ){
        InfoTitle infoTitleTemp = InfoTitleService.getCondition(infoTitle) ;
        if (  infoTitleTemp !=null){
            return  infoTitleTemp;
        }

        return null;
    }

    @ResponseBody
    @RequestMapping(value="/list",method= RequestMethod.GET )
    public List<InfoTitleDto> getList( InfoTitleDto infoTitleDto  ){

        if (infoTitleDto.getPage() == null||"".equals(infoTitleDto.getPage())) {
            infoTitleDto.setPage("1");
        }
        List<InfoTitleDto> list=InfoTitleService.list(infoTitleDto);
        return  list ;
    }

    @RequestMapping(value="/addPrepared",method= RequestMethod.GET )
    public String addPrepared(Model model,InfoTitleDto infoTitleDto ){
        infoTitleDto.setTid(UuidUtil.get16UUID());

        model.addAttribute("submitType", "POST");
        model.addAttribute("InfoTitleDto", infoTitleDto);
        return "adm/InfoTitle-form";
    }

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.POST )
    public String post(@RequestBody InfoTitleDto infoTitle ){
        int res=InfoTitleService.insert(infoTitle);
        if (res>0){
            return "添加信息成功!";
        }else {
            return "添加信息失败!";
        }
    }
    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.PUT )
    public String put(@RequestBody InfoTitleDto infoTitle ){
        int res=InfoTitleService.update(infoTitle);
        if (res>0){
        return "修改信息成功!";
        }else {
        return "修改信息失败!";
        }
    }



    public static void main(String argv[]){
        SpringApplication.run(InfoTitleController.class,argv);
    }

}
