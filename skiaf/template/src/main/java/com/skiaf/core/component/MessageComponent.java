/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.MessageService;
import com.skiaf.core.config.KeySearchableResourceBundleMessageSource;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * MessageSource에 대한 접근 및 사용을 용이하게 하는 클래스
 *
 * History
 * - 2018. 8. 7. | in01868 | 최초작성.
 * </pre>
 */
@Component("messageComponent")
@Slf4j
public class MessageComponent {

    private static final int LANGUAGE_INDEX_ZERO = 0;
    private static final int LANGUAGE_INDEX_ONE = 1;
    private static final int LANGUAGE_INDEX_TWO = 2;
/* 4번째 언어 추가시, 주석 해제
    private static final int LANGUAGE_INDEX_THREE = 3;
*/

    public static final String JS_MESSAGES_KEY_PATTERN = "__MESSAGE_KEY_PATTERN__";
    public static final String JS_MESSAGES_PROGRAM_IDS = "__MESSAGE_PROGRAM_IDS__";

    /** 기본 언어 코드 */
    private String langDefaultCode;

    /** 사용하는 언어 코드 목록 */
    private List<String> langSupportedCodeList;

    /** 메시지 소스 */
    private MessageSource messageSource;

    private MessageService messageService;

    @Autowired
    public MessageComponent(
            @Value("${bcm.language.default}") String langDefaultCode,
            @Value("#{'${bcm.language.support}'.split(',')}") List<String> langSupportedCodeList,
            @Lazy MessageSource messageSource,
            @Lazy MessageService messageService) {

        this.langDefaultCode = langDefaultCode;
        this.langSupportedCodeList = langSupportedCodeList;
        this.messageSource = messageSource;
        this.messageService = messageService;
    }

    /**
     * 사용중인 언어가 몇번째 인덱스인지 확인
     */
    public int getLanguageIndex() {
        int langIndex = 0;
        if (langSupportedCodeList == null) {
            return langIndex;
        }
        String langCode = LocaleContextHolder.getLocale().toString();
        for (int i = 0, max = langSupportedCodeList.size(); i < max; i++) {
            if (langCode.equals(langSupportedCodeList.get(i))) {
                langIndex = i;
                break;
            }
        }
        return langIndex;
    }

    /**
     * 기본 언어 코드 조회
     * @return eg. "ko"
     */
    public String getDefaultLangCode() {
        return langDefaultCode;
    }

    /**
     * 사용중인 언어 코드 조회
     * @return eg. ["ko","en","zh"]
     */
    public List<String> getSupportedCodeList() {
        return langSupportedCodeList;
    }

    /**
     * 메시지 조회
     * @param code message key
     * @return
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, null, LocaleContextHolder.getLocale());
    }

    /**
     * 메시지 조회
     * @param code message key
     * @param args 메세지의 {0}, {1}, {2} 부분을 대체할 객체들에 대한 array
     * @return
     */
    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, null, LocaleContextHolder.getLocale());
    }

    /**
     * 메시지 조회
     * @param code message key
     * @param args 메세지의 {0}, {1}, {2} 부분을 대체할 객체들에 대한 array
     * @param defaultMessage 메세지 기본값
     * @return
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * <pre>
     * ResourceBundleMessageSource와 DatabaseMessageSource에서 해당되는 메세지들을 검색.
     * </pre>
     *
     * @param keyPatterns ResourceBundleMessageSource에서 검색할 key 패턴에 대한 배열<br>
     *                    eg. "bcm.common.exception.*"
     * @param programIds BCM의 메세지 관리에서 메세지유형이 Program인것 중 검색할 programId.<br>
     *                    하나를 지정하고자 하면 String 타입으로. 복수를 지정하고자 한다면 String[] 타입으로.
     * @return
     */
    public Map<String, String> filterWith(String[] keyPatterns, Object programIds) {
        Map<String, String> result = new HashMap<>();

        // keyPattern에 해당하는 메세지들 담기.
        for (String keyPattern : ArrayUtils.nullToEmpty(keyPatterns)) {
            if (StringUtils.isNotBlank(keyPattern)) {
                KeySearchableResourceBundleMessageSource keyMessageSource = null;

                if (messageSource instanceof KeySearchableResourceBundleMessageSource) {
                    keyMessageSource = (KeySearchableResourceBundleMessageSource) messageSource;
                } else if (messageSource instanceof Advised) {
                    Object messageSourceTarget = null;

                    // MessageSource 는 @Bean으로 proxy된 객체라서.
                    try {
                        messageSourceTarget = ((Advised) messageSource).getTargetSource().getTarget();
                    } catch (Exception e) {
                        if (log.isErrorEnabled()) {
                            log.error(e.getMessage());
                        }
                    }

                    if (messageSourceTarget instanceof HierarchicalMessageSource
                            && ((HierarchicalMessageSource) messageSourceTarget).getParentMessageSource() instanceof KeySearchableResourceBundleMessageSource) {
                        keyMessageSource = (KeySearchableResourceBundleMessageSource) ((HierarchicalMessageSource) messageSourceTarget).getParentMessageSource();
                    }
                }

                if (keyMessageSource != null) {
                    List<String> keys = keyMessageSource.filterKeys(keyPattern);
                    for (String key : keys) {
                        result.put(key, keyMessageSource.getMessage(key, null, LocaleContextHolder.getLocale()));
                    }
                }
            }
        }

        String[] pids = null;
        if (programIds instanceof String) {
            pids = new String[] { (String) programIds };
        } else if (programIds instanceof String[]) {
            pids = (String[]) programIds;
        }

        // 지정한 programId의 메세지들 담기.
        if (pids != null) {
            int currLangIndex = this.getLanguageIndex();
            List<Message> messages = null;
            switch (currLangIndex) {
                case LANGUAGE_INDEX_ZERO:
                    for (String programId : pids) {
                        messages = messageService.findByTarget(programId);
                        messages.forEach((Message msg) -> result.put(msg.getMessageKey(), msg.getMessageName1()));
                    }
                    break;
                case LANGUAGE_INDEX_ONE:
                    for (String programId : pids) {
                        messages = messageService.findByTarget(programId);
                        messages.forEach((Message msg) -> result.put(msg.getMessageKey(), msg.getMessageName2()));
                    }
                    break;
                case LANGUAGE_INDEX_TWO:
                    for (String programId : pids) {
                        messages = messageService.findByTarget(programId);
                        messages.forEach((Message msg) -> result.put(msg.getMessageKey(), msg.getMessageName3()));
                    }
                    break;
/* 4번째 언어 추가시, 주석 해제
                case LANGUAGE_INDEX_THREE:
                    for (String programId : pids) {
                        messages = messageService.findByTarget(programId);
                        messages.forEach((Message msg) -> result.put(msg.getMessageKey(), msg.getMessageName4()));
                    }
                    break;
*/
                default:
                    for (String programId : pids) {
                        messages = messageService.findByTarget(programId);
                        messages.forEach((Message msg) -> result.put(msg.getMessageKey(), msg.getMessageName1()));
                    }
            }
        }

        return result;
    }
}
