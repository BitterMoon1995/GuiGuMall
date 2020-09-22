package com.lewo.zmall.model;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
@Data
public class UmsUserReceiveAddress implements Serializable {

    @Id
    private Long id;
    private Long userId;
    private String  name;
    private String  phoneNumber;
    private int defaultStatus;
    private String postCode;
    private String province;
    private String city;
    private String region;
    private String detailAddress;

}
