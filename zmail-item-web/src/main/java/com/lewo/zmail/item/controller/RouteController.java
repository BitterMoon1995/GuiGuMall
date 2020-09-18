package com.lewo.zmail.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.SkuService;
import com.lewo.zmall.service.SpuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RouteController {
    @DubboReference
    SkuService skuService;//SOA架构的要义：面向服务架构
    @DubboReference
    SpuService spuService;

    @RequestMapping("index")
    public String index(Model model){
        ArrayList<String> list = new ArrayList<>();
        list.add("近平维尼");
        list.add("维尼登基");
        list.add("维尼扛麦");
        list.add("维尼大帝");
        list.add("维尼称帝");

        model.addAttribute("list",list);
        model.addAttribute("str","来吧，展示");
        return "index";
    }
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap) throws InterruptedException {
        PmsSkuInfo skuInfo=skuService.getById(skuId);
        //这里的错误页代表没有该skuID的商品
        if (skuInfo.getId().equals("noValue")) return "error";
        modelMap.put("skuInfo",skuInfo);
        //生成所属SPU的销售属性集，其中当前SKU的销售属性被标记被选中状态
        String spuId = skuInfo.getSpuId();
        List<PmsProductSaleAttr> spuSaleAttrList =  spuService.getCheckedSaleAttr(spuId,skuId);
        modelMap.put("spuSaleAttrListCheckBySku",spuSaleAttrList);
        //生成<SPU的销售属性对>-<SkuID>的映射集
        Map<String, String> skuMap = skuService.generateSkuMap(spuId);
        String toJSON = JSON.toJSONString(skuMap);
        //返的是解析后的JSON串！我头尼玛又卡1小时
        modelMap.put("skuSaleAttrHashJsonStr",toJSON);

        return "item";
    }

}
