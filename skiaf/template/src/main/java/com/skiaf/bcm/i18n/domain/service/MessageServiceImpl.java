/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.repository.MessageRepository;
import com.skiaf.bcm.i18n.domain.service.dto.MessageSearchDTO;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.service.AbstractBCMService;
import com.skiaf.core.vo.PageDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCM 메시지 Service Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * - 2018. 9. 28. | in01868 | lucyXssFilter 적용.
 * </pre>
 */
@Service
@Transactional
@Slf4j
public class MessageServiceImpl extends AbstractBCMService implements MessageService {

    private static final String USER_MESSAGE_KEY_EMPTY = "bcm.common.EMPTY";
    private static final String USER_MESSAGE_KEY_DUPLICATE = "bcm.common.DUPLICATE";

    private static final String MESSAGE_CACHE_NAME = "bcm.message";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private LucyXssFilter lucyXssFilter;

    @Override
    public Message findOne(String messageKey) {
        return messageRepository.findOne(messageKey);
    }

    @Cacheable(cacheNames = MESSAGE_CACHE_NAME, key = "#messageKey")  // 'message'라는 명칭의 캐시를 사용하게 함. 개별 캐시의 기준은 parameter인 messageKey값.
    @Override
    public Message findOneCached(String messageKey) {
        log.info("findOneCached, messageKey={}", messageKey);
        return messageRepository.findOne(messageKey);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @CachePut(cacheNames = MESSAGE_CACHE_NAME, key = "#message.messageKey") // 'message' 캐시 내의 해당 항목을 반영함.
    @Override
    public Message create(Message message) {
        if (message == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                                        .withSystemMessage("message == null").build();
        }

        Message exsistMessage = this.findOne(message.getMessageKey());
        if (exsistMessage != null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                                        .withSystemMessage("exsistMessage != null").build();
        }

        // lucyXssFilter 적용
        message.setMessageName1(lucyXssFilter.doFilter(message.getMessageName1()));
        message.setMessageName2(lucyXssFilter.doFilter(message.getMessageName2()));
        message.setMessageName3(lucyXssFilter.doFilter(message.getMessageName3()));
/* 4번째 언어 추가시, 주석 해제
        message.setMessageName4(lucyXssFilter.doFilter(message.getMessageName4()));
*/
        message.setMessageDesc(lucyXssFilter.doFilter(message.getMessageDesc()));

        return messageRepository.save(message);
    }

    @CachePut(cacheNames = MESSAGE_CACHE_NAME, key = "#message.messageKey")  // 'message' 캐시 내의 해당 항목을 반영함.
    @Override
    public Message update(String messageKey, Message message) {
        if (StringUtils.isEmpty(messageKey)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                                        .withSystemMessage("messageKey isEmpty").build();
        }

        Message exsistMessage = this.findOne(messageKey);
        if (exsistMessage == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                                        .withSystemMessage("exsistMessage == null").build();
        }

        message.setMessageKey(messageKey);

        // lucyXssFilter 적용
        message.setMessageName1(lucyXssFilter.doFilter(message.getMessageName1()));
        message.setMessageName2(lucyXssFilter.doFilter(message.getMessageName2()));
        message.setMessageName3(lucyXssFilter.doFilter(message.getMessageName3()));
/* 4번째 언어 추가시, 주석 해제
        message.setMessageName4(lucyXssFilter.doFilter(message.getMessageName4()));
*/
        message.setMessageDesc(lucyXssFilter.doFilter(message.getMessageDesc()));

        return messageRepository.save(message);
    }

    @CacheEvict(cacheNames = MESSAGE_CACHE_NAME, key = "#messageKey") // 'message' 캐시 내의 해당 항목을 제거함.
    @Override
    public void delete(String messageKey) {
        Message message = this.findOne(messageKey);
        if (message != null) {
            messageRepository.delete(message);
        }
    }

    @CacheEvict(cacheNames = MESSAGE_CACHE_NAME, allEntries = true)   // 'message' 캐시 내의 모든 항목을 제거함.
    @Override
    public void deleteCacheAll() {
        log.info("deleteCacheAll");
    }

    @Override
    public List<Message> findByTarget(String target) {
        return messageRepository.findByTarget(target);
    }

    @Override
    public PageDTO<Message> findQueryBySearch(MessageSearchDTO search, Pageable pageable) {
        return messageRepository.findQueryBySearch(search, pageable);
    }

}
