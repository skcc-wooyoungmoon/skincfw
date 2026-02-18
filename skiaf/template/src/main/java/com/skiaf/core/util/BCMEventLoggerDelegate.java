/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.context.ApplicationContext;

import com.skiaf.core.component.ApplicationContextProvider;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.LoggingConstant;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCMEventLogger 사용 시, lombok 의 log 객체를 넘겨, loggerName를 실제 호출하는 클래스로 인식되도록 도와주는 클래스 
 * ex) BCMEventLogger.withLogger(log).putLoginEvent("login-success", "gildong-uuid", "sso");
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
public class BCMEventLoggerDelegate {

    private static final String DOT = ".";
    private static final String MESSAGE_CODE_PREFIX = "bcm.log.message.";
    private static final String MESSAGECOMPONENT = "messageComponent";

    @NonNull
    private Logger logger;

    /**
     * <pre>
     * EventGroup 이 LOGIN 인 이벤트 기록용 함수.
     * </pre>
     */
    public void putLoginEvent(String eventType, Object... objects) {
        if (logger == null) {
            // 명시된 logger가 없으면, lombok의 log를 사용.
            logger = log;
        }
        
        if (BCMEventLogger.usesEventLog() && BCMEventLogger.usesLogin()) {

            // 프로퍼티에 지정되어있는 이벤트 로그 메세지를 저장
            String messageCode = MESSAGE_CODE_PREFIX + LoggingConstant.EventGroupType.LOGIN + DOT + eventType;
    
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            MessageComponent messageComponent = (MessageComponent) applicationContext.getBean(MESSAGECOMPONENT);
    
            String message = messageComponent.getMessage(messageCode);

            // 이벤트 그룹
            MDC.put(LoggingConstant.MappedKey.EVENT_GROUP.getName(), LoggingConstant.EventGroupType.LOGIN);
            // 이벤트 타입
            MDC.put(LoggingConstant.MappedKey.EVENT_TYPE.getName(), eventType);

            // 세션 로그인 ID
            if (SessionUtil.getLoginUser() != null) {
                MDC.put(LoggingConstant.MappedKey.LOGIN_ID.getName(), SessionUtil.getLoginUser().getLoginId());
            }

            // Marker를 사용
            logger.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

    /**
     * <pre>
     * EventGroup 이 ROLE 인 이벤트 기록용 함수.
     * </pre>
     */
    public void putRoleEvent(String eventType, Object... objects) {
        if (logger == null) {
            logger = log;
        }
        
        if (BCMEventLogger.usesEventLog() && BCMEventLogger.usesRole()) {

            // 프로퍼티에 지정되어있는 이벤트 로그 메세지를 저장
            String messageCode = MESSAGE_CODE_PREFIX + LoggingConstant.EventGroupType.ROLE + DOT + eventType;
    
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            MessageComponent messageComponent = (MessageComponent) applicationContext.getBean(MESSAGECOMPONENT);
    
            String message = messageComponent.getMessage(messageCode);

            // 이벤트 그룹
            MDC.put(LoggingConstant.MappedKey.EVENT_GROUP.getName(), LoggingConstant.EventGroupType.ROLE);
            // 이벤트 타입
            MDC.put(LoggingConstant.MappedKey.EVENT_TYPE.getName(), eventType);

            // 세션 로그인 ID
            if (SessionUtil.getLoginUser() != null) {
                MDC.put(LoggingConstant.MappedKey.LOGIN_ID.getName(), SessionUtil.getLoginUser().getLoginId());
            }

            // Marker를 사용
            logger.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

    /**
     * <pre>
     * EventGroup 이 USER 인 이벤트 기록용 함수.
     * </pre>
     */
    public void putUserEvent(String eventType, Object... objects) {
        if (logger == null) {
            logger = log;
        }
        
        if (BCMEventLogger.usesEventLog() && BCMEventLogger.usesUser()) {

            // 프로퍼티에 지정되어있는 이벤트 로그 메세지를 저장
            String messageCode = MESSAGE_CODE_PREFIX + LoggingConstant.EventGroupType.USER + DOT + eventType;
    
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            MessageComponent messageComponent = (MessageComponent) applicationContext.getBean(MESSAGECOMPONENT);
    
            String message = messageComponent.getMessage(messageCode);
        
            // 이벤트 그룹
            MDC.put(LoggingConstant.MappedKey.EVENT_GROUP.getName(), LoggingConstant.EventGroupType.USER);
            // 이벤트 타입
            MDC.put(LoggingConstant.MappedKey.EVENT_TYPE.getName(), eventType);

            // 세션 로그인 ID
            if (SessionUtil.getLoginUser() != null) {
                MDC.put(LoggingConstant.MappedKey.LOGIN_ID.getName(), SessionUtil.getLoginUser().getLoginId());
            }

            // Marker를 사용
            logger.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

}
