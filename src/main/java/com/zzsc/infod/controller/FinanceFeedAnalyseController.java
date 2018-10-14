package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.service.FinanceFeedAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.NumUtil;
import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/FinanceFeedAnalyse")
public class FinanceFeedAnalyseController {

    @Autowired
    private FinanceFeedAnalyseServiceExcel financeFeedAnalyseServiceExcel;

    @Value( "${financeFeed.city.upload.path}")
    private String financeFeedCityUpload;
    @Value( "${financeFeed.vallage.upload.path}")
    private String financeFeedVallageUpload;

    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;

    private Logger logger =   LoggerFactory.getLogger(this.getClass());


   @RequestMapping(value="/CityListview",method= RequestMethod.GET )
    public String getCityListview(Model model, AnalyseExcelUploadDto analyseExcelUploadDto  ){
        model.addAttribute("getFileListUrl","/FinanceFeedAnalyse/CityList");
        model.addAttribute("analyseType","/FinanceFeedAnalyse/analyseCity");
        model.addAttribute("actionUrl","/FinanceFeedAnalyse/CityListview");
        model.addAttribute("type",Constant.financeFeedCity);
        model.addAttribute("uploadType",Constant.FinanceFeedAnalyse);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.financeFeedCityFileApplication)==null){
            list=financeFeedAnalyseServiceExcel.getAnalyseExcelUploadDtoList(financeFeedCityUpload);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.financeFeedCityFileApplication);
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

        return "adm/AnalyseExcelFinanceFeedUpload-list";
    }
    @RequestMapping(value="/VallageListview",method= RequestMethod.GET )
    public String getVallageListview(Model model,AnalyseExcelUploadDto analyseExcelUploadDto  ){

        model.addAttribute("getFileListUrl","/FinanceFeedAnalyse/VallageList");
        model.addAttribute("analyseType","/FinanceFeedAnalyse/analyseVallage");
        model.addAttribute("actionUrl","/FinanceFeedAnalyse/VallageListview");
        model.addAttribute("type",Constant.financeFeedVallage);
        model.addAttribute("uploadType",Constant.FinanceFeedAnalyse);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.financeFeedVallageFileApplication)==null){
            list=financeFeedAnalyseServiceExcel.getAnalyseExcelUploadDtoList(financeFeedVallageUpload);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.financeFeedVallageFileApplication);
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

        return "adm/AnalyseExcelFinanceFeedUpload-list";
    }

    @ResponseBody
    @RequestMapping("/getProgress")
    public String getProgress(  ){
        if(session.getAttribute("uploadProgress")==null){
            return "0";
        }
        return session.getAttribute("uploadProgress").toString();
    }

    @ResponseBody
    @RequestMapping("/upload")
    public String  fileUpload(@RequestParam(value = "inputfile",required = false) MultipartFile[] files,
                              @RequestParam(value = "type",required = true) String type  ) throws IOException {
        for ( MultipartFile file:files) {
            String fileName = file.getOriginalFilename();
            if(!fileName.endsWith(Constant.FILE_EXT_XLS)&&!fileName.endsWith(Constant.FILE_EXT_XLSX)){
                return Constant.ERR_UPLOAD_FILE_TYPE;
            }
        }
        String uploadPath=null;
        session.setAttribute("uploadProgress",0);
        Map<String,InputStream> files_;
        files_ = new HashMap<>();
        if(files.length>0){
            if(type.equals(Constant.financeFeedCity)){
                uploadPath=financeFeedCityUpload;
            }else if(type.equals(Constant.financeFeedVallage)){
                uploadPath=financeFeedVallageUpload;

            }
            FileUtil.createPath(uploadPath);
            FileUtil.emptyPath(uploadPath);
        }
        int progress=0;
        Map<String, FinanceFeedDto> res =new HashMap<>();
        List<AnalyseExcelUploadDto> fileList=new ArrayList<>();
        int id=1;
        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();
            if (file.getSize()<Constant.maxFileSize){
                File file_=new File(uploadPath + "/" + fileName);
                file.transferTo(file_);
                progress++;
                session.setAttribute("uploadProgress", NumUtil.getProgress(files.length,progress));
                AnalyseExcelUploadDto info=new AnalyseExcelUploadDto();
                info.setFileName(fileName);
                info.setFileSize(FileUtil.getFileSize('k',file.getSize()));
                info.setFileType("Excel");
                info.setUploadProgress(100);
                info.setResult("上传成功");
                info.setId(id++);
                fileList.add(info);
            } else{
                return (Constant.ERR_FILE_MAX_SIZE);
            }
        }



        if(type.equals(Constant.financeFeedCity)){
            //applications.setAttribute(Constant.financeFeedCityApplication,res);
            applications.setAttribute(Constant.financeFeedCityFileApplication ,fileList);

        }
        if(type.equals(Constant.financeFeedVallage)){
            applications.setAttribute(Constant.financeFeedVallageFileApplication ,fileList);

        }


        return Constant.SUCCESS;



    }

    @ResponseBody
    @RequestMapping(value="/analyseCity",method= RequestMethod.GET)
    public String  analyseCity(  HttpServletRequest request) throws IOException {

        Object target=applications.getAttribute(Constant.financeFeedCityApplication);
        if(target==null){
            target=financeFeedAnalyseServiceExcel.initByPath(financeFeedCityUpload,Constant.financeFeedCity);
            if(target==null)
                return Constant.ERR_NO_FINANCEFEED_CITY_FILE;
            applications.setAttribute(Constant.financeFeedCityApplicationMap,target);
        }

        if (  target instanceof Map ){
            Map<String, FinanceFeedDto> res=(Map<String, FinanceFeedDto> )target;
            if(!res.isEmpty()){
                List<FinanceFeedDto> mapKeyList = new ArrayList<FinanceFeedDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.financeFeedCityApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if(  target instanceof List ){
            return Constant.SUCCESS;
        }
        return  Constant.ERR;
    }

    @ResponseBody
    @RequestMapping(value="/analyseVallage",method= RequestMethod.GET)
    public String  analyseVallage(  HttpServletRequest request) throws IOException {
        Object target=applications.getAttribute(Constant.financeFeedVallageApplication);
        if(target==null){
            target=financeFeedAnalyseServiceExcel.initByPath(financeFeedVallageUpload,Constant.financeFeedVallage);
            if(target==null)
                return Constant.ERR_NO_FINANCEFEED_VALLAGE_FILE;
            applications.setAttribute(Constant.financeFeedVallageApplicationMap,target);
        }
        if( target instanceof Map ){
            Map<String, FinanceFeedDto> res=(Map<String, FinanceFeedDto> )target;
            if(!res.isEmpty()){
                List<FinanceFeedDto> mapKeyList = new ArrayList<FinanceFeedDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.financeFeedVallageApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if( target instanceof List ){
            return Constant.SUCCESS;
        }
        return  Constant.ERR;
    }
    @ResponseBody
    @RequestMapping(value="/analyseAll",method= RequestMethod.GET)
    public String  analyseAll(  HttpServletRequest request) throws IOException {
        Map<String, FinanceFeedDto> all=null;
        {
            Object targetVallage=applications.getAttribute(Constant.financeFeedVallageApplicationMap);
            Object targetCity=applications.getAttribute(Constant.financeFeedCityApplicationMap);
            if(targetVallage==null){
                return Constant.ERR_VALLAGE_ANALYSE_NOT_YET_FINANCEFEED;
            }
            if(targetCity==null){
                return Constant.ERR_CITY_ANALYSE_NOT_YET_FINANCEFEED;
            }
            Map<String, FinanceFeedDto> resVallage=(Map<String, FinanceFeedDto> )targetVallage;
            Map<String, FinanceFeedDto> resCity=(Map<String, FinanceFeedDto> )targetCity;
            all=financeFeedAnalyseServiceExcel.initMerge(resCity,resVallage);
            applications.setAttribute(Constant.financeFeedAllApplicationMap,all);
        }


        if(!all.isEmpty()){
            List<FinanceFeedDto> mapKeyList = new ArrayList<FinanceFeedDto>(all.values());
            mapKeyList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
            mapKeyList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());
            applications.setAttribute(Constant.financeFeedAllApplication,mapKeyList);
            return Constant.SUCCESS;
        }

        return  Constant.ERR;

    }
    //导出数据时检查是否有重复数据
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckAll",method= RequestMethod.GET )
    public String checkFinanceFeedDifExcelFile(   ){

        return financeFeedAnalyseServiceExcel.checkFinanceFeedDifExcelFile(
                applications,Constant.financeFeedAllApplication,
                Constant.ERR_ALL_ANALYSE_NOT_YET_FINANCEFEED,Constant.EMPTY_ALL_ANALYSE_NOT_YET_FINANCEFEED);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckCity",method= RequestMethod.GET )
    public String checkFinanceFeedDifExcelFileCity(    ){

        return financeFeedAnalyseServiceExcel.checkFinanceFeedDifExcelFile(
                applications,Constant.financeFeedCityApplication,
                Constant.ERR_CITY_ANALYSE_NOT_YET_FINANCEFEED,Constant.EMPTY_ALL_ANALYSE_NOT_YET_FINANCEFEED);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckVallage",method= RequestMethod.GET )
    public String checkFinanceFeedDifExcelFileVallage(    ){

        return financeFeedAnalyseServiceExcel.checkFinanceFeedDifExcelFile(
                applications,Constant.financeFeedVallageApplication,
                Constant.ERR_VALLAGE_ANALYSE_NOT_YET_FINANCEFEED,Constant.EMPTY_ALL_ANALYSE_NOT_YET_FINANCEFEED);

    }
    @RequestMapping(value="/outPutExcelAll",method= RequestMethod.GET )
    public String getListFinanceFeedDifExcelFileAll(HttpServletResponse response  ){
        financeFeedAnalyseServiceExcel.getListFinanceFeedDifExcelFile(applications,response,Constant.financeFeedAllApplication,Constant.dataTitleFinanceFeedAll);
        return null;
    }
    @RequestMapping(value="/outPutExcelCity",method= RequestMethod.GET )
    public String getListFinanceFeedDifExcelFileCity(HttpServletResponse response  ){
        financeFeedAnalyseServiceExcel.getListFinanceFeedDifExcelFile(applications,response,Constant.financeFeedCityApplication,Constant.dataTitleFinanceFeedCity);
        return null;
    }
    @RequestMapping(value="/outPutExcelVallage",method= RequestMethod.GET )
    public String getListFinanceFeedDifExcelFileVallage(HttpServletResponse response  ){
        financeFeedAnalyseServiceExcel.getListFinanceFeedDifExcelFile(applications,response,Constant.financeFeedVallageApplication,Constant.dataTitleFinanceFeedVallage);
        return null;
    }

   
}
