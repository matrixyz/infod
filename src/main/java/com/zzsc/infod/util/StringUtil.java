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
    public static final String commonCnName="赵,钱,孙,李,周,吴,郑,王,冯,陈,褚,卫,蒋,沈,韩,杨,朱,秦,尤,许,何,吕,施,张,孔,曹,严,华,金," +
            "魏,陶,姜,戚,谢,邹,喻,柏,水,窦,章,云,苏,潘,葛,奚,范,彭,郎,鲁,韦,昌,马,苗,凤,花,方,俞,任,袁,柳,酆,鲍,史,唐,费,廉,岑,薛,雷," +
            "贺,倪,汤,滕,殷,罗,毕,郝,邬,安,常," +
            "乐,于,时,傅,皮,卞,齐,康,伍,余,元,卜,顾,孟,平,黄,和,穆,萧,尹,姚,邵,湛,汪,祁,毛,禹,狄,米,贝,明,臧,计,伏,成,戴,谈,宋,茅,庞," +
            "熊,纪,舒,屈,项,祝,董,粱,杜,阮,蓝,闵,席,季,麻,强,贾,路,娄,危,江,童,颜,郭,梅,盛,林,刁,钟,徐,邱,骆,高,夏,蔡,田,樊,胡,凌,霍," +
            "虞,万,支,柯,昝,管,卢,莫,经,房,裘,缪,干,解,应,宗,丁,宣,贲,邓,郁,单,杭,洪,包,诸,左,石,崔,吉,钮,龚,程,嵇,邢,滑,裴,陆,荣,翁," +
            "荀,羊,於,惠,甄,麴,家,封,芮,羿,储,靳,汲,邴,糜,松,井,段,富,巫,乌,焦,巴,弓,牧,隗,山,谷,车,侯,宓,蓬,全,郗,班,仰,秋,仲,伊,宫," +
            "宁,仇,栾,暴,甘,钭,厉,戎,祖,武,符,刘,景,詹,束,龙,叶,幸,司,韶,郜,黎,蓟,薄,印,宿,白,怀,蒲,邰,从,鄂,索,咸,籍,赖,卓,蔺,屠,蒙," +
            "池,乔,阴,欎,胥,能,苍,双,闻,莘,党,翟,谭,贡,劳,逄,姬,申,扶,堵,冉,宰,郦,雍,舄,璩,桑,桂,濮,牛,寿,通,边,扈,燕,冀,郏,浦,尚,农," +
            "温,别,庄,晏,柴,瞿,阎,充,慕,连,茹,习,宦,艾,鱼,容,向,古,易,慎,戈,廖,庾,终,暨,居,衡,步,都,耿,满,弘,匡,国,文,寇,广,禄,阙,东," +
            "殴,殳,沃,利,蔚,越,夔,隆,师,巩,厍,库,聂,晁,勾,敖,融,冷,訾,辛,阚,那,简,饶,空,曾,毋,沙,乜,养,鞠,须,丰,巢,关,蒯,相,查,後,荆,红," +
            "游,竺,权,逯,盖,益,桓,公,万俟,司马,上官,欧阳,夏侯,诸葛,闻人,东方,赫连,皇甫,尉迟,公羊,澹台,公冶,宗政,濮阳,淳于,单于,太叔,申屠,公孙,仲孙,轩辕,令狐,钟离,宇文," +
            "长孙,慕容,鲜于,闾丘,司徒,司空,亓官,司寇,仉,督,子车,颛孙,端木,巫马,公西,漆雕,乐正,壤驷,公良,拓跋,夹谷," +
            "宰父,谷梁,晋,楚,闫,法,汝,鄢,涂,钦,段干,百里,东郭,南门,呼延,归,海,羊舌,微生,岳,帅,缑,亢,况,后,有,琴,梁丘,左丘" +
            "东门,西门,商,牟,佘,佴,伯,赏,南宫,墨,哈,谯,笪,年,爱,阳,佟" +
            "言,福,百,家,姓,终";
    //中文姓名验证
    public static boolean isChineseName(String str){


        if(StringUtil.isEmpty(str)||commonCnName.contains(str.substring(0,1))==false)
            return false;
        Pattern pattern=Pattern.compile("^([\\u4e00-\\u9fa5]{2,4})$");
        Matcher match=pattern.matcher(str);
        return match.matches();

    }
    public static boolean isChineseName(String[] str){

        if(str[0]==null&&str[1]==null ){
            return false;
        }
        if(str[0]!=null&&str[1]==null ){
            return isChineseName(str[0]);
        }
        if(str[0]!=null&&str[1]!=null ){
            if(str[0].equals(str[1])){//两行同一列的两个个字符串相同，判断应该不是姓名
                return false;
            }else{
                return isChineseName(str[0]);
            }
        }
        return false;
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
            System.out.println( isChineseName("刘斯蒂芬房"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
