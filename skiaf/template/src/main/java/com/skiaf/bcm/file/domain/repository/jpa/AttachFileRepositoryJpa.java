/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.repository.AttachFileRepository;

/**
 * <pre>
 *
 * History
 * - 2018. 09. 11. | in01866 | 최초작성.
 * </pre>
 */
public interface AttachFileRepositoryJpa extends AttachFileRepository, JpaRepository<AttachFile, String>{

}
