/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.core.validation.annotation.Ids;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *
 * BCM 사용자그룹 Entity
 *
 * History
 * - 2018. 7. 18. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_USER_GRP")
public class UserGroup extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 2261155874943004708L;

    /** 그룹 아이디 */
    @ApiModelProperty(required = true, example = "GRP_DEV3")
    @Id
    @NotBlank
    @Ids
    @Column(name = "USER_GRP_ID", nullable = false, unique = true)
    private String userGroupId;

    /** 그룹 이름 */
    @ApiModelProperty(required = true, example = "석유정제")
    @NotBlank
    @Length(max = 128)
    @Column(name = "USER_GRP_NM", length = 128, nullable = false)
    private String userGroupName;

    /** 회사 코드 */
    @ApiModelProperty(required = false, example = "H20")
    @Length(max = 20)
    @Column(name = "COMP_CD", length = 20)
    private String companyCode;

    /** 그룹 설명 */
    @ApiModelProperty(required = false, example = "석유정제 서울지역 그룹")
    @Length(max = 2000)
    @Column(name = "USER_GRP_DESC", length = 2000)
    private String userGroupDesc;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserGroupUserMap> userGroupUserList;

    /** 회사 명 */
    @Transient
    private String companyName;

    @Transient
    private int userCount;

    /** Role 매핑 정보 */
    @Transient
    private Long roleMapId;

    @Transient
    private Boolean roleUseYn;

    @Transient
    private String roleBeginDt;

    @Transient
    private String roleEndDt;

    /** User 매핑 정보 */
    @Transient
    private Date userMapDt;

}
