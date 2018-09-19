package com.zzsc.infod.util;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
    private int pageNo = 1;  //当前页
    private int pageSize = 20; //每页个数
    private int totalCount;  //总记录数
    private int totalPages;  //总页数--只读
    private int fromIndex;//分页后某页数据的开始索引
    private int toIndex;//分页后某夜数据结束的索引



    private boolean isFirstPage=false;
    private boolean hasPreviousPage=false;
    private boolean hasNextPage=false;
    private boolean isLastPage=false;

    public boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }




    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    private int prePage=0;
    private int nextPage=0;


    public List<Integer> getCurrentPageNoList() {
        return currentPageNoList;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    private List<Integer> navigatepageNums;//当前分页页码对应的分页集合
    private List<Integer> currentPageNoList;//当前页码分页对应的分页集合的节点编号
    private int pageListSize=8;//页码集合的长度


    private List<T> pageList;  //每页对应的集合泛型
    public int getPageNo() {
        return pageNo;
    }
    //当前页码不能小于1不能大于总页数
    public void setPageNo(int pageNo) {
        if(pageNo<1)
            this.pageNo = 1;
        else if(pageNo > totalPages)
            this.pageNo = totalPages;
        else
            this.pageNo = pageNo;

        this.fromIndex=(pageNo-1)*pageSize;

        if(totalCount%pageSize==0)
            this.toIndex=fromIndex+pageSize-1;
        else
            this.toIndex=this.fromIndex+totalCount%pageSize;

        int pagesListSize=(int)Math.ceil(totalPages*1.0/pageListSize);
        for(int i=0;i<=pagesListSize;i++){
            if(i==0){
                currentPageNoList.add(1);
            }else if(i==pagesListSize){
                currentPageNoList.add(totalPages);
            }else {
                currentPageNoList.add(i*pageListSize);
            }
        }

        for(int j=0;j<currentPageNoList.size();j++ ){
            if(pageNo>=currentPageNoList.get(j)){
                navigatepageNums.clear();
                for(int i=currentPageNoList.get(j);i<currentPageNoList.get(j+1);i++){
                    navigatepageNums.add(i);
                }
            }
        }


        if(pageNo==1) {
            isFirstPage = true;
            prePage=1;
        }else{
            prePage=pageNo-1;
        }
        if(pageNo>1)
            hasPreviousPage=true;
        if(pageNo==totalPages) {
            isLastPage = true;
            nextPage=totalPages;
        }else{
            nextPage=pageNo+1;
        }

        if(pageNo<totalPages) {
            hasNextPage = true;

        }



    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    //总记录数决定总页数
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.totalPages = (this.totalCount%this.pageSize==0)?this.totalCount/this.pageSize:this.totalCount/this.pageSize+1;
    }
    public int getTotalCount() {
        return totalCount;
    }

    //只读
    public int getTotalPages() {
        return totalPages;
    }


    public List<T> getPageList() {
        return pageList;
    }
    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }
    public PageBean(int pageNo, int pageSize, int totalCount, int totalPages,
                    List<T> pageList) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount=totalCount;
        setTotalCount(totalCount);
        setPageNo(pageNo);
        this.totalPages = totalPages;
        this.pageList = pageList;
        this.currentPageNoList=new ArrayList<>();
    }

    public int getFromIndex() {
        return fromIndex;
    }



    public int getToIndex() {
        return toIndex;
    }


    public PageBean() {
        super();
        this.currentPageNoList=new ArrayList<>();
        this.navigatepageNums=new ArrayList<>();
    }

    public static void main(String[] args) {


        PageBean pageInfo=new PageBean();

        pageInfo.setTotalCount(1233);
        pageInfo.setPageNo(6);
        List<Integer> i=pageInfo.getCurrentPageNoList();
        for (int x : i ) {
            System.out.println(x);
        }

    }
}
