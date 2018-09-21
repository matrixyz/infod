package com.zzsc.infod.service.impl;

import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EndowmentAnalyseServiceExcelImpl implements EndowmentAnalyseServiceExcel {



    @Override

    public List<EndowmentDto> analyseCityExcel(File file) {
        List<EndowmentDto> EndowmentDtos=new ArrayList<>();

        Workbook workbook = null;
        try {

            workbook = WorkbookFactory.create(file);
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
             int rowLength=sheet.getLastRowNum();

            for (int i = 3; i < rowLength; i++) {
                Row  row = sheet.getRow(i);

                EndowmentDto endowmentDto=new EndowmentDto();
                endowmentDto.setCid(row.getCell(2).toString());
                endowmentDto.setName(row.getCell(3).toString());
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
    public List<EndowmentDto> analyseVallageExcel(File file) {


        return null;
    }

    @Override
    public File[] getFiles(String path) {
        return FileUtil.getFilesInPath(path);
    }

    @Override
    public Map<String, EndowmentDto> getEndowmentFromRow(File[] files) {
        Map<String, EndowmentDto> res=new HashMap<>();
        for (File file: files) {
            List<EndowmentDto> EndowmentDtos = analyseCityExcel(file);
            for (EndowmentDto one:EndowmentDtos  ) {
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
    public Map<String, EndowmentDto> init(String path){
        return getEndowmentFromRow(getFiles(path));
    }

    public static void main(String[] args) {

        File  file=new File("C:\\Users\\Administrator\\Desktop\\叶县社保数据对比\\20180611 叶县城乡居民参保数据\\保安.xlsx");
        Workbook workbook = null;
        try {
            boolean isE2007=false;
            if(file.getName().endsWith("xlsx")){
                isE2007=true;
            }
            if(isE2007){
                workbook = new XSSFWorkbook(file);
            }else {
                workbook = WorkbookFactory.create(file);
            }

            //workbook = WorkbookFactory.create(file);
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength=sheet.getLastRowNum();

            for (int i =1; i < 200; i++) {
                Row  row = sheet.getRow(i);
                for (int j=0;j<10;j++){
                    System.out.print(row.getCell(j).toString());
                    System.out.print("-");

                }
                System.out.println("");

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
    }
}
