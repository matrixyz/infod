package com.zzsc.infod.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtil {

    /**
     * 将父类对象的属性赋值给子类对象的属性
     * @param father
     * @param child
     * @throws Exception
     */
    public static void fatherToChild (Object father,Object child)throws Exception{
        if(!(child.getClass().getSuperclass()==father.getClass())){
            throw new Exception("child不是father的子类");
        }
        Class fatherClass= father.getClass();
        Field ff[]= fatherClass.getDeclaredFields();
        Field cf[]= child.getClass().getDeclaredFields();
        for(int i=0;i<ff.length;i++) {
            Field f = ff[i];//取出每一个属性，如deleteDate
            Class type = f.getType();
            Method m = fatherClass.getMethod("get" + upperHeadChar(f.getName()));//方法getDeleteDate
            Object obj = m.invoke(father);//取出属性值
            Method mc =child.getClass().getSuperclass().getMethod("set" + upperHeadChar(f.getName()),type);
            mc.invoke(child, obj);

        }
    }


    public static String upperHeadChar(String in){
        String head=in.substring(0,1);
        String out=head.toUpperCase()+in.substring(1,in.length());
        return out;
    }

    public static void main(String[] args) {

        /*GContent GContent=
                (GContent) MapperUtil.getDefaultPo(GContent.class,"dt","g_content");
        GContentDto GContentDto=new GContentDto();
        try {
            fatherToChild(GContent,GContentDto);
            System.out.println(GContentDto.getContentId());
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }
}
