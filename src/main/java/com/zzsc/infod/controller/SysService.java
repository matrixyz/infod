package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.SysUser;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
    @Value( "${financeFeed.city.upload.path}")
    private String financeFeedCityUpload;
    @Value( "${financeFeed.vallage.upload.path}")
    private String financeFeedVallageUpload;
    @Value( "${someXls.upload.path}")
    private String someXlsUpload;

    @Value( "${sys.err.path}")
    private String sysErrPath;
    @Value( "${sys.info.path}")
    private String sysInfoPath;

    String sysErrPathRealPath;
    String sysInfoPathRealPath;

    @PostConstruct
    public void setPath(){
        sysErrPathRealPath =FileUtil.getBaseJarPath()+"/"+sysErrPath ;
        sysInfoPathRealPath=FileUtil.getBaseJarPath()+"/"+sysInfoPath ;
    }

    @Autowired
    ServletContext applications;

    @Autowired
    private MedicalAnalyseServiceExcel medicalAnalyseServiceExcel;

    @RequestMapping(value="/clearAll",method= RequestMethod.GET )
    public String clearAll(HttpServletRequest request){

        FileUtil.emptyPath( FileUtil.fixRealPath(medicalCityUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(medicalVallageUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(endowmentCityUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(endowmentVallageUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(financeFeedCityUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(financeFeedVallageUpload));
        FileUtil.emptyPath( FileUtil.fixRealPath(someXlsUpload));

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

        applications.setAttribute(Constant.financeFeedAllApplication,null);
        applications.setAttribute(Constant.financeFeedCityApplication,null);
        applications.setAttribute(Constant.financeFeedVallageApplication,null);
        applications.setAttribute(Constant.financeFeedCityFileApplication,null);
        applications.setAttribute(Constant.financeFeedVallageFileApplication,null);

        applications.setAttribute(Constant.someXlsAllApplication,null);
        applications.setAttribute(Constant.someXlsApplication,null);
        applications.setAttribute(Constant.someXlsFileApplication,null);
        HttpSession session = request.getSession(true);

        Object u=session.getAttribute("user");
        if(u!=null){
            SysUser ux=(SysUser)u;
            if(ux.getUserName().equals("管理员"))
                return "redirect:/MedicalAnalyse/CityListview";
        }

        return "redirect:/SomeXlsAnalyse/Listview";

    }

    @RequestMapping(value="/help",method= RequestMethod.GET )
    public String help(HttpServletRequest request){

        return "adm/help";

    }
    //查看日志信息
    @RequestMapping(value="/log/{type}",method= RequestMethod.GET )
    public String getErrLs(Model model, @PathVariable String type,AnalyseExcelUploadDto analyseExcelUploadDto){

        model.addAttribute("actionUrl","/SysService/log/"+type);
        List<AnalyseExcelUploadDto> list=null;
        if("err".equals(type)){
            model.addAttribute("filePath", sysErrPath);
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(sysErrPathRealPath);
        }else{
            model.addAttribute("filePath", sysInfoPath);
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(sysInfoPathRealPath);
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
    //将excel 文件转换为html  内容并在网页中显示出来
    @RequestMapping(value="/showExcelContent",method= RequestMethod.GET )
    public String showExcelContent(@RequestParam(value = "filePath",required = false) String filePath, HttpServletResponse res){

        String path=FileUtil.getBaseJarPath()+"/"+filePath;
        File file = new File(path);
        res.setContentType("text/html;charset=utf-8");

        try {
            PrintWriter out = null;
            out = res.getWriter();
            if(file.exists()){

                String html= POIExcelToHtmlA.excelToHtml(file);
                    out.print(html);
            }else {
                out.print("文件不存在或已被删除!"+"<a href=\"javascript:window.opener=null;window.close();\">点击关闭</a>");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //读取日志文本内容在网页中显示出来
    @RequestMapping(value="/showLogInfoContent",method= RequestMethod.GET )
    public String showLogInfoContent(@RequestParam(value = "filePath",required = false) String filePath, HttpServletResponse res){

        String path=FileUtil.getBaseJarPath()+"/"+filePath;
        File file = new File(path);
        res.setContentType("text/html;charset=utf-8");

        try {
            PrintWriter out = null;
            out = res.getWriter();
            if(file.exists()){

                String html= FileUtils.readTxt(file);
                    out.print(html);
            }else {
                out.print("文件不存在或已被删除!"+"<a href=\"javascript:window.opener=null;window.close();\">点击关闭</a>");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
