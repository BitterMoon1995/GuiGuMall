package com.lewo.zmail.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 冷知识：1.Redis事务不支持回滚
 * 2.跑脚本别用这种配置方式，很SB(整个项目只有一个脚本)，直接new DefaultRedisScript就行了
 */
@Configuration
public class LuaConfig {

    @Bean
    public DefaultRedisScript<Long> skuLock() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/skuLock.lua")));
        //指定ReturnType为Long.class，注意这里不能使用Integer.class，
        //因为ReturnType不支持。只支持List.class, Boolean.class和Long.class
        script.setResultType(Long.class);
        return script;
    }
}
