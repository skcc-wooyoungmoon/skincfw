/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.examp.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 인터페이스 전문시 사용
 * 
 * History
 * - 2018. 7. 31. | in01943 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class ExampFormatDTO implements Serializable {
    private static final long serialVersionUID = 4906908496609848920L;

    private String useConfigType;
    private String configFileName;
    private String configContents;
    private String sendJsonData;

    private String sendStatusCode;
    private String sendStatusMessage;
    private String sendData;
    private String sendUseConfig;

    private String receiveStatusCode;
    private String receiveStatusMessage;
    private String receiveData;
    private String receiveUseConfig;
}
