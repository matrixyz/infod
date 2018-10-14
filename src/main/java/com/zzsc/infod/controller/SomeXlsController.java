package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.model.SomeXlsDto;
import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/SomeXls")
public class SomeXlsController {

    @Autowired
    ServletContext applications;



    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String  getAnalyseRusultList( Model model, SomeXlsDto financeFeedDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(financeFeedDto.getPage()) ) {
            financeFeedDto.setPage("1");
        }
        String appDataName=null;
        String dataTitle=null;
        if(type.equals(Constant.someXls)){

            appDataName=Constant.someXlsApplication;
            dataTitle=Constant.dataTitleSomeXls ;

            model.addAttribute("checkExportType","outPutExcelCheck");
            model.addAttribute("outPutExcelTypeUrl","outPutExcel");
        }else if(type.equals(Constant.someXlsAllApplication)){
            appDataName=Constant.someXlsAllApplication;
            dataTitle=Constant.dataTitleSomeXlsAll ;

            model.addAttribute("checkExportType","outPutExcelCheckAll");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelAll");
        }
        boolean flag=false;
        if(applications.getAttribute(appDataName)!=null){
            List<FinanceFeedDto> lists=( List<FinanceFeedDto>)applications.getAttribute(appDataName);
            if(lists.size()==0){
                flag=true;
            }else {
                int pageNum= Integer.parseInt(financeFeedDto.getPage());
                long targetCount=lists.stream().filter(x-> x.getRepeatTimes()>0).count();
                model.addAttribute("dataTitle",dataTitle+targetCount+" 条");
                PageBean pageInfo=new PageBean();
                pageInfo.setTotalCount(lists.size());
                pageInfo.setPageNo(pageNum);
                model.addAttribute("pageInfo",pageInfo);
               model.addAttribute("SomeXlsList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));


                model.addAttribute("queryParams",financeFeedDto);
                model.addAttribute("type",type);
            }


        }else{
            flag=true;
        }
        if(flag){
            List<SomeXlsDto> lists=new ArrayList<>();
            SomeXlsDto SomeXlsDto=new SomeXlsDto();
            SomeXlsDto.setName("暂无数据");
            lists.add(SomeXlsDto);

            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(1);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("SomeXlsList",lists );

            model.addAttribute("queryParams",SomeXlsDto);
            model.addAttribute("type",type);
        }
        return  "adm/AnalyseExcelSomeXlsResut-list";
    }






    public static void main(String argv[]){
        SpringApplication.run(SomeXlsController.class,argv);
    }

}
