package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsBaseCatalog1;
import com.lewo.zmall.model.PmsBaseCatalog2;
import com.lewo.zmall.model.PmsBaseCatalog3;
import com.lewo.zmall.service.PmsSpuService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class CatalogController {
    @Reference
    PmsSpuService PmsSpuService;
    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        return PmsSpuService.getCatalog1();
    }
    @RequestMapping("/getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(@RequestParam("catalog1Id") String id){return PmsSpuService.getCatalog2(id);}
    @RequestMapping("/getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(@RequestParam("catalog2Id") String id){return PmsSpuService.getCatalog3(id);}
}
