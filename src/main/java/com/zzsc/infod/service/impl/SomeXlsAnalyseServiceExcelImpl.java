package com.zzsc.infod.service.impl;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeed;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.model.SomeXlsDto;
import com.zzsc.infod.service.SomeXlsAnalyseServiceExcel;
import com.zzsc.infod.util.EventModelReadExcel;
import com.zzsc.infod.util.ExcelUtil;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SomeXlsAnalyseServiceExcelImpl implements SomeXlsAnalyseServiceExcel {

    private static Logger logger = LoggerFactory.getLogger(SomeXlsAnalyseServiceExcelImpl.class);




    @Override
    public List<SomeXlsDto> analyseSomeExcel(File file) {
        List<SomeXlsDto> SomeXlsDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            int type=ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                List<String[]> cells = ExcelUtil.getWorkbookInfo(1, new int[]{2, 3, 4}, workbook);

                for (String[] item : cells) {
                    SomeXlsDto SomeXlsDto = new SomeXlsDto();
                    SomeXlsDto.setName(item[0]);
                    SomeXlsDto.setCid(item[1]);
                    SomeXlsDto.setOrgName(item[2]);
                    SomeXlsDtos.add(SomeXlsDto);
                }
                is.close();
            }else{
                logger.warn("分析了养老 xlsx文件- -  "+file.getName());

                return analyseSomeExcelEventmode(file);
            }
        } catch ( Exception e) {
            logger.error("methodName = analyseSomeExcel\n"+e.getMessage());
        }finally {
            try {
                if(workbook!=null){
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SomeXlsDtos;
    }
    public List<SomeXlsDto> analyseSomeExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<SomeXlsDto> SomeXlsDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(1);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            String areaName=String.valueOf(res.get(0).get(3));
            res=res.subList(1,res.size());
            for (List<Object> re : res) {
                int col=0;
                if(re.size()<3||re.get(2)==null||re.get(2).toString().length()!=18){
                    continue;
                }

                SomeXlsDto SomeXlsDto=new SomeXlsDto();
                for (Object o : re) {
                    if(col==3)
                        SomeXlsDto.setName(String.valueOf(o));
                    else if(col==2)
                        SomeXlsDto.setCid(String.valueOf(o));

                    SomeXlsDto.setOrgName(areaName);
                    col++;
                }
                SomeXlsDtos.add(SomeXlsDto);

            }


        } catch (Exception e) {
            logger.error("methodName = analyseCityExcelEventmode\n"+e.getMessage());
        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return SomeXlsDtos;
    }
    
    

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

    

    @Override
    public Map<String, SomeXlsDto> getSomeXlsFromRow(String type,File[] files) throws Exception {

        Map<String, SomeXlsDto> res=new HashMap<>();
        for (File file: files) {
            List<SomeXlsDto> SomeXlsDtos =null;
            if(type.equals(Constant.someXls)){
                SomeXlsDtos= analyseSomeExcel(file);
            }
            if(StringUtil.isChineseName(SomeXlsDtos.get(0).getName())==false){
                throw new Exception(Constant.ERR_SOME_XLS_FILE_FORMAT+"["+file.getName()+"]");
            }
            if(StringUtil.isChineseUid(SomeXlsDtos.get(0).getCid())==false){
                throw new Exception(Constant.ERR_SOME_XLS_FILE_FORMAT+"["+file.getName()+"]");
            }
            for (SomeXlsDto one:SomeXlsDtos  ) {
                if(one.getCid()==null)
                    continue;
                StringBuilder key=new StringBuilder().append(one.getName()).append(one.getCid());

                if(res.containsKey(key.toString())){
                    FinanceFeedDto o=res.get(key.toString());
                    o.setRepeatTimesAdd();
                    o.setOrgName(o.getOrgName()+"<br>"+one.getOrgName());
                }else {
                    res.put(key.toString(), one);
                }

            }

        }
        return res;
    }
   
    @Override
    public Map<String, SomeXlsDto> initByPath(String path,String type) throws Exception {
        File[] files=getFiles(path);
        if(files==null||files.length==0)
            return null;
        return getSomeXlsFromRow(  type,files);
    }
    /**
     * 将城市、城镇医疗保险数据合并到一个map里
     */

    public Map<String, FinanceFeedDto> initMerge(Map<String, FinanceFeedDto> financeFeed, Map<String, SomeXlsDto> someXls){

        Set<String> set=someXls.keySet();
        for (String key:set ) {
            if(financeFeed.containsKey(key)){
                FinanceFeedDto o=financeFeed.get(key.toString());
                o.setRepeatTimesAdd();
                o.setOrgName(o.getOrgName()+"<br>"+someXls.get(key).getOrgName());

            }else {
                financeFeed.put(key,someXls.get(key));
            }
        }
        return financeFeed;
    }
    @Override
    public List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String endowmentUpload) {
        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        File[] files= FileUtil.getFilesInPath(endowmentUpload);
        int id=1;
        if(files==null||files.length==0){
            AnalyseExcelUploadDto temp=new AnalyseExcelUploadDto();
            temp.setFileName("没有数据!");
            list.add(temp);
            return list;
        }
        for ( File file:files){
            String fileName=       file.getName();
            AnalyseExcelUploadDto info=new AnalyseExcelUploadDto();
            info.setFileName(fileName);
            info.setFileSize(FileUtil.getFileSize('k',file.length()));
            info.setFileType("Excel");
            info.setUploadProgress(100);
            info.setResult("上传成功");
            info.setId(id++);
            list.add(info);
        }

        return list;
    }

    @Override
    public String checkSomeXlsDifExcelFile(ServletContext applications, String appType, String errType, String emptyType) {
        Object all=applications.getAttribute(appType);
        if(all==null){
            return errType;
        }
        List<SomeXlsDto> mapKeyList = (List<SomeXlsDto> ) all;
        List<SomeXlsDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(SomeXlsDto::getRepeatTimes).reversed());
        if(tempList.size()==0){
            return emptyType;
        }
        return Constant.SUCCESS;
    }

    @Override
    public int getListSomeXlsDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle) {
        Object all=applications.getAttribute(appType);

        List<SomeXlsDto> mapKeyList = (List<SomeXlsDto> ) all;
        List<SomeXlsDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(SomeXlsDto::getRepeatTimes).reversed());

        ServletOutputStream os =null;

        try {
            os = response.getOutputStream();// 取得输出流
            response.setCharacterEncoding("UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(excelFileTitle.getBytes("gb2312"), "iso8859-1") + ".xls\"");//fileName为下载时用户看到的文件名利用jxl 将数据从后台导出为excel
            response.setHeader("Content-Type", "application/msexcel");
            String[] titles = new String[]{
                    "序号","姓名","身份证号码","单位","重复次数"
            };
            List<String[]> tempData=new ArrayList<>();
            int index=1;
            for (SomeXlsDto medicalDto : tempList) {
                String[] item=new String[]{
                        String.valueOf(index),
                        medicalDto.getName(),
                        medicalDto.getCid(),
                        medicalDto.getOrgName(),
                        String.valueOf(medicalDto.getRepeatTimes())  };
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
        return 0;
    }
}
