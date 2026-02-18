/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Business Error 유형의 오류. 
 * client에 response될 때는 HTTP STATUS CODE가 500로 리턴됨.
 * 
 * History
 * - 2018. 8. 22. | in01865 | 2차수정. 
 * </pre>
 * @see ExceptionControllerAdvice
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 3584878617244811296L;

    /**
     * client에게 HTTP STATUS CODE가 200(ok)로 리턴되게 강제하는지의 여부.
     */
    @Getter
    @Setter
    private boolean forcesOK = false;

    public BizException(String systemMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            String userMessage) {
        super(systemMessage, cause, enableSuppression, writableStackTrace, userMessage);
    }

    public static BaseExceptionBuilder withUserMessage(String userMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(BizException.class);
        builder.withUserMessage(userMessage);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessage(String userMessageFormat, Object... objects) {
        return BizException.withUserMessage(BaseExceptionBuilder.formatMessage(userMessageFormat, objects));
    }
    
    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(BizException.class);
        builder.withUserMessageKey(userMessageKey);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey, Object... objects) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(BizException.class);
        builder.withUserMessageKey(userMessageKey, objects);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(BizException.class);
        builder.withSystemMessage(systemMessage);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessageFormat, Object... objects) {
        return BizException.withSystemMessage(BaseExceptionBuilder.formatMessage(systemMessageFormat, objects));
    }
}
