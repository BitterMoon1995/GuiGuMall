package com.lewo.zmall.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class PmsSearchSkuInfo implements Serializable{

    @Id
    private String id;
    private String skuName;
    private String skuDesc;
    private String catalog3Id;
    private BigDecimal price;
    private String skuDefaultImg;
    private double hotScore;
    private String spuId;
    private List<PmsSkuAttrValue> skuAttrValueList;

}
