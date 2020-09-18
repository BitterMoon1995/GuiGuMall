package com.lewo.zmail.cart.controller;

import com.alibaba.fastjson.JSON;
import com.lewo.zmail.cart.function.CartFunction;
import com.lewo.zmail.web.utils.CookieUtil;
import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    String cartOfCookie="cookieCart";

    @DubboReference
    SkuService skuService;
    @Autowired
    CartFunction function;

    @RequestMapping("addToCart")
    public String addToCart(String skuId,Integer quantity,
    HttpServletRequest request, HttpServletResponse response) throws InterruptedException {

        System.out.println(quantity);
        quantity=1;

        //调用商品服务查询商品信息
        PmsSkuInfo skuInfo = skuService.getById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem cartItem = function.convert(skuInfo, quantity);

        //获取之前的cookie
        String currentCartCookie = CookieUtil.getCookieValue(request, cartOfCookie, true);
        //如果用户的cookie购物车不为空
        if (StringUtils.isNotBlank(currentCartCookie)) {
            List<OmsCartItem> currentCart = JSON.parseArray(currentCartCookie, OmsCartItem.class);
            //如果当前购物车已经存在该商品，则更新那一条目的数量，把新增的数量加上去
            if (function.itemExists(cartItem, currentCart)){
                currentCart.forEach(item -> {
                    if (item.getProductSkuId().equals(skuId)){
                        item.setPrice(item.getPrice().add(cartItem.getPrice()));
                        item.setQuantity(item.getQuantity()+cartItem.getQuantity());
                    }
                });
            }
            //如果新增的商品在当前购物车中不存在,则把该条目加入当前购物车
            else {
                currentCart.add(cartItem);
            }
            System.out.println(JSON.toJSONString(currentCart));
            CookieUtil.setCookie(request,response,cartOfCookie,
                    JSON.toJSONString(currentCart),60*60*24*3,true);
        }
        //如果是空的，new一个购物车把当前商品加进去
        else {
            ArrayList<OmsCartItem> cart = new ArrayList<>();
            cart.add(cartItem);
            CookieUtil.setCookie(request,response,cartOfCookie,
                    JSON.toJSONString(cart),60*60*24*3,true);
        }
        return "redirect:/success.html";
    }
}
