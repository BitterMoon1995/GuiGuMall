package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsBaseSaleAttr;
import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.model.PmsProductSaleAttr;
import com.lewo.zmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class SpuController {
    @Reference
    SpuService spuService;
    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){
        return spuService.spuList(catalog3Id);
    }
    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        return spuService.baseSaleAttrList();
    }
    @RequestMapping("/saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        return spuService.saveSpuInfo(pmsProductInfo);
    }
}
