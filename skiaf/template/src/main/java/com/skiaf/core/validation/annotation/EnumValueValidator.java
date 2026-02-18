/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.validation.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

/**
 * <pre>
 * 
 * History
 * - 2018. 8. 28. | in01876 | 최초작성.
 * </pre>
 */
public class EnumValueValidator implements ConstraintValidator<Enum, String> {
    private Enum annotation;

    @Override
    public void initialize(Enum annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {
        
        if (StringUtils.isBlank(valueForValidation)) {
            return true;        
        }
        
        boolean result = false;
        
        Field[] fields = this.annotation.enumClass().getFields();
        for (Field field : fields) {
            Object enumValue = null;
            try {
                ReflectionUtils.makeAccessible(field);
                Object obj = field.get(null);
                Method method = obj.getClass().getDeclaredMethod("getName");
                enumValue = method.invoke(obj);
            } catch (NoSuchMethodException e) {
                enumValue = field.getName();
            } catch (Exception e) {} 
            
            if(enumValue == null) {
                return false;
            }else {
                if (enumValue.toString().equals(valueForValidation) || (this.annotation.ignoreCase()
                        && enumValue.toString().equalsIgnoreCase(valueForValidation))) {
                    result = true;
                    break;
                }    
            }
        }
        
        return result;
    }
}
