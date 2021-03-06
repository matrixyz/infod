package com.zzsc.infod.util;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class ExcelUtil {
    /**
     *
     * @param rowStartIndex 工作表开始获取数据 的行索引
     * @param targetColumnIndex 工作表需要获取数据的  列索引集合
     * @param workbook 工作表
     * @return 需要获取的工作表的集合
     */
    public static List<String[]> getWorkbookInfo(int rowStartIndex,int[] targetColumnIndex,Workbook workbook) {
        List<String[]> res=new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int rowLength=sheet.getLastRowNum();
        try {
            for (int i = rowStartIndex; i <= rowLength; i++) {
                Row row = sheet.getRow(i);
                String[] rowInfo=new String[targetColumnIndex.length];
                int j=0;
                for (int c  :targetColumnIndex) {
                    if(row.getCell(c)!=null)
                        rowInfo[j++]=row.getCell(c).toString();
                    else
                        rowInfo[j++]=null;
                }
                res.add(rowInfo);
                if (StringUtil.isEmpty(rowInfo[0])&&StringUtil.isEmpty(rowInfo[1])){
                    break;
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return res;
    }
    /**
     *
     * @param workbook 工作表
     * @return 需要获取的工作表的姓名和身份证列的索引
     */
    public static int[] getWorkbookKeyColumIndex( Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        int rowLength = sheet.getLastRowNum();
        int startRowIndex = -1;
        int idCardIndex = -1;
        int nameIndex = -1;
        for (int i = 1; i <= rowLength; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < 19; j++) {//通过前8列定位身份证号码列的 列位置和行开始位置
                String t = null;
                if (row.getCell(j) != null) {
                    t = row.getCell(j).toString().replace("\"","");
                }
                if (IdcardUtils.validateCard(t)) {
                    idCardIndex = j;
                    if (nameIndex > -1) {
                        break;
                    }
                }
            }
            if(idCardIndex>-1){
                Row r1= sheet.getRow(i);
                Row r2= sheet.getRow(i+1);
                for (int g = 0; g < 19; g++){
                    Cell c =r1.getCell(g);
                   if(c!=null&&StringUtil.isNotEmpty(c.toString())){
                       if(StringUtil.isChineseName(
                               new String[]{c.toString(),
                                       (r2==null?null:(r2.getCell(g)==null?null:r2.getCell(g).toString()))
                               }
                               )){
                           nameIndex =g;
                           break;
                       }
                   }
               }
            }
            if (nameIndex > -1 && idCardIndex > -1) {
                startRowIndex = i;
                return new int[]{startRowIndex, nameIndex, idCardIndex};
            }
            if (i > 20) {
                return null;
            }
        }
        return null;
    }
    /**
     * 需要获取的工作表的姓名和身份证列的索引
     * @param res
     * @return
     */
    public static int[] getWorkbookKeyColumIndex(  List<List<Object>> res){
        int rowLength=res.size();
        int startRowIndex=-1;
        int idCardIndex=-1;
        int nameIndex=-1;
        for (int i = 1; i <= rowLength; i++) {
            List<Object> row=res.get(i);
            int rowLen=row.size();
            for (int j = 0; j <rowLen; j++) {
                String t=null;
                if(row.get(j)==null){
                    continue;
                }
                t=String.valueOf( row.get(j));
                t=t.replace("\"","");
                if(IdcardUtils.validateCard(t)){
                    idCardIndex=j;
                    if(nameIndex>-1){
                        break;
                    }
                }
            }
            if(idCardIndex>-1){
                List<Object> r1=res.get(i);
                List<Object> r2=res.get(i+1);
                int k=0;
                for (Object c:r1 ){
                    if(c!=null&&StringUtil.isNotEmpty(c.toString())){
                        if(StringUtil.isChineseName(
                                new String[]{c.toString(),
                                        (r2==null?null:(r2.get(k)==null?null:r2.get(k).toString()))
                                }
                        )){
                            nameIndex = k;
                            break;
                        }
                    }
                    k++;
                    continue;
                }
            }
            if(nameIndex>-1&&idCardIndex>-1){
                startRowIndex=i;
                return new int[]{startRowIndex,nameIndex,idCardIndex};
            }
            if(i>20){
                return null;
            }
        }
        return null;
    }
    public static String[] convertCharToNum(String col_a,String col_b){
        String squ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return new String[]{String.valueOf(squ.indexOf(col_a.toUpperCase())),String.valueOf(squ.indexOf(col_b.toUpperCase()))};
    }
    public static String getWorkbookInfo(int rowIndex,int colIndex,Workbook workbook){
        Sheet sheet = workbook.getSheetAt(0);
        if(sheet!=null){
            Row row = sheet.getRow(rowIndex);
            if(row!=null){
                return row.getCell(colIndex).toString();
            }
        }
        return null;
    }
    public void exportExcelFix(String sheetName, String[] titles,List<String[]> cells,
                               ServletOutputStream out) {
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
    public static int switchFileType(File file) throws IOException{
        InputStream inp=new FileInputStream(file);
        InputStream is = FileMagic.prepareToCheckMagic(inp);
        FileMagic fm = FileMagic.valueOf(is);
        switch(fm) {
            case OLE2:
                is.close();
                return  2003;
            case OOXML:
                is.close();
                return 2007;
            default:
                throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
        }
    }
}