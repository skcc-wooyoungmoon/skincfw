/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.AbstractMessageSource;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * database로부터 message를 조회해 오는 MessageSource
 *
 * History
 * - 2018. 8. 7. | in01865 | 최초작성.
 * </pre>
 */
@Slf4j
public class DatabaseMessageSource extends AbstractMessageSource {

    private static final int LANGUAGE_INDEX_ZERO = 0;
    private static final int LANGUAGE_INDEX_ONE = 1;
    private static final int LANGUAGE_INDEX_TWO = 2;
/* 4번째 언어 추가시, 주석 해제
    private static final int LANGUAGE_INDEX_THREE = 3;
*/

    /** 언어 코드 */
    private String[] langSupportedCodes;

    /** 메시지 서비스 */
    private MessageService messageService;

    public DatabaseMessageSource(MessageService messageService, String[] activeLangCode) {
        this.messageService = messageService;
        this.langSupportedCodes = activeLangCode;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        int langIndex = ArrayUtils.indexOf(langSupportedCodes, locale.getLanguage());

        Message message = messageService.findOneCached(code);

        String msg = null;
        if (message != null && message.isUseYn()) {
            switch (langIndex) {
                case LANGUAGE_INDEX_ZERO: msg = message.getMessageName1();  break;
                case LANGUAGE_INDEX_ONE:  msg = message.getMessageName2();  break;
                case LANGUAGE_INDEX_TWO:  msg = message.getMessageName3();  break;
/* 4번째 언어 추가시, 주석 해제
                case LANGUAGE_INDEX_THREE:  msg = message.getMessageName4();  break;
*/
                default:
                    // 지원되지 않는 lang이 요구되었으며, 이 경우 메세지는 null 리턴.
                    log.warn("no supported lang, lang={}, langSupportedCodes={}", locale.getLanguage(), langSupportedCodes);
            }
        }

        MessageFormat result = null;
        if (StringUtils.isNotBlank(msg)) {
            result = super.createMessageFormat(msg, locale);
        }
        return result;
    }
}
