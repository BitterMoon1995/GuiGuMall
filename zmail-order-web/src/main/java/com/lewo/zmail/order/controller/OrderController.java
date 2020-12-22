package com.lewo.zmail.order.controller;

import com.lewo.exception.DbException;
import com.lewo.zmall.unified.iResult;
import com.lewo.utils.Predicate;
import com.lewo.zmail.order.function.OrderFunction;
import com.lewo.zmail.web.annotation.Entrance;
import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.CartService;
import com.lewo.zmall.service.OrderService;
import com.lewo.zmall.service.SkuService;
import com.lewo.zmall.service.UserAddressService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单页面应该只负责
 *  1）对用户展示购物车中已勾选的商品
 *  2）用户选择收货地址、支付方式、优惠券，填写备注
 *  3）若用户请求支付，只接收用户的userID，要支付的商品、款项由server从数据库核验
 *      (不可能从前端接收，前端页面极易篡改)
 *
 *  购物车勾选要买的——提交订单——订单页面确认订单——弹出支付layer——支付——                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      支付成功页
 */
@CrossOrigin
@Controller
public class OrderController {
    @Autowired
    OrderFunction function;
    @DubboReference
    CartService cartService;
    @DubboReference
    UserAddressService userAddrService;
    @DubboReference
    OrderService service;
    @DubboReference
    SkuService skuService;

    @Entrance
    @CheckLogin
    @RequestMapping("toTrade")//生成订单页面数据，这里唯一的写操作就是生成交易码
    public String toTrade(HttpServletRequest request, ModelMap modelMap){
        String userId = (String) request.getAttribute("userId");
        System.out.println(userId);
        /*根据userID查询用户购物车信息和用户收货地址*/
        List<OmsCartItem> cartItems = cartService.getUsersCart(userId);
        List<UmsUserReceiveAddress> userAddresses = userAddrService.getUserAddress(userId);
        /*把购物车中勾选了的条目转化为订单的条目*/
        List<OmsOrderItem> orderItems = function.cartToOrder(cartItems);
        /*生成交易码，订单页和Redis各放一份，设置好过期时间*/
        String tradeCode = service.genTradeCode(userId);
        System.out.println(tradeCode);

        /*返订单数据和用户收货地址列表*/
        modelMap.put("omsOrderItems",orderItems);
        modelMap.put("userAddressList",userAddresses);
        modelMap.put("tradeCode",tradeCode);
        return "trade";
    }

    @CheckLogin
    @RequestMapping("submitOrder")//发起提交订单，★涉及到幂等★
    public String submitOrder(String receiveAddressId,
        String tradeCode,HttpServletRequest request)
            throws DbException{

        String userId = (String) request.getAttribute("userId");
        String nickname = (String) request.getAttribute("nickname");
        if (StringUtils.isAnyBlank(receiveAddressId,tradeCode,userId))
            return "error";

        //检查与比对交易码
        iResult result = service.checkTradeCode(userId,tradeCode);
        if (!Predicate.suc(result))
            return "error";
        //薅出购物车中已勾选的商品
        List<OmsCartItem> checkedCartItems = function.getCheckedCartItems(cartService.getUsersCart(userId));
        //转化为待处理的订单项
        List<OmsOrderItem> orderItems = function.cartToOrder(checkedCartItems);
        //遍历所有订单项，调用skuService进行验价和验库存(后面做)
            // 1.没货了拍尼玛呢
            // 2.预防某一打折商品恰好在此时此刻折扣过期的情况)
            // 3.不替用户做决定
            //我们终将重逢

        //调用地址服务，查询用户收货地址信息
        //全局异常处理搞好了这里不用try catch然后throw，直接在方法声明一下要抛的异常就行了
        UmsUserReceiveAddress recAddr = userAddrService.getById(receiveAddressId);

        //生成订单
        OmsOrder order = function.genOrder(orderItems, userId, nickname, recAddr);
        /*    测试代码：测试总价     */
        order.setTotalAmount(new BigDecimal(8));
        //把订单写入DB，之后把本次提交的订单条目与之关联，也写入DB
        service.insertOrder(order,orderItems);

        //删除购物车中本次购买的商品
        cartService.delItems(checkedCartItems);

        //重定向至支付系统
        return "redirect:http://localhost:2070/index.html?orderSn="
                +order.getOrderSn()+"&totalAmount="+order.getTotalAmount();
    }
}
