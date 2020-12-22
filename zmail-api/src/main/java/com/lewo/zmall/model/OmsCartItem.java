package com.lewo.zmall.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class OmsCartItem implements Serializable{

    @Id
    private String id;
    private String productId;
    private String productSkuId;
    private String userId;
    //商品数量
    private Integer quantity;
    // 商品单价
    private BigDecimal price;
    private String sp1;
    private String sp2;
    private String sp3;
    private String productPic;
    private String productName;
    private String productSubTitle;
    private String productSkuCode;
    private String userNickname;
    private Date createDate;
    private Date modifyDate;
    private Integer deleteStatus;
    private String productCategoryId;
    private String productBrand;
    private String productSn;
    private String productAttr;
    private Boolean isChecked;

    //任何金融和电商系统或者只要涉及到钱，就都用BigDecimal否则各种精度问题
    @Transient
    //本条目总价
    private BigDecimal totalPrice;

}
