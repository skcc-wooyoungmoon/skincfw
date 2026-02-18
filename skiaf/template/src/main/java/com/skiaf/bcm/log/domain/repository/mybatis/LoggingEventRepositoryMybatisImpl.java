/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.repository.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.log.domain.service.dto.LoggingDTO;
import com.skiaf.bcm.log.domain.service.dto.LoggingSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 로그 이벤트 mybatis mapper 구현
 * 
 * History
 * - 2018. 9. 17. | in01869 | 최초작성.
 * </pre>
 */
@Repository
public class LoggingEventRepositoryMybatisImpl implements LoggingEventRepositoryMybatis{

    @Resource
    private LoggingEventRepositoryMybatisMapper loggingEventRepositoryMybatisMapper;

    @Override
    public PageDTO<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Pageable pageable) {
        
        PageDTO<LoggingDTO> result = new PageDTO<>();
        Map<String, Object> searchMap = searchConvertMap(search, pageable);
        
        result.setTotalCount(loggingEventRepositoryMybatisMapper.findTotalCount(searchMap));
        result.setList(loggingEventRepositoryMybatisMapper.findPaginated(searchMap));
        
        return result;
    }

    @Override
    public List<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Sort sort) {
        Map<String, Object> searchMap = searchConvertMap(search, sort);
        
        List<LoggingDTO> list = new ArrayList<>();
        list = loggingEventRepositoryMybatisMapper.findPaginated(searchMap);

        return list;
    }

    private Map<String, Object> searchConvertMap(LoggingSearchDTO search, Object object){
        
        Map<String, Object> searchMap = new HashMap<>();
        
        if(object instanceof Pageable) {
            searchMap.put("pageNumber", ((Pageable) object).getPageNumber());
            searchMap.put("pageSize", ((Pageable) object).getPageSize());
            searchMap.put("sort", ((Pageable) object).getSort());
            
        } else if(object instanceof Sort) {
            searchMap.put("sort", (Sort) object);
        }
        
        searchMap.put("loggingType", search.getLoggingType());
        searchMap.put("eventLogInclude", search.isEventLogInclude());
        
        searchMap.put("keyword", search.getKeyword());
        searchMap.put("logger", search.getLogger());
        searchMap.put("caller", search.getCaller());
        
        searchMap.put("eventGroup", search.getEventGroup());
        searchMap.put("eventType", search.getEventType());
        searchMap.put("loginId", search.getLoginId());
        
        // 기간 시작날짜
        if (StringUtils.isNotBlank(search.getStartDate())) {
            
            String input = search.getStartDate() + " " + search.getStartTime() + ":00";
            Timestamp time = Timestamp.valueOf(input);

            searchMap.put("startTimestamp", time.getTime());
        }

        // 기간 종료날짜
        if (StringUtils.isNotBlank(search.getEndDate())) {
            String input = search.getEndDate() + " " + search.getEndTime() + ":59";
            Timestamp time = Timestamp.valueOf(input);

            searchMap.put("endTimestamp", time.getTime());
        }
        
        // 로그 레벨 - LEVEL_STRING
        if (StringUtils.isNotBlank(search.getLevels())) {

            List<String> levels = new ArrayList<>();
            String[] arr = search.getLevels().split(",");
            for (int i = 0, len = arr.length; i < len; i++) {
                levels.add(arr[i]);
            }
            searchMap.put("levels", levels);
        }
        
        return searchMap;
    }

}
