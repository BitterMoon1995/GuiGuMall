package com.lewo.zmail.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsBaseAttrValue;
import com.lewo.zmall.service.PmsBaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class AttrController {
    @Reference
    PmsBaseService PmsBaseService;
    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> getAttrList(String id){
        return PmsBaseService.getAttrList(id);
    }
    @RequestMapping("/saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo attrInfo){
        return PmsBaseService.saveAttrInfo(attrInfo);
    }
    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam("attrId") String infoId){
        //请求参数和方法参数名不一样时，@RequestParam内指定要绑定的请求参数
        return PmsBaseService.getAttrValueList(infoId);
    }

}
