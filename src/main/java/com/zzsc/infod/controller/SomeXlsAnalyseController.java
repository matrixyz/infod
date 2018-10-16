package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.model.SomeXlsDto;
import com.zzsc.infod.service.SomeXlsAnalyseServiceExcel;
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

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/SomeXlsAnalyse")
public class SomeXlsAnalyseController {

    @Autowired
    private SomeXlsAnalyseServiceExcel someXlsAnalyseServiceExcel;

    @Value( "${someXls.upload.path}")
    private String someXlsUpload ;

    private String someXlsUploadRealPath ;

    @PostConstruct
    public void setPath(){
        someXlsUploadRealPath =FileUtil.getBaseJarPath()+"/"+someXlsUpload ;

    }

    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;

    private Logger logger =   LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value="/Listview",method= RequestMethod.GET )
    public String getListview(Model model, AnalyseExcelUploadDto analyseExcelUploadDto  ){
        model.addAttribute("getFileListUrl","/SomeXlsAnalyse/List");
        model.addAttribute("analyseType","/SomeXlsAnalyse/analyse");
        model.addAttribute("actionUrl","/SomeXlsAnalyse/Listview");
        model.addAttribute("type",Constant.someXls);
        model.addAttribute("uploadType",Constant.SomeXlsAnalyse);
        model.addAttribute("filePath",someXlsUpload);

        List<AnalyseExcelUploadDto> list=null;
        if(applications.getAttribute(Constant.someXlsFileApplication)==null){
            list=someXlsAnalyseServiceExcel.getAnalyseExcelUploadDtoList(someXlsUploadRealPath);
        }else{
            list=(List<AnalyseExcelUploadDto>)applications.getAttribute(Constant.someXlsFileApplication);
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

        return "adm/AnalyseExcelSomeXlsUpload-list";
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
            if(type.equals(Constant.someXls)){
                uploadPath=someXlsUploadRealPath;
            }else if(type.equals(Constant.someXls)){
                uploadPath=someXlsUploadRealPath;

            }
            FileUtil.createPath(uploadPath);
            FileUtil.emptyPath(uploadPath);
        }
        int progress=0;
        Map<String, SomeXlsDto> res =new HashMap<>();
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



        if(type.equals(Constant.someXls)){
            //applications.setAttribute(Constant.someXlsApplication,res);
            applications.setAttribute(Constant.someXlsFileApplication ,fileList);

        }
        if(type.equals(Constant.someXls)){
            applications.setAttribute(Constant.someXlsFileApplication ,fileList);

        }


        return Constant.SUCCESS;



    }

    @ResponseBody
    @RequestMapping(value="/analyse",method= RequestMethod.GET)
    public String  analyse(  HttpServletRequest request) throws IOException {

        Object target=applications.getAttribute(Constant.someXlsApplication);
        if(target==null){
            try {
                target=someXlsAnalyseServiceExcel.initByPath(someXlsUploadRealPath,Constant.someXls);

            } catch (Exception e) {
                logger.error(e.getMessage());
                return e.getMessage();
            }
            if(target==null)
                return Constant.ERR_NO_SOMEXLS_FILE;
            applications.setAttribute(Constant.someXlsApplicationMap,target);
        }

        if (  target instanceof Map ){
            Map<String, SomeXlsDto> res=(Map<String, SomeXlsDto> )target;
            if(!res.isEmpty()){
                List<SomeXlsDto> mapKeyList = new ArrayList<SomeXlsDto>(res.values());
                mapKeyList.sort(Comparator.comparingInt(SomeXlsDto::getRepeatTimes).reversed());
                applications.setAttribute(Constant.someXlsApplication,mapKeyList);
                return Constant.SUCCESS;
            }
        }else if(  target instanceof List ){
            return Constant.SUCCESS;
        }
        return  Constant.ERR;
    }


    @ResponseBody
    @RequestMapping(value="/analyseAll",method= RequestMethod.GET)
    public String  analyseAll(  HttpServletRequest request) throws IOException {
        Map<String, FinanceFeedDto> all=null;
        {
            Object financeFeedAllApplicationMap=applications.getAttribute(Constant.financeFeedAllApplicationMap);
            Object someXlsApplicationMap=applications.getAttribute(Constant.someXlsApplicationMap);
            if(financeFeedAllApplicationMap==null){
                return Constant.ERR_ALL_ANALYSE_NOT_YET_FINANCEFEED;
            }
            if(someXlsApplicationMap==null){
                return Constant.ERR_ANALYSE_NOT_YET_SOMEXLS;
            }

            Map<String, FinanceFeedDto> financeFeed=(Map<String, FinanceFeedDto> )financeFeedAllApplicationMap;
            Map<String, SomeXlsDto> someXls=(Map<String, SomeXlsDto> )someXlsApplicationMap;
            all=someXlsAnalyseServiceExcel.initMerge(financeFeed,someXls);
        }


        if(!all.isEmpty()){
            List<FinanceFeedDto> mapKeyList = new ArrayList<FinanceFeedDto>(all.values());
            mapKeyList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
            mapKeyList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());
            applications.setAttribute(Constant.someXlsAllApplication,mapKeyList);
            return Constant.SUCCESS;
        }

        return  Constant.ERR;

    }
    //导出数据时检查是否有重复数据

    @ResponseBody
    @RequestMapping(value="/outPutExcelCheck",method= RequestMethod.GET )
    public String checkSomeXlsDifExcelFile(    ){

        return someXlsAnalyseServiceExcel.checkSomeXlsDifExcelFile(
                applications,Constant.someXlsApplication,
                Constant.ERR_ANALYSE_NOT_YET_SOMEXLS,Constant.EMPTY_ALL_ANALYSE_NOT_YET_SOMEXLS);

    }

    @RequestMapping(value="/outPutExcelAll",method= RequestMethod.GET )
    public String getListSomeXlsDifExcelFileAll(HttpServletResponse response  ){
        someXlsAnalyseServiceExcel.getListSomeXlsDifExcelFile(applications,response,Constant.someXlsAllApplication,Constant.dataTitleSomeXlsAll);
        return null;
    }


   
}
