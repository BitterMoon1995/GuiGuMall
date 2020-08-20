import com.lewo.zmail.manage.Manage_Service;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.service.SkuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = Manage_Service.class)
public class HudieDream {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    SkuService skuService;
    @Test
    public void test() {
        ValueOperations<String, Object> op = redisTemplate.opsForValue();
        op.set("1","司马了",60, TimeUnit.SECONDS);
    }
    @Test
    public void test2() throws InterruptedException {
        ValueOperations<String, Object> op = redisTemplate.opsForValue();
        PmsSkuInfo info = skuService.getById("110");
        op.set("goodLilyLove",info,120, TimeUnit.SECONDS);
    }
    @Test
    public void test3() {
        ValueOperations<String, Object> op = redisTemplate.opsForValue();
        PmsSkuInfo o = (PmsSkuInfo) op.get("goodLilyLove");
        assert o != null;
        System.out.println(o.getId()+o.getSkuAttrValueList()+o.getSkuImageList());
    }
}
