package com.zzsc.infod.model;
import com.zzsc.infod.util.StringUtil;
public class EndowmentDto extends Endowment{

    String page;
    String beginDate;
    String endDate;

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