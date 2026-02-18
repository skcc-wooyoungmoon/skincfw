/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.cache.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheInfoDTO {

    private String key;
    private Object data;
    private long expiredTime;

}
