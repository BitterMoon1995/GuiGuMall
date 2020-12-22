import com.lewo.utils.TimeUtils;
import com.lewo.zmail.Log_Service;
import com.lewo.zmall.model.LmsErrorLog;
import com.lewo.zmall.service.ErrorLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Log_Service.class)
public class LocalTest {
    @Autowired
    ErrorLogService errorLogService;
    @Test
    public void test() {
        LmsErrorLog errorLog = new LmsErrorLog();
        errorLog.setCreateTime(TimeUtils.curTime());
        errorLog.setMsg("斩下尼头");
    }
}
