/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * <pre>
 * @Valdation - @Ids 설정
 * 
 * History
 * - 2018. 9. 6. | in01868 | 최초작성.
 * </pre>
 */
@Documented
@Constraint(validatedBy = IdsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ids {
    String message() default "bcm.validation.ids";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
