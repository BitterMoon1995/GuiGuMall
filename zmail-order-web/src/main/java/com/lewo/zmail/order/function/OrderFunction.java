package com.lewo.zmail.order.function;

import com.lewo.utils.RandomUtils;
import com.lewo.utils.TimeUtils;
import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;
import com.lewo.zmall.model.UmsUserReceiveAddress;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderFunction {
    public List<OmsOrderItem> cartToOrder(List<OmsCartItem> cart){
        ArrayList<OmsOrderItem> orderItems = new ArrayList<>();
        for (OmsCartItem cartItem : cart) {
            if (cartItem.getIsChecked()) {
                OmsOrderItem orderItem = new OmsOrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductSkuId(cartItem.getProductSkuId());
                orderItem.setProductCategoryId(cartItem.getProductCategoryId());
                orderItem.setProductSubtotal(cartItem.getTotalPrice());
                orderItem.setProductQuantity(cartItem.getQuantity());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductPic(cartItem.getProductPic());
                orderItem.setProductPrice(cartItem.getPrice());
                orderItems.add(orderItem);
            }
        }
        return orderItems;
    }

    public List<OmsCartItem> getCheckedCartItems(List<OmsCartItem> usersCart) {
        return usersCart.stream()
                .filter(omsCartItem -> omsCartItem.getDeleteStatus() == 0)//未删除的
                .filter(OmsCartItem::getIsChecked)//勾选了的
                .collect(Collectors.toList());
    }

    public BigDecimal orderTotalPrice(List<OmsOrderItem> orderItems) {
        BigDecimal totalPrice = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItems) {
            BigDecimal quantity = new BigDecimal(orderItem.getProductQuantity());
            BigDecimal subtotal = orderItem.getProductPrice()
                    .multiply(quantity);
            totalPrice = totalPrice.add(subtotal);
        }
        return totalPrice;
    }

    //★初始化订单★
    public OmsOrder genOrder(List<OmsOrderItem> orderItems
        , String userId, String nickname, UmsUserReceiveAddress receiveAddr) {
        OmsOrder order = new OmsOrder();
        //订单序列号生成策略：当前毫秒时间戳+随机6位数字
        String orderSn = System.currentTimeMillis() + RandomUtils.randomNum(6);
        //核心
        order.setUserId(userId);
        order.setUsername(nickname);
        order.setTotalAmount(orderTotalPrice(orderItems));//总价，以后把折扣算进去
        order.setOmsOrderItems(orderItems);
        order.setOrderSn(orderSn);//外部订单号
        order.setCreateTime(TimeUtils.curTime());
        //收货这块
        order.setAutoConfirmDay(7);//七天自动收货
        order.setReceiverDetailAddress(receiveAddr.getDetailAddress());
        order.setReceiverName(receiveAddr.getName());
        order.setReceiverCity(receiveAddr.getCity());
        order.setReceiverPhone(receiveAddr.getPhoneNumber());//......差不多得了

        //各种状态码
        order.setOrderType(0);//0正常订单，1秒杀订单
        order.setSourceType(0);//0.PC 1.APP
        order.setStatus(0);//0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setPayType(0);//支付方式：0->未支付；1->支付宝；2->微信

        return order;
    }
}
