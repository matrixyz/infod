package com.zzsc.infod.util;

/**
 * 用于从数据库表中存储字段的名称，类型，注释，和默认值
 */
public class BeanCommon {
    private String name;
    private String type;
    private String comment;
    private String defaults;
    private String primary;
    private String column;//字段名称
    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }



    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }




    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
