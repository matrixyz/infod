package com.zzsc.infod.controller;

import com.ApplicationContextProvider;
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.service.ServiceFactory;
import com.zzsc.infod.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/EndowmentAnalyse")
public class EndowmentAnalyseController {

    Map<String, ServiceFactory> map = ApplicationContextProvider.getApplicationContext().getBeansOfType(ServiceFactory.class);


    @Autowired
    private EndowmentAnalyseServiceExcel endowmentAnalyseServiceExcel;

    @Value( "${endowment.city.upload.path}")
    private String endowmentCityUpload ;
    @Value( "${endowment.vallage.upload.path}")
    private String endowmentVallageUpload ;

    private String endowmentCityUploadRealPath ;
    private String endowmentVallageUploadRealPath ;
    @PostConstruct
    public void setPath(){
        endowmentCityUploadRealPath =FileUtil.getBaseJarPath()+"/"+endowmentCityUpload ;
        endowmentVallageUploadRealPath =FileUtil.getBaseJarPath()+"/"+endowmentVallageUpload ;
    }
    
    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;

    private Logger logger =   LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value="/CityListview",method= RequestMethod.GET )
    public String getCityListview(Model model, AnalyseExcelUploadDto analyseExcelUploadDto  ){
        model.addAttribute("getFileListUrl","/EndowmentAnalyse/CityList");
        model.addAttribute("analyseType","/EndowmentAnalyse/analyseCity");
        model.addAttribute("actionUrl","/EndowmentAnalyse/CityListview");
        model.addAttribute("type",Constant.endowmentCity);
        model.addAttribute("uploadType",Constant.EndowmentAnalyse);
        model.addAttribute("filePath", endowmentCityUpload);
        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.endowmentCityFileApplication)==null){
            list=endowmentAnalyseServiceExcel.getAnalyseExcelUploadDtoList(endowmentCityUploadRealPath);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.endowmentCityFileApplication);
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

        return "adm/AnalyseExcelEndowmentUpload-list";
    }
    @RequestMapping(value="/VallageListview",method= RequestMethod.GET )
    public String getVallageListview(Model model,AnalyseExcelUploadDto analyseExcelUploadDto  ){

        model.addAttribute("getFileListUrl","/EndowmentAnalyse/VallageList");
        model.addAttribute("analyseType","/EndowmentAnalyse/analyseVallage");
        model.addAttribute("actionUrl","/EndowmentAnalyse/VallageListview");
        model.addAttribute("type",Constant.endowmentVallage);
        model.addAttribute("uploadType",Constant.EndowmentAnalyse);
        model.addAttribute("filePath", endowmentVallageUpload);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.endowmentVallageFileApplication)==null){
            list=endowmentAnalyseServiceExcel.getAnalyseExcelUploadDtoList(endowmentVallageUploadRealPath);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.endowmentVallageFileApplication);
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

        return "adm/AnalyseExcelEndowmentUpload-list";
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
            if(type.equals(Constant.endowmentCity)){
                uploadPath=endowmentCityUploadRealPath;
            }else if(type.equals(Constant.endowmentVallage)){
                uploadPath=endowmentVallageUploadRealPath;

            }
            FileUtil.createPath(uploadPath);

            FileUtil.emptyPath(uploadPath);
        }
        int progress=0;
        Map<String, EndowmentDto> res =new HashMap<>();
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



        if(type.equals(Constant.endowmentCity)){
            //applications.setAttribute(Constant.endowmentCityApplication,res);
            applications.setAttribute(Constant.endowmentCityFileApplication ,fileList);

        }
        if(type.equals(Constant.endowmentVallage)){
            // applications.setAttribute(Constant.endowmentVallageApplication,res);
            applications.setAttribute(Constant.endowmentVallageFileApplication ,fileList);

        }

        // new MyThread2(files_,uploadPath).start();

        return Constant.SUCCESS;



    }

    @ResponseBody
    @RequestMapping(value="/analyseCity",method= RequestMethod.GET)
    public String  analyseCity(  HttpServletRequest request) throws IOException {
        for (String key : map.keySet()) {
            System.out.println(key);
            System.out.println(map.get(key));
        }
        Object target=applications.getAttribute(Constant.endowmentCityApplication);
        if(target==null){
            try {
                target=endowmentAnalyseServiceExcel.initByPath(endowmentCityUploadRealPath,Constant.endowmentCity);

            } catch (Exception e) {
                logger.error(e.getMessage());
                return e.getMessage();
            }
            if(target==null)
                return Constant.ERR_NO_ENDOWMENT_CITY_FILE;

            applications.setAttribute(Constant.endowmentCityApplicationMap,target);
        }

        if (  target instanceof Map ){
            Map<String, EndowmentDto> res=(Map<String, EndowmentDto> )target;
            if(!res.isEmpty()){
                List<EndowmentDto> mapKeyList = new ArrayList<EndowmentDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.endowmentCityApplication,mapKeyList);
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
        Object target=applications.getAttribute(Constant.endowmentVallageApplication);
        if(target==null){
            try {
                target=endowmentAnalyseServiceExcel.initByPath(endowmentVallageUploadRealPath,Constant.endowmentVallage);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return e.getMessage();
            }

            if(target==null)
                return Constant.ERR_NO_ENDOWMENT_VALLAGE_FILE;
            applications.setAttribute(Constant.endowmentVallageApplicationMap,target);
        }
        if( target instanceof Map ){
            Map<String, EndowmentDto> res=(Map<String, EndowmentDto> )target;
            if(!res.isEmpty()){
                List<EndowmentDto> mapKeyList = new ArrayList<EndowmentDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.endowmentVallageApplication,mapKeyList);
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
        Map<String, EndowmentDto> all=null;
        {
            Object targetVallage=applications.getAttribute(Constant.endowmentVallageApplicationMap);
            Object targetCity=applications.getAttribute(Constant.endowmentCityApplicationMap);
            if(targetVallage==null){
                return Constant.ERR_VALLAGE_ANALYSE_NOT_YET_ENDOWMENT;
            }
            if(targetCity==null){
                return Constant.ERR_CITY_ANALYSE_NOT_YET_ENDOWMENT;
            }
            Map<String, EndowmentDto> resVallage=(Map<String, EndowmentDto> )targetVallage;
            Map<String, EndowmentDto> resCity=(Map<String, EndowmentDto> )targetCity;
            all=endowmentAnalyseServiceExcel.initMerge(resCity,resVallage);

        }

        //Map<String, EndowmentDto> all=endowmentAnalyseServiceExcel.initMerge(endowmentCityUploadRealPath,endowmentVallageUploadRealPath);

        if(!all.isEmpty()){
            List<EndowmentDto> mapKeyList = new ArrayList<EndowmentDto>(all.values());
            mapKeyList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
            mapKeyList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());
            applications.setAttribute(Constant.endowmentAllApplication,mapKeyList);
            return Constant.SUCCESS;
        }

        return  Constant.ERR;

    }

    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckAll",method= RequestMethod.GET )
    public String checkEndowmentDifExcelFile(   ){

        return endowmentAnalyseServiceExcel.checkEndowmentDifExcelFile(
                applications,Constant.endowmentAllApplication,
                Constant.ERR_ALL_ANALYSE_NOT_YET_ENDOWMENT,Constant.EMPTY_ALL_ANALYSE_NOT_YET_ENDOWMENT);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckCity",method= RequestMethod.GET )
    public String checkEndowmentDifExcelFileCity(    ){

        return endowmentAnalyseServiceExcel.checkEndowmentDifExcelFile(
                applications,Constant.endowmentCityApplication,
                Constant.ERR_CITY_ANALYSE_NOT_YET_ENDOWMENT,Constant.EMPTY_ALL_ANALYSE_NOT_YET_ENDOWMENT);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckVallage",method= RequestMethod.GET )
    public String checkEndowmentDifExcelFileVallage(    ){

        return endowmentAnalyseServiceExcel.checkEndowmentDifExcelFile(
                applications,Constant.endowmentVallageApplication,
                Constant.ERR_VALLAGE_ANALYSE_NOT_YET_ENDOWMENT,Constant.EMPTY_ALL_ANALYSE_NOT_YET_ENDOWMENT);

    }
    @RequestMapping(value="/outPutExcelAll",method= RequestMethod.GET )
    public String getListEndowmentDifExcelFileAll(HttpServletResponse response  ){
        endowmentAnalyseServiceExcel.getListEndowmentDifExcelFile(applications,response,Constant.endowmentAllApplication,Constant.dataTitleEndowmentAll);
        return null;
    }
    @RequestMapping(value="/outPutExcelCity",method= RequestMethod.GET )
    public String getListEndowmentDifExcelFileCity(HttpServletResponse response  ){
        endowmentAnalyseServiceExcel.getListEndowmentDifExcelFile(applications,response,Constant.endowmentCityApplication,Constant.dataTitleEndowmentCity);
        return null;
    }
    @RequestMapping(value="/outPutExcelVallage",method= RequestMethod.GET )
    public String getListEndowmentDifExcelFileVallage(HttpServletResponse response  ){
        endowmentAnalyseServiceExcel.getListEndowmentDifExcelFile(applications,response,Constant.endowmentVallageApplication,Constant.dataTitleEndowmentVallage);
        return null;
    }

   
}
