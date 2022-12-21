package com.example.esdemo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @Description
 * @date 2022/12/19 17:32
 */
@Data
public class Page <T>{
//   @ApiModelProperty("当前页数据")
    private List<T> records;

//    @ApiModelProperty("总条数")
    private long total;

//    @ApiModelProperty("每页条数")
    private long size;

//    @ApiModelProperty("第几页")
    private long current;

//    @ApiModelProperty("sortCursor 游标")
    private Object[] sortCursor;

    public Page(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total   = total;
        this.size    = size;
        this.current = current;
    }

    public Page(List<T> records, Page page) {
        this.records = records;
        this.total   = page.getTotal();
        this.size    = page.getSize();
        this.current = page.getCurrent();
    }

    public static Page of(List records, Page page) {
        return new Page(records, page);
    }

    public static <T> Page<T> of(Page<T> page) {
        return new Page<>(page.getRecords(), page);
    }

}
