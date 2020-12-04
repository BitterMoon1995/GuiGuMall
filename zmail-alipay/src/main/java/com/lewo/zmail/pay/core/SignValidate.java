package com.lewo.zmail.pay.core;

import javax.servlet.http.HttpServletRequest;

public class SignValidate {
    public static void main(String[] args) {

    }

    public static boolean validate(HttpServletRequest request){
        String sign = request.getParameter("sign");

        String auth_app_id = request.getParameter("auth_app_id");
        String charset = request.getParameter("charset");
        String method = request.getParameter("method");
        String out_trade_no = request.getParameter("out_trade_no");
        String seller_id = request.getParameter("seller_id");
        String subject = request.getParameter("subject");
        String trade_no = request.getParameter("trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String version = request.getParameter("version");

        return true;
    }
}
