package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.*;
import org.apache.tomcat.util.bcel.Const;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.zzsc.infod.service.impl" })
@MapperScan(basePackages={"com.zzsc.infod.mapper" })
@Controller
@RequestMapping("/MedicalAnalyse")
public class MedicalAnalyseController {
    public static void main(String argv[]){
        SpringApplication.run(MedicalAnalyseController.class,argv);
    }
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

    private Logger logger =   LoggerFactory.getLogger(this.getClass());



    @RequestMapping(value="/CityListview",method= RequestMethod.GET )
    public String getCityListview(Model model, AnalyseExcelUploadDto analyseExcelUploadDto  ){
      model.addAttribute("getFileListUrl","/MedicalAnalyse/CityList");
        model.addAttribute("analyseType","/MedicalAnalyse/analyseCity");
        model.addAttribute("actionUrl","/MedicalAnalyse/CityListview");
        model.addAttribute("type",Constant.medicalCity);
        model.addAttribute("uploadType",Constant.MedicalAnalyse);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.medicalCityFileApplication)==null){
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(medicalCityUpload);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.medicalCityFileApplication);
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

        return "adm/AnalyseExcelUpload-list";
    }
    @RequestMapping(value="/VallageListview",method= RequestMethod.GET )
    public String getVallageListview(Model model,AnalyseExcelUploadDto analyseExcelUploadDto  ){

        model.addAttribute("getFileListUrl","/MedicalAnalyse/VallageList");
        model.addAttribute("analyseType","/MedicalAnalyse/analyseVallage");
        model.addAttribute("actionUrl","/MedicalAnalyse/VallageListview");
        model.addAttribute("type",Constant.medicalVallage);
        model.addAttribute("uploadType",Constant.MedicalAnalyse);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.medicalVallageFileApplication)==null){
            list=medicalAnalyseServiceExcel.getAnalyseExcelUploadDtoList(medicalVallageUpload);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.medicalVallageFileApplication);
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

        return "adm/AnalyseExcelUpload-list";
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
            if(type.equals(Constant.medicalCity)){
                uploadPath=medicalCityUpload;
            }else if(type.equals(Constant.medicalVallage)){
                uploadPath=medicalVallageUpload;

            }
            FileUtil.emptyPath(uploadPath);

        }


        int progress=0;
        Map<String, MedicalDto> res =new HashMap<>();
        List<AnalyseExcelUploadDto> fileList=new ArrayList<>();
        int id=1;
        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();
            if (file.getSize()<Constant.maxFileSize){
                //medicalAnalyseServiceExcel.init(res,file,type);

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
            //files_.put(fileName,file.getInputStream());
        }



        if(type.equals(Constant.medicalCity)){
           // applications.setAttribute(Constant.medicalCityApplication,null);
            applications.setAttribute(Constant.medicalCityFileApplication ,fileList);

        }
        if(type.equals(Constant.medicalVallage)){
            //applications.setAttribute(Constant.medicalVallageApplication,null);
             applications.setAttribute(Constant.medicalVallageFileApplication ,fileList);

        }
       // applications.setAttribute(Constant.medicalAllApplication, null);
       // new MyThread2(files_,uploadPath).start();

        return Constant.SUCCESS;



    }
    /**
     * 将上传的文件异步的写入文件
     */
    class MyThread2 extends Thread {
        private Map<String,InputStream>  files_;
        private String finalUploadPath;

        public MyThread2( Map<String,InputStream>  files,String finalUploadPath) {
            this.files_ = files;
            this.finalUploadPath = finalUploadPath;

        }

        public void run() {


            for (String key : files_.keySet()) {
                String fileName = key;
                try {
                    OutputStream outputStream =new FileOutputStream(new File(finalUploadPath + "\\" + fileName));

                    int bytesWritten = 0;
                    int byteCount = 0;
                    byte[] bytes = new byte[1024*1000*10];
                    while ((byteCount = files_.get(key).read(bytes)) != -1)
                    {
                        outputStream.write(bytes, bytesWritten, byteCount);
                        bytesWritten += byteCount;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        files_.get(key).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }
    @ResponseBody
    @RequestMapping(value="/analyseCity",method= RequestMethod.GET)
    public String  analyseCity(  HttpServletRequest request) throws Exception {

        /*Object target=applications.getAttribute(Constant.medicalCityApplication);
        if(target==null){
            target=medicalAnalyseServiceExcel.initByPath(medicalCityUpload,Constant.medicalCity);
            applications.setAttribute(Constant.medicalCityApplicationMap,target);
        }*/
        Object target=  medicalAnalyseServiceExcel.initByPath(medicalCityUpload,Constant.medicalCity);
            applications.setAttribute(Constant.medicalCityApplicationMap,target);

        if (  target instanceof Map ){
            Map<String, MedicalDto> res=(Map<String, MedicalDto> )target;
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.medicalCityApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if(  target instanceof List ){
            return Constant.SUCCESS;
        }
        logger.error("未找到有效的EXCLE文件");
        return  Constant.ERR_UPLOAD_FILE_FORMAT;
    }

    @ResponseBody
    @RequestMapping(value="/analyseVallage",method= RequestMethod.GET)
    public String  analyseVallage(  HttpServletRequest request) throws IOException {
        Object target=applications.getAttribute(Constant.medicalVallageApplication);
        if(target==null){
            target=medicalAnalyseServiceExcel.initByPath(medicalVallageUpload,Constant.medicalVallage);
            applications.setAttribute(Constant.medicalVallageApplicationMap,target);
        }
        if( target instanceof Map ){
            Map<String, MedicalDto> res=(Map<String, MedicalDto> )target;
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.medicalVallageApplication,mapKeyList);
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
        Map<String, MedicalDto> all=null;
        {
            Object targetVallage=applications.getAttribute(Constant.medicalVallageApplicationMap);
            Object targetCity=applications.getAttribute(Constant.medicalCityApplicationMap);
            if(targetVallage==null){
                return Constant.ERR_VALLAGE_ANALYSE_NOT_YET;
            }
            if(targetCity==null){
                return Constant.ERR_CITY_ANALYSE_NOT_YET;
            }
            Map<String, MedicalDto> resVallage=(Map<String, MedicalDto> )targetVallage;
            Map<String, MedicalDto> resCity=(Map<String, MedicalDto> )targetCity;
            all=medicalAnalyseServiceExcel.initMerge(resCity,resVallage);

        }

        //Map<String, MedicalDto> all=medicalAnalyseServiceExcel.initMerge(medicalCityUpload,medicalVallageUpload);

        if(!all.isEmpty()){
            List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(all.values());
            mapKeyList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
            mapKeyList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());
            applications.setAttribute(Constant.medicalAllApplication,mapKeyList);
            return Constant.SUCCESS;
        }

        return  Constant.ERR;

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckAll",method= RequestMethod.GET )
    public String checkMedicalDifExcelFile(   ){

        return medicalAnalyseServiceExcel.checkMedicalDifExcelFile(
                applications,Constant.medicalAllApplication,
                Constant.ERR_ALL_ANALYSE_NOT_YET_MEDICAL,Constant.EMPTY_ALL_ANALYSE_NOT_YET_MEDICAL);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckCity",method= RequestMethod.GET )
    public String checkMedicalDifExcelFileCity(    ){

        return medicalAnalyseServiceExcel.checkMedicalDifExcelFile(
                applications,Constant.medicalCityApplication,
                Constant.ERR_CITY_ANALYSE_NOT_YET_MEDICAL,Constant.EMPTY_ALL_ANALYSE_NOT_YET_MEDICAL);

    }
    @ResponseBody
    @RequestMapping(value="/outPutExcelCheckVallage",method= RequestMethod.GET )
    public String checkMedicalDifExcelFileVallage(    ){

        return medicalAnalyseServiceExcel.checkMedicalDifExcelFile(
                applications,Constant.medicalVallageApplication,
                Constant.ERR_VALLAGE_ANALYSE_NOT_YET_MEDICAL,Constant.EMPTY_ALL_ANALYSE_NOT_YET_MEDICAL);

    }
    @RequestMapping(value="/outPutExcelAll",method= RequestMethod.GET )
    public String getListMedicalDifExcelFileAll(HttpServletResponse response  ){
        medicalAnalyseServiceExcel.getListMedicalDifExcelFile(applications,response,Constant.medicalAllApplication,Constant.dataTitleMedicalAll);
        return null;
    }
    @RequestMapping(value="/outPutExcelCity",method= RequestMethod.GET )
    public String getListMedicalDifExcelFileCity(HttpServletResponse response  ){
        medicalAnalyseServiceExcel.getListMedicalDifExcelFile(applications,response,Constant.medicalCityApplication,Constant.dataTitleMedicalCity);
        return null;
    }
    @RequestMapping(value="/outPutExcelVallage",method= RequestMethod.GET )
    public String getListMedicalDifExcelFileVallage(HttpServletResponse response  ){
        medicalAnalyseServiceExcel.getListMedicalDifExcelFile(applications,response,Constant.medicalVallageApplication,Constant.dataTitleMedicalVallage);
        return null;
    }



}
