package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.FinanceFeedDto;
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
@RequestMapping("/FinanceFeed")
public class FinanceFeedController {

    @Autowired
    ServletContext applications;



    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String  getAnalyseRusultList( Model model, FinanceFeedDto financeFeedDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(financeFeedDto.getPage()) ) {
            financeFeedDto.setPage("1");
        }
        String appDataName=null;
        String dataTitle=null;
        if(type.equals(Constant.financeFeedCity)){
            appDataName=Constant.financeFeedCityApplication;

            dataTitle=Constant.dataTitleFinanceFeedCity ;
            model.addAttribute("checkExportType","outPutExcelCheckCity");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelCity");

        }  else if(type.equals(Constant.financeFeedVallage)){
            appDataName=Constant.financeFeedVallageApplication;

            dataTitle=Constant.dataTitleFinanceFeedVallage ;
            model.addAttribute("checkExportType","outPutExcelCheckVallage");
            model.addAttribute("outPutExcelTypeUrl","outPutExcelVallage");

        }else{
            appDataName=Constant.financeFeedAllApplication;
            dataTitle=Constant.dataTitleFinanceFeedAll ;

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
                model.addAttribute("dataTitle",dataTitle );
                model.addAttribute("dataCount", targetCount );
                PageBean pageInfo=new PageBean();
                pageInfo.setTotalCount(lists.size());
                pageInfo.setPageNo(pageNum);
                model.addAttribute("pageInfo",pageInfo);
               model.addAttribute("FinanceFeedList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));


                model.addAttribute("queryParams",financeFeedDto);
                model.addAttribute("type",type);
            }


        }else{
            flag=true;
        }
        if(flag){
            List<FinanceFeedDto> lists=new ArrayList<>();
            FinanceFeedDto FinanceFeedDto=new FinanceFeedDto();
            FinanceFeedDto.setName("暂无数据");
            lists.add(FinanceFeedDto);

            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(1);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("FinanceFeedList",lists );

            model.addAttribute("queryParams",FinanceFeedDto);
            model.addAttribute("type",type);
        }
        return  "adm/AnalyseExcelFinanceFeedResut-list";
    }






    public static void main(String argv[]){
        SpringApplication.run(FinanceFeedController.class,argv);
    }

}
