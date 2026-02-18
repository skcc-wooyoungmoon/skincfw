/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.repository.ProgramRepository;


/**
 * <pre>
 * BCM 프로그램 JPA Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface ProgramRepositoryJpa extends ProgramRepository, JpaRepository<Program, String>, ProgramRepositoryJpaExtend {

}
