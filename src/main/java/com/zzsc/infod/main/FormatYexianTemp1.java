package com.zzsc.infod.main;

import com.zzsc.infod.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatYexianTemp1 {
    public static void main(String[] args) {
        initAreaCode(new File("D:\\叶县民政局\\行政区划.xls"));

        File files = new File("D:\\叶县民政局\\2019年3季度复审后低保\\");

        File[] fileList = files.listFiles();

        for ( File file :fileList){
            List<String[]> analyseExcel=analyseExcel(file);

            String[] title=new String[]{"姓名","证件类别","证件号码","参与项目行政区划","民族","住址","联系电话","补贴金额","银行类别","开户姓名","银行账号"};
            try {
                String sheetName=analyseExcel.get(0)[0];
                analyseExcel.remove(0);
                exportExcelFix(sheetName , title, analyseExcel, new FileOutputStream(new File("D:\\叶县民政局\\2019年3季度复审后低保\\"+
                        file.getName().substring(0,file.getName().lastIndexOf("."))+"_new.xls"))  );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //break;
        }


    }
    static Map<String,String> ac;
    public static void initAreaCode(File file){
        ac=new HashMap<>();


        Workbook workbook = null;
        List<String[]> res=new ArrayList<>();
        try {
            int type= ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                Sheet sheet = workbook.getSheet("Sheet1");

                for (int i = 0; i <=685; i++) {
                    Row row = sheet.getRow(i);
                   // String[] rows=new String[]{row.getCell(0).toString(),row.getCell(1).toString()};
                    if( row!=null&& row.getCell(1)!=null&&row.getCell(0)!=null){
                        ac.put(row.getCell(1).toString().trim(),row.getCell(0).toString());
                    }




                }
                is.close();
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

    }

    public static List<String[]> analyseExcel(File file) {
        Workbook workbook = null;
        List<String[]> res=new ArrayList<>();
        try {
            int type= ExcelUtil.switchFileType(file);
            if(type==2003) {//2003
                InputStream is = new FileInputStream(file);
                workbook = new HSSFWorkbook(is);

                String sheetName=   workbook.getSheetAt(0).getSheetName();
                res.add(new String[]{sheetName});
                int sl=workbook.getNumberOfSheets();
                for (int i = 0; i < sl; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    int len=getSheetRealRowCount(sheet);
                    for (int j = 0; j <len; j++) {
                        Row row = sheet.getRow(j);
                        String pNum=getCellValueByCell(row.getCell(6));//此行人数
                        int pcount=1;//此行人数
                        try {
                            pcount=Integer.parseInt(pNum);
                        }catch (Exception e){

                            System.out.println("数字格式异常"+j);
                        }
                        for (int k = 1; k <= pcount; k++) {
                            String[] line=new String[11];
                            line[0]=getCellValueByCell(row.getCell(3));//姓名
                            line[1]="1";//身份证类别
                            line[2]=getCellValueByCell(row.getCell(4));//身份证号码
                            if(ac.get(row.getCell(2).toString())==null){
                                line[3]=ac.get(row.getCell(2).toString()+"村");
                            }else{
                                line[3]=ac.get(row.getCell(2).toString() );

                            }

                            line[4]="1";
                            line[5]=getCellValueByCell(row.getCell(2));//地址
                            line[6]=getCellValueByCell(row.getCell(0));//电话
                            if(k==1){
                                line[7]=(row.getCell(9).getNumericCellValue()+row.getCell(10).getNumericCellValue( ))+"";
                            }else{
                                line[7]=(row.getCell(9).getNumericCellValue()/2)+"";
                            }

                            line[8]="开户银行";//银行卡号
                            line[9]=getCellValueByCell(row.getCell(3));//开户姓名
                            line[10]=getCellValueByCell(row.getCell(5));//银行卡号
                            res.add(line);
                        }

                    }

                }

                is.close();
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

        return res;
    }
    private static String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        CellType cellType=cell.getCellType();

        if(cellType==CellType.STRING){
            return cell.getStringCellValue();
        }else if(cellType==CellType.NUMERIC){
            return new BigDecimal(cell.getNumericCellValue()).longValue()+"";
        }

        return cellValue;
    }
    public static void exportExcelFix(String sheetName, String[] titles,List<String[]> cells, OutputStream out) {
        /**
         * 注意这只是07版本以前的做法对应的excel文件的后缀名为.xls
         * 07版本和07版本以后的做法excel文件的后缀名为.xlsx
         */
        //创建新工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //新建工作表
        HSSFSheet sheet = workbook.createSheet(sheetName);
        int rowLen=cells.size();
        int colLen=titles.length;
        //设置标题样式-颜色
        // 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 20); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("黑体"); //字体
        HSSFCellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setBorderBottom(BorderStyle.THIN); //下边框
        styleTitle.setBottomBorderColor((short)0 ); //下边框
        styleTitle.setBorderLeft(BorderStyle.THIN);//左边框
        styleTitle.setLeftBorderColor((short)0 ); //下边框
        styleTitle.setBorderTop(BorderStyle.THIN);//上边框
        styleTitle.setBorderRight(BorderStyle.THIN);//右边框
        styleTitle.setFillForegroundColor((short)256 );
        styleTitle.setFont(font);
        styleTitle.setAlignment( HorizontalAlignment.CENTER); //水平布局：居中
        styleTitle.setWrapText(true);
        HSSFFont fontContent = workbook.createFont();
        fontContent.setFontHeightInPoints((short) 10); //字体高度
        fontContent.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        HSSFCellStyle styleContent = workbook.createCellStyle();
        styleContent.setBorderBottom(BorderStyle.THIN); //下边框
        styleContent.setBottomBorderColor((short)0 ); //下边框
        styleContent.setBorderLeft(BorderStyle.THIN);//左边框
        styleContent.setBorderTop(BorderStyle.THIN);//上边框
        styleContent.setBorderRight(BorderStyle.THIN);//右边框
        styleContent.setRightBorderColor((short)0);
        styleContent.setFont(fontContent);
        styleContent.setAlignment(HorizontalAlignment.CENTER); //水平布局：居中
        styleContent.setWrapText(true);
        HSSFRow rowFirst = sheet.createRow(0);
        for (int i = 0; i < colLen; i++) {
            HSSFCell cell=rowFirst.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(styleTitle);
        }
        for (int i = 1; i <= rowLen; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < colLen; j++) {
                HSSFCell cell=row.createCell(j);
                cell.setCellValue( cells.get(i-1)[j]);
                cell.setCellStyle(styleContent);
            }
        }
        for (int i = 0; i < colLen; i++) {
            sheet.autoSizeColumn(i);//宽度自适应
        }
        //输出到磁盘中
        try {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int getSheetRealRowCount( Sheet sheet) {

         int rr=0;
        for (int i = 0; i <= sheet.getLastRowNum();i++) {
            Row r = sheet.getRow(i);
            int temp=0;
            for (int j = 0; j < 5; j++) {
              if(r.getCell(j)==null||r.getCell(j).getCellType()==CellType.BLANK){
                  temp++;
              }
            }
            if(temp>3){
                break;
            }
            rr++;
        }
        return rr;
    }
}
