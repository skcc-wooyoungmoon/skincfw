/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 *
 */
package com.skiaf.demo.validation;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ScriptAssert;

import com.skiaf.core.constant.MenuOpenType;
import com.skiaf.core.constant.MenuType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;
import com.skiaf.core.validation.annotation.Ip;
import com.skiaf.core.validation.groups.Update;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <pre>
 * 
 * History
 * - 2018. 8. 27. | in01868 | 최초작성.
 * </pre>
 */
@ScriptAssert(lang = "javascript"
    , alias = "_"
    , script = "(_.email != null && _.email.length() > 0 ) || (_.cellNumber != null && _.cellNumber.length() > 0 )"
    , message = "이메일 혹은 휴대폰 둘중 하나는 필수 입니다.")
@Data
public class ValidationModel {

    @ApiModelProperty(required = false, example = "userId")
    @Ids(groups = Update.class)
    @NotBlank(groups = Update.class)
    private String id;

    @ApiModelProperty(required = true, example = "test title")
    @NotBlank(message = "title is blank")
    private String title;

    @ApiModelProperty(required = false, example = "02-987-6543")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 아닙니다.")
    private String phoneNumber;

    @ApiModelProperty(required = false, example = "010-1234-5678")
    private String cellNumber;

    @ApiModelProperty(required = false, example = "ski.in01234@partner.sk.com")
    @Email
    private String email;

    @ApiModelProperty(required = false, example = "127.0.0.1")
    @Ip
    private String ip;
    
    @ApiModelProperty(required = true, example = "PROGRAM")
    @NotBlank
    @Enum(enumClass = MenuType.class, ignoreCase = true)
    private String menuType;
    
    @ApiModelProperty(required = true, example = "C")
    @NotBlank
    @Enum(enumClass = MenuOpenType.class)
    private String menuOpenType; 

    @ApiModelProperty(required = false, example = "30")
    @Digits(integer = 4, fraction = 0)
    @Min(10)
    private int point;

}
