package com.zzsc.infod.controller;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
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
@RequestMapping("/EndowmentAnalyse")
public class EndowmentAnalyseController {

    @Autowired
    private EndowmentAnalyseServiceExcel endowmentAnalyseServiceExcel;
    @Value( "${endowment.city.upload.path}")
    private String endowmentCityUpload;
    @Autowired
    ServletContext applications;
    @Autowired
    HttpSession session;

    @ResponseBody
    @RequestMapping(value="/list",method= RequestMethod.GET )
    public  List<AnalyseExcelUploadDto> getList(EndowmentDto endowmentDto  ){

        if (endowmentDto.getPage() == null||"".equals(endowmentDto.getPage())) {
            endowmentDto.setPage("1");
        }
        int pageNum= Integer.parseInt(endowmentDto.getPage());
        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        File[] files=FileUtil.getFilesInPath(endowmentCityUpload);
        int id=1;
        for ( File file:files){
            String fileName=       file.getName();

            AnalyseExcelUploadDto info=new AnalyseExcelUploadDto();

            info.setFileName(fileName);
            info.setDataSize(FileUtil.getFileSize('k',file.length()));
            info.setFileType("Excel");
            info.setUploadProgress(100);
            info.setResult("上传成功");
            info.setId(id++);
            list.add(info);
        }

       if(files.length==0){
           AnalyseExcelUploadDto temp=new AnalyseExcelUploadDto();
           temp.setFileName("没有数据!");
           list.add(temp);
       }




        return list;
    }


    @RequestMapping(value="/listview",method= RequestMethod.GET )
    public String getListview(Model model, EndowmentDto endowmentDto  ){


        return "adm/EndowmentAnalyseExcelUpload-list";
    }


    @ResponseBody
    @RequestMapping("/upload")
    public List<AnalyseExcelUploadDto>  fileUpload(@RequestParam(value = "inputfile",required = false) MultipartFile[] files, HttpServletRequest request) throws IOException {


        if(files.length>0)
            FileUtil.emptyPath(endowmentCityUpload);
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
            File temp=new File(endowmentCityUpload+"\\"+fileName);
            file.getSize();


            if (file.getSize()<1024*1000){
                file.transferTo(temp);

            } else{

            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;



    }
    @ResponseBody
    @RequestMapping(value="/analyse",method= RequestMethod.GET)
    public String  analyse(  HttpServletRequest request) throws IOException {

        String type=request.getParameter("type");
        if(type.equals(Constant.endowmentCity)){
            Map<String, EndowmentDto> res=endowmentAnalyseServiceExcel.init(endowmentCityUpload);
            if(!res.isEmpty()){
                List<EndowmentDto> mapKeyList = new ArrayList<EndowmentDto>(res.values());

                applications.setAttribute(Constant.endowmentCityApplication,mapKeyList);
                return Constant.SUCCESS;
            }



        }

        return  Constant.ERR;

    }

    @RequestMapping(value="/excelAnalyseList",method= RequestMethod.GET)
    public String  excelAnalyseList( Model model, EndowmentDto endowmentDto,BindingResult bindingResult) throws IOException {
        if (StringUtil.isEmpty(endowmentDto.getPage()) ) {
            endowmentDto.setPage("1");
        }
        if(applications.getAttribute(Constant.endowmentCityApplication)!=null){


            List<EndowmentDto> lists=( List<EndowmentDto>)applications.getAttribute(Constant.endowmentCityApplication);
            int pageNum= Integer.parseInt(endowmentDto.getPage());

            PageBean pageInfo=new PageBean();
           // pageInfo.setPageList(lists);
            pageInfo.setTotalCount(lists.size());
            pageInfo.setPageNo(pageNum);



            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("EndowmentList",lists.subList(pageInfo.getFromIndex(),pageInfo.getToIndex()));
            model.addAttribute("queryParams",endowmentDto);
        }



        return  "adm/EndowmentAnalyseExcelResut-list";

    }
    @InitBinder
    public void InitBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            public void setAsText(String value) {
                try {
                    setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));
                } catch(ParseException e) {
                    setValue(null);
                }
            }

            public String getAsText() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) getValue());
            }

        });
    }
}
