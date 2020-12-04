package com.lewo.zmail.order.service;

import com.lewo.exception.DbException;
import com.lewo.unified.iResult;
import com.lewo.utils.RandomUtils;
import com.lewo.zmail.order.db.OrderItemMapper;
import com.lewo.zmail.order.db.OrderMapper;
import com.lewo.zmail.utils.LuaUtils;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;
import com.lewo.zmall.service.OrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper mapper;
    @Autowired
    OrderItemMapper itemMapper;
    @Autowired
    RedisTemplate<String, Object> redis;

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
            throw new DbException();
        }
    }
}
