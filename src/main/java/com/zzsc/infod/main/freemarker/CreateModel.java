package com.zzsc.infod.main.freemarker;

import com.zzsc.infod.util.*;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateModel extends FmkBase{



    @Override
    public void process(String tableName ,List<BeanCommon> ls ) throws IOException, TemplateException {
        String className=StringUtil.underline2Camel(tableName, false);

        //2.向root中放入模版中所需信息
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("attrs",ls);
        root.put("packageName", packageName );
        root.put("packageNameDao", packageName+".dao");
        root.put("packageNameModel", ModelPackageName );
        root.put("packageNameService", ModelPackageName+".Service");
        root.put("className", className);
        //3.将模版进行指定文件的输出
        String outPath=System.getProperty("user.dir")+"/src/main/java"+"/"+OUT_PUT_PATH+"/"+className+".java";
        write(root,outPath);

    }
    //重写父类的方法
    public void init(){
        OUT_PUT_PATH="com/zzsc/infod/model";

        TEMPLATE_NAME="model.ftl";
    }
    /*
     * 执行入口
     */
    public static void main(String[] args) throws Exception {

        CreateModel hfm = new CreateModel();
        hfm.init(args[0]);
         hfm.preparedData();

    }



}
