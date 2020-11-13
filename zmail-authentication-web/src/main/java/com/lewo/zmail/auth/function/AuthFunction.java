package com.lewo.zmail.auth.function;

import com.lewo.zmail.web.utils.HttpClientUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AuthFunction {
    static String accessTokenUrl = "https://api.weibo.com/oauth2/access_token";
    static String client_id = "235360557";
    static String client_secret = "6af489dc602d67d656c65f78f1e634aa";
    static String grant_type = "authorization_code";
    static String redirect_uri = "http://auth.zmail.com:2050/thirdLogin";
    static String userInfo_url = "https://api.weibo.com/2/users/show.json";
    static String home_url = "http://auth.zmail.com:2050/success";

    public String acqWeiboToken(String licenseCode){
        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type",grant_type);
        map.put("client_secret",client_secret);
        map.put("client_id",client_id);
        map.put("redirect_uri",redirect_uri);
        map.put("code",licenseCode);
        return HttpClientUtil.doPost(accessTokenUrl, map);
    }
}
