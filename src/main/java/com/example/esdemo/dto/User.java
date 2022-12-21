package com.example.esdemo.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author
 * @Description
 * @date 2022/12/20 10:14
 */
@Data
@Builder
@Document(indexName = "user_index")
public class User {
    @Id
    private Integer id;
    private String userName;
    private Integer age;
    private Long createTime;
    private Long updateTime;
}
