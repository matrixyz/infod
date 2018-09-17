package com.zzsc.infod.util;

import java.util.List;

public class HtmlUtil {

//根据数据表 构建 html的  form 表单
    public static String getFormByTable( ) {
    StringBuilder stringBuilder = new StringBuilder();

    List<BeanCommon> ls=JDBCUtil.getComment("g_content","dt");

        for(BeanCommon bs : ls){
            stringBuilder.append("<div class=\"form-group\">");
            stringBuilder.append("\n");
            stringBuilder.append("<label class=\"col-sm-3 " +
                    "control-label no-padding-right\" for=\""
                    +StringUtil.underline2Camel(bs.getName(),true)+"\"> "+bs.getComment()+"：</label>");
            stringBuilder.append("\n");
            stringBuilder.append("<div class=\"col-sm-9\">");
            stringBuilder.append("\n");
            if ("text".equals(bs.getType())){
                stringBuilder.append("<textarea "+
                        " id=\""+StringUtil.underline2Camel(bs.getName(),true)+
                        "\" name=\""+StringUtil.underline2Camel(bs.getName(),true)+"\" " +" class=\"form-control\" rows=\"6\" >" +
                        "${Content."+StringUtil.underline2Camel(bs.getName(),true) + "}"+
                        "</textarea>");
            }else if ("varchar".equals(bs.getType())||bs.getType().contains("int")||"datetime".equals(bs.getType())){
                stringBuilder.append("<input type=\"text\"" +
                        " id=\""+StringUtil.underline2Camel(bs.getName(),true)+
                        "\" name=\""+StringUtil.underline2Camel(bs.getName(),true)+"\" " +
                        "placeholder=\""+ bs.getComment() +"\" class=\"col-xs-10 col-sm-5\" value=\"${Content."+StringUtil.underline2Camel(bs.getName(),true)+"}\"/>");

            }else if ("datetime".equals(bs.getType())){

            }else {

            }

            stringBuilder.append("\n");

            stringBuilder.append("</div>");

            stringBuilder.append("\n");
            stringBuilder.append("</div>");
            stringBuilder.append("\n");
            stringBuilder.append("<div class=\"space-4\"></div>");
            stringBuilder.append("\n");
        }


        System.out.println(stringBuilder.toString());
    return null;
}

    public static void main(String[] args) {
        HtmlUtil.getFormByTable();
    }
}
