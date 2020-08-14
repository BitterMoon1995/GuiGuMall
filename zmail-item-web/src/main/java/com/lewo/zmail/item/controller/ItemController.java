package com.lewo.zmail.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.service.SkuService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Reference
    SkuService skuService;


}
