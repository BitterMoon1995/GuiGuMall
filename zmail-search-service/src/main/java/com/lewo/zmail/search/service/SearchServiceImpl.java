package com.lewo.zmail.search.service;

import com.alibaba.fastjson.JSON;
import com.lewo.zmall.model.PmsSearchParam;
import com.lewo.zmall.model.PmsSearchSkuInfo;
import com.lewo.zmall.model.PmsSkuAttrValue;
import com.lewo.zmall.model.PmsSkuSaleAttrValue;
import com.lewo.zmall.service.SearchService;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@DubboService
public class SearchServiceImpl implements SearchService {
    @Autowired
    RestHighLevelClient esClient;

    @Override
    public List<PmsSearchSkuInfo> searchSku(PmsSearchParam param,
    Integer from, Integer size,String sortField,Boolean isDesc) throws IOException {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        List<PmsSkuAttrValue> attrValueList = param.getValueId();
        String keyword = param.getKeyword();
        String catalog3Id = param.getCatalog3Id();
        //3级分类
        if (!StringUtils.isEmpty(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolBuilder.filter(termQueryBuilder);
        }
        //平台属性值
        if (attrValueList!=null){
            attrValueList.forEach(attrValue->{
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", attrValue.getValueId());
                boolBuilder.filter(termQueryBuilder);
            });
        }
        //搜索关键字
        if (!StringUtils.isEmpty(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolBuilder.must(matchQueryBuilder);
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        //排序
        sourceBuilder.sort(sortField, isDesc ? SortOrder.DESC:SortOrder.ASC);
        //分页
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //设置高亮字段
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.field("skuName");
        highlight.preTags("<span style='color: pink;font-weight: bolder;'>");
        highlight.postTags("</span>");
        sourceBuilder.highlighter(highlight);
        //创建SearchRequest、指定索引
        SearchRequest searchRequest = new SearchRequest("pmsskuinfo");
        searchRequest.source(sourceBuilder);
        //搜索、获取结果
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //结果集处理为List
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<SearchHit> hitList = Arrays.asList(hits);
        //结果集类型转换
        ArrayList<PmsSearchSkuInfo> results = new ArrayList<>();
        hitList.forEach(hit->{
            String sourceString = hit.getSourceAsString();
            PmsSearchSkuInfo searchInfo = JSON.parseObject(sourceString, PmsSearchSkuInfo.class);
            //高亮字段替换
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("skuName");
            if (highlightField!=null) {
                Text fragment = highlightField.getFragments()[0];
                searchInfo.setSkuName(fragment.toString());
            }
            results.add(searchInfo);
        });
        return results;
    }
}
