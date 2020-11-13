package com.lewo.zmail.test.controller;

import com.lewo.zmail.web.filter.CheckLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class CallForLove {
    @CheckLogin
    @RequestMapping("toApiCenter")
    public String toApiCenter(){
        return "apiCenter";
    }
}
