package com.zzsc.infod.main.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.zzsc.infod.util.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class FmkBase {

    Configuration cfg;
    String TEMPLATE_NAME ;
    String OUT_PUT_PATH ;
    Properties prop;//某个配置文件的全部属性
    public String   packageName ="com.zzsc.infod";
    public String   ModelPackageName ="com.zzsc.infod.model";
    public String targetTableName = null;
    /**
     * 初始化工作
     * 参数指的是配置文件 的文件名不带后缀properties
     */
    public void init(String tableName) throws Exception {

        // 负责管理的实例创建+设置模板文件所在的目录
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDefaultEncoding("UTF-8");
        String templatePath= FileUtil.getAppPath(PropertiesUtil.class)+"/templates/fmtl/sys/"  ;
        cfg.setDirectoryForTemplateLoading(new File(templatePath ));
        targetTableName = tableName;
        init(   );
    }
    public void init( ){ }

    public List<BeanCommon> getBeanInfoByTable(String table_name, String db_name) {

        List<BeanCommon> ls= JDBCUtil.getComment(table_name,db_name);
        String getPrimaryColumn=JDBCUtil.getPrimaryColumn(table_name,db_name);
        for(BeanCommon bs : ls){

            if ("varchar".equals(bs.getType())||bs.getType().contains("text") ||bs.getType().contains("char")){
                bs.setType("String");
            }else if ( bs.getType().contains("int") ){
                if ( bs.getType().equals("bigint") ){
                    bs.setType("long");
                }else {
                    bs.setType("int");
                }
            } else if ("datetime".equals(bs.getType())){
                bs.setType("java.util.Date");
            }else if ("decimal".equals(bs.getType())){
                bs.setType("java.math.BigDecimal");
            }else if ("double".equals(bs.getType())){
                bs.setType("double");
            }else if ("float".equals(bs.getType())){
                bs.setType("float");
            }else if ("blob".equals(bs.getType())){
                bs.setType("byte[]");
            }else if ("boolean".equals(bs.getType())){
                bs.setType("boolean");
            }
            bs.setColumn(bs.getName());
            bs.setName(StringUtil.underline2Camel(bs.getName(),true));

        }
        ls.get(0).setPrimary(getPrimaryColumn);
        return ls;
    }
    public void process(String className ,List<BeanCommon> ls ) throws IOException, TemplateException {};
    public void preparedData(){
        if(targetTableName==null){
            System.out.println("目标表为null，请设定targetTableName的值!");
            new LetterMatrix().printString("error");
            return;

        }
        List<String> ls=JDBCUtil.getTables("zzsc_infod");
        //在这里定位需要生成全套代码的表
        ls=ls.stream().filter(x->   x.equals(targetTableName)  ).collect(Collectors.toList());
        //ls = ls.subList(0, 1);
        ls.forEach((tableName)->{

            try {
                process(tableName ,getBeanInfoByTable(tableName,"zzsc_infod"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        });

    }
    /*
     * 将模版进行指定文件的输出
     */
    public void write(Map<String, Object> root,String outPutPath) throws IOException, TemplateException{
        Template t = cfg.getTemplate(TEMPLATE_NAME);
        OutputStream out = new FileOutputStream(  new File(outPutPath));
        t.process(root, new OutputStreamWriter(out));
    }
}
