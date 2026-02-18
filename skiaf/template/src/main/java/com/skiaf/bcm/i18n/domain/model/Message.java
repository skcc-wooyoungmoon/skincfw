/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 메시지 Entity
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "TB_BCM_MSG")
public class Message extends BaseModelUseYnSupport {

    private static final long serialVersionUID = -809692337424007898L;

    /** 메시지 키 */
    @Id
    @NotBlank
    @Column(name="MSG_KEY", nullable = false)
    private String messageKey;

    /** 메시지 명1 */
    @Length(max = 2000)
    @Column(name="MSG_NM1", length = 2000)
    private String messageName1;

    /** 메시지 명2 */
    @Length(max = 2000)
    @Column(name="MSG_NM2", length = 2000)
    private String messageName2;

    /** 메시지 명3 */
    @Length(max = 2000)
    @Column(name="MSG_NM3", length = 2000)
    private String messageName3;

    /** 메시지 명4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 2000)
    @Column(name="MSG_NM4", length = 2000)
    private String messageName4;
*/

    /** 메시지 설명 */
    @Length(max = 2000)
    @Column(name="MSG_DESC", length = 2000)
    private String messageDesc;

    /**
     * 사용처 (프로그램 : {PROG_ID}, 공통 : COMMON, 시스템 : SYSTEM)
     */
    @NotBlank
    @Length(max = 128)
    @Column(name="TARGET", length = 128, nullable = false)
    private String target;

}
