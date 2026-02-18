/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.skiaf.bcm.code.domain.model.Code;

/**
 * <pre>
 * BCM 코드 JPA Repository Extend
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeRepositoryJpaExtend {

    public List<Code> findQueryByCodeGroupId(String codeGroupId, Sort sort);
}
