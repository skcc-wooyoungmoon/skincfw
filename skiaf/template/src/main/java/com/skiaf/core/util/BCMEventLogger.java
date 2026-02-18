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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.skiaf.core.component.ApplicationContextProvider;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.LoggingConstant;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCM 이벤트 로그 처리 Util
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Component
@Slf4j
public class BCMEventLogger {

    private static final String DOT = ".";
    private static final String MESSAGE_CODE_PREFIX = "bcm.log.message.";
    private static final String MESSAGECOMPONENT = "messageComponent";

    // 이벤트 로그 사용여부
    private static boolean useEventLog;

    // 이벤트 그룹 login 사용여부
    private static boolean useLogin;

    // 이벤트 그룹 role 사용여부
    private static boolean useRole;

    // 이벤트 그룹 user 사용여부
    private static boolean useUser;

    @Value("${bcm.log.event}")
    private void setUseEventLog(boolean propUseEventLog) {
        useEventLog = propUseEventLog;
    }

    @Value("${bcm.log.event.group.login}")
    private void setUseLogin(boolean propUseLogin) {
        useLogin = propUseLogin;
    }

    @Value("${bcm.log.event.group.role}")
    private void setUseRole(boolean propUseRole) {
        useRole = propUseRole;
    }

    @Value("${bcm.log.event.group.user}")
    private void setUseTypeUser(boolean propUseUser) {
        useUser = propUseUser;
    }

    /**
     * 이벤트 로그의 사용여부.
     */
    public static boolean usesEventLog() {
        return useEventLog;
    }

    /**
     * 이벤트 그룹 login의 사용여부
     */
    public static boolean usesLogin() {
        return useLogin;
    }

    /**
     * 이벤트 그룹 role의 사용여부
     */
    public static boolean usesRole() {
        return useRole;
    }

    /**
     * 이벤트 그룹 User의 사용여부
     */
    public static boolean usesUser() {
        return useUser;
    }

    /**
     * <pre>
     * EventGroup 이 LOGIN 인 이벤트 기록용 함수.
     * </pre>
     */
    public static void putLoginEvent(String eventType, Object... objects) {

        if (BCMEventLogger.usesEventLog() && useLogin) {
        
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
            log.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

    /**
     * <pre>
     * EventGroup 이 ROLE 인 이벤트 기록용 함수.
     * </pre>
     */
    public static void putRoleEvent(String eventType, Object... objects) {

        if (BCMEventLogger.usesEventLog() && useRole) {
            
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
            log.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

    /**
     * <pre>
     * EventGroup 이 USER 인 이벤트 기록용 함수.
     * </pre>
     */
    public static void putUserEvent(String eventType, Object... objects) {

        if (BCMEventLogger.usesEventLog() && useUser) {
            
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
            log.info(MarkerFactory.getMarker(LoggingConstant.MATCH_MARKER_FILTER), message, objects);

            MDC.clear();
        }
    }

    /**
     * <pre>
     * BCMEventLogger 사용 시, lombok 의 log 객체를 넘겨, loggerName를 실제 호출하는 클래스로 인식되도록 함. 
     * ex) BCMEventLogger.withLogger(log).putLoginEvent("login-success", "gildong-uuid", "sso");
     * </pre>
     */
    public static BCMEventLoggerDelegate withLogger(@NonNull Logger logger) {
        return new BCMEventLoggerDelegate(logger);
    }
}
