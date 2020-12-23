package com.lewo.zmall.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**（支付宝）支付单/交易单
 * @param
 * @return
 */
@Data
@Accessors(chain = true)
public class Payment {

    @Column
    @Id
    private String  id;

    @Column
    private String orderSn;

    @Column
    private String orderId;

    @Column
    private String alipayTradeNo;

    @Column
    private BigDecimal totalAmount;

    @Column
    private String Subject;

    @Column
    private String paymentStatus;

    @Column
    private Date createTime;

    @Column
    private Date callbackTime;

    @Column
    private String callbackContent;
}
