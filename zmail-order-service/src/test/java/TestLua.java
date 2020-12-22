import com.lewo.utils.TimeUtils;
import com.lewo.zmail.Order_Service;
import com.lewo.zmail.order.msg.MsgProvider;
import com.lewo.zmall.model.LmsErrorLog;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.service.ErrorLogService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = Order_Service.class)
public class TestLua {
    @Autowired
    RedisTemplate<String,Object> redis;
    @DubboReference
    ErrorLogService errorLogService;
    @Autowired
    MsgProvider msgProvider;
    @Test
    public void test() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/letsgo.lua")));
        script.setResultType(Long.class);
        List<String> keys = Collections.singletonList("k");
        Long res = redis.execute(script, keys, 4396);
        System.out.println(res);
    }
    @Test
    public void test2() {
        String trueCode = Objects.requireNonNull(redis.opsForValue().get("userId:" + 1 + ":tradeCode")).toString();
        System.out.println(trueCode);
    }
    @Test
    public void test4() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/checkTC.lua")));
        script.setResultType(Long.class);
        List<String> keys = Collections.singletonList("userId:1:tradeCode");
        Long res = redis.execute(script, keys, "nmslese");
        System.out.println(res);
    }
    @Test
    public void test5() {
        LmsErrorLog errorLog = new LmsErrorLog();
        errorLog.setCreateTime(TimeUtils.curTime());
        errorLog.setMsg("黑色大尼哥");
    }

    @Test
    public void test6() {
        OmsOrder order = new OmsOrder();
        order.setOrderSn("正义帅丁真");
        order.setId("支那做题家");
        msgProvider.orderPaidPost(order);
    }
}
