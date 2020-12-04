package com.lewo.zmail.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

public class LuaUtils {
    /*
    生成执行后返回值为long的脚本类，参数为脚本文件全名，文件位置固定在resources/script/下
     */
    public static DefaultRedisScript<Long> keQing(String filename){
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/"+filename)));
        script.setResultType(Long.class);
        return script;
    }
}
