package com.lewo.zmail.search.utils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lewo.zmall.service.SkuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class QueryUtil {

    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    static RestHighLevelClient esClient;

    @PostConstruct
    public void init(){
        esClient = restHighLevelClient;
    }

    @DubboReference
    SkuService skuService;

    public static List<SearchHit> boolQuery(String index,String matchField,String matchText,
    String termField,String termKeyword,Integer from,Integer size) throws IOException {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery(matchField, matchText));
        boolBuilder.filter(QueryBuilders.termQuery(termField, termKeyword));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        System.out.println(sourceBuilder.toString());
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        return Arrays.asList(hits);
    }
}
