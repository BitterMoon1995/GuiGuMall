package com.lewo.zmail.test.controller;

import com.lewo.exception.AuthException;
import com.lewo.exception.DbException;
import com.lewo.zmail.test.service.GardenOfTheWorld;
import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class CallForLove {

    @Autowired
    GardenOfTheWorld gardenOfTheWorld;

    @CheckLogin
    @RequestMapping("apiCenter")
    public String apiCenter(){
        return "apiCenter";
    }

    @ResponseBody
    @GetMapping("testTL")
    public String testThreadLocal(){
        ThreadLocal<UmsUser> threadLocal = new ThreadLocal<>();

        UmsUser user = new UmsUser();
        user.setId("nigger wife");
        user.setNickname("薇儿");
        user.setCoordinate("公主殿");

        threadLocal.set(user);
        gardenOfTheWorld.testThreadLocal(threadLocal);
        return "薇儿爱爱";
    }

    @ResponseBody
    @GetMapping("testGEH")
    public String testGEH(String eName) throws NullPointerException{
        System.out.println(eName.substring(2));
        //实测抛了异常就不会返数据了
        return "这下支了";
    }
}
