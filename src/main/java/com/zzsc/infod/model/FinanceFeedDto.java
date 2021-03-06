package com.zzsc.infod.model;

public class FinanceFeedDto extends FinanceFeed{

    String page;
    String beginDate;
    String endDate;
    int repeatTimes;//重复次数
    String someCol;//某些需要导出的列数据
    String fileName;//用来存储重复数据所在文件名

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSomeCol() {
        return someCol;
    }

    public void setSomeCol(String someCol) {
        this.someCol = someCol;
    }



    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }
    public void setRepeatTimesAdd( ) {
        this.repeatTimes+=1;
    }
    public void setRepeatTimesAdd(int t ) {
        this.repeatTimes+=t;
    }
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}