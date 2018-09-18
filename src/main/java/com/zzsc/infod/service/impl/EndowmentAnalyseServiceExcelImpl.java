package com.zzsc.infod.service.impl;

import com.zzsc.infod.model.EndowmentDto;
import com.zzsc.infod.service.EndowmentAnalyseServiceExcel;
import com.zzsc.infod.util.FileUtil;
import com.zzsc.infod.util.StringUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
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

        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            List res=new ArrayList();
            for (int index = 0 ;index <1; index++) {

                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                //获取该sheet 的有效数据的行数
                int totalRows=sheet.getRows();

                for (int i = 3; i < totalRows; i++) {
                    if(sheet.getCell(0, i)!=null&&StringUtil.isEmpty(sheet.getCell(0, i).getContents()))
                        continue;



                    EndowmentDto endowmentDto=new EndowmentDto();

                    if(StringUtil.isNotEmpty(sheet.getCell(2, i).getContents())) {
                        endowmentDto.setCid(sheet.getCell(2, i).getContents());
                    }
                    if(StringUtil.isNotEmpty(sheet.getCell(3, i).getContents())) {
                        endowmentDto.setName(sheet.getCell(3, i).getContents());
                    }

                    EndowmentDtos.add(endowmentDto);
                }
            }
            wb.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EndowmentDtos;
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
}
