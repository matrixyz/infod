package com.zzsc.infod.service.impl;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.service.FinanceFeedAnalyseServiceExcel;
import com.zzsc.infod.util.EventModelReadExcel;
import com.zzsc.infod.util.ExcelUtil;
import com.zzsc.infod.util.FileUtil;
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
public class FinanceFeedAnalyseServiceExcelImpl implements FinanceFeedAnalyseServiceExcel {

    private static Logger logger = LoggerFactory.getLogger(FinanceFeedAnalyseServiceExcelImpl.class);

    public List<FinanceFeedDto> analyseCityExcel(MultipartFile file) {
        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            String fileName=file.getOriginalFilename();
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);
            List<String[]> cells= ExcelUtil.getWorkbookInfo(2,new int[]{0,1,2},workbook);
            for (String[] item:cells) {
                FinanceFeedDto FinanceFeedDto=new FinanceFeedDto();
                FinanceFeedDto.setCid(item[1]);
                FinanceFeedDto.setName(item[0]);
                FinanceFeedDto.setOrgName(item[2]);
                FinanceFeedDtos.add(FinanceFeedDto);

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
        return FinanceFeedDtos;
    }

    @Override
    public List<FinanceFeedDto> analyseVallageExcel(MultipartFile file) {



        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();

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
                FinanceFeedDto FinanceFeedDto=new FinanceFeedDto();
                FinanceFeedDto.setCid(row.getCell(6).toString());
                FinanceFeedDto.setName(row.getCell(3).toString());
                FinanceFeedDto.setOrgName(row.getCell(11).toString());
                FinanceFeedDtos.add(FinanceFeedDto);

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




        return FinanceFeedDtos;
    }

    @Override
    public List<FinanceFeedDto> analyseCityExcel(File file) {
        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            int type=ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                List<String[]> cells = ExcelUtil.getWorkbookInfo(1, new int[]{2, 3, 4}, workbook);

                for (String[] item : cells) {
                    FinanceFeedDto FinanceFeedDto = new FinanceFeedDto();
                    FinanceFeedDto.setName(item[0]);
                    FinanceFeedDto.setCid(item[1]);
                    FinanceFeedDto.setOrgName(item[2]);
                    FinanceFeedDtos.add(FinanceFeedDto);
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
        return FinanceFeedDtos;
    }
    public List<FinanceFeedDto> analyseCityExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();

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

                FinanceFeedDto FinanceFeedDto=new FinanceFeedDto();
                for (Object o : re) {
                    if(col==3)
                        FinanceFeedDto.setName(String.valueOf(o));
                    else if(col==2)
                        FinanceFeedDto.setCid(String.valueOf(o));

                    FinanceFeedDto.setOrgName(areaName);
                    col++;
                }
                FinanceFeedDtos.add(FinanceFeedDto);

            }


        } catch (Exception e) {
            logger.error("methodName = analyseCityExcelEventmode\n"+e.getMessage());
        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return FinanceFeedDtos;
    }
    @Override
    public List<FinanceFeedDto> analyseVallageExcel(File file) {
        long startTime = System.currentTimeMillis();
        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();
        Workbook workbook = null;
        try {
            int type=ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                Sheet sheet = workbook.getSheetAt(0);
                int rowLength = sheet.getLastRowNum();

                for (int i = 1; i < rowLength; i++) {
                    Row row = sheet.getRow(i);
                    FinanceFeedDto FinanceFeedDto = new FinanceFeedDto();
                    if (row.getCell(3) == null || row.getCell(4) == null) {
                        continue;
                    }
                    FinanceFeedDto.setCid(row.getCell(3).toString().replaceAll("\"", ""));
                    FinanceFeedDto.setName(row.getCell(4).toString());
                    FinanceFeedDto.setOrgName(row.getCell(2).toString());
                    FinanceFeedDtos.add(FinanceFeedDto);

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

        return FinanceFeedDtos;
    }
    public List<FinanceFeedDto> analyseVallageExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<FinanceFeedDto> FinanceFeedDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(2);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            for (List<Object> re : res) {
                int col=0;
                if(re.size()<3||re.get(3)==null||re.get(3).toString().length()!=18){
                    continue;
                }
                FinanceFeedDto FinanceFeedDto=new FinanceFeedDto();
                for (Object o : re) {
                    if(col==4)
                        FinanceFeedDto.setName(String.valueOf(o));
                    else if(col==3)
                        FinanceFeedDto.setCid(String.valueOf(o));
                    else if(col==2)
                        FinanceFeedDto.setOrgName(String.valueOf(o));
                    col++;
                }
                FinanceFeedDtos.add(FinanceFeedDto);

            }


        } catch (Exception e) {
            logger.error("methodName = analyseVallageExcelEventmode\n"+e.getMessage());

        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return FinanceFeedDtos;
    }

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

    @Override
    public Map<String, FinanceFeedDto> getFinanceFeedFromRow(Map<String,FinanceFeedDto> FinanceFeedDtoMap, MultipartFile file,String type) {


        List<FinanceFeedDto> FinanceFeedDtos =null;
        if(type.equals(Constant.financeFeedCity)){
            FinanceFeedDtos= analyseCityExcel(file);
        }
        if(type.equals(Constant.financeFeedVallage)){
            FinanceFeedDtos= analyseVallageExcel(file);
        }
        for (FinanceFeedDto one:FinanceFeedDtos  ) {
            if(one.getCid()==null)
                continue;
            String key=one.getName()+one.getCid();
            if(FinanceFeedDtoMap.containsKey(key)){
                FinanceFeedDtoMap.get(key).setRepeatTimesAdd();
            }else {
                FinanceFeedDtoMap.put(key, one);
            }

        }

        return FinanceFeedDtoMap;
    }

    @Override
    public Map<String, FinanceFeedDto> getFinanceFeedFromRow(String type,File[] files) {

        Map<String, FinanceFeedDto> res=new HashMap<>();
        for (File file: files) {
            List<FinanceFeedDto> FinanceFeedDtos =null;
            if(type.equals(Constant.financeFeedCity)){
                FinanceFeedDtos= analyseCityExcel(file);
            }
            if(type.equals(Constant.financeFeedVallage)){
                FinanceFeedDtos= analyseVallageExcel(file);
            }
            for (FinanceFeedDto one:FinanceFeedDtos  ) {
                if(one.getCid()==null)
                    continue;
                StringBuilder key=new StringBuilder().append(one.getName()).append(one.getCid());

                if(res.containsKey(key.toString())){
                    res.get(key.toString()).setRepeatTimesAdd();
                }else {
                    res.put(key.toString(), one);
                }

            }

        }
        return res;
    }
    @Override
    public Map<String, FinanceFeedDto> init(Map<String, FinanceFeedDto> res,MultipartFile file,String type){

        return getFinanceFeedFromRow(res,  file,type);
    }
    @Override
    public Map<String, FinanceFeedDto> initByPath(String path,String type){
        File[] files=getFiles(path);
        if(files==null||files.length==0)
            return null;

        return getFinanceFeedFromRow(  type,files);
    }
    /**
     * 将城市、城镇医疗保险数据合并到一个map里
     */
    public Map<String, FinanceFeedDto> initMerge(String pathCity,String pathVallage){
        Map<String, FinanceFeedDto> city=getFinanceFeedFromRow(   Constant.financeFeedCity,getFiles(pathCity));
        Map<String, FinanceFeedDto> vallage=getFinanceFeedFromRow( Constant.financeFeedVallage,getFiles(pathVallage));

        for (String key:city.keySet()  ) {
            if(vallage.containsKey(key)){
                vallage.get(key).setRepeatTimesAdd(city.get(key).getRepeatTimes());
            }else {
                vallage.put(key,city.get(key));
            }
        }
        return vallage;
    }
    public Map<String, FinanceFeedDto> initMerge( Map<String, FinanceFeedDto> city, Map<String, FinanceFeedDto> vallage){


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
    public List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String FinanceFeedUpload) {
        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        File[] files= FileUtil.getFilesInPath(FinanceFeedUpload);
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
    public String checkFinanceFeedDifExcelFile(ServletContext applications, String appType, String errType, String emptyType) {
        Object all=applications.getAttribute(appType);
        if(all==null){
            return errType;
        }
        List<FinanceFeedDto> mapKeyList = (List<FinanceFeedDto> ) all;
        List<FinanceFeedDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());
        if(tempList.size()==0){
            return emptyType;
        }
        return Constant.SUCCESS;
    }

    @Override
    public int getListFinanceFeedDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle) {
        Object all=applications.getAttribute(appType);

        List<FinanceFeedDto> mapKeyList = (List<FinanceFeedDto> ) all;
        List<FinanceFeedDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(FinanceFeedDto::getRepeatTimes).reversed());

        ServletOutputStream os =null;

        try {
            os = response.getOutputStream();// 取得输出流
            response.setCharacterEncoding("UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(excelFileTitle.getBytes("gb2312"), "iso8859-1") + ".xls");//fileName为下载时用户看到的文件名利用jxl 将数据从后台导出为excel
            response.setHeader("Content-Type", "application/msexcel");
            String[] titles = new String[]{
                    "序号","姓名","身份证号码","单位","重复次数"
            };
            List<String[]> tempData=new ArrayList<>();
            int index=1;
            for (FinanceFeedDto medicalDto : tempList) {
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
