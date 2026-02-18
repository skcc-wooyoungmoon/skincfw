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
public class AttachFileDetailDTO implements Serializable {

    private static final long serialVersionUID = -8786343657599059869L;

    /** 파일 아이디 */
    private Long fileId;

    /** 사용처 ID */
    private String targetId;

    /** 사용처 유형 */
    private String targetType;

    /** 원본 파일 이름 */
    private String originalFileName;

    /** 서버 파일 이름 */
    private String severFileName;

    /** 파일 경로 */
    private String filePath;

    /** 확장자 */
    private String fileExtension;

    /** 파일 사이즈 */
    private int fileSize;

}
