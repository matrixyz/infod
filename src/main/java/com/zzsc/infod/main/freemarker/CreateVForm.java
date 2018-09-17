package com.zzsc.infod.main.freemarker;

import com.zzsc.infod.util.BeanCommon;
import com.zzsc.infod.util.StringUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateVForm extends FmkBase {

    public void process(String tableName , List<BeanCommon> ls ) throws IOException, TemplateException {
        String className=StringUtil.underline2Camel(tableName, false);
        //2.向root中放入模版中所需信息
        Map<String, Object> root = new HashMap<String, Object>();



        root.put("attrs",ls);
        root.put("packageNameDao", packageName+".mapper");
        root.put("packageNameModel", ModelPackageName+".model");
        root.put("packageNameService", ModelPackageName+".service");
        root.put("className",  className);
        root.put("tableName",  tableName);
        root.put("primaryColumn",  ls.get(0).getPrimary());
        root.put("primaryProp",  StringUtil.underline2Camel(ls.get(0).getPrimary(), true));

        //3.将模版进行指定文件的输出
        String outPath=System.getProperty("user.dir")+"/src/main/resources"+"/"+OUT_PUT_PATH+"/"+className+"-form.html";
        write(root,outPath);

    }
    //重写父类的方法
    public void init(){
        OUT_PUT_PATH="templates/adm";

        TEMPLATE_NAME="v-form.ftl";
    }
    /*
     * 执行入口
     */
    public static void main(String[] args) throws Exception {

        CreateVForm hfm = new CreateVForm();
        hfm.init(args[0]);
        hfm.preparedData();

    }



}
