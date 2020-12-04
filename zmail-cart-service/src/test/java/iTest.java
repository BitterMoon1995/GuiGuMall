import com.lewo.zmail.cart.Cart_Service;
import com.lewo.zmail.cart.db.CartMapper;
import com.lewo.zmall.model.OmsCartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = Cart_Service.class)
public class iTest {
    @Autowired
    CartMapper cartMapper;
    @Test
    public void test() {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setUserId("1");
        List<OmsCartItem> cartItems = cartMapper.select(cartItem);
        List<OmsCartItem> collect = cartItems.stream()
                .filter(omsCartItem -> omsCartItem.getDeleteStatus() == 0)//未删除的
                .filter(OmsCartItem::getIsChecked)//勾选了的
                .collect(Collectors.toList());
        System.out.println(collect.size());
    }
}
