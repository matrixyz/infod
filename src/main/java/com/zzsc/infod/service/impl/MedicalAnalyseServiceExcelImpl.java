package com.zzsc.infod.service.impl;

import com.zzsc.infod.constant.Constant;
import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import com.zzsc.infod.service.MedicalAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            String fileName=file.getName();
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);
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
                String key=one.getName()+one.getCid();
                if(medicalDtoMap.containsKey(key)){
                    medicalDtoMap.get(key).setRepeatTimesAdd();
                }else {
                    medicalDtoMap.put(key, one);
                }

            }

        return medicalDtoMap;
    }
    public Map<String, MedicalDto> init(Map<String, MedicalDto> res,MultipartFile file,String type){

        return getMedicalFromRow(res,  file,type);
    }
    /**
     * 将城市、城镇医疗保险数据合并到一个map里
     */

    /*public Map<String, MedicalDto> initMerge(String pathCity,String pathVallage){
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
    }*/
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
