package com.zzsc.infod.service.impl;

import com.sun.deploy.net.HttpResponse;
import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.EventModelReadExcel;
import com.zzsc.infod.util.ExcelUtil;
import com.zzsc.infod.util.FileUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
public class MedicalAnalyseServiceExcelImpl implements MedicalAnalyseServiceExcel {

    private static Logger logger = Logger.getLogger(MedicalAnalyseServiceExcelImpl.class);


    public List<MedicalDto> analyseCityExcel(MultipartFile file) {
        List<MedicalDto> MedicalDtos=new ArrayList<>();

        Workbook workbook = null;
        try {
            String fileName=file.getOriginalFilename();
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);
            List<String[]> cells=ExcelUtil.getWorkbookInfo(3,new int[]{2,3},workbook);
            String area=ExcelUtil.getWorkbookInfo(1,3,workbook);
            for (String[] item:cells) {
                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(item[0]);
                medicalDto.setName(item[1]);
                medicalDto.setAreaName(area);
                MedicalDtos.add(medicalDto);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
        return MedicalDtos;
    }

    @Override
    public List<MedicalDto> analyseVallageExcel(MultipartFile file) {



        List<MedicalDto> MedicalDtos=new ArrayList<>();

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
                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(row.getCell(6).toString());
                medicalDto.setName(row.getCell(3).toString());
                medicalDto.setAreaName(row.getCell(11).toString());
                MedicalDtos.add(medicalDto);

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




        return MedicalDtos;
    }

    @Override
    public List<MedicalDto> analyseCityExcel(File file) {
        List<MedicalDto> MedicalDtos=new ArrayList<>();

        Workbook workbook = null;
        try {

                //2003
           workbook = new HSSFWorkbook(new FileInputStream(file));

            List<String[]> cells=ExcelUtil.getWorkbookInfo(3,new int[]{2,3},workbook);
            String area=ExcelUtil.getWorkbookInfo(1,3,workbook);

            for (String[] item:cells) {
                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(item[0]);
                medicalDto.setName(item[1]);
                medicalDto.setAreaName(area);
                MedicalDtos.add(medicalDto);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            return analyseCityExcelEventmode(file);

        }
        return MedicalDtos;
    }
    public List<MedicalDto> analyseCityExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<MedicalDto> MedicalDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(2);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            String areaName=String.valueOf(res.get(0).get(3));
            res=res.subList(2,res.size());
            for (List<Object> re : res) {
                int col=0;
                if(re.get(0)==null||re.get(1)==null){
                    continue;
                }
                MedicalDto medicalDto=new MedicalDto();

                for (Object o : re) {
                    if(col==3)
                        medicalDto.setName(String.valueOf(o));
                    else if(col==2)
                        medicalDto.setCid(String.valueOf(o));

                    medicalDto.setAreaName(areaName);
                    col++;
                }
                MedicalDtos.add(medicalDto);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return MedicalDtos;
    }
    @Override
    public List<MedicalDto> analyseVallageExcel(File file) {
        long startTime = System.currentTimeMillis();
        List<MedicalDto> MedicalDtos=new ArrayList<>();
        Workbook workbook = null;
        try {
            //workbook = WorkbookFactory.create(file);
            try {
                    //2003
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } catch (Exception e) {
            // TODO: handle exception
            //2007
                return analyseVallageExcelEventmode(file);
            }

            Sheet sheet = workbook.getSheetAt(0);
            int rowLength=sheet.getLastRowNum();

            for (int i = 1; i < rowLength; i++) {
                Row row = sheet.getRow(i);
                if(row.getCell(6)==null||row.getCell(3)==null){

                    continue;
                }
                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(row.getCell(6).toString());
                medicalDto.setName(row.getCell(3).toString());
                medicalDto.setAreaName(row.getCell(11).toString());
                MedicalDtos.add(medicalDto);

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
        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return MedicalDtos;
    }
    public List<MedicalDto> analyseVallageExcelEventmode(File file) {


        long startTime = System.currentTimeMillis();
        List<MedicalDto> MedicalDtos=new ArrayList<>();

        EventModelReadExcel reader=new EventModelReadExcel(2);
        try {
            reader.processOneSheet(file);
            List<List<Object>> res=reader.getAllValueList();
            for (List<Object> re : res) {
                int col=0;
                if(re.get(0)==null||re.get(1)==null){
                    continue;
                }
                MedicalDto medicalDto=new MedicalDto();
                for (Object o : re) {
                    if(col==3)
                        medicalDto.setName(String.valueOf(o));
                    else if(col==6)
                        medicalDto.setCid(String.valueOf(o));
                    else if(col==11)
                        medicalDto.setAreaName(String.valueOf(o));
                    col++;
                }
                MedicalDtos.add(medicalDto);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"+file.getName());

        return MedicalDtos;
    }

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

    @Override
    public Map<String, MedicalDto> getMedicalFromRow(Map<String,MedicalDto> medicalDtoMap, MultipartFile file,String type) {


            List<MedicalDto> MedicalDtos =null;
            if(type.equals(Constant.medicalCity)){
                MedicalDtos= analyseCityExcel(file);
            }
            if(type.equals(Constant.medicalVallage)){
                MedicalDtos= analyseVallageExcel(file);
            }
            for (MedicalDto one:MedicalDtos  ) {
                if(one.getCid()==null)
                    continue;
                String key=one.getName()+one.getCid();
                if(medicalDtoMap.containsKey(key)){
                    medicalDtoMap.get(key).setRepeatTimesAdd();
                }else {
                    medicalDtoMap.put(key, one);
                }

            }

        return medicalDtoMap;
    }

    @Override
    public Map<String, MedicalDto> getMedicalFromRow(String type,File[] files) {

        Map<String, MedicalDto> res=new HashMap<>();
        for (File file: files) {
            List<MedicalDto> MedicalDtos =null;
            if(type.equals(Constant.medicalCity)){
                MedicalDtos= analyseCityExcel(file);
            }
            if(type.equals(Constant.medicalVallage)){
                MedicalDtos= analyseVallageExcel(file);
            }
            for (MedicalDto one:MedicalDtos  ) {
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
    public Map<String, MedicalDto> init(Map<String, MedicalDto> res,MultipartFile file,String type){

        return getMedicalFromRow(res,  file,type);
    }
    @Override
    public Map<String, MedicalDto> initByPath(String path,String type){
        return getMedicalFromRow(  type,getFiles(path));
    }
    /**
     * 将城市、城镇医疗保险数据合并到一个map里
     */
    public Map<String, MedicalDto> initMerge(String pathCity,String pathVallage){
        Map<String, MedicalDto> city=getMedicalFromRow(   Constant.medicalCity,getFiles(pathCity));
        Map<String, MedicalDto> vallage=getMedicalFromRow( Constant.medicalVallage,getFiles(pathVallage));

        for (String key:city.keySet()  ) {
            if(vallage.containsKey(key)){
                vallage.get(key).setRepeatTimesAdd(city.get(key).getRepeatTimes());
            }else {
                vallage.put(key,city.get(key));
            }
        }
        return vallage;
    }
    public Map<String, MedicalDto> initMerge( Map<String, MedicalDto> city, Map<String, MedicalDto> vallage){


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
    public List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String medicalUpload) {
        List<AnalyseExcelUploadDto> list=new ArrayList<>();
        File[] files= FileUtil.getFilesInPath(medicalUpload);
        if(files==null||files.length==0){
            AnalyseExcelUploadDto temp=new AnalyseExcelUploadDto();
            temp.setFileName("没有数据!");
            list.add(temp);
            return list;
        }
        int id=1;
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
    public String checkMedicalDifExcelFile(ServletContext applications, String appType,String errType,String emptyType) {

        Object all=applications.getAttribute(appType);
        if(all==null){
            return errType;
        }
        List<MedicalDto> mapKeyList = (List<MedicalDto> ) all;
        List<MedicalDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());
        if(tempList.size()==0){
            return emptyType;
        }
        return Constant.SUCCESS;
    }

    @Override
    public int getListMedicalDifExcelFile(ServletContext applications,HttpServletResponse response, String appType, String excelFileTitle) {
        Object all=applications.getAttribute(appType);

        List<MedicalDto> mapKeyList = (List<MedicalDto> ) all;
        List<MedicalDto> tempList=  mapKeyList.stream().filter(x-> x.getRepeatTimes()>0).collect(Collectors.toList());
        tempList.sort(Comparator.comparingInt(MedicalDto::getRepeatTimes).reversed());

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
            for (MedicalDto medicalDto : tempList) {
                String[] item=new String[]{
                        String.valueOf(index),
                        medicalDto.getName(),
                        medicalDto.getCid(),
                        medicalDto.getAreaName(),
                        String.valueOf(medicalDto.getRepeatTimes())  };
                index++;
                tempData.add(item);
            }


            ExcelUtil obj = new ExcelUtil();
            obj.exportExcelFix("城镇、城乡医疗保险重复数据",titles,tempData,os);

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
