package com.zzsc.infod.util;

import java.io.*;
import java.util.*;

public class PropertiesUtil {
    static  Properties prop;
    static{prop = new Properties();}
    public static String  get(String name){

        String res=null;
        try {
            prop.load( PropertiesUtil.class.getResourceAsStream("/application.properties"));
            res= prop.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static Properties  getFreemarkerCgf(String fileName ){
        Properties prop=null;
        try {
            prop = new Properties();
            prop.load( PropertiesUtil.class.getResourceAsStream("/freemarker/"+fileName+".properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    public static Properties  getCommonCgf(String fileName ){
        Properties prop=null;
        try {
            prop = new Properties();
            prop.load( PropertiesUtil.class.getResourceAsStream("/"+fileName+".properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    public static List<String> getValuesByKeyPatten(Properties properties, String keyPatten) {
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        List<String> res=new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if(key.toString().contains(keyPatten))
                res.add(value.toString());
        }
        return res;
    }
    //获取配对的用户的name和pwd
    public static List<String> getValuesAll(Properties properties ) {
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();

        List<String> res=new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object value = entry.getValue();
            Object key = entry.getKey();
            if(key.toString().contains("name")){
                res.add(value.toString()+properties.get(key.toString().replace("name","pwd")));
            }


        }



        return res;
    }
    public static boolean setSomeValue(String valueName,String valuePwd,Properties properties) {
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();

        List<String> res=new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object value = entry.getValue();
            Object key = entry.getKey();
            if(value.toString().equals(valueName)){

                 properties.setProperty(key.toString().replace("name","pwd"),valuePwd) ;
                OutputStream fos = null;
                try {
                    fos = new FileOutputStream(new File(PropertiesUtil.class.getResource("/").getPath()+"user.properties"));
                    properties.store(fos, "Comment");
                    fos.close();
                } catch ( Exception e) {
                    e.printStackTrace();
                }

                return true;
            }


        }



        return false;
    }
    public static void main(String[] args) {
        System.out.println(4%2);
    }
}
