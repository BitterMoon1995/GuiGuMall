package com.lewo.zmail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.lewo.utils.Constant;
import com.lewo.utils.VerifyRes;
import com.lewo.zmail.web.utils.HttpClientUtil;
import com.lewo.zmail.web.utils.IPUtils;
import com.lewo.zmail.web.utils.JwtUtil;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weibo4j.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@CrossOrigin//震惊！竟然有用了！！！
@Controller
public class AuthenticController {

    @DubboReference
    UserService userService;

    static String accessTokenUrl = "https://api.weibo.com/oauth2/access_token";
    static String client_id = "235360557";
    static String client_secret = "6af489dc602d67d656c65f78f1e634aa";
    static String grant_type = "authorization_code";
    static String redirect_uri = "http://auth.zmail.com:2050/thirdLogin";
    static String userInfo_url = "https://api.weibo.com/2/users/show.json";
    static String home_url = "http://auth.zmail.com:2050/success";
    private String access_token;

    @RequestMapping("index.html")
    public String index(String ReturnUrl, ModelMap modelMap){
        System.out.println(ReturnUrl);
        modelMap.put("ReturnUrl",ReturnUrl);
        return "index";
    }

    @ResponseBody
    @RequestMapping("login")
    //不能在这里设置cookie，因为请求是从登录页过来的，在这里设置只能设到登录页，而非ReturnUrl
    public String login(UmsUser umsUser, HttpServletRequest request,
                        HttpServletResponse response){
        UmsUser user = userService.login(umsUser);
        //登陆失败
        if (user==null){
            return "fail";
        }
        //登陆成功
        else {
            //ip作为盐值
            String ip = IPUtils.getCurrIP(request);

            //生成JWT
            HashMap<String, Object> encodeMap = new HashMap<>();
            encodeMap.put("userId",user.getId());
            encodeMap.put("nickname",user.getNickname());
            System.out.println("编码IP:"+ip);

            String token = JwtUtil.encode(Constant.jwtKey, encodeMap, ip);

            //存入userService缓存一份
            userService.storeToken(token,String.valueOf(user.getId()));

            /*★★采用服务并行策略，在用户登录后尝试同步购物车数据
            需要调用的其实还有可能的短信服务、其他服务等等......
            但是用户登录服务是决不可串行调用(依赖)其他服务的
             */
            return token;
        }
    }

    /*
    只被Auth拦截器调用
    只会在二次拦截、2+N次拦截被调用，第一次拦截，拦截器会直接打到登录页
    没过校验，token非法，返失败类，拦截器收到失败类，不放行，打回登录页
    过了校验，token合法，将用户信息传递给拦截器，拦截器
        ①将用户信息写在请求域
        ②将合法的token写在cookie
        ③放行
     */
    @ResponseBody
    @RequestMapping("verify")
    /*
    关于currIp：
        登陆的时候，用户在自己电脑打开登录页请求登录，那么请求域里的IP就是用户的userIP，
        我们用userIP作为盐生成了JWP
        而如果试图从这里的request里获取IP，作为盐来解token，是必然错误的
        因为请求verify接口的并不是用户的电脑，而是被拦截的服务(拦截器是统一配置的)所在的服务器
        所有这里有一个currIP，来自于拦截器的请求域
     */
    public VerifyRes verify(String token,String currIP){
        Map<String, Object> decodeRes = JwtUtil.decode(token, Constant.jwtKey, currIP);
        if (decodeRes==null)
            return VerifyRes.fail;

        String userId = String.valueOf(decodeRes.get("userId"));
        String nickname = String.valueOf(decodeRes.get("nickname"));
        return VerifyRes.success(userId,nickname);
    }

    @RequestMapping("thirdLogin")
    public void thirdLogin(String code){
        System.out.println(code);

        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type",grant_type);
        map.put("client_secret",client_secret);
        map.put("client_id",client_id);
        map.put("redirect_uri",redirect_uri);
        map.put("code",code);

        String tokenJson = HttpClientUtil.doPost(accessTokenUrl, map);
        System.out.println(tokenJson);
        HashMap tokenMap = JSON.parseObject(tokenJson, HashMap.class);
        assert tokenMap != null;
        String access_token = tokenMap.get("access_token").toString();
        String uid = tokenMap.get("uid").toString();
        System.out.println(access_token+"....."+uid);

        String userJson = HttpClientUtil.doGet("https://api.weibo.com/2/users/show.json?"
                + "access_token=" + access_token
                + "&uid=" + uid);
//        User weiboUser = JSON.parseObject(userJson, User.class);
        System.out.println(userJson);
//        assert weiboUser != null;
//        System.out.println(weiboUser.toString());
    }

    @RequestMapping("success")
    public String success(){
        System.out.println("niggerの胜利");
        return "success";
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties"));
        String accessTokenURL = properties.get("accessTokenURL").toString();
        String client_id = properties.get("client_ID").toString();
        String redirect_uri = properties.get("redirect_URI").toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id",client_id);
        map.put("redirect_uri",redirect_uri);
        String s = HttpClientUtil.doPost("https://api.weibo.com/oauth2/authorize", map);
        System.out.println(s);
    }
}
