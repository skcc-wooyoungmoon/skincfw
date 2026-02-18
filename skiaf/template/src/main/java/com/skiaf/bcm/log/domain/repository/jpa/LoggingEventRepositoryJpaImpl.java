/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.repository.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.skiaf.bcm.log.domain.model.LoggingEvent;
import com.skiaf.bcm.log.domain.model.LoggingEventProperty;
import com.skiaf.bcm.log.domain.service.dto.LoggingDTO;
import com.skiaf.bcm.log.domain.service.dto.LoggingSearchDTO;
import com.skiaf.core.constant.LoggingConstant.MappedKey;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 로그 이벤트 JPA Extend 구현
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public class LoggingEventRepositoryJpaImpl implements LoggingEventRepositoryJpaExtend {

    private static final String SYSTEM = "SYSTEM";
    private static final String EVENT = "EVENT";

    private static final String WHERE = "where";
    private static final String PARAMMAP = "paramMap";

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public PageDTO<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Pageable pageable) {

        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | 조건 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        Map<String, Object> whereMap = setWhere(search);
        
        StringBuilder where = (StringBuilder) whereMap.get(WHERE);
        Map<String, Object> paramMap = (Map<String, Object>) whereMap.get(PARAMMAP);
        

        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | 정렬 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        Sort sort = pageable.getSort();
        String orderBy = "";

        String[] orderByText = { "" };
        sort.forEach((Order arg) -> orderByText[0] = "log." + toCamelCase(arg.getProperty()) + " " + arg.getDirection() + " ");

        if (orderByText[0] != "") {
            orderBy += "ORDER BY " + orderByText[0];
        }

        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | Query 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        // Query 생성
        String query = "SELECT log FROM LOGGING_EVENT log " + where + orderBy;

        TypedQuery<LoggingEvent> typedQuery = em.createQuery(query, LoggingEvent.class);

        // 조건 파라메터 추가
        paramMap.forEach(typedQuery::setParameter);

        PageDTO<LoggingDTO> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        // 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<LoggingDTO> dtoList = new ArrayList<>();
        List<LoggingEvent> loggingEventList = typedQuery.getResultList();
        loggingEventList.forEach((LoggingEvent log) -> {

            LoggingDTO dto = modelMapper.map(log, LoggingDTO.class);
            LoggingEventProperty prop = null;
            for (int i = 0, max = log.getProperties().size(); i < max; i++) {
                prop = log.getProperties().get(i);

                // logging_event_property의 mappedKey 맵핑
                if (prop.getMappedKey().equals(MappedKey.LOGIN_ID.getName())) {
                    dto.setLoginId(prop.getMappedValue());
                } else if (prop.getMappedKey().equals(MappedKey.EVENT_GROUP.getName())) {
                    dto.setEventGroup(prop.getMappedValue());
                } else if (prop.getMappedKey().equals(MappedKey.EVENT_TYPE.getName())) {
                    dto.setEventType(prop.getMappedValue());
                }
            }

            dtoList.add(dto);
        });
        result.setList(dtoList);

        return result;
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public List<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Sort sort) {
        
        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | 조건 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        Map<String, Object> whereMap = setWhere(search);
        
        StringBuilder where = (StringBuilder) whereMap.get(WHERE);
        Map<String, Object> paramMap = (Map<String, Object>) whereMap.get(PARAMMAP);
        

        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | 정렬 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        String orderBy = "";

        String[] orderByText = { "" };
        sort.forEach((Order arg) -> orderByText[0] = "log." + arg.getProperty() + " " + arg.getDirection() + " ");

        if (orderByText[0] != "") {
            orderBy += "ORDER BY " + orderByText[0];
        }

        /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        | Query 처리
        |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

        // Query 생성
        String query = "SELECT log FROM LOGGING_EVENT log " + where + orderBy;

        TypedQuery<LoggingEvent> typedQuery = em.createQuery(query, LoggingEvent.class);

        // 조건 파라메터 추가
        paramMap.forEach(typedQuery::setParameter);

        List<LoggingDTO> dtoList = new ArrayList<>();
        List<LoggingEvent> loggingEventList = typedQuery.getResultList();
        loggingEventList.forEach((LoggingEvent log) -> {

            LoggingDTO dto = modelMapper.map(log, LoggingDTO.class);
            LoggingEventProperty prop = null;
            for (int i = 0, max = log.getProperties().size(); i < max; i++) {
                prop = log.getProperties().get(i);

                // logging_event_property의 mappedKey 맵핑
                if (prop.getMappedKey().equals(MappedKey.LOGIN_ID.getName())) {
                    dto.setLoginId(prop.getMappedValue());
                } else if (prop.getMappedKey().equals(MappedKey.EVENT_GROUP.getName())) {
                    dto.setEventGroup(prop.getMappedValue());
                } else if (prop.getMappedKey().equals(MappedKey.EVENT_TYPE.getName())) {
                    dto.setEventType(prop.getMappedValue());
                }
            }

            dtoList.add(dto);
        });


        return dtoList;
    }

    private StringBuilder appendAnd(StringBuilder inputValue) {
        return inputValue.append("AND ");
    }
    
    /**
     * <pre>
     * where 절 생성
     * </pre>
     */
    private Map<String, Object> setWhere(LoggingSearchDTO search) {
        
        Map<String, Object> result = new HashMap<>();
        
        StringBuilder where = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<>();

        // 검색 공통
        String input = "";
        Timestamp time = null;

        // 기간 시작날짜
        if (StringUtils.isNotBlank(search.getStartDate())) {
            where = appendAnd(where);
            input = search.getStartDate() + " " + search.getStartTime() + ":00";
            time = Timestamp.valueOf(input);

            where.append("log.timestmp > :startDate ");
            paramMap.put("startDate", time.getTime());
        }

        // 기간 종료날짜
        if (StringUtils.isNotBlank(search.getEndDate())) {
            where = appendAnd(where);
            input = search.getEndDate() + " " + search.getEndTime() + ":59";
            time = Timestamp.valueOf(input);

            where.append("log.timestmp <= :endDate ");
            paramMap.put("endDate", time.getTime());
        }

        // 로그 타입별
        if (SYSTEM.equals(search.getLoggingType())) {
            // 시스템 로그

            // 기본 검색어
            if (StringUtils.isNotBlank(search.getKeyword())) {
                where = appendAnd(where);

                where.append("(UPPER(log.formattedMessage) LIKE :keyword ")
                        .append("OR UPPER(log.levelString) LIKE :keyword ")
                        .append("OR UPPER(log.loggerName) LIKE :keyword ")
                        .append("OR UPPER(log.callerClass) LIKE :keyword ")
                        .append("OR UPPER(log.callerMethod) LIKE :keyword ")
                        .append("OR UPPER(log.callerLine) LIKE :keyword) ");

                paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
            }

            // 이벤트 로그 포함하지 않음
            if (!search.isEventLogInclude()) {
                where = appendAnd(where);
                where.append(
                        "(SELECT COUNT(1) AS CNT FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId) = 0 ");
            }

            // 로그 레벨 - LEVEL_STRING
            if (StringUtils.isNotBlank(search.getLevels())) {
                where = appendAnd(where).append("(");

                String[] arr = search.getLevels().split(",");
                String level = "";
                for (int i = 0, len = arr.length; i < len; i++) {
                    level = arr[i];
                    where.append(((i == 0) ? "" : "OR")).append(" log.levelString LIKE '%").append(level).append("%' ");
                }
                where.append(") ");
            }

            // 로그 명 - LOGGER_NAME
            if (StringUtils.isNotBlank(search.getLogger())) {
                where = appendAnd(where);
                where.append("UPPER(log.loggerName) LIKE :loggerName ");

                paramMap.put("loggerName", "%" + search.getLogger().toUpperCase() + "%");
            }

            // 로그 호출 정보 - CALLER_CLASS + ' : ' + CALLER_METHOD + ' : ' + CALLER_LINE
            if (StringUtils.isNotBlank(search.getCaller())) {
                where = appendAnd(where);

                where.append("(UPPER(log.callerClass) LIKE :caller ").append("OR UPPER(log.callerMethod) LIKE :caller ")
                        .append("OR UPPER(log.callerLine) LIKE :caller) ");

                paramMap.put("caller", "%" + search.getCaller().toUpperCase() + "%");
            }
        } else if (EVENT.equals(search.getLoggingType())) {

            // 이벤트 로그
            where = appendAnd(where);
            where.append(
                    "(SELECT COUNT(1) FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId) > 0 ");

            // 기본 검색어
            if (StringUtils.isNotBlank(search.getKeyword())) {
                where = appendAnd(where);

                where.append("(UPPER(log.formattedMessage) LIKE :keyword ");
                where.append(
                        "OR (SELECT UPPER(mappedValue) FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId AND p.mappedKey = '")
                        .append(MappedKey.EVENT_TYPE.getName()).append("') LIKE :keyword ");
                where.append(
                        "OR (SELECT UPPER(mappedValue) FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId AND p.mappedKey = '")
                        .append(MappedKey.LOGIN_ID.getName()).append("') LIKE :keyword) ");

                paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
            }

            // 이벤트 그룹
            if (StringUtils.isNotBlank(search.getEventGroup())) {
                where.append(
                        "AND (SELECT mappedValue FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId AND p.mappedKey = '")
                        .append(MappedKey.EVENT_GROUP.getName()).append("') = :eventGroup ");

                paramMap.put("eventGroup", search.getEventGroup().toUpperCase());
            }

            // 이벤트 타입
            if (StringUtils.isNotBlank(search.getEventType())) {
                where.append(
                        "AND (SELECT UPPER(mappedValue) FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId AND p.mappedKey = '")
                        .append(MappedKey.EVENT_TYPE.getName()).append("') LIKE :eventType ");

                paramMap.put("eventType", "%" + search.getEventType().toUpperCase() + "%");
            }

            // userId
            if (StringUtils.isNotBlank(search.getLoginId())) {
                where.append(
                        "AND (SELECT UPPER(mappedValue) FROM LOGGING_EVENT_PROPERTY p WHERE log.eventId = p.loggingEvent.eventId AND p.mappedKey = '")
                        .append(MappedKey.LOGIN_ID.getName()).append("') LIKE :loginId ");

                paramMap.put("loginId", "%" + search.getLoginId().toUpperCase() + "%");
            }

        }

        // 조건이 하나라도 있는지 검사
        if (StringUtils.isNotBlank(where)) {
            where = new StringBuilder("WHERE 1=1 ").append(where);
        }
        
        result.put(WHERE, where);
        result.put(PARAMMAP, paramMap);
        
        return result;
    }
    

    /**
     * <pre>
     * CamelCase 로 변경
     * event_id -> eventId
     * </pre>
     */
    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if(i == 0) {
                camelCaseString = part;
            } else {
                camelCaseString = camelCaseString + toProperCase(part);    
            }
        }
        return camelCaseString;
    }

    private String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
