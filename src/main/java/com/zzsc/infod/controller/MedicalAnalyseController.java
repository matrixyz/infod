package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.NumUtil;
import com.zzsc.infod.util.PageBean;
import com.zzsc.infod.util.StringUtil;
import org.mybatis.spring.annotation.MapperScan;
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
import javax.servlet.http.HttpServletRequest;
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
        System.out.println("ccccccccccccccccccccccc"+session.getId());
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
    @RequestMapping("/getProgress")
    public String getProgress(  ){
        if(session.getAttribute("uploadProgress")==null){
            System.out.println("aaaaaaaaaaaaaaaaaaaa"+session.getId());
            return "0";
        }
        return session.getAttribute("uploadProgress").toString();
    }

    @ResponseBody
    @RequestMapping("/upload")
    public List<AnalyseExcelUploadDto>  fileUpload(@RequestParam(value = "inputfile",required = false) MultipartFile[] files,
                                                   @RequestParam(value = "type",required = true) String type  ) throws IOException {

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

        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        int id=1;
        int progress=0;
        //Queue<MultipartFile> QueueMedical=new LinkedList<>();
        Map<String, MedicalDto> res =new HashMap<>();


        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();

            AnalyseExcelUploadDto info=new AnalyseExcelUploadDto();

            info.setFileName(fileName);
            info.setFileSize(FileUtil.getFileSize('k',file.getSize()));
            info.setFileType("Excel");
            info.setUploadProgress(100);
            info.setResult("上传成功");
            info.setId(id++);
            list.add(info);
            file.getSize();
            if (file.getSize()<Constant.maxFileSize){

                medicalAnalyseServiceExcel.init(res,file,type);
                progress++;
                session.setAttribute("uploadProgress", NumUtil.getProgress(files.length,progress));
            } else{
                System.out.println(Constant.ERR_FILE_MAX_SIZE);
            }
            files_.put(fileName,file.getInputStream());
        }



        if(type.equals(Constant.medicalCity)){
            applications.setAttribute(Constant.medicalCityApplication,res);
        }
        if(type.equals(Constant.medicalVallage)){
            applications.setAttribute(Constant.medicalVallageApplication,res);
        }

        new MyThread2(files_,uploadPath).start();

        return list;



    }

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
    public String  analyseCity(  HttpServletRequest request) throws IOException {
        Object target=applications.getAttribute(Constant.medicalCityApplication);
        if(target!=null&& target instanceof Map ){
            Map<String, MedicalDto> res=(Map<String, MedicalDto> ) applications.getAttribute(Constant.medicalCityApplication);
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());
                applications.setAttribute(Constant.medicalCityApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if(target!=null&& target instanceof List ){
            return Constant.SUCCESS;
        }
        return  Constant.ERR;
    }

    @ResponseBody
    @RequestMapping(value="/analyseVallage",method= RequestMethod.GET)
    public String  analyseVallage(  HttpServletRequest request) throws IOException {
        Object target=applications.getAttribute(Constant.medicalVallageApplication);
        if(target!=null&& target instanceof Map ){
            Map<String, MedicalDto> res=(Map<String, MedicalDto> ) applications.getAttribute(Constant.medicalVallageApplication);
            if(!res.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(res.values());
                applications.setAttribute(Constant.medicalVallageApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if(target!=null&& target instanceof List ){
            return Constant.SUCCESS;
        }
        return  Constant.ERR;
    }
    @ResponseBody
    @RequestMapping(value="/analyseAll",method= RequestMethod.GET)
    public String  analyseAll(  HttpServletRequest request) throws IOException {


            //Map<String, MedicalDto> all=medicalAnalyseServiceExcel.initMerge(medicalCityUpload,medicalVallageUpload);
            Map<String, MedicalDto> all=null;

            if(!all.isEmpty()){
                List<MedicalDto> mapKeyList = new ArrayList<MedicalDto>(all.values());
               // mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
                mapKeyList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.medicalAllApplication,mapKeyList);
                return Constant.SUCCESS;
            }

        return  Constant.ERR;

    }






}
