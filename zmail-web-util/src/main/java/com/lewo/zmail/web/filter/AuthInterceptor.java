package com.lewo.zmail.web.filter;

import com.lewo.utils.HttpClientUtil;
import com.lewo.zmail.web.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {
        System.out.println("嘻嘻");
/*      小知识：
        Spring MVC应用启动时会搜集并分析每个Web控制器方法，从中提取对应的"<请求匹配条件,控制器方法>“映射关系，" +
        "形成一个映射关系表保存在一个RequestMappingHandlerMapping bean中。然后在客户请求到达时，" +
        "再使用RequestMappingHandlerMapping中的该映射关系表找到相应的控制器方法去处理该请求。" +
        "在RequestMappingHandlerMapping中保存的每个”<请求匹配条件,控制器方法>"映射关系对儿中,
        "请求匹配条件"通过RequestMappingInfo包装和表示，而"控制器方法"则通过HandlerMethod来包装和表示。*/
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckLogin checkLogin = handlerMethod.getMethodAnnotation(CheckLogin.class);

        //没有@CheckLogin注解，直接放行
        if (checkLogin==null)
            return true;

        String token = null;
        //尝试进行token的赋值和覆盖
        String oldToken = CookieUtil.getCookieValue(request, "token", true);
        if (StringUtils.isNotBlank(oldToken))
            token = oldToken;
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken))
            token = newToken;

        System.out.println(token);
        String res;
        //老师坑点：token和res没有判空
        //首先token必须保证非空，再尝试发验证请求，得到验证结果
        if (StringUtils.isNotBlank(token)){
            res = HttpClientUtil.doGet("http://localhost:2050/verify?token="+token);
        }
        //如果token是空的，又是必须成功登陆的方法，直接打回去
        else if (checkLogin.mustLogin()){
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect("http://localhost:2050/index.html?ReturnUrl="+requestURL);
            return false;
        }
        //虽然token是空的，但是方法并不强制登陆(京东未登录购物车)，设置空的用户信息，放行
        else {
            request.setAttribute("userId","nigger");
            request.setAttribute("nickname","black nigger slave");
            return true;
        }

        //验证结果也得判空，如果验证结果为空说明验证中心down了，重定向到某错误页，然后准备找工作
        if (res==null) {
            response.sendRedirect("http://localhost:2030/error.html");
            return false;
        }

        //这里开始已经保证token非空且已经得到了验证结果且验证结果也非空
        //mustLogin==true表示需要验证登录状态并且必须登录成功的模块，如订单、物流、支付...
        if (checkLogin.mustLogin()){
            //token有效，刷新token
            //验证通过，放入token携带的用户信息，放行
            if (res.equals("success")){
                request.setAttribute("userId","godz");
                request.setAttribute("nickname","女神薇儿");
                //覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
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
            //用户已登录，将用户token携带的用户信息写入
            if (res.equals("success")){
                request.setAttribute("userId","godz");
                request.setAttribute("nickname","女神薇儿");
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"token",token,60*60*2,true);
                }
            }
            //用户未登录
            else {
                request.setAttribute("userId","notlogin");
                request.setAttribute("nickname","notlogin");
            }

        }
        return true;
    }
}
