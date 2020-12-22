package com.lewo.zmall.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PmsSearchCrumb {

    private String valueId;

    private String valueName;

    private String urlParam;
}
