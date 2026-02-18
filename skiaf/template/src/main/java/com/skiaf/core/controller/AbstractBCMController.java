/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.skiaf.core.validation.validator.CollectionValidator;

/**
 * <pre>
 * BCM 컨트롤러 베이스
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public abstract class AbstractBCMController {

    /**
     * <pre>
     * "@Validated"의 validator 설정
     * </pre>
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new CollectionValidator());
    }
}
