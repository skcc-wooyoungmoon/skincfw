/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.repository.CodeGroupRepository;

/**
 * <pre>
 * BCM 코드 그룹 JPA Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeGroupRepositoryJpa extends CodeGroupRepository, JpaRepository<CodeGroup, String>, CodeGroupRepositoryJpaExtend {

}
