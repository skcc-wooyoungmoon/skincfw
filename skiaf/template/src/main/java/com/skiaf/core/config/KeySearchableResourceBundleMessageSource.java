/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * <pre>
 * ResourceBundleMessageSource 에 key 검색기능이 추가된 MessageSource
 * </pre>
 */
public class KeySearchableResourceBundleMessageSource extends ResourceBundleMessageSource {

    /**
     * <pre>
     * keyPattern 에 해당하는 key들을 리턴.
     * 검색 대상은 이 ResourceBundleMessageSource 객체내에 한함. 
     * parent MessageSource나 commonMessage는 검색하지 않음.
     * </pre>
     * 
     * @param keyPattern
     *            "bcm.common.exception.*" 형태의 message key에 대한 검색 패턴
     * @return 검색된 key목록
     */
    public List<String> filterKeys(String keyPattern) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isBlank(keyPattern)) {
            return result;
        }
        Set<String> basenameSet = this.getBasenameSet();
        for (String basename : basenameSet) {
            ResourceBundle bundle = this.getResourceBundle(basename, LocaleContextHolder.getLocale()); 
            for (Enumeration<String> keys = bundle.getKeys(); keys.hasMoreElements();) {
                String key = keys.nextElement();
                if (key.matches(keyPattern)) {
                    result.add(key);
                }
            }
        }
        return result;
    }
}
