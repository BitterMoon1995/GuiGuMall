package com.lewo.zmail.order.service;

import com.lewo.exception.DbException;
import com.lewo.utils.Predicate;
import com.lewo.zmail.order.msg.MsgProvider;
import com.lewo.zmall.service.ErrorLogService;
import com.lewo.zmall.unified.Code;
import com.lewo.zmall.unified.iResult;
import com.lewo.utils.RandomUtils;
import com.lewo.zmail.order.db.OrderItemMapper;
import com.lewo.zmail.order.db.OrderMapper;
import com.lewo.zmail.utils.LuaUtils;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;
import com.lewo.zmall.service.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper mapper;
    @Autowired
    OrderItemMapper itemMapper;
    @Autowired
    RedisTemplate<String, Object> redis;
    @Autowired
    MsgProvider msgProvider;
    @DubboReference
    ErrorLogService errLogService;
    /*
    生成交易码
     */
    @Override
    public String genTradeCode(String userId) {
        String tradeCode = RandomUtils.genNonceStr();
        redis.opsForValue().set("userId:" + userId + ":tradeCode", tradeCode, 30, TimeUnit.MINUTES);
        return tradeCode;
    }

    /*
    核验交易码
     */
    @Override
    public iResult checkTradeCode(String userId, String tradeCode) {
        String key = "userId:" + userId + ":tradeCode";
/*        这里进行tradeCode的check-compare-delete操作，因为在多线程下这是一连串的非原子操作，是不安全的（？）
          黑客可以携带已生成的tradeCode发起并发攻击，所以这一系列操作必须是原子操作
          先用Java演示流程*/
//        Object codeObj = redis.opsForValue().get(key);
//        if (codeObj==null)
//            return iResult.invalidAuth;
//
//        String trueCode;
//        trueCode = codeObj.toString();
//        if (!trueCode.equals(tradeCode))
//            return iResult.illegalParam;
//
//        redis.delete(key);

        /* lua脚本实现 */
        List<String> singleKey = Collections.singletonList(key);
        //keQing：指原神角色刻晴，周神的结发正妻
        Long res = redis.execute(LuaUtils.keQing("checkTC.lua"), singleKey, tradeCode);
        if (res == 0)
            return iResult.success;
        else return iResult.illegalParam;
    }

    /*
    把订单写入DB，再查出来，把与之关联的订单条目写入DB
     */
    @Override
    public void insertOrder(OmsOrder order,List<OmsOrderItem> orderItems) {
        try {
            mapper.insertSelective(order);
            OmsOrder savedOrder = mapper.selectByPrimaryKey(order);
            orderItems.forEach(orderItem -> {
                orderItem.setOrderId(savedOrder.getId());
                orderItem.setOrderSn(savedOrder.getOrderSn());
                itemMapper.insert(orderItem);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException("麻了");
        }
    }

    @Override
    /*多被MQ Consumer调用。根据平台订单序列号更新订单已支付状态*/
    public iResult orderSucPayStatusUpdate(String orderSn) {
        Example e = new Example(OmsOrder.class);
        e.createCriteria().andEqualTo("orderSn",orderSn);

        //根据序列号查出订单,设置更新字段
        OmsOrder order = mapper.selectOneByExample(e);
        order.setPayType(1).setStatus(1);//未支付→支付宝。待付款→待发货
        //MQ传输更新后的订单
        iResult mqRes = msgProvider.orderPaidPost(order);
        if (Predicate.fail(mqRes)){
            errLogService.newError("MQ","已支付订单通知库存失败","orderSn",orderSn);
        }
        //再新建一个更新字段载体，用以修改持久层订单状态信息
        OmsOrder carrier = new OmsOrder();
        carrier.setPayType(1).setStatus(1);//未支付→支付宝。待付款→待发货
        //DB
        try {
            mapper.updateByExampleSelective(carrier,e);
        } catch (Exception exception) {
            exception.printStackTrace();
            return iResult.dbException;
        }
        return new iResult(order, Code.success);
    }
}
