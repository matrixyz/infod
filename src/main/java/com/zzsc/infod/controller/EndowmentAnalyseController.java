package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Controller
@RequestMapping("/EndowmentAnalyse")
public class EndowmentAnalyseController {

    @Autowired
    private EndowmentAnalyseServiceExcel endowmentAnalyseServiceExcel;

    @Value( "${endowment.city.upload.path}")
    private String endowmentCityUpload;
    @Value( "${endowment.vallage.upload.path}")
    private String endowmentVallageUpload;

    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;




    @RequestMapping(value="/CityListview",method= RequestMethod.GET )
    public String getCityListview(Model model, AnalyseExcelUploadDto analyseExcelUploadDto  ){
        model.addAttribute("getFileListUrl","/EndowmentAnalyse/CityList");
        model.addAttribute("analyseType","/EndowmentAnalyse/analyseCity");
        model.addAttribute("actionUrl","/EndowmentAnalyse/CityListview");
        model.addAttribute("type",Constant.endowmentCity);
        model.addAttribute("uploadType",Constant.EndowmentAnalyse);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.endowmentCityFileApplication)==null){
            list=endowmentAnalyseServiceExcel.getAnalyseExcelUploadDtoList(endowmentCityUpload);
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

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.endowmentVallageFileApplication)==null){
            list=endowmentAnalyseServiceExcel.getAnalyseExcelUploadDtoList(endowmentVallageUpload);
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

        String uploadPath=null;
        session.setAttribute("uploadProgress",0);
        Map<String,InputStream> files_;
        files_ = new HashMap<>();
        if(files.length>0){
            if(type.equals(Constant.endowmentCity)){
                uploadPath=endowmentCityUpload;
            }else if(type.equals(Constant.endowmentVallage)){
                uploadPath=endowmentVallageUpload;

            }
            FileUtil.emptyPath(uploadPath);

        }


        int progress=0;
        Map<String, EndowmentDto> res =new HashMap<>();
        List<AnalyseExcelUploadDto> fileList=new ArrayList<>();
        int id=1;
        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();
            if (file.getSize()<Constant.maxFileSize){
                //endowmentAnalyseServiceExcel.init(res,file,type);

                File file_=new File(uploadPath + "\\" + fileName);
                file.transferTo(file_);
                System.out.println(res.size());
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
    public String  analyseCity(  HttpServletRequest request) throws IOException {

        Object target=applications.getAttribute(Constant.endowmentCityApplication);
        if(target==null){
            target=endowmentAnalyseServiceExcel.initByPath(endowmentCityUpload,Constant.endowmentCity);
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
            target=endowmentAnalyseServiceExcel.initByPath(endowmentVallageUpload,Constant.endowmentVallage);
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
        if( applications.getAttribute(Constant.endowmentAllApplication)!=null){
            return Constant.SUCCESS;
        }else{
            Object targetVallage=applications.getAttribute(Constant.endowmentVallageApplicationMap);
            Object targetCity=applications.getAttribute(Constant.endowmentCityApplicationMap);
            if(targetVallage==null){
                return Constant.ERR_VALLAGE_ANALYSE_NOT_YET;
            }
            if(targetCity==null){
                return Constant.ERR_CITY_ANALYSE_NOT_YET;
            }
            Map<String, EndowmentDto> resVallage=(Map<String, EndowmentDto> )targetVallage;
            Map<String, EndowmentDto> resCity=(Map<String, EndowmentDto> )targetCity;
            all=endowmentAnalyseServiceExcel.initMerge(resCity,resVallage);

        }

        //Map<String, EndowmentDto> all=endowmentAnalyseServiceExcel.initMerge(endowmentCityUpload,endowmentVallageUpload);

        if(!all.isEmpty()){
            List<EndowmentDto> mapKeyList = new ArrayList<EndowmentDto>(all.values());
            mapKeyList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
            mapKeyList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());
            applications.setAttribute(Constant.endowmentAllApplication,mapKeyList);
            return Constant.SUCCESS;
        }

        return  Constant.ERR;

    }

    @RequestMapping(value="/outPutExcel",method= RequestMethod.GET )
    public String getListEndowmentDifExcelFile(HttpServletResponse response  ){

        Object all=applications.getAttribute(Constant.endowmentAllApplication);

        List<EndowmentDto> mapKeyList = (List<EndowmentDto> ) all;
        List<EndowmentDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());

        ServletOutputStream os =null;

        try {
            os = response.getOutputStream();// 取得输出流
            response.setCharacterEncoding("UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(Constant.dataTitleEndowmentAll.getBytes("gb2312"), "iso8859-1") + ".xls");//fileName为下载时用户看到的文件名利用jxl 将数据从后台导出为excel
            response.setHeader("Content-Type", "application/msexcel");
            String[] titles = new String[]{
                    "序号","姓名","身份证号码","单位","重复次数"
            };
            List<String[]> tempData=new ArrayList<>();
            int index=1;
            for (EndowmentDto endowmentDto : tempList) {
                String[] item=new String[]{
                        String.valueOf(index),
                        endowmentDto.getName(),
                        endowmentDto.getCid(),
                        endowmentDto.getOrgName(),
                        String.valueOf(endowmentDto.getRepeatTimes())  };
                index++;
                tempData.add(item);
            }


            ExcelUtil obj = new ExcelUtil();
            obj.exportExcelFix("城镇、城乡养老保险重复数据",titles,tempData,os);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

   
}
