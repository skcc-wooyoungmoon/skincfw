/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.skiaf.core.component.ApplicationContextProvider;
import com.skiaf.core.constant.MessageDisplayType;

/**
 * <pre>
 * throw BizException.withUserMessage("a 입력값이 잘못 입력되었습니다.")
 *                      .withSystemMessage("a is too long")
 *                      .build();
 * 위와 같은 형태의 코딩이 가능하게 하기 위한 빌더 클래스.
 * 
 * History
 * - 2018.08.09 | in01865 | 2차수정.
 * </pre>
 */
public class BaseExceptionBuilder {
    private String userMessage;
    private String userMessageKey;
    private Object[] userMessageArgs;
    private String systemMessage;
    private Throwable cause;
    private boolean enableSuppression;
    private boolean writableStackTrace;
    private MessageDisplayType displayType;
    private boolean forcesOK = false;
    private String code;
    
    private Class<?> exceptionType;
    
    private MessageSource messageSource;
    
    public BaseExceptionBuilder(Class<?> exceptionType) {
        this.exceptionType = exceptionType;

        this.messageSource = ApplicationContextProvider.getApplicationContext().getBean("messageSource", MessageSource.class);
    }

    public BaseExceptionBuilder withUserMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    public BaseExceptionBuilder withUserMessage(String userMessageFormat, Object... objects) {
        String msg = MessageFormatter.arrayFormat(userMessageFormat, objects).getMessage();
        return this.withUserMessage(msg);
    }

    public BaseExceptionBuilder withUserMessageKey(String userMessageKey) {
        return this.withUserMessageKey(userMessageKey, (Object[]) null);
    }

    public BaseExceptionBuilder withUserMessageKey(String userMessageKey, Object... objects) {
        this.userMessageKey = userMessageKey;
        this.userMessageArgs = objects;
        return this;
    }

    public BaseExceptionBuilder withSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
        return this;
    }

    public BaseExceptionBuilder withSystemMessage(String systemMessageFormat, Object... objects) {
        return this.withSystemMessage(formatMessage(systemMessageFormat, objects));
    }

    public BaseExceptionBuilder withCause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public BaseExceptionBuilder withEnableSuppression(boolean enableSuppression) {
        this.enableSuppression = enableSuppression;
        return this;
    }

    public BaseExceptionBuilder withWritableStackTrace(boolean writableStackTrace) {
        this.writableStackTrace = writableStackTrace;
        return this;
    }

    public BaseExceptionBuilder withDisplayType(MessageDisplayType displayType) {
        this.displayType = displayType;
        return this;
    }

    /**
     * client에게 HTTP STATUS CODE가 200(ok)로 리턴되게 강제하는지의 여부 지정. BizException에 대해서만
     * 적용됨.
     * 
     * @see BizException
     */
    public BaseExceptionBuilder withForcesOK(boolean forcesOK) {
        this.forcesOK = forcesOK;
        return this;
    }

    public BaseExceptionBuilder withCode(String code) {
        this.code = code;
        return this;
    }
    
    /**
     * <pre>
     * 설정한 값들을 이용하여, 해당 타입의 BaseException 객체 생성.
     * </pre>
     */
    public BaseException build() {
        // userMessage 설정.
        if (StringUtils.isNotBlank(this.userMessageKey)) {
            this.userMessage = messageSource.getMessage(this.userMessageKey, this.userMessageArgs,
                                                LocaleContextHolder.getLocale());
        }

        // 유형별 userMessage 설정.
        if (StringUtils.isBlank(this.userMessage)) {
            if (exceptionType.equals(NotFoundException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.notfound", 
                                                    null, LocaleContextHolder.getLocale());
            } else if (exceptionType.equals(ValidationException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.validation", 
                                                    null, LocaleContextHolder.getLocale());
            } else if (exceptionType.equals(UnauthorizedException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.unauthorized", 
                                                    null, LocaleContextHolder.getLocale());
            } else if (exceptionType.equals(ForbiddenException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.forbidden", 
                                                    null, LocaleContextHolder.getLocale());
            } else if (exceptionType.equals(BizException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.biz", 
                                                    null, LocaleContextHolder.getLocale());
            } else if (exceptionType.equals(InterfaceException.class)) {
                this.userMessage = messageSource.getMessage(
                                                    "bcm.common.exception.interface", 
                                                    null, LocaleContextHolder.getLocale());
}
        }

        // 유형별 객체 생성.
        BaseException be = null;
        if (exceptionType.equals(NotFoundException.class)) {
            be = new NotFoundException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression, 
                        this.writableStackTrace,
                        this.userMessage);
        } else if (exceptionType.equals(ValidationException.class)) {
            be = new ValidationException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression,
                        this.writableStackTrace, 
                        this.userMessage);
        } else if (exceptionType.equals(UnauthorizedException.class)) {
            be = new UnauthorizedException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression,
                        this.writableStackTrace, 
                        this.userMessage);
        } else if (exceptionType.equals(ForbiddenException.class)) {
            be = new ForbiddenException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression, 
                        this.writableStackTrace,
                        this.userMessage);
        } else if (exceptionType.equals(BizException.class)) {
            be = new BizException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression, 
                        this.writableStackTrace,
                        this.userMessage);
            ((BizException) be).setForcesOK(this.forcesOK);
        } else if (exceptionType.equals(InterfaceException.class)) {
            be = new InterfaceException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression, 
                        this.writableStackTrace,
                        this.userMessage);
        } else {
            be = new BaseException(this.systemMessage, 
                        this.cause, 
                        this.enableSuppression, 
                        this.writableStackTrace,
                        this.userMessage);
        } 
        be.setDisplayType(displayType);
        be.setCode(code);

        return be;
    }

    /**
     * <pre>
     * String a = "a";
     * String b = "b";
     * BaseExceptionBuilder.formatMessage("value1={}, value2={}", a, b);
     * // -> "value1=a, value2=b"
     * </pre>
     */
    public static String formatMessage(String format, Object... objects) {
        return MessageFormatter.arrayFormat(format, objects).getMessage();
    }
}
