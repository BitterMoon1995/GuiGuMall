package com.lewo.zmail.user.db;

import com.lewo.zmall.model.UmsUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<UmsUser> {

}