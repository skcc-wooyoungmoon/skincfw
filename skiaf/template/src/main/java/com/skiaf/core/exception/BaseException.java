/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.exception;

import com.skiaf.core.constant.MessageDisplayType;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * skiaf 내에서 정의한 Exception의 기본 클래스.
 * 
 * History
 * - 2018. 8. 22. | in01865 | 2차수정.
 * </pre>
 * @see BizException
 * @see ForbiddenException
 * @see NotFoundException
 * @see UnauthorizedException
 * @see ValidationException
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -5119762081943226106L;

    /**
     * 사용자에게 노출되는 오류 설명.
     */
    @Getter
    private String userMessage;

    /**
     * exception에 대한 프로그래밍적인 설명. 사용자에게 직접적으로 노출되지 않을 내용이며, 디버깅에 도움이 될 내용을 담음.
     */
    @Getter
    private String systemMessage;

    /**
     * 오류 메세지에 대한 표시 방법.
     */
    @Getter
    @Setter
    private MessageDisplayType displayType;

    /**
     * 오류에 대한 업무적으로 정의한 코드(http status code가 아님)
     */
    @Getter
    @Setter
    private String code;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Constructor
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public BaseException() {
        super();
    }

    public BaseException(String systemMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(systemMessage, cause, enableSuppression, writableStackTrace);

        // Throwable의 message 는 BaseException에서는 systemMessage인 것으로 간주함.
        this.systemMessage = systemMessage;
    }

    public BaseException(String systemMessage, Throwable cause) {
        super(systemMessage, cause);

        // Throwable의 message 는 BaseException에서는 systemMessage인 것으로 간주함.
        this.systemMessage = systemMessage;
    }

    public BaseException(String systemMessage) {
        super(systemMessage);

        // Throwable의 message 는 BaseException에서는 systemMessage인 것으로 간주함.
        this.systemMessage = systemMessage;
    }

    public BaseException(String systemMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            String userMessage) {
        super(systemMessage, cause, enableSuppression, writableStackTrace);

        // Throwable의 message 는 BaseException에서는 systemMessage인 것으로 간주함.
        this.systemMessage = systemMessage;
        this.userMessage = userMessage;
    }

}