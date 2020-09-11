package littleSkirt;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@EnableDubbo
@MapperScan(basePackages = "littleSkirt.dao")
@SpringBootApplication
public class Zmail_Test {
    public static void main(String[] args) {
        SpringApplication.run(Zmail_Test.class,args);
    }
}
