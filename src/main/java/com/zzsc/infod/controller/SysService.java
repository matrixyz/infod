package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/SysService")
public class SysService {


    @Value( "${medical.city.upload.path}")
    private String medicalCityUpload;
    @Value( "${medical.vallage.upload.path}")
    private String medicalVallageUpload;

    @Value( "${endowment.city.upload.path}")
    private String endowmentCityUpload;
    @Value( "${endowment.vallage.upload.path}")
    private String endowmentVallageUpload;
    @Autowired
    ServletContext applications;


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
}
