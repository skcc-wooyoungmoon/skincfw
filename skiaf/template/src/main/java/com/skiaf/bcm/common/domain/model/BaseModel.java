/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.common.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.core.util.SessionUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * 모든 모델(엔티티)의 공통
 *
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 9176671936939908250L;

    /** 등록 ID */
    @ApiModelProperty(hidden = true)
    @Column(name = "REG_USER_ID", length = 36, updatable = false)
    protected String createBy;

    /** 수정 ID */
    @ApiModelProperty(hidden = true)
    @Column(name = "UPD_USER_ID", length = 36)
    protected String updateBy;

    /** 생성일자 */
    @ApiModelProperty(hidden = true)
    @Column(name = "REG_DTM", updatable = false)
    protected Date createDate;

    /** 수정일자 */
    @ApiModelProperty(hidden = true)
    @Column(name = "UPD_DTM")
    protected Date updateDate;

    @PrePersist
    public void prePersist() {
        this.createDate = new Date();
        this.updateDate = this.createDate;

        User user = SessionUtil.getLoginUser();
        if (user != null) {
            this.createBy = user.getLoginId();
            this.updateBy = this.createBy;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = new Date();

        User user = SessionUtil.getLoginUser();
        if (user != null) {
            this.updateBy = user.getLoginId();
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
