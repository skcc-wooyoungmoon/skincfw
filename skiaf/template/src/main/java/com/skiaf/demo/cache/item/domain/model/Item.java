/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(required = true, example = "1")
    private Long itemId;

    @ApiModelProperty(required = false, example = "name")
    private String itemName;

    @ApiModelProperty(required = false, example = "desc")
    private String itemDesc;
}
