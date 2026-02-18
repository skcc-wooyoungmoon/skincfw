/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.dto.MessageSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 메시지 Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface MessageRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 메시지 조회
     * </pre>
     */
    Message findOne(String messageKey);

    /**
     * <pre>
     * 메시지 저장/수정
     * </pre>
     */
    Message save(Message message);

    /**
     * <pre>
     * 메시지 삭제
     * </pre>
     */
    void delete(Message message);

    /**
     * <pre>
     * 메시지 전체 조회
     * </pre>
     */
    List<Message> findAll();

    /**
     * <pre>
     * 사용처가 일치하는 메시지 목록 조회
     * </pre>
     */
    List<Message> findByTarget(String target);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 메시지 페이징, 검색 목록 조회
     * </pre>
     */
    public PageDTO<Message> findQueryBySearch(MessageSearchDTO search, Pageable pageable);

}
