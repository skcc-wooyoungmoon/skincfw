/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.validation.validator;

import java.util.Collection;

import javax.validation.Validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import com.skiaf.core.util.Util;

/**
 * <pre>
 * List형태의 객체의 Validation 처리를 위한 Validator
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Component
public class CollectionValidator implements SmartValidator {

    private SpringValidatorAdapter validator;

    public CollectionValidator() {
        this.validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof Collection) {
            Collection collection = (Collection) target;

            for (Object object : Util.nullToEmpty(collection)) {
                validator.validate(object, errors);
            }
        } else {
            validator.validate(target, errors);
        }

    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {

        if (target instanceof Collection) {
            Collection collection = (Collection) target;

            for (Object object : Util.nullToEmpty(collection)) {
                validator.validate(object, errors, validationHints);
            }
        } else {
            validator.validate(target, errors, validationHints);
        }
    }

}
