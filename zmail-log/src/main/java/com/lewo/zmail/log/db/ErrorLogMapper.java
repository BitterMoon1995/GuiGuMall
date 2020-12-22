package com.lewo.zmail.log.db;

import com.lewo.zmall.model.LmsErrorLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface ErrorLogMapper extends Mapper<LmsErrorLog> {
}
