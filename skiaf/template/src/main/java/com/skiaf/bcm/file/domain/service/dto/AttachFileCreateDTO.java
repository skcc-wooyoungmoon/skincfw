/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachFileCreateDTO implements Serializable {

    private static final long serialVersionUID = -54725768852398151L;

    /** 파일 아이디 */
    private String fileId;

    /** 사용처 ID */
    private String targetId;

    /** 사용처 유형 */
    private String targetType;
}
