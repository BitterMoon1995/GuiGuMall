package com.lewo.zmail.search.core;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lewo.zmail.search.Search_Service;
import com.lewo.zmail.search.utils.QueryUtil;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsSearchSkuInfo;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.PlatformAttrService;
import com.lewo.zmall.service.SkuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest(classes = Search_Service.class)
public class MysqlToES {
    @DubboReference
    SkuService skuService;
    @Autowired
    RestHighLevelClient esClient;
    @DubboReference
    PlatformAttrService attrService;

    @Test //判断是否存在
    public void determineExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest("pmsskuinfo");
        boolean exists = esClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    public void bulkInsertTest() throws InvocationTargetException, IllegalAccessException {
        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllSku();
        ArrayList<PmsSearchSkuInfo> searchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo info : pmsSkuInfos
        ) {
            PmsSearchSkuInfo searchSkuInfo = new PmsSearchSkuInfo();
            //注意这里使用的是spring的BeanUtils，用Apache的全部为null(骚的一逼)
            BeanUtils.copyProperties(info, searchSkuInfo);
            searchSkuInfos.add(searchSkuInfo);
        }
        bulkInsert("pmsskuinfo", searchSkuInfos);
    }

    //基本功问题：JSON理解不足
    public void bulkInsert(String indexName, List<PmsSearchSkuInfo> insertList) {
        BulkRequest bulkRequest = new BulkRequest();
        insertList.forEach(item -> {
            bulkRequest.add(new IndexRequest(indexName)
//                    .id(item.getId()).source(JSON.toJSON(item), XContentType.JSON));
                    //★必须传对象对应的JSON字符串★
                    .id(item.getId()).source(JSON.toJSONString(item), XContentType.JSON));
        });
        try {
            esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void reviewJSON() {
        PmsSearchSkuInfo info = new PmsSearchSkuInfo();
        info.setId("22");
        info.setSkuName("孙笑川");
        info.setSkuDesc("孙笑川死爹妈");
        System.out.println(JSON.toJSON(info).getClass());
        System.out.println(JSON.toJSONString(info).getClass());
    }

    @Test
    public void bulkDeleteTest() throws InvocationTargetException, IllegalAccessException {
        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllSku();
        ArrayList<String> idList = new ArrayList<>();

        for (PmsSkuInfo info : pmsSkuInfos
        ) {
            idList.add(info.getId());
        }
        bulkDelete("pmsskuinfo", idList);
    }

    //根据ID的集合批量删除
    public void bulkDelete(String indexName, List<String> idList) {
        BulkRequest bulkRequest = new BulkRequest();
        idList.forEach(id -> {
            bulkRequest.add(new DeleteRequest(indexName, id));
        });
        try {
            esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //match+term搜索
    @Test
    public void search() throws IOException {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("skuDesc", "全网通"));
        boolBuilder.filter(QueryBuilders.termQuery("skuDesc", "小米"));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100);
        System.out.println(sourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest("pmsskuinfo");
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("查询返回体："+JSON.toJSONString(searchResponse));

        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        System.out.println("命中条目：");
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("总命中数：");
        System.out.println(hits.length);
    }
    @Test
    public void testSearch() throws IOException {
        List<SearchHit> searchHits = QueryUtil.boolQuery("pmsskuinfo", "skuDesc", "全网通",
                "skuDesc", "小米", 0, 100);
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getSourceAsString());
        }
    }
    @Test
    public void getAttrInfoByAttrInfoValueIds() {
        HashSet<String> strings = new HashSet<>();
        strings.add("42");strings.add("47");strings.add("48");
        strings.add("49");strings.add("52");strings.add("53");
        strings.add("55");strings.add("56");strings.add("61");
        strings.add("63");strings.add("68");
        List<PmsBaseAttrInfo> attrs = attrService.attrListByValues(strings);
        attrs.forEach(System.out::println);
    }
}
