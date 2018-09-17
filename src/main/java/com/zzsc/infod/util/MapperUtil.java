package com.zzsc.infod.util;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {
    public static void main(String argv[]){
        String res=MapperUtil.getInsertSql("g_channel","dt");
        //  GContent GContent=(GContent) MapperUtil.getDefaultPo(GContent.class,"dt","g_content");

        System.out.println(res);
    }
    /**
     * 1.用于获取结果集的映射关系,在main方法中执行即可获取mapper 的 @Results 的映射字符
     */
    public static String getResultsStr(Class origin) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@Results({\n");
        for (Field field : origin.getDeclaredFields()) {
            String property = field.getName();
            //映射关系：对象属性(驼峰)->数据库字段(下划线)
            String column = new PropertyNamingStrategy.SnakeCaseStrategy().translate(field.getName()).toUpperCase();
            stringBuilder.append(String.format("@Result(property = \"%s\", column = \"%s\"),\n", property, column));
        }
        stringBuilder.append("})");
        return stringBuilder.toString();
    }

    public  static String getUpdateSql(String table_name,String db_name){
        StringBuilder stringBuilder = new StringBuilder();

        List<BeanCommon> ls=JDBCUtil.getComment(table_name,db_name);

        for(BeanCommon bs : ls) {

            stringBuilder.append("<if test=\"");
            stringBuilder.append(StringUtil.underline2Camel(bs.getName(),true));
            stringBuilder.append("!=null\">");
            stringBuilder.append(bs.getName());
            stringBuilder.append("=#{");
            stringBuilder.append(StringUtil.underline2Camel(bs.getName(),true));
            stringBuilder.append("},</if>\n");
        }

        return stringBuilder.toString();
    }
    public  static String getInsertSql(String table_name,String db_name){
        StringBuilder stringBuilder = new StringBuilder();

        List<BeanCommon> ls=JDBCUtil.getComment(table_name,db_name);

        for(BeanCommon bs : ls) {

            stringBuilder.append(bs.getName());
            stringBuilder.append(",\n");


        }
        stringBuilder.append(")values(\n");
        for(BeanCommon bs : ls) {

            stringBuilder.append("#{");
            stringBuilder.append(StringUtil.underline2Camel(bs.getName(),true));

            stringBuilder.append("},\n");


        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 根据数据库中字段的默认值，实例化对象并用默认值赋值给对象
     * @param catClass 目标对象
     * @param db_name 对象对应数据库名称
     * @param table_name
     * @return
     */
    public static Object getDefaultPo( Class catClass,String db_name, String table_name  ){
        Object obj =null;
        List<BeanCommon> ls=JDBCUtil.getComment(table_name,db_name);
        try {
        // 实例化这个类
          obj = catClass.newInstance();
        // 获得这个类的所有方法
        Method[] methods = catClass.getMethods();
        // 循环查找想要的方法
        for(Method method : methods) {
            if (method.getName().contains("set")) {
                List<BeanCommon> lss = ls.stream()
                        .filter(x ->
                                ("set"+StringUtil.underline2Camel(x.getName(),false))
                                        .equals(method.getName())).collect(Collectors.toList());


                if(lss.size()>0){

                    if (lss.get(0).getType().contains( "int")  )
                        method.invoke(obj,  Integer.parseInt( StringUtil.isNumInt(lss.get(0).getDefaults())?lss.get(0).getDefaults():"0" ));
                    else if (lss.get(0).getType().contains( "varchar") )
                        method.invoke(obj,  lss.get(0).getDefaults());
                    else if ( lss.get(0).getType().contains( "text"))
                        method.invoke(obj,  lss.get(0).getDefaults());
                    else if (lss.get(0).getType().contains( "datetime") )
                        method.invoke(obj,  new Date());
                    else
                        method.invoke(obj,"null" );
                }

            }
        }

        }  catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  obj;
    }


}
