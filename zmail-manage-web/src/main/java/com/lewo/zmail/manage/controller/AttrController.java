package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.service.PmsSpuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class AttrController {
    @Reference
    PmsSpuService pmsSpuService;
    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> spuList(String id){
        return pmsSpuService.spuList(id);
    }
}
