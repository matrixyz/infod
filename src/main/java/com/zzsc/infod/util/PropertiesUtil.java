package com.zzsc.infod.util;

import java.io.IOException;
import java.util.Properties;

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

}
