/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 검색 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class CodeSearchDTO implements Serializable {

    private static final long serialVersionUID = -8663212786455753116L;

    /** 기본검색 */
    @Length(max = 20)
    private String keyword;

    /** 코드명, 코드ID 포함 검색 */
    private boolean isCodeInclude = false;

    /** 미사용 포함여부 */
    private boolean isUnusedInclude = false;
    
    /** 페이징 사용여부 */
    private boolean isPaging = false;

    public boolean isCodeInclude() {
        return isCodeInclude;
    }

    public void setCodeInclude(boolean isCodeInclude) {
        this.isCodeInclude = isCodeInclude;
    }

    public boolean isUnusedInclude() {
        return isUnusedInclude;
    }

    public void setIsUnusedInclude(boolean isUnusedInclude) {
        this.isUnusedInclude = isUnusedInclude;
    }
    
    public boolean isPaging() {
        return isPaging;
    }
    
    public void setIsPaging(boolean isPaging) {
        this.isPaging = isPaging;
    }

}
