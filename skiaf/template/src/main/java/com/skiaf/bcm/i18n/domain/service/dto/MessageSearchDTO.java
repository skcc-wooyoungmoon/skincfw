/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 메시지 검색 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class MessageSearchDTO implements Serializable {
    
    private static final long serialVersionUID = -5854560420605930075L;

    /** 기본검색 */
    @Length(max = 20)
    private String keyword;

    /** 미사용 포함여부 */
    private boolean isUnusedInclude = false;

    public boolean isUnusedInclude() {
        return isUnusedInclude;
    }

    public void setIsUnusedInclude(boolean isUnusedInclude) {
        this.isUnusedInclude = isUnusedInclude;
    }

}
