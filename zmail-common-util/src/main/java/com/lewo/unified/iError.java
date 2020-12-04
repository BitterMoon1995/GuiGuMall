package com.lewo.unified;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class iError {
    private Integer code;
    private String msg;
    private Date date;
}
