import com.lewo.zmail.Order_Service;
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
}
