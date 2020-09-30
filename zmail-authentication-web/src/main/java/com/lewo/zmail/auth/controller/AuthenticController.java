package com.lewo.zmail.auth.controller;

import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin//震惊！竟然有用了！！！
@Controller
public class AuthenticController {

    @DubboReference
    UserService userService;

    @CheckLogin(mustLogin = false)//登录页也想过拦截器？
    @RequestMapping("index.html")
    public String index(String ReturnUrl, ModelMap modelMap){
        System.out.println(ReturnUrl);
        modelMap.put("ReturnUrl",ReturnUrl);
        return "index";
    }

    @ResponseBody
    @RequestMapping("login")
    public String login(UmsUser umsUser, HttpServletRequest request,
                        HttpServletResponse response){
        UmsUser user = userService.login(umsUser);
        //登陆失败
        if (user==null){
            return "fail";
        }
        //登陆成功
        else {
            String token = "带带大nigger";
            //不能在这里设置cookie，因为请求是从登录页过来的，在这里设置只能设到登录页，而非ReturnUrl
            System.out.println(user);
            return token;
        }
    }

    @ResponseBody
    @RequestMapping("verify")
    public String verify(String token){
        return "success";
    }

    public static void main(String[] args) {
        String a = "cart.zmail.com";
        String[] split = a.split("\\.");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
