package com.zzsc.infod.util;

import javax.servlet.ServletContext;
import java.util.List;

public class AttributeProcess {
    public static List<?> getList(Class cls, ServletContext app, String attrName){
        Object obj=app.getAttribute(attrName);
        if (obj==null){


        }

        return (List<?>)obj;

    }
}
