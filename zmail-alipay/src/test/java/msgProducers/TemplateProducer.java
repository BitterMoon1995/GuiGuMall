package msgProducers;

import com.lewo.zmail.Alipay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.HashMap;

@SpringBootTest(classes = Alipay.class)
public class TemplateProducer {
    /**
     * @Autowired 一般按类型注入(byType)。也可以指定对象名注入，但是对象名就只能是@Bean的方法名了，有丶被动，
     * 嗯要自由选取对象名，又要byName注入，就得配合@Qualifier("xxx")，不太好看了。
     * @Resource(name = "xxx")，比较优雅。
     */
    @Resource(name = "myQueue")
    private Queue queue1;
    @Resource(name = "thatNigger")
    private Queue queue2;
    @Resource(name = "paySucQueue")
    private Queue paySucQueue;
    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    @Test
    public void test() {
        jmsTemplate.convertAndSend(queue1,"玩耍玩耍\n");
        jmsTemplate.convertAndSend(queue2,"这下纳了\n");
    }
    @Test
    public void test2() {
        jmsTemplate.convertAndSend("drink","nigger必死");
    }
    @Test
    public void test2_2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("nigger","尼哥儿尼哥儿小尼哥儿");
        jmsTemplate.convertAndSend("drink",map);
    }
    @Test
    public void test3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("nigger","尼哥儿尼哥儿小尼哥儿");
        jmsTemplate.convertAndSend("miHoYo",map);
        /*java.lang.IllegalArgumentException: Payload must not be null*/
//        String s = null;
//        jmsTemplate.convertAndSend("miHoYo",s);
    }
}
