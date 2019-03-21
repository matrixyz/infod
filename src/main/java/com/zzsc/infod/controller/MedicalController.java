package com.zzsc.infod.controller;
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.service.MedicalService;
import com.zzsc.infod.model.*;

import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.zzsc.infod.util.UuidUtil;
@Controller
@EnableAutoConfiguration
@RequestMapping("/Medical")
public class MedicalController {
    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;
    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String  getAnalyseRusultList( Model model, MedicalDto MedicalDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(MedicalDto.getPage()) ) {
            MedicalDto.setPage("1");
        }
        String appDataName=null;
        String dataTitle=null;

        if(type.equals(Constant.medicalCity)){
            appDataName=Constant.medicalCityApplication;
            dataTitle=Constant.dataTitleMedicalCity ;
            model.addAttribute("checkExportType","outPutExcelCheckCity");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelCity");

        }  else if(type.equals(Constant.medicalVallage)){
            appDataName=Constant.medicalVallageApplication;
            dataTitle=Constant.dataTitleMedicalVallage ;
            model.addAttribute("checkExportType","outPutExcelCheckVallage");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelVallage");

        }else{
            appDataName=Constant.medicalAllApplication;
            dataTitle=Constant.dataTitleMedicalAll ;
            model.addAttribute("checkExportType","outPutExcelCheckAll");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelAll");

        }

        if(applications.getAttribute(appDataName)!=null&&(( List<MedicalDto>)applications.getAttribute(appDataName)).size()>0){
            List<MedicalDto> lists=( List<MedicalDto>)applications.getAttribute(appDataName);
            int pageNum= Integer.parseInt(MedicalDto.getPage());
            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            long targetCount=lists.stream().filter(x-> x.getRepeatTimes()>0).count();
            model.addAttribute("dataTitle",dataTitle );
            model.addAttribute("dataCount", targetCount );

            pageInfo.setPageNo(pageNum);
            model.addAttribute("targetCount",targetCount);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("MedicalList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));
            model.addAttribute("queryParams",MedicalDto);
            model.addAttribute("type",type);

        }else{
            List<MedicalDto> lists=new ArrayList<>();
            MedicalDto medicalDto=new MedicalDto();
            medicalDto.setName("暂无数据");
            lists.add(medicalDto);

            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(1);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("MedicalList",lists );

            model.addAttribute("queryParams",MedicalDto);
            model.addAttribute("type",type);
        }
        return  "adm/AnalyseExcelResut-list";
    }

    public static void main(String argv[]){
        SpringApplication.run(MedicalController.class,argv);
    }

}
