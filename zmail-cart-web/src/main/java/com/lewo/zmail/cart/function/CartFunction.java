package com.lewo.zmail.cart.function;

import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.PmsSkuInfo;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CartFunction {
    public OmsCartItem convert(PmsSkuInfo skuInfo,Integer quantity){
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setCreateDate(new Date());
        cartItem.setModifyDate(new Date());
        cartItem.setPrice(skuInfo.getPrice());
        cartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        cartItem.setProductId(skuInfo.getSpuId());
        cartItem.setProductName(skuInfo.getSkuName());
        cartItem.setProductPic(skuInfo.getSkuDefaultImg());
        cartItem.setProductSkuId(skuInfo.getId());
        cartItem.setQuantity(quantity);
        cartItem.setProductSubTitle(skuInfo.getSkuDesc());
        cartItem.setIsChecked(false);
        return cartItem;
    }
    //突然高雅！
    public boolean itemExists(OmsCartItem cartItem, List<OmsCartItem> currentCart) {
        String skuId = cartItem.getProductSkuId();
        return currentCart.stream()
                .anyMatch(curItem -> curItem.getProductSkuId().equals(skuId));
    }
    //不再高雅、god周
    public BigDecimal getTotalAmount(List<OmsCartItem> cart) {
        BigDecimal totalAmount = new BigDecimal("0");
                  for (OmsCartItem cartItem :cart) {
            if (cartItem.getIsChecked())
                totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }
        return totalAmount;
    }
}
