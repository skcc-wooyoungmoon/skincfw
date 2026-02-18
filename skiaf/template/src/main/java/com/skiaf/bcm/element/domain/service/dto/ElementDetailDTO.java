/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.validation.annotation.Ids;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 프로그램요소 상세보기 DTO
 * 
 * History
 * - 2018. 8. 13. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ElementDetailDTO implements Serializable {

    private static final long serialVersionUID = -6539890710997315319L;

    @NotBlank
    @Ids
    private String programId;

    @NotBlank
    @Length(max = 128)
    private String elementKey;

    @Length(max = 2000)
    private String elementDesc;

    private boolean useYn = true;

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }
}
