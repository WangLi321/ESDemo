package com.example.esdemo.controller;


import com.alibaba.fastjson.JSON;
import com.example.esdemo.dto.BasePageForm;
import com.example.esdemo.dto.Page;
import com.example.esdemo.dto.User;
import com.example.esdemo.util.EsUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @Description
 * @date 2022/12/20 10:06
 */
@RestController
@RequestMapping("/es")
@Slf4j
public class EsController {
    private static String indexName = "user_index";

    @Resource
    private EsUtil esUtil;

    @GetMapping("/createIndex")
    public Boolean create(@RequestParam("indexName") String indexName) {
        return esUtil.createIndex(indexName);
    }

    @GetMapping("searchIndex")
    public List<String> searchIndex(@RequestParam("indexName") String indexName) {
        return esUtil.searchIndex(indexName);
    }

    @GetMapping("deleteIndex")
    public Boolean deleteIndex(@RequestParam("indexName") String indexName) {
        return esUtil.deleteIndex(indexName);
    }


    @GetMapping("insert")
    public String insert(@RequestParam("id") String id) {
        User user = User.builder().userName("张十一").age(22).createTime(System.currentTimeMillis()).build();
        return esUtil.insert(indexName, id, JSON.toJSONString(user));
    }

    @GetMapping("insertBatch")
    public BulkResponse insertBatch() {
        Map<String, String> build = new ImmutableMap.Builder<String, String>()
                .put("1", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis()).userName("张三").age(22).build()))
                .put("2", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 10000).userName("王五").age(23).build()))
                .put("3", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 20000).userName("张三").age(25).build()))
                .put("4", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 30000).userName("王五").age(26).build()))
                .put("5", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 40000).userName("张三").age(28).build()))
                .put("6", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 50000).userName("王五").age(27).build()))
                .put("7", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 60000).userName("张三").age(24).build()))
                .put("8", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 70000).userName("王五").age(22).build()))
                .put("9", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 80000).userName("张三").age(23).build()))
                .put("10", JSON.toJSONString(User.builder().createTime(System.currentTimeMillis() + 90000).userName("王五").age(23).build()))
                .build();
        return esUtil.insertBatch(indexName, build);
    }

    @GetMapping("update")
    public String update(@RequestParam("id") String id) {
        User user = User.builder().userName("李五").updateTime(System.currentTimeMillis()).build();
        return esUtil.update(indexName, id, JSON.toJSONString(user));
    }

    @GetMapping("updateBatch")
    public BulkResponse updateBatch() {
        Map<String, String> build = new ImmutableMap.Builder<String, String>()
                .put("2", JSON.toJSONString(User.builder().userName("张四").updateTime(System.currentTimeMillis()).build()))
                .put("3", JSON.toJSONString(User.builder().userName("张五").updateTime(System.currentTimeMillis()).build()))
                .build();
        return esUtil.insertBatch(indexName, build);
    }

    @GetMapping("delete")
    public String delete(@RequestParam("id") String id) {
        return esUtil.delete(indexName, id);
    }

    @GetMapping("deleteBatch")
    public List<String> deleteBatch(@RequestParam("ids") List<String> ids) {
        return esUtil.deleteBatch(indexName, ids);
    }

    @GetMapping("searchById")
    public String searchById(@RequestParam("id") String id) {
        return esUtil.searchById(indexName, id);
    }

    @GetMapping("findTotal")
    public Long findTotal() {
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("userName", "王五");
        return esUtil.findTotal(indexName, queryBuilder);
    }

    @GetMapping("findAll")
    public List<Map<String, Object>> findAll() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.fuzzyQuery("userName", "张四"));
        return esUtil.findAll(indexName, queryBuilder);
    }

    @PostMapping("fromSizePage")
    public Page<Map<String, Object>> fromSizePage(@RequestBody BasePageForm basePageForm) {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userName", "王五");
        return esUtil.fromSizePage(indexName, termQueryBuilder, basePageForm);
    }

    @PostMapping("searchAfterPage")
    public Page<Map<String, Object>> searchAfterPage(@RequestBody BasePageForm basePageForm) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime")
                .from("1671499513000").to("1671510313000");
        return esUtil.searchAfterPage(indexName, rangeQueryBuilder, basePageForm);
    }

    @GetMapping("sinFieldsAggregateQuery")
    public List<Object> sinFieldsAggregateQuery() {
        // 需要分组的字段，可以随意指定
        List<String> fieldList = Lists.newArrayList("age");
        TermsAggregationBuilder termsAge = AggregationBuilders.terms(fieldList.get(0)).field(fieldList.get(0))
                .subAggregation(AggregationBuilders.avg("avg").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.sum("sum").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.min("min").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.count("count").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.cardinality("cardinality").field(fieldList.get(0)));
        return esUtil.aggregateQuery(indexName, fieldList, termsAge);
    }

    @GetMapping("multipleFieldsAggregateQuery")
    public List<Object> multipleFieldsAggregateQuery() {
        // 需要分组的字段，可以随意指定
        List<String> fieldList = Lists.newArrayList("age", "createTime");
        TermsAggregationBuilder termsAge = AggregationBuilders.terms(fieldList.get(0)).field(fieldList.get(0));
        TermsAggregationBuilder termsCreateTime = AggregationBuilders.terms(fieldList.get(1)).field(fieldList.get(1))
                .subAggregation(AggregationBuilders.avg("avg").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.sum("sum").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.min("min").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.count("count").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.cardinality("cardinality").field(fieldList.get(0)));
        return esUtil.aggregateQuery(indexName, fieldList, termsAge.subAggregation(termsCreateTime));
    }
}
