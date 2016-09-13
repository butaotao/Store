package com.dachen.dgroupdoctorcompany.archive.entity;

import com.dachen.imsdk.archive.entity.ArchiveItem;

import java.util.List;

/**
 * Created by Mcp on 2016/1/12.
 */
public class ArchiveListResult {
    public long total;
    public int start;
    public int pageSize;
    public int pageIndex;
    public int pageCount;
    public List<ArchiveItem> pageData;
}
