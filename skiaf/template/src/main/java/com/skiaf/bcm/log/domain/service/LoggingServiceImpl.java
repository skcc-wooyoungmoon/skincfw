/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.log.domain.repository.LoggingEventRepository;
import com.skiaf.bcm.log.domain.service.dto.LoggingDTO;
import com.skiaf.bcm.log.domain.service.dto.LoggingSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 *  BCM 로그 보기 ServiceImpl
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Service
public class LoggingServiceImpl implements LoggingService {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Autowired
    LoggingEventRepository loggingEventRepository;

    @Override
    public PageDTO<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Pageable pageable) {
        PageDTO<LoggingDTO> result = loggingEventRepository.findQueryBySearch(search, pageable);
        result.setPage(pageable);
        
        return result;
    }

    @Override
    public List<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Sort sort) {
        return loggingEventRepository.findQueryBySearch(search, sort);
    }
}
