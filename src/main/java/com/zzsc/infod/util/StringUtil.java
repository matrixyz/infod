package com.zzsc.infod.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
    /**
     * 判断是否是空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(null==str  ||"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断是否是空
     * @param str
     * @return
     */
    public static String filterEmpty(String str){
        if(null==str  ||"".equals(str.trim())){
            return null;
        }else{
            return str;
        }
    }
    /**
     * 判断是否是有效金额
     * @param str
     * @return
     */
    public static String filterMoneyStr(String str){
        if(null==str  ||"".equals(str.trim())){
            return "0";
        }else{
            if(isDecimal(str))
                return str;
            else
                return "0";
        }
    }
    /**
     * 判断是否不是空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        if((str!=null)&&!"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 转换字符为整数，默认值为0
     * @param str
     * @return
     */
    public static int getNum(String str){
        if(!isNumInt(str)){
            return 0;
        }else{
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    /*
     * 匹配是否包含中文 str 要匹配的字符串 如果包含返回true 否则返回false
     */
    public static boolean isContainChinese(String str) {
        if(StringUtil.isEmpty(str))
            return false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find())
        {
            return true;
        }
        return false;
    }
    /*
     * 匹配是否为整数 如果是返回true 否则返回false
     */
    public static boolean isNumInt(String str) {

        if(StringUtil.isEmpty(str))
            return false;
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(str);
        if(m.find())
        {
            return true;
        }
        return false;
    }
    //金额验证
    public static boolean isDecimal(String str){
        if(StringUtil.isEmpty(str))
            return false;
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false){
            return false;
        }else{
            return true;
        }
    }

    //中文姓名验证
    public static boolean isChineseName(String str){
        if(StringUtil.isEmpty(str))
            return false;
        Pattern pattern=Pattern.compile("^([\\u4e00-\\u9fa5]{2,5})$");
        Matcher match=pattern.matcher(str);
        return match.matches();

    }
    //中国身份证号码验证
    public static boolean isChineseUid(String str){
        if(StringUtil.isEmpty(str))
            return false;
        Pattern pattern=Pattern.compile("^([0-9xX]{15,18})$");
        Matcher match=pattern.matcher(str);
        return match.matches();

    }
    public static boolean aa(String str) throws Exception {

        for (int i = 0; i < 100; i++) {
            System.out.println("aaa"+i);
            if(i==10)
                throw new Exception("出异常");
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            aa("a");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
