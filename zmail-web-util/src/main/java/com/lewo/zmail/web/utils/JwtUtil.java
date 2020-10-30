package com.lewo.zmail.web.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static String encode(String key, Map<String, Object> param, String salt) {
        if (salt != null) {
            key += salt;
        }
        System.out.println(key);

        JwtBuilder jwtBuilder = Jwts.builder()
                 /*HEADER
                 由算法和type构成，type是写死的JWT
                 只要你有sign，compact()里面会帮你解析header，所以这里不需要set*/

                 /*PAYLOAD
                 这里由Claims里的规范键和你的自定义键组成
                 设置payload中键【iat】(Claims.ISSUED_AT)的值
                 该键代表token的创建日期，要做token过期的话用得到

                 注意：payload并不是密文，可以直接通过base64解码！
                 */
                .setIssuedAt(new Date())
                 //设置自定义键值
                .setClaims(param)

                 /*VERIFY SIGNATURE
                 指定自定义签名算法、secretKey，secretKey会和header、payload一起被base64解码成二进制数组
                 然后生成校验签名，校验签名是无论如何必须要有的*/
                .signWith(SignatureAlgorithm.HS256, key);

        return jwtBuilder.compact();

    }


    public static Map<String, Object> decode(String token, String key, String salt) {
        // Claims是jjwt为广大玩家制作的标准化傻瓜式JSON Map,放在payload里面
        // 有规范化的键，放东西和JSON序列化也方便
        Claims claims = null;
        if (salt != null) {
            key += salt;
        }
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            System.out.println("token无效！");
            return null;
        }
        return claims;
    }
}
