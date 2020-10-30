import com.lewo.zmail.auth.Authentication_Web;
import com.lewo.zmail.web.utils.JwtUtil;
import com.lewo.zmall.model.UmsUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = Authentication_Web.class)
public class iTest {
    @Test
    public void test() {
        HashMap<String, Object> map = new HashMap<>();
        UmsUser user = new UmsUser();
        user.setUsername("nigger");
        user.setPassword("8964");
        map.put("username","nigger");
        map.put("age",8964);
        String encode = JwtUtil.encode("black", map, "rtgs5984");
        System.out.println(encode);
        //nig 行 ni xxx nix 不行
        Map<String, Object> decode = JwtUtil.decode(encode, "black", "rtgs");

        decode.forEach((i, o)->{
            System.out.println(i+o);
        });
    }
}
