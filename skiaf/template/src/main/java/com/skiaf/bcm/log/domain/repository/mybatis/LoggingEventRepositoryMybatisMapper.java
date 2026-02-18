/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.skiaf.bcm.log.domain.service.dto.LoggingDTO;

/**
 * <pre>
 * BCM 로그보기
 * mybatis mapper interface
 * 
 * History
 * - 2018. 9. 18. | in01869 | 최초작성.
 * </pre>
 */
@Mapper
public interface LoggingEventRepositoryMybatisMapper{
    
    /**
     * <pre>
     * 로그 목록 갯수
     * </pre>
     */
    public int findTotalCount(@Param("search") Map<String, Object> search);

    /**
     * <pre>
     * 로그 페이징 목록
     * </pre>
     */
    public List<LoggingDTO> findPaginated(@Param("search") Map<String, Object> search);
    
    /**
     * <pre>
     * 로그 목록 (페이징 제외)
     * </pre>
     */
    public List<LoggingDTO> findBySearch(@Param("search") Map<String, Object> search);
}
