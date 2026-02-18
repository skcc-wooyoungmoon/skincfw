/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.dto.MessageSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 메시지 Service
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface MessageService {

    /**
     * <pre>
     * 메시지 조회
     * </pre>
     */
    public Message findOne(String messageKey);
    
    /**
     * <pre>
     * 메시지 조회 (Cachable)
     * </pre>
     */
    public Message findOneCached(String messageKey);    

    /**
     * <pre>
     * 메시지 목록 조회
     * </pre>
     */
    public List<Message> findAll();

    /**
     * <pre>
     * 메시지 저장
     * </pre>
     */
    public Message create(Message message);

    /**
     * <pre>
     * 메시지 수정
     * </pre>
     */
    public Message update(String messageKey, Message message);

    /**
     * <pre>
     * 메시지 삭제
     * </pre>
     */
    public void delete(String msgKey);
    
    /**
     * <pre>
     * 메시지 캐시 전체를 삭제함.
     * </pre>
     */
    public void deleteCacheAll();

    /**
     * <pre>
     * 사용처가 일치하는 메시지 목록 조회
     * </pre>
     */
    public List<Message> findByTarget(String target);

    /**
     * <pre>
     * 메시지 페이징, 검색 목록 조회
     * </pre>
     */
    public PageDTO<Message> findQueryBySearch(MessageSearchDTO search, Pageable pageable);

}
