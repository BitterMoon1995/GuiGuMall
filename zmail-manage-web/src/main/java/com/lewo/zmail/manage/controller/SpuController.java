package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.webUtils.FileUploadUtil;
import com.lewo.zmall.model.PmsBaseSaleAttr;
import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.service.SpuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imageUrl = FileUploadUtil.uploadImage(multipartFile);
        System.out.println(imageUrl);
        return imageUrl;
    }
}
