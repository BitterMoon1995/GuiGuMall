package com.lewo.zmall.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class UmsUser implements Serializable {
    //通用mapper注解，表示这个是主键
    @Id
    /* JPA提供的四种标准用法为TABLE,SEQUENCE,IDENTITY,AUTO. 
    TABLE：使用一个特定的数据库表格来保存主键。 
    SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。 
    IDENTITY：主键由数据库自动生成（主要是自动增长型） 
    AUTO：主键由程序控制。 */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userLevelId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private int status;
    private Date createTime;
    private String icon;
    private String gender;
    private Date birthday;
    private String city;
    private String job;
    private String personalizedSignature;
    private String sourceType;
    private int integration;
    private int growth;
    private int luckeyCount;
    private int historyIntegration;
    private String sourceUid;
    private String accessToken;
    private String accessCode;
}
