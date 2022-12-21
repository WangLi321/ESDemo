package com.example.esdemo.dto;

import lombok.Data;

/**
 * @author
 * @Description
 * @date 2022/12/19 17:16
 */
@Data
public class BasePageForm {

    private int pageSize = 20;

    private int pageNum = 1;

//    @ApiModelProperty(value = "排序字段: 可选: 不同列表排序不同需再协商", example = "")
    private String orderBy;

//    @ApiModelProperty(value = "排序规则 true升序 false降序")
    private Boolean orderType = false;

//    @ApiModelProperty("排序游标")
    private Object[] sortCursor;

//    @ApiModelProperty("查询所有: 默认查询今日及所有未审核单子")
    private boolean isAll = false;
}
