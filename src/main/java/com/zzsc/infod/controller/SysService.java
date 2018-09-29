package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/SysService")
public class SysService {

    private Logger logger =   LoggerFactory.getLogger(this.getClass());

    @Value( "${medical.city.upload.path}")
    private String medicalCityUpload;
    @Value( "${medical.vallage.upload.path}")
    private String medicalVallageUpload;

    @Value( "${endowment.city.upload.path}")
    private String endowmentCityUpload;
    @Value( "${endowment.vallage.upload.path}")
    private String endowmentVallageUpload;
    @Value( "${sys.err.path}")
    private String sysErrPath;
    @Autowired
    ServletContext applications;

    @Autowired
    private MedicalAnalyseServiceExcel medicalAnalyseServiceExcel;

    @RequestMapping(value="/loginOut",method= RequestMethod.GET )
    public String login(HttpServletRequest request){
        request.getSession().invalidate();

        return "redirect:/index.html";

    }
    @RequestMapping(value="/clearAll",method= RequestMethod.GET )
    public String clearAll(HttpServletRequest request){


        FileUtil.emptyPath(medicalCityUpload);
        FileUtil.emptyPath(medicalVallageUpload);
        FileUtil.emptyPath(endowmentCityUpload);
        FileUtil.emptyPath(endowmentVallageUpload);


        applications.setAttribute(Constant.medicalAllApplication,null);
        applications.setAttribute(Constant.medicalCityApplication,null);
        applications.setAttribute(Constant.medicalVallageApplication,null);
        applications.setAttribute(Constant.endowmentAllApplication,null);
        applications.setAttribute(Constant.endowmentCityApplication,null);
        applications.setAttribute(Constant.endowmentVallageApplication,null);
        applications.setAttribute(Constant.medicalCityFileApplication,null);
        applications.setAttribute(Constant.medicalVallageFileApplication,null);
        applications.setAttribute(Constant.endowmentCityFileApplication,null);
        applications.setAttribute(Constant.endowmentVallageFileApplication,null);

        return "redirect:/MedicalAnalyse/CityListview";

    }

    @RequestMapping(value="/help",method= RequestMethod.GET )
    public String help(HttpServletRequest request){



        return "adm/help";

    }

    @RequestMapping(value="/errLs",method= RequestMethod.GET )
    public String getVallageListview(Model model,AnalyseExcelUploadDto analyseExcelUploadDto,
                                     @RequestParam(value = "type",required = false) String type ){


        model.addAttribute("actionUrl","/SysService/errLs");

        List<AnalyseExcelUploadDto> list=null;
        if("err".equals(type)){
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(sysErrPath);
        }else{
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(sysErrPath);
        }
        if (StringUtil.isEmpty(analyseExcelUploadDto.getPage()) ) {
            analyseExcelUploadDto.setPage("1");
        }
        int pageNum= Integer.parseInt(analyseExcelUploadDto.getPage());
        PageBean pageInfo=new PageBean();
        pageInfo.setTotalCount(list.size());
        pageInfo.setPageNo(pageNum);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("FileList",list.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));

        return "adm/AnalyseSysLog-list";
    }

}
