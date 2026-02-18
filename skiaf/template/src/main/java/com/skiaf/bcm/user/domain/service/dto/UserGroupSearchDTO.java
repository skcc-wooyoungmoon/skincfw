/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 사용자그룹 검색 DTO
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class UserGroupSearchDTO implements Serializable {

    private static final long serialVersionUID = -2491668489258367398L;

    @Length(max = 20)
    private String keyword;
    
    private String groupId;

    private String groupName;
    
    @Length(max = 20)
    private String companyCode;

    /** 미사용 포함여부 */
    private boolean isUnusedInclude = false;

    public boolean isUnusedInclude() {
        return isUnusedInclude;
    }

    public void setIsUnusedInclude(boolean isUnusedInclude) {
        this.isUnusedInclude = isUnusedInclude;
    }
}
