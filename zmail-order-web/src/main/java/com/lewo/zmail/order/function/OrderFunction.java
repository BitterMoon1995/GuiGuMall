package com.lewo.zmail.order.function;

import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.OmsOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderFunction {
    public List<OmsOrderItem> cartToOrder(List<OmsCartItem> cart){
        ArrayList<OmsOrderItem> orderItems = new ArrayList<>();
        for (OmsCartItem cartItem : cart) {
            if (cartItem.getIsChecked()) {
                OmsOrderItem orderItem = new OmsOrderItem();
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
}
