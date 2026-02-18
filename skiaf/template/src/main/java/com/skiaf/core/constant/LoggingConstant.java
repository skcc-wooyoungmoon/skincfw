/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.constant;

/**
 * <pre>
 * 로그 관련 상수 클래스
 * 
 * History
 * - 2018. 7. 24. | in01869 | 최초작성.
 * </pre>
 */
public class LoggingConstant {

    private LoggingConstant() {
        throw new IllegalStateException("LoggingConstant Class");
    }

    /**
     * <pre>
     * logback-spring.xml 에 설정한 MARKER 명칭. 
     * (Marker를 사용해서 로그를 남길 경우레벨설정에 상관없이 무조건 로그를 남긴다.) 
     * 
     * 마커 사용하는 방법 :
     *  log.info(MarkerFactory.getMarker("EVENT"), message);
     * </pre>
     */
    public static final String MATCH_MARKER_FILTER = "EVENT";

    /**
     * <pre>
     * logging_event_property 에 mappedKey 컬럼에 저장되는 키값
     * </pre>
     */
    public enum MappedKey {

        LOGIN_ID("LOGIN_ID"), EVENT_GROUP("EVENT_GROUP"), EVENT_TYPE("EVENT_TYPE");

        private final String name;

        private MappedKey(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * <pre>
     * 이벤트 타입 종류 (mappedKey 값이 EVENT_GROUP일 경우 mappedValue에 저장되는 값)
     * </pre>
     */
    public enum EventGroupType {
        LOGIN, ROLE, USER
    }

}
