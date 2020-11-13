package com.lewo.zmail.user.db;

import com.lewo.zmall.model.UmsUserReceiveAddress;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserAddressMapper extends Mapper<UmsUserReceiveAddress> {

}