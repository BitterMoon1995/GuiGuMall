package com.lewo.zmail.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.PlatformAttrService;
import com.lewo.zmall.service.SearchService;
import com.lewo.zmall.service.SkuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Controller
public class SearchController {
    @DubboReference
    SearchService service;
    @DubboReference
    PlatformAttrService platformAttrService;

    @RequestMapping("index.html")
    public String index(){
        return "index";
    }

    @GetMapping("list")
    public String list(PmsSearchParam param, Model model) throws IOException {
        List<PmsSearchSkuInfo> results = service.searchSku(param, 0, 200,"price", true);

        HashSet<String> baseAttrSet = new HashSet<>();
        results.forEach(info->{
            List<PmsSkuAttrValue> valueList = info.getSkuAttrValueList();
            valueList.forEach(value->{
                System.out.println(value.getValueId());
                baseAttrSet.add(value.getValueId());
            });
        });
        List<PmsBaseAttrInfo> attrInfos = platformAttrService.attrListByValues(baseAttrSet);
        model.addAttribute("attrList",attrInfos);
        model.addAttribute("skuLsInfoList",results);

        return "list";
    }
}
