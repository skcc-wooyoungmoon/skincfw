/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.exception;

/**
 * <pre>
 * Interface 관련 오류 
 * client에 response될 때는 HTTP STATUS CODE가 500로 리턴됨.
 * 
 * History
 * - 2018. 9. 20. | in01865 | 최초작성.
 * </pre>
 * @see ExceptionControllerAdvice
 */
public class InterfaceException extends BaseException {

    private static final long serialVersionUID = 5761217633060879205L;

    public InterfaceException(String systemMessage, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, String userMessage) {
        super(systemMessage, cause, enableSuppression, writableStackTrace, userMessage);
    }

    public static BaseExceptionBuilder withUserMessage(String userMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(InterfaceException.class);
        builder.withUserMessage(userMessage);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessage(String userMessageFormat, Object... objects) {
        return InterfaceException.withUserMessage(BaseExceptionBuilder.formatMessage(userMessageFormat, objects));
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(InterfaceException.class);
        builder.withUserMessageKey(userMessageKey);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey, Object... objects) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(InterfaceException.class);
        builder.withUserMessageKey(userMessageKey, objects);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(InterfaceException.class);
        builder.withSystemMessage(systemMessage);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessageFormat, Object... objects) {
        return InterfaceException.withSystemMessage(BaseExceptionBuilder.formatMessage(systemMessageFormat, objects));
    }
}
