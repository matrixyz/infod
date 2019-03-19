package com.zzsc.infod.service.impl;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.util.EventModelReadExcel;
import com.zzsc.infod.util.ExcelUtil;
import com.zzsc.infod.util.FileUtil;

import com.zzsc.infod.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EndowmentAnalyseServiceExcelImpl implements EndowmentAnalyseServiceExcel {

    private static Logger logger = LoggerFactory.getLogger(EndowmentAnalyseServiceExcelImpl.class);

   /* public List<EndowmentDto> analyseCityExcel(MultipartFile file) {
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            String fileName=file.getOriginalFilename();
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);
            List<String[]> cells= ExcelUtil.getWorkbookInfo(2,new int[]{0,1,2},workbook);
            for (String[] item:cells) {
                EndowmentDto endowmentDto=new EndowmentDto();
                endowmentDto.setCid(item[1]);
                endowmentDto.setName(item[0]);
                endowmentDto.setOrgName(item[2]);
                EndowmentDtos.add(endowmentDto);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return EndowmentDtos;
    }

    @Override
    public List<EndowmentDto> analyseVallageExcel(MultipartFile file) {



        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        Workbook workbook = null;
        try {

            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);

            //List<String[]> cells=ExcelUtil.getWorkbookInfo(3,new int[]{6,3,11},workbook);

            List<String[]> res=new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength=sheet.getLastRowNum();

            for (int i = 3; i < rowLength; i++) {
                Row row = sheet.getRow(i);
                EndowmentDto endowmentDto=new EndowmentDto();
                endowmentDto.setCid(row.getCell(6).toString());
                endowmentDto.setName(row.getCell(3).toString());
                endowmentDto.setOrgName(row.getCell(11).toString());
                EndowmentDtos.add(endowmentDto);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(workbook!=null){
                    workbook.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return EndowmentDtos;
    }*/

    @Override
    public List<EndowmentDto> analyseCityExcel(File file) {
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            int type=ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                List<String[]> cells = ExcelUtil.getWorkbookInfo(1, new int[]{2, 3, 4}, workbook);

                for (String[] item : cells) {
                    EndowmentDto endowmentDto = new EndowmentDto();
                    endowmentDto.setCid(item[1]);
                    endowmentDto.setName(item[0]);
                    endowmentDto.setOrgName(item[2]);
                    endowmentDto.setFileName(file.getName());
                    EndowmentDtos.add(endowmentDto);
                }
                is.close();
            }else{
                logger.warn("分析了养老 xlsx文件- -  "+file.getName());

                return analyseCityExcelEventmode(file);
            }
        } catch ( Exception e) {
            logger.error("methodName = analyseCityExcel\n"+e.getMessage());
        }finally {
            try {
                if(workbook!=null){
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return EndowmentDtos;
    }
    public List<EndowmentDto> analyseCityExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(1);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            String areaName=String.valueOf(res.get(0).get(3));
            res=res.subList(1,res.size());
            for (List<Object> re : res) {
                int col=0;


                EndowmentDto endowmentDto=new EndowmentDto();
                for (Object o : re) {
                    if(col==3)
                        endowmentDto.setName(String.valueOf(o));
                    else if(col==2)
                        endowmentDto.setCid(String.valueOf(o));

                    endowmentDto.setOrgName(areaName);
                    endowmentDto.setFileName(file.getName());
                    col++;
                }
                EndowmentDtos.add(endowmentDto);

            }


        } catch (Exception e) {
            logger.error("methodName = analyseCityExcelEventmode\n"+e.getMessage());
        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return EndowmentDtos;
    }
    @Override
    public List<EndowmentDto> analyseVallageExcel(File file) {
        long startTime = System.currentTimeMillis();
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();
        Workbook workbook = null;
        try {
            int type=ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                Sheet sheet = workbook.getSheetAt(0);
                int rowLength = sheet.getLastRowNum();

                for (int i = 1; i <=rowLength; i++) {
                    Row row = sheet.getRow(i);
                    EndowmentDto endowmentDto = new EndowmentDto();

                    endowmentDto.setCid(row.getCell(3).toString().replaceAll("\"", ""));
                    endowmentDto.setName(row.getCell(4).toString());
                    endowmentDto.setOrgName(row.getCell(2).toString());
                    endowmentDto.setFileName(file.getName());
                    EndowmentDtos.add(endowmentDto);

                }
                is.close();
            }else{
                return analyseVallageExcelEventmode(file);
            }

        } catch (Exception e) {
            logger.error("methodName = analyseVallageExcel\n"+e.getMessage());

        }finally {
            try {
                if(workbook!=null){
                    workbook.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return EndowmentDtos;
    }
    public List<EndowmentDto> analyseVallageExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(2);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            for (List<Object> re : res) {
                int col=0;

                EndowmentDto endowmentDto=new EndowmentDto();
                for (Object o : re) {
                    if(col==4)
                        endowmentDto.setName(String.valueOf(o));
                    else if(col==3)
                        endowmentDto.setCid(String.valueOf(o));
                    else if(col==2)
                        endowmentDto.setOrgName(String.valueOf(o));

                    endowmentDto.setFileName(file.getName());
                    col++;
                }
                EndowmentDtos.add(endowmentDto);

            }


        } catch (Exception e) {
            logger.error("methodName = analyseVallageExcelEventmode\n"+e.getMessage());

        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return EndowmentDtos;
    }

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

   /* @Override
    public Map<String, EndowmentDto> getEndowmentFromRow(Map<String,EndowmentDto> endowmentDtoMap, MultipartFile file,String type) {


        List<EndowmentDto> EndowmentDtos =null;
        if(type.equals(Constant.endowmentCity)){
            EndowmentDtos= analyseCityExcel(file);
        }
        if(type.equals(Constant.endowmentVallage)){
            EndowmentDtos= analyseVallageExcel(file);
        }
        for (EndowmentDto one:EndowmentDtos  ) {
            if(one.getCid()==null)
                continue;
            String key=one.getName()+one.getCid();
            if(endowmentDtoMap.containsKey(key)){
                endowmentDtoMap.get(key).setRepeatTimesAdd();
            }else {
                endowmentDtoMap.put(key, one);
            }

        }

        return endowmentDtoMap;
    }*/

    @Override
    public Map<String, EndowmentDto> getEndowmentFromRow(String type,File[] files) throws Exception {

        Map<String, EndowmentDto> res=new HashMap<>();
        for (File file: files) {
            List<EndowmentDto> EndowmentDtos =null;
            String err_info=null;
            if(type.equals(Constant.endowmentCity)){
                EndowmentDtos= analyseCityExcel(file);
                err_info=Constant.ERR_ENDOWMENT_CITY_FILE_FORMAT;
            }
            if(type.equals(Constant.endowmentVallage)){
                EndowmentDtos= analyseVallageExcel(file);
                err_info=Constant.ERR_ENDOWMENT_VALLAGE_FILE_FORMAT;
            }
            if(EndowmentDtos==null||EndowmentDtos.size()==0){
                throw new Exception(err_info+"["+file.getName()+"]");
            }
            if(StringUtil.isChineseName(EndowmentDtos.get(0).getName())==false){
                throw new Exception(err_info+"["+file.getName()+"]");
            }
            if(StringUtil.isChineseUid(EndowmentDtos.get(0).getCid())==false){
                throw new Exception(err_info+"["+file.getName()+"]");
            }



            for (EndowmentDto one:EndowmentDtos  ) {
                if(one.getCid()==null)
                    continue;
                StringBuilder key=new StringBuilder().append(one.getName()).append(one.getCid());

                if(res.containsKey(key.toString())){
                    EndowmentDto endowmentDto=res.get(key.toString());
                    endowmentDto.setRepeatTimesAdd();
                    endowmentDto.setOrgName(endowmentDto.getOrgName()+"<br>"+one.getOrgName());
                    endowmentDto.setFileName(endowmentDto.getFileName()+"<br>"+one.getFileName());
                }else {
                    res.put(key.toString(), one);
                }

            }

        }
        return res;
    }
   /* @Override
    public Map<String, EndowmentDto> init(Map<String, EndowmentDto> res,MultipartFile file,String type){

        return getEndowmentFromRow(res,  file,type);
    }*/
    @Override
    public Map<String, EndowmentDto> initByPath(String path,String type) throws Exception {
        File[] files=getFiles(path);
        if(files==null||files.length==0)
            return null;
        return getEndowmentFromRow(  type,files);
    }
    /**
     * 将城市、城镇医疗保险数据合并到一个map里
     */
    public Map<String, EndowmentDto> initMerge(String pathCity,String pathVallage){
        Map<String, EndowmentDto> city= null;
        Map<String, EndowmentDto> vallage= null;

        try {
            city = getEndowmentFromRow(   Constant.endowmentCity,getFiles(pathCity));
            vallage = getEndowmentFromRow( Constant.endowmentVallage,getFiles(pathVallage));
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (String key:city.keySet()  ) {
            if(vallage.containsKey(key)){
                vallage.get(key).setRepeatTimesAdd(city.get(key).getRepeatTimes());
            }else {
                vallage.put(key,city.get(key));
            }
        }
        return vallage;
    }
    public Map<String, EndowmentDto> initMerge( Map<String, EndowmentDto> city, Map<String, EndowmentDto> vallage){


        for (String key:city.keySet()  ) {
            if(vallage.containsKey(key)){
                vallage.get(key).setRepeatTimesAdd(city.get(key).getRepeatTimes());
            }else {
                vallage.put(key,city.get(key));
            }
        }
        return vallage;
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
    public String checkEndowmentDifExcelFile(ServletContext applications, String appType, String errType, String emptyType) {
        Object all=applications.getAttribute(appType);
        if(all==null){
            return errType;
        }
        List<EndowmentDto> mapKeyList = (List<EndowmentDto> ) all;
        List<EndowmentDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());
        if(tempList.size()==0){
            return emptyType;
        }
        return Constant.SUCCESS;
    }

    @Override
    public int getListEndowmentDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle) {
        Object all=applications.getAttribute(appType);

        List<EndowmentDto> mapKeyList = (List<EndowmentDto> ) all;
        List<EndowmentDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(EndowmentDto::getRepeatTimes).reversed());

        ServletOutputStream os =null;

        try {
            os = response.getOutputStream();// 取得输出流
            response.setCharacterEncoding("UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(excelFileTitle.getBytes("gb2312"), "iso8859-1") + ".xls\"");//fileName为下载时用户看到的文件名利用jxl 将数据从后台导出为excel
            response.setHeader("Content-Type", "application/msexcel");
            String[] titles = new String[]{
                    "序号","姓名","身份证号码","单位","所在文件名","重复次数"
            };
            List<String[]> tempData=new ArrayList<>();
            int index=1;
            for (EndowmentDto medicalDto : tempList) {
                String[] item=new String[]{
                        String.valueOf(index),
                        medicalDto.getName(),
                        medicalDto.getCid(),
                        medicalDto.getOrgName(),
                        medicalDto.getFileName(),
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
