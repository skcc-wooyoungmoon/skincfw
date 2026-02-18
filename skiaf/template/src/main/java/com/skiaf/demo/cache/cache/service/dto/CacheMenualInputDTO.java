/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.cache.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 캐시 수동입력 DTO
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class CacheMenualInputDTO {

    @ApiModelProperty(required = true, example = "menual")
    private String name;

    @ApiModelProperty(required = true, example = "menualKey")
    private String key;

    @ApiModelProperty(required = true, example = "menualData")
    private Object data;

    @ApiModelProperty(required = true, example = "-1")
    private long expiredTime = -1L;

}
