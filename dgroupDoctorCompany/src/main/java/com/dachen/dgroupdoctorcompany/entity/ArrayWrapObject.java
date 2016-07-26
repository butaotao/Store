package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.imsdk.archive.entity.ArchiveItem;

import java.util.List;

/**
 * @author gaozhuo
 * @date 2016/3/10
 *
 */
public class ArrayWrapObject<T> {
    public long total;
    public int start;
    public int pageSize;
    public int pageIndex;
    public int pageCount;
    public List<T> pageData;
}
