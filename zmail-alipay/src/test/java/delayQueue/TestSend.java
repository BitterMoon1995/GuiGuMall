package delayQueue;

import com.lewo.zmail.Alipay;
import com.lewo.zmail.pay.msg.MsgProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Alipay.class)
public class TestSend {
    @Autowired
    MsgProvider msgProvider;
    @Test
    public void test() {
        msgProvider.checkPayStatus("nigger");
    }
}
