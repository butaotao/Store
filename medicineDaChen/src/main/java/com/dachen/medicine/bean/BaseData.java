package com.dachen.medicine.bean;

import android.os.Handler;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.io.Serializable;

/**
 * Created by TianWei on 2016/3/16.
 */
public class BaseData implements Serializable{
    private static final long serialVersionUID = -1012675329646121927L;
    private int pageCount;
//    private int pageNo;
    private int pageSize;
    private int total;
    private int start;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

//    public int getPageNo() {
//        return pageNo;
//    }
//
//    public void setPageNo(int pageNo) {
//        this.pageNo = pageNo;
//    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 处理分页
     *
     * @param refreshlistview
     * @param pageNo
     * @param totalCount
     */
    @SuppressWarnings("rawtypes")
    public void doPageInfo(final PullToRefreshBase refreshlistview, int pageNo, int totalCount) {
        doPageInfo(refreshlistview, pageNo, totalCount, 15);
    }

    /**
     * 处理分页
     *
     * @param refreshlistview
     * @param pageNo
     * @param totalCount
     */
    @SuppressWarnings("rawtypes")
    public void doPageInfo(final PullToRefreshBase refreshlistview, int pageNo, int totalCount, int pagesize) {
        if ((pageNo * pagesize) >= totalCount) {
            new Handler().postAtTime(new Runnable() {
                @Override
                public void run() {
                    refreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }, 1000);
        } else {
            refreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }
}
