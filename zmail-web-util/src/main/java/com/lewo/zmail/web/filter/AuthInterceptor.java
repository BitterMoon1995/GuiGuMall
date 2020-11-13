package com.lewo.zmail.web.filter;

import com.alibaba.fastjson.JSON;
import com.lewo.unified.Constant;
import com.lewo.zmail.web.utils.HttpClientUtil;
import com.lewo.unified.VerifyRes;
import com.lewo.zmail.web.utils.CookieUtil;
import com.lewo.zmail.web.utils.IPUtils;
import com.lewo.zmail.web.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    static String notLogin = "notLogin";

    @Override
    public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {
/*      小知识：
        Spring MVC应用启动时会搜集并分析每个Web控制器方法，从中提取对应的"<请求匹配条件,控制器方法>“映射关系，" +
        "形成一个映射关系表保存在一个RequestMappingHandlerMapping bean中。然后在客户请求到达时，" +
        "再使用RequestMappingHandlerMapping中的该映射关系表找到相应的控制器方法去处理该请求。" +
        "在RequestMappingHandlerMapping中保存的每个”<请求匹配条件,控制器方法>"映射关系对儿中,
        "请求匹配条件"通过RequestMappingInfo包装和表示，而"控制器方法"则通过HandlerMethod来包装和表示。*/


        HandlerMethod handlerMethod;
        try {
            handlerMethod = (HandlerMethod) handler;
        //如果不能转换，那么可能是拦截到静态资源请求了(ResourceHttpRequestHandler),就直接放行
        } catch (Exception e) {
            return true;
        }
        System.out.println("嘻嘻");
        CheckLogin checkLogin = handlerMethod.getMethodAnnotation(CheckLogin.class);

        //没有@CheckLogin注解，直接放行
        if (checkLogin==null)
            return true;

        String token = getToken(request);
        String currIP = IPUtils.getCurrIP(request);

        //不强制要求认证但需要解析、处理token的接口，比如首页
        if (checkLogin.type() == 2){
            //未登录
            if (token == null) {
                notLogin(request);
            }
            //已登录，token一定是有效的！！！（确信）
            else {
                Map<String, Object> decodeMap = JwtUtil.decode(token, Constant.jwtKey, currIP);
                if (decodeMap==null){
                    System.out.println("currIP:"+currIP);
                    return true;
                }
                request.setAttribute("userId",decodeMap.get("userId"));
                request.setAttribute("nickname",decodeMap.get("nickname"));
                CookieUtil.setCookie(request,response,"token",token,3600*2,true);
            }
            return true;
        }
        String res;

        /*老师坑点：token和res没有判空，token必须保证非空，才能尝试发验证请求*/
        if (StringUtils.isNotBlank(token)){
            //这里token就位于请求参数中，也就是firstToken
            res = HttpClientUtil
                    .doGet("http://localhost:2050/verify?token="+token+"&currIP="+currIP);
        }
        /*第 1 次拦截 (请求域没有携带token)*/
        //如果token是空的，又是必须成功登陆的方法，直接打回去
        else if (checkLogin.mustLogin()){
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect("http://localhost:2050/index.html?ReturnUrl="+requestURL);
            return false;
        }
        //虽然token是空的，但是方法并不强制登陆(京东未登录购物车)，设置空的用户信息，放行
        else {
            notLogin(request);
            return true;
        }

        //验证结果也得判空，如果验证结果为空说明验证中心down了，重定向到某错误页，然后准备找工作
        if (res==null) {
            response.sendRedirect("http://localhost:2030/error.html");
            return false;
        }
        //解析验证结果对象
        VerifyRes verifyRes = JSON.parseObject(res, VerifyRes.class);

        /*进这个分支意味着是第 n+1 (n>0) 次拦截，之前已经被拦截并请求登录过了
        如果n=1(第二次登陆)，token在请求域
        如果n>1(第三次及以后)，token在cookie里面*/
        if (checkLogin.mustLogin()){
            //token有效，刷新token
            //验证通过，放入token携带的用户信息，放行
            if (verifyRes.getCode()==VerifyRes.successCode){
                request.setAttribute("userId",verifyRes.getUserId());
                request.setAttribute("nickname",verifyRes.getNickname());
                //覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    //response.addCookie(cookie);
                    CookieUtil.setCookie(request,response,"token",token,60*60*2,true);
                }
                return true;
            }
            //验证失败，打回登录页
            else {
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://localhost:2050/index.html?ReturnUrl="+requestURL);
                return false;
            }
        }
        //mustLogin==false表示需要获取登录信息，但不要求必须登录成功，而是根据登录状态走不同的业务分支的模块，比如购物车
        else {
            //用户成功登录，将用户token携带的用户信息写入
            if (verifyRes.getCode()==VerifyRes.successCode){
                request.setAttribute("userId",verifyRes.getUserId());
                request.setAttribute("nickname",verifyRes.getNickname());
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"token",token,60*60*2,true);
                }
            }
            //用户未成功登录
            else {
                notLogin(request);
            }

        }
        return true;
    }

    //尝试从请求域或cookie中获取token
    String getToken(HttpServletRequest request){
        String token = null;
        //尝试进行token的赋值和覆盖
        //firstToken指登陆成功过，cookie里面已经有token
        String firstToken = CookieUtil.getCookieValue(request, "token", true);
        if (StringUtils.isNotBlank(firstToken))
            token = firstToken;
        /*newToken指第一次登陆，登陆成功后从登陆页面转发到目标页面，请求路径中携带的token
        叫newToken是因为请求login接口成功后会颁发新的token
         */
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken))
            token = newToken;
        System.out.println(request.getRequestURL());
        System.out.println(token);
        
        return token;
    }

    void notLogin(HttpServletRequest request){
        request.setAttribute("userId",notLogin);
        request.setAttribute("nickname",notLogin);
    }
}
