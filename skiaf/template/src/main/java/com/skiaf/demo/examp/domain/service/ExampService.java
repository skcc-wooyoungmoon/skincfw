/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.examp.domain.service;

import com.skiaf.demo.examp.domain.service.dto.ExampFormatDTO;

/**
 * <pre>
 * interface 전문 테스트 서비스
 *
 * History
 * - 2018. 8. 2. | in01943 | 최초작성.
 * </pre>
 */
public interface ExampService {

    /**
     * <pre>
     * XML config 파일 조회
     * </pre>
     */
    public ExampFormatDTO readConfigFile(ExampFormatDTO dto);

    /**
     * <pre>
     * 전문 변환
     * </pre>
     */
    public ExampFormatDTO convertData(ExampFormatDTO dto);

}
