package com.lewo.zmail.cart.controller;

import com.alibaba.fastjson.JSON;
import com.lewo.zmail.cart.function.CartFunction;
import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmail.web.utils.CookieUtil;
import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.CartService;
import com.lewo.zmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    String cookieName="cookieCart";

    @DubboReference
    SkuService skuService;

    @DubboReference
    CartService service;

    @Autowired
    CartFunction function;

    @CheckLogin(mustLogin = false)
    @RequestMapping("addToCart")
    public String addToCart(String skuId,Integer quantity,
    HttpServletRequest request, HttpServletResponse response) throws InterruptedException {

        //调用商品服务查询商品信息
        PmsSkuInfo skuInfo = skuService.getById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem cartItem = function.convert(skuInfo, quantity);

        String userId = "godz";
        boolean isLogin = true;
        //判断用户是否登录，如果登录走DB
        if (isLogin){
            //查询用户购物车中的该商品
            OmsCartItem usersItem = service.getUsersItem(userId,skuId);
            //用户购物车中并没有该商品，执行插入方法
            if (usersItem==null){
                cartItem.setUserId(userId);
                service.addUsersItem(cartItem);
            }
            //已经有该商品，执行更新方法，更新该商品的数量
            else {
                usersItem.setQuantity(usersItem.getQuantity()+cartItem.getQuantity());
                service.updateUsersItem(usersItem);
            }
            //刷新缓存
            service.flushCache(userId);
        }
        //未登录，走cookie
        else {
            //获取之前的cookie
            String currentCartCookie = CookieUtil.getCookieValue(request, cookieName, true);
            //如果用户的cookie购物车不为空
            if (StringUtils.isNotBlank(currentCartCookie)) {
                List<OmsCartItem> currentCart = JSON.parseArray(currentCartCookie, OmsCartItem.class);
                //如果当前购物车已经存在该商品，则更新那一条目的数量，把新增的数量加上去
                if (function.itemExists(cartItem, currentCart)) {
                    currentCart.forEach(item -> {
                        if (item.getProductSkuId().equals(skuId)) {
                            item.setPrice(item.getPrice().add(cartItem.getPrice()));
                            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                        }
                    });
                }
                //如果新增的商品在当前购物车中不存在,则把该条目加入当前购物车
                else {
                    currentCart.add(cartItem);
                }
                CookieUtil.setCookie(request, response, cookieName,
                        JSON.toJSONString(currentCart), 60 * 60 * 24 * 3, true);
            }
            //如果是空的，new一个购物车把当前商品加进去
            else {
                ArrayList<OmsCartItem> cart = new ArrayList<>();
                cart.add(cartItem);
                CookieUtil.setCookie(request, response, cookieName,
                        JSON.toJSONString(cart), 60 * 60 * 24 * 3, true);
            }
        }
        return "redirect:/success.html";
    }

    @CheckLogin(mustLogin = false)
    @RequestMapping("cartList")
    public String cartList(ModelMap modelMap, HttpServletRequest request,
    HttpServletResponse response,String userId){
        System.out.println(request.getAttribute("nickname"));
        System.out.println(request.getAttribute("userId"));

        //空值的校验不要在service层进行，在web层就要确保传输的值没有问题
        if (StringUtils.isBlank(userId))
            userId = "godz";

        List<OmsCartItem> cart = new ArrayList<>();
        //已登录查缓存
        if (StringUtils.isNotBlank(userId)){
            cart = service.getUsersCart(userId);
        }
        //未登录查cookie
        else {
            String cookieValue = CookieUtil.getCookieValue(request, cookieName, true);
            if (StringUtils.isNotBlank(cookieValue))
                cart = JSON.parseArray(cookieValue,OmsCartItem.class);
        }
        BigDecimal totalAmount = function.getTotalAmount(cart);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",cart);
        return "cartList";
    }

    /*
    未完成：未登录状态的check修改？
     */

    @CheckLogin(mustLogin = false)
    @RequestMapping("checkCart")
    public String checkCart(Boolean isChecked,String productSkuId,
    ModelMap modelMap,HttpServletRequest request,HttpServletResponse response){

        String userId = "godz";
        service.checkCart(userId,isChecked,productSkuId);

        modelMap.put("cartList",service.getUsersCart(userId));
        //返回的是内嵌页面
        return "cartListInner";
    }
    /*
    未完成：登录后，cookie中购物车数据同步到cache和db以及页面？
           checkCart逻辑过长，一致性要求过高。
     */

    @CheckLogin
    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request,
                          HttpServletResponse response){
        System.out.println(request.getAttribute("nickname"));
        System.out.println(request.getAttribute("userId"));
        return "trade";
    }

    public static void main(String[] args) {
        String s = "";
        System.out.println(StringUtils.isBlank(s));
    }

    @ResponseBody
    @RequestMapping("noAuth")
    public String noAuth(){
        return "小心尼哥";
    }
}
