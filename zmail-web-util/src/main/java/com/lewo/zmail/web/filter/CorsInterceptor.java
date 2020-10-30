package com.lewo.zmail.web.filter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers","Origin," +
                "Access-Control-Request-Headers,Access-Control-Allow-Headers,DNT," +
                "X-Requested-With,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-M" +
                "Last-Modified,If-Modified-Since,Cache-Control,Content-Type,Accept,Connection," +
                "Cookie,X-XSRF-TOKEN,X-CSRF-TOKEN,Authorization");
    }
}
/*关于cookie跨域传输：
唯一方案是 Access-Control-Allow-Origin 指定单一的域名，然后在指定的域名发请求，不能用通配符
并且必须(axios.defaults.)withCredentials = true。
而一旦设置了 Access-Control-Allow-Origin: * ，不论怎么猴，cookie都绝对垮不了域
 */