/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 사용자그룹 <=> 사용자 매핑 아이디 Entity
 * 
 * History
 * - 2018. 7. 23. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 작성.
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class UserGroupUserMapId implements Serializable {

    private static final long serialVersionUID = -3316465822438276597L;

    /** 사용자 */
    @ApiModelProperty(required = true, example = "sunghyun-cuid")
    private String userId;

    /** 사용자 그룹 */
    @ApiModelProperty(required = true, example = "GRP_DEV1")
    private String userGroupId;

    public UserGroupUserMapId() {

    }

    public UserGroupUserMapId(String userId, String userGroupId) {
        this.userId = userId;
        this.userGroupId = userGroupId;
    }

}
