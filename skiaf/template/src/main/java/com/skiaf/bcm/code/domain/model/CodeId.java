/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.model;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 ID
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CodeId implements Serializable {

    private static final long serialVersionUID = -3008752536791896023L;

    /** 코드 그룹 아이디 */
    private String codeGroup;

    /** 코드 상세 아이디 */
    private String codeId;

    public CodeId(String codeGroup, String cdDtlId) {
        super();
        this.codeGroup = codeGroup;
        this.codeId = cdDtlId;
    }
}
