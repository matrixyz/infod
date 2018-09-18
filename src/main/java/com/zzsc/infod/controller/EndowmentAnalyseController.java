package com.zzsc.infod.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.EndowmentAnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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

    @RequestMapping(value="/list",method= RequestMethod.GET )
    public String getList(Model model, EndowmentDto endowmentDto  ){

        if (endowmentDto.getPage() == null||"".equals(endowmentDto.getPage())) {
            endowmentDto.setPage("1");
        }
        System.out.println("");
        int pageNum= Integer.parseInt(endowmentDto.getPage());
        PageHelper.startPage(pageNum,10);


        List<EndowmentAnalyseExcelUploadDto> list=new ArrayList<>();
        EndowmentAnalyseExcelUploadDto temp=new EndowmentAnalyseExcelUploadDto();
        temp.setFileName("没有数据!");
        list.add(temp);
        PageInfo<EndowmentAnalyseExcelUploadDto> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("EndowmentAnalyseExcelUploadDto",list);

        return "adm/EndowmentAnalyseExcelUpload-list";
    }
    @ResponseBody
    @RequestMapping("/upload")
    public List<EndowmentAnalyseExcelUploadDto>  fileUpload(@RequestParam(value = "inputfile",required = false) MultipartFile[] files, HttpServletRequest request) throws IOException {

        List<EndowmentAnalyseExcelUploadDto> list=new ArrayList<>();
        int id=1;
        for ( MultipartFile file:files){
            String fileName=       file.getOriginalFilename();

            EndowmentAnalyseExcelUploadDto info=new EndowmentAnalyseExcelUploadDto();

            info.setFileName(fileName);
            info.setDataSize(FileUtil.getFileSize('k',file.getSize()));
            info.setFileType("Excel");
            info.setUploadProgress(100);
            info.setResult("上传成功");
            info.setId(id++);
            list.add(info);
            File temp=new File(endowmentCityUpload+"\\"+fileName);
            file.getSize();
            System.out.println(fileName);


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
    public String  excelAnalyseList( Model model, EndowmentDto endowmentDto) throws IOException {
        if (endowmentDto.getPage() == null||"".equals(endowmentDto.getPage())) {
            endowmentDto.setPage("1");
        }
        if(applications.getAttribute(Constant.endowmentCityApplication)!=null){
            List<EndowmentDto> lists=( List<EndowmentDto>)applications.getAttribute(Constant.endowmentCityApplication);
            int pageNum= Integer.parseInt(endowmentDto.getPage());
            PageHelper.startPage(pageNum,50);


            PageInfo<EndowmentDto> pageInfo = new PageInfo<>(lists);

            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("EndowmentList",lists);
            model.addAttribute("queryParams",endowmentDto);
        }



        return  "adm/EndowmentAnalyseExcelResut-list";

    }

}
