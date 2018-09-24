package com.zzsc.infod.controller;
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.service.EndowmentService;
import com.zzsc.infod.model.*;

import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.zzsc.infod.util.UuidUtil;
@Controller
@EnableAutoConfiguration
@RequestMapping("/Endowment")
public class EndowmentController {
    @Autowired
    private EndowmentService EndowmentService;
    @Autowired
    ServletContext applications;

    @RequestMapping(value="",method= RequestMethod.GET )
    public Endowment get(Model model,EndowmentDto endowment ){
        Endowment endowmentTemp = EndowmentService.getCondition(endowment) ;
        if (  endowmentTemp !=null){
            return  endowmentTemp;
        }

        return null;
    }

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String  getAnalyseRusultList( Model model, EndowmentDto EndowmentDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(EndowmentDto.getPage()) ) {
            EndowmentDto.setPage("1");
        }
        String appDataName=null;
        if(type.equals(Constant.endowmentCity)){
            appDataName=Constant.endowmentCityApplication;
            model.addAttribute("dataTitle",Constant.dataTitleEndowmentCity);

        }  else if(type.equals(Constant.endowmentVallage)){
            appDataName=Constant.endowmentVallageApplication;
            model.addAttribute("dataTitle",Constant.dataTitleEndowmentVallage);

        }else{
            appDataName=Constant.endowmentAllApplication;
            model.addAttribute("dataTitle",Constant.dataTitleEndowmentAll);

        }
        boolean flag=false;
        if(applications.getAttribute(appDataName)!=null){
            List<EndowmentDto> lists=( List<EndowmentDto>)applications.getAttribute(appDataName);
            if(lists.size()==0){
                flag=true;
            }else {
                int pageNum= Integer.parseInt(EndowmentDto.getPage());
                PageBean pageInfo=new PageBean();
                pageInfo.setTotalCount(lists.size());
                pageInfo.setPageNo(pageNum);
                model.addAttribute("pageInfo",pageInfo);
                model.addAttribute("EndowmentList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));

                model.addAttribute("queryParams",EndowmentDto);
                model.addAttribute("type",type);
            }


        }else{
            flag=true;
        }
        if(flag){
            List<EndowmentDto> lists=new ArrayList<>();
            EndowmentDto endowmentDto=new EndowmentDto();
            endowmentDto.setName("暂无数据");
            lists.add(endowmentDto);

            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(1);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("EndowmentList",lists );

            model.addAttribute("queryParams",EndowmentDto);
            model.addAttribute("type",type);
        }
        return  "adm/AnalyseExcelEndowmentResut-list";
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
