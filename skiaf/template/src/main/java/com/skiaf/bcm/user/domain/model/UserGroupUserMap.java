/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *
 * BCM 사용자그룹 매핑 Entity
 *
 * History
 * - 2018. 7. 23. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 작성.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_USER_GRP_USER_MAP")
public class UserGroupUserMap extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 3389196111274816424L;

    @EmbeddedId
    private UserGroupUserMapId mapId;

    /** 사용자 */
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    /** 사용자 그룹 */
    @MapsId("userGroupId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_GRP_ID")
    @JsonBackReference
    private UserGroup userGroup;

}
