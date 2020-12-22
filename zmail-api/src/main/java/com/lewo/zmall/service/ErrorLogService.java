package com.lewo.zmall.service;

import com.lewo.zmall.unified.iResult;

public interface ErrorLogService {
    iResult newError(String type,String msg,String errKey,String errValue);
}
