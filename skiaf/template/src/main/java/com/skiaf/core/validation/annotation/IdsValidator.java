/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * @Valdation - @Ids의 validator
 * Ids Validation 체크
 * 
 * History
 * - 2018. 9. 6. | in01868 | 최초작성.
 * </pre>
 */
public class IdsValidator implements ConstraintValidator<Ids, String> {
    
    @SuppressWarnings("unused")
    private Ids ids;

    @Override
    public void initialize(Ids ids) {
        this.ids = ids;
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        if (StringUtils.isBlank(field))
            return true;
        
        // 영어, 숫자, 특수문자('.','-','_') 허용
        return field.matches("^[0-9a-zA-Z._-]*$");
    }
}
