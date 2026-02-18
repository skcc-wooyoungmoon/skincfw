/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.log.domain.service.dto.LoggingDTO;
import com.skiaf.bcm.log.domain.service.dto.LoggingSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 로그 이벤트 Jpa Extend
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public interface LoggingEventRepositoryJpaExtend {

    public PageDTO<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Pageable pageable);
    
    public List<LoggingDTO> findQueryBySearch(LoggingSearchDTO search, Sort sort);

}
