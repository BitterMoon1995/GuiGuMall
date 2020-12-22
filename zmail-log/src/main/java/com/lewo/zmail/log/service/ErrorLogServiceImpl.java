package com.lewo.zmail.log.service;

import com.lewo.zmall.unified.iResult;
import com.lewo.utils.TimeUtils;
import com.lewo.zmail.log.db.ErrorLogMapper;
import com.lewo.zmall.model.LmsErrorLog;
import com.lewo.zmall.service.ErrorLogService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class ErrorLogServiceImpl implements ErrorLogService {
    @Autowired
    ErrorLogMapper mapper;

    @Override
    public iResult newError(String type,String msg,String errKey,String errValue) {

        LmsErrorLog errorLog = new LmsErrorLog();
        errorLog.setCreateTime(TimeUtils.curTime());
        errorLog.setType(type);
        errorLog.setMsg(msg);
        errorLog.setErrKey(errKey);
        errorLog.setErrValue(errValue);

        try {
            mapper.insert(errorLog);
        } catch (Exception e) {
            e.printStackTrace();
            return iResult.dbException;
        }
        return iResult.success;
    }
}
