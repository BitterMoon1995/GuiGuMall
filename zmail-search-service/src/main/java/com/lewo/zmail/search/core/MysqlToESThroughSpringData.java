//package com.lewo.zmail.search.core;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.lewo.zmail.search.Search_Service;
//import com.lewo.zmail.search.repository.SkuRepository;
//import com.lewo.zmall.model.PmsSearchSkuInfo;
//import com.lewo.zmall.model.PmsSkuInfo;
//import com.lewo.zmall.service.SkuService;
//import org.elasticsearch.common.io.stream.StreamOutput;
//import org.elasticsearch.index.Index;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
//import org.springframework.data.elasticsearch.core.query.Criteria;
//import org.springframework.data.elasticsearch.core.query.IndexQuery;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//import java.io.BufferedOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest(classes = Search_Service.class)
//public class MysqlToESThroughSpringData {
//    @DubboReference
//    SkuService skuService;
//    @Autowired
//    ElasticsearchRestTemplate esTemplate;
//    @Autowired
//    SkuRepository skuRepository;
//    @Test
//    //珍 贵 历 史 资 料
//    public void transformThroughSpringStarter() {
//        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllSku();
//        ArrayList<PmsSearchSkuInfo> searchSkuInfos = new ArrayList<>();
//        ArrayList<IndexQuery> indexQueries = new ArrayList<>();//要插入的文档集
//
//        for (PmsSkuInfo info:pmsSkuInfos
//             ) {
//            PmsSearchSkuInfo searchSkuInfo = new PmsSearchSkuInfo();
//            BeanUtils.copyProperties(info,searchSkuInfo);
//            searchSkuInfos.add(searchSkuInfo);
//        }
//        //死妈点在于由于7.x+要废弃type，导致整个流程改完了，网上的教程全部没用，只能自己摸索
//        //最终找到ElasticsearchRestTemplate.bulkIndex(List<IndexQuery> queries, IndexCoordinates index)这个方法
//        IndexCoordinates indexCoordinates = IndexCoordinates.of("pmsskuinfo");
//
//        searchSkuInfos.forEach(searchInfo -> {
//            IndexQuery indexQuery = new IndexQuery();
//            indexQuery.setId(searchInfo.getId());//设置文档ID
//            indexQuery.setObject(searchInfo);//设置文档内容
//            indexQueries.add(indexQuery);
//        });
//        //批量插入的方法，参数是要插入的文档集和要插入的index坐标
//        esTemplate.bulkIndex(indexQueries,indexCoordinates);
//    }
//}
