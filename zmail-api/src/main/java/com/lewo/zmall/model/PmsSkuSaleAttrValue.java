package com.lewo.zmall.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @param
 * @return
 */
@Data
public class PmsSkuSaleAttrValue implements Serializable {

    @Id
    @Column
    String id;

    @Column
    String skuId;

    @Column
    String saleAttrId;//spu销售属性ID   1,2,3,4

    @Column
    String saleAttrValueId;//spu销售属性值ID

    @Column
    String saleAttrName;//spu销售属性名  颜色，尺寸，版本，容量

    @Column
    String saleAttrValueName;//spu销售属性值

}
