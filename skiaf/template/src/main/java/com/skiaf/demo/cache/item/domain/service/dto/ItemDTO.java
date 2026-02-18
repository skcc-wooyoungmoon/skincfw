/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item.domain.service.dto;

import java.io.Serializable;

import com.skiaf.core.cache.CacheKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private Long itemId;
    
    @CacheKey
    private String itemName;
    
    private String itemDesc;

}
