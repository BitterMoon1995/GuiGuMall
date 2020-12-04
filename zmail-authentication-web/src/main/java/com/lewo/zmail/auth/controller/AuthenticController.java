package com.lewo.zmail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.lewo.common.Constant;
import com.lewo.unified.VerifyRes;
import com.lewo.zmail.auth.function.AuthFunction;
import com.lewo.zmail.auth.utils.AuthUtils;
import com.lewo.zmail.web.utils.HttpClientUtil;
import com.lewo.zmail.web.utils.IPUtils;
import com.lewo.zmail.web.utils.JwtUtil;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    AuthFunction function;

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

            /*用户登录后故事才刚刚开始
            其实后续可能必须要调用同步购物车数据、日志服务、短信服务、邮箱服务、平台通知服务、平台消息服务等等......
            但是用户登录服务是绝不可串行调用这一系列服务的，不然挂一个服务整个应用连登陆都没法登了
            所以需要采取服务并行策略，用MQ来实现分布式事务
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
    public String thirdLogin(String code,HttpServletRequest request){
        //使用用户的授权码获取用户的授权令牌
        String tokenJson = function.acqWeiboToken(code);
        //薅出授权令牌
        HashMap<String,String> tokenMap = JSON.parseObject(tokenJson, HashMap.class);
        assert tokenMap != null;
        String access_token = tokenMap.get("access_token");
        String uid = tokenMap.get("uid");
        //根据授权令牌，获取用户信息
        String userJson = HttpClientUtil.doGet(userInfo_url
                + "?access_token=" + access_token
                + "&uid=" + uid);
        //根据用户信息，注册新用户 或 找出已有的用户，返回用户对象
        UmsUser umsUser = userService.loginFromWeibo(userJson, access_token, code);
        //根据用户对象，生成token
        String token = AuthUtils.encodeUser(umsUser, IPUtils.getCurrIP(request));
        //(第三方)登陆成功，携带token重定向至首页
        return "redirect:http://localhost:1989/apiCenter?token="+token;
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
