package com.zzsc.infod.service.impl;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MedicalAnalyseServiceExcelImpl implements MedicalAnalyseServiceExcel {
    @Override

    public List<MedicalDto> analyseCityExcel(File file) {
        List<MedicalDto> MedicalDtos=new ArrayList<>();

        Workbook workbook = null;
        try {

            workbook = WorkbookFactory.create(file);
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength=sheet.getLastRowNum();

            for (int i = 3; i < rowLength; i++) {
                Row row = sheet.getRow(i);

                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(row.getCell(2).toString());
                medicalDto.setName(row.getCell(3).toString());
                MedicalDtos.add(medicalDto);
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




        return MedicalDtos;
    }

    @Override
    public List<MedicalDto> analyseVallageExcel(File file) {

        long starTime=System.currentTimeMillis();

            List<MedicalDto> MedicalDtos=new ArrayList<>();

        Workbook workbook = null;
        try {

            workbook = WorkbookFactory.create(file);
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength=sheet.getLastRowNum();

            for (int i = 3; i < rowLength; i++) {
                Row row = sheet.getRow(i);

                MedicalDto medicalDto=new MedicalDto();
                medicalDto.setCid(row.getCell(6).toString());
                medicalDto.setName(row.getCell(3).toString());
                MedicalDtos.add(medicalDto);
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
        long endTime=System.currentTimeMillis();
        long Time=endTime-starTime;
        System.out.println(Time);



        return MedicalDtos;
    }

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

    @Override
    public Map<String, MedicalDto> getMedicalFromRow(File[] files,String type) {
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
                String key=one.getName()+one.getCid();
                if(res.containsKey(key)){
                    res.get(key).setRepeatTimesAdd();
                }else {
                    res.put(key, one);
                }

            }

        }
        return res;
    }
    public Map<String, MedicalDto> init(String path,String type){
        return getMedicalFromRow(getFiles(path),  type);
    }
    public Map<String, MedicalDto> initMerge(String pathCity,String pathVallage){
        Map<String, MedicalDto> city=getMedicalFromRow(getFiles(pathCity),  Constant.medicalCity);
        Map<String, MedicalDto> vallage=getMedicalFromRow(getFiles(pathVallage),  Constant.medicalVallage);

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
        if(files.length==0){
            AnalyseExcelUploadDto temp=new AnalyseExcelUploadDto();
            temp.setFileName("没有数据!");
            list.add(temp);
        }
        return list;
    }
}
