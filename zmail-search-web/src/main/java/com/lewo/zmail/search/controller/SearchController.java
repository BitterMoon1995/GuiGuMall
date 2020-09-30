package com.lewo.zmail.search.controller;

import com.lewo.zmail.search.function.SearchFunction;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.PlatformAttrService;
import com.lewo.zmall.service.SearchService;
import com.lewo.zmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@CrossOrigin
@Controller
public class SearchController {
    @DubboReference
    SearchService service;
    @DubboReference
    PlatformAttrService attrService;
    @Autowired
    SearchFunction function;

    @RequestMapping("index.html")
    public String index(){
        return "index";
    }

    @GetMapping("list.html")
    public String list(PmsSearchParam param, Model model) throws IOException {
        //核心功能
        List<PmsSearchSkuInfo> results = service.searchSku(param, 0, 200,"price", true);

        //获取查询所有查询结果的平台属性值ID的集合，封装为SET(去重)
        HashSet<String> valueIdSet = new HashSet<>();
        results.forEach(info->{
            List<PmsSkuAttrValue> valueList = info.getSkuAttrValueList();
            valueList.forEach(value->{
                valueIdSet.add(value.getValueId());
            });
        });
        //再根据valueIdSet，聚合查询对应的平台属性，从而在页面生成属性：属性值列表，方便用户拣选
        List<PmsBaseAttrInfo> aggAttrs = attrService.attrListByValues(valueIdSet);

        //如果接受的查询参数包含平台属性值ID，那么
        //1.要在返回给前端的聚合平台属性的集合中，删除这些平台属性值ID所映射的平台属性
        //2.为改平台属性值生成面包屑
        String[] searchValueIds = param.getValueId();
        if (searchValueIds!=null) {
            ArrayList<PmsSearchCrumb> crumbs = new ArrayList<>();
            aggAttrs = function.processSelectedAttr(aggAttrs, searchValueIds, param,crumbs);
            //传输面包屑
            model.addAttribute("attrValueSelectedList",crumbs);
        }

        //搜索结果，SKU列表
        model.addAttribute("skuLsInfoList",results);

        //将当前请求的请求参数拼接为URL
        model.addAttribute("urlParam",function.genUrlParams(param));

        //传输平台属性列表
        model.addAttribute("attrList",aggAttrs);

        //面包屑：搜索关键字
        String keyword = param.getKeyword();
        if (StringUtils.isNotBlank(keyword))
            model.addAttribute("keyword",keyword);

        return "list";
    }

    @RequestMapping("error.html")
    public String error(){
        return "error";
    }

}
