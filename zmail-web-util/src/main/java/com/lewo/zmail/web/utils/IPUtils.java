package com.lewo.zmail.web.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
    public static String getCurrIP(HttpServletRequest request){
        String currIP = null;
        String forwardedIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(forwardedIp))
            currIP = forwardedIp;
        else {
            String ip = request.getRemoteAddr();
            //竟然整了个IPV6地址出来！骚东西
            if (StringUtils.isBlank(ip) || ip.equals("0:0:0:0:0:0:0:1"))
                currIP = "127.0.0.1";
            else currIP = ip;
        }
        return currIP;
    }
}
