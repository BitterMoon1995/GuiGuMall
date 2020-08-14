package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.SkuService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    @RequestMapping("/saveSkuInfo")
    public void saveSkuInfo(@RequestBody PmsSkuInfo skuInfo){
        skuService.saveSkuInfo(skuInfo);
    }
    @RequestMapping("/delSku")
    public void delSku(@RequestParam String id){
        skuService.delSku(id);
    }
}
