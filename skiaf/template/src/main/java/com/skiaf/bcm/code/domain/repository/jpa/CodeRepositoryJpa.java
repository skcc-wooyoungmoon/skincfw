/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.model.CodeId;
import com.skiaf.bcm.code.domain.repository.CodeRepository;

/**
 * <pre>
 * BCM 코드 JPA Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeRepositoryJpa extends CodeRepository, JpaRepository<Code, CodeId>, CodeRepositoryJpaExtend {

}
