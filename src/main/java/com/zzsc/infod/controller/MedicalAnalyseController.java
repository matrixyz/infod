package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/MedicalAnalyse")
public class MedicalAnalyseController {

    @Autowired
    private MedicalAnalyseServiceExcel medicalAnalyseServiceExcel;

    @Value( "${medical.city.upload.path}")
    private String medicalCityUpload;
    @Value( "${medical.vallage.upload.path}")
    private String medicalVallageUpload;

    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;


    @ResponseBody
    @RequestMapping(value="/CityList",method= RequestMethod.GET )
    public List<AnalyseExcelUploadDto> getCityList(MedicalDto MedicalDto  ){
        return medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(medicalCityUpload);
    }
    @ResponseBody
    @RequestMapping(value="/VallageList",method= RequestMethod.GET )
    public List<AnalyseExcelUploadDto> getVallageList(MedicalDto MedicalDto  ){
        return medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(medicalVallageUpload);
    }


    @RequestMapping(value="/CityListview",method= RequestMethod.GET )
    public String getCityListview(Model model, MedicalDto MedicalDto  ){

        model.addAttribute("getFileListUrl","/MedicalAnalyse/CityList");
        model.addAttribute("analyseType","/MedicalAnalyse/analyseCity");
        model.addAttribute("analyseResultUrl","/MedicalAnalyse/analyseCity");
        model.addAttribute("type",Constant.medicalCity);
        model.addAttribute("uploadType",Constant.MedicalAnalyse);
        return "adm/AnalyseExcelUpload-list";
    }
    @RequestMapping(value="/VallageListview",method= RequestMethod.GET )
    public String getVallageListview(Model model, MedicalDto MedicalDto  ){

        model.addAttribute("getFileListUrl","/MedicalAnalyse/VallageList");
        model.addAttribute("analyseType","/MedicalAnalyse/analyseVallage");

        model.addAttribute("type",Constant.medicalVallage);
        model.addAttribute("uploadType",Constant.MedicalAnalyse);

        return "adm/AnalyseExcelUpload-list";
    }


    @ResponseBody
    @RequestMapping("/upload")
    public List<AnalyseExcelUploadDto>  fileUpload(@RequestParam(value = "inputfile",required = false) MultipartFile[] files,
                                                   @RequestParam(value = "type",required = true) String type,
                                                   HttpServletRequest request) throws IOException {

        String uploadPath=null;
        if(files.length>0){
            if(type.equals(Constant.medicalCity)){
                uploadPath=medicalCityUpload;
            }else if(type.equals(Constant.medicalVallage)){
                uploadPath=medicalVallageUpload;

            }
            FileUtil.emptyPath(uploadPath);
        }

        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        int id=1;
        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();

            AnalyseExcelUploadDto info=new AnalyseExcelUploadDto();

            info.setFileName(fileName);
            info.setDataSize(FileUtil.getFileSize('k',file.getSize()));
            info.setFileType("Excel");
            info.setUploadProgress(100);
            info.setResult("上传成功");
            info.setId(id++);
            list.add(info);
            File temp=new File(uploadPath+"\\"+fileName);
            file.getSize();


            if (file.getSize()<Constant.maxFileSize){
                file.transferTo(temp);

            } else{
                System.out.println(Constant.ERR_FILE_MAX_SIZE);
            }
        }


        return list;



    }
    @ResponseBody
    @RequestMapping(value="/analyseCity",method= RequestMethod.GET)
    public String  analyseCity(  HttpServletRequest request) throws IOException {


            Map<String, MedicalDto> res=medicalAnalyseServiceExcel.init(medicalCityUpload,Constant.medicalCity);
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());

                applications.setAttribute(Constant.medicalCityApplication,mapKeyList);
                return Constant.SUCCESS;
            }





        return  Constant.ERR;

    }

    @ResponseBody
    @RequestMapping(value="/analyseVallage",method= RequestMethod.GET)
    public String  analyseVallage(  HttpServletRequest request) throws IOException {


            Map<String, MedicalDto> res=medicalAnalyseServiceExcel.init(medicalVallageUpload,Constant.medicalVallage);
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());

                applications.setAttribute(Constant.medicalVallageApplication,mapKeyList);
                return Constant.SUCCESS;
            }





        return  Constant.ERR;

    }
    @ResponseBody
    @RequestMapping(value="/analyseAll",method= RequestMethod.GET)
    public String  analyseAll(  HttpServletRequest request) throws IOException {


            Map<String, MedicalDto> all=medicalAnalyseServiceExcel.initMerge(medicalCityUpload,medicalVallageUpload);

            if(!all.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(all.values());

                applications.setAttribute(Constant.medicalAllApplication,mapKeyList);
                return Constant.SUCCESS;
            }

        return  Constant.ERR;

    }



    @RequestMapping(value="/getAnalyseRusultList",method= RequestMethod.GET)
    public String  excelAnalyseList( Model model, MedicalDto MedicalDto, @RequestParam(value = "type",required = true) String type  ) throws IOException {
        if (StringUtil.isEmpty(MedicalDto.getPage()) ) {
            MedicalDto.setPage("1");
        }
        String appDataName=null;
        if(type.equals(Constant.medicalCity)){
            appDataName=Constant.medicalCityApplication;
        }  else if(type.equals(Constant.medicalVallage)){
            appDataName=Constant.medicalVallageApplication;
        }else{
            appDataName=Constant.medicalAllApplication;
        }
        if(applications.getAttribute(appDataName)!=null){
            List<MedicalDto> lists=( List<MedicalDto>)applications.getAttribute(appDataName);
            int pageNum= Integer.parseInt(MedicalDto.getPage());
            PageBean pageInfo=new PageBean();
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(pageNum);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("MedicalList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));

            model.addAttribute("queryParams",MedicalDto);
            model.addAttribute("type",type);
        }
        return  "adm/AnalyseExcelResut-list";
    }


}
