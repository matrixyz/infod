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
    ServletContext applications;



    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String  getAnalyseRusultList( Model model, EndowmentDto EndowmentDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(EndowmentDto.getPage()) ) {
            EndowmentDto.setPage("1");
        }
        String appDataName=null;
        String dataTitle=null;
        if(type.equals(Constant.endowmentCity)){
            appDataName=Constant.endowmentCityApplication;

            dataTitle=Constant.dataTitleEndowmentCity ;
            model.addAttribute("checkExportType","outPutExcelCheckCity");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelCity");

        }  else if(type.equals(Constant.endowmentVallage)){
            appDataName=Constant.endowmentVallageApplication;

            dataTitle=Constant.dataTitleEndowmentVallage ;
            model.addAttribute("checkExportType","outPutExcelCheckVallage");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelVallage");

        }else{
            appDataName=Constant.endowmentAllApplication;
            dataTitle=Constant.dataTitleEndowmentAll ;

            model.addAttribute("checkExportType","outPutExcelCheckAll");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelAll");
        }
        boolean flag=false;
        if(applications.getAttribute(appDataName)!=null){
            List<EndowmentDto> lists=( List<EndowmentDto>)applications.getAttribute(appDataName);
            if(lists.size()==0){
                flag=true;
            }else {
                int pageNum= Integer.parseInt(EndowmentDto.getPage());
                long targetCount=lists.stream().filter(x-> x.getRepeatTimes()>0).count();
                model.addAttribute("dataTitle",dataTitle+targetCount+" 条");
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






    public static void main(String argv[]){
        SpringApplication.run(EndowmentController.class,argv);
    }

}
