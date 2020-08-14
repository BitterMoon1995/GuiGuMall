package com.lewo.zmail.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsProductSaleAttr;
import com.lewo.zmall.model.PmsProductSaleAttrValue;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.SkuService;
import com.lewo.zmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RouteController {
    @Reference
    SkuService skuService;//SOA架构的要义：面向服务架构
    @Reference
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
    public String item(@PathVariable String skuId, ModelMap modelMap){
        System.out.println(skuId);
        PmsSkuInfo skuInfo=skuService.getById(skuId);
        modelMap.put("skuInfo",skuInfo);

        String spuId = skuInfo.getSpuId();
        List<PmsProductSaleAttr> spuSaleAttrList =  spuService.getCheckedSaleAttr(spuId,skuId);
        modelMap.put("spuSaleAttrListCheckBySku",spuSaleAttrList);

        return "item";
    }

}
