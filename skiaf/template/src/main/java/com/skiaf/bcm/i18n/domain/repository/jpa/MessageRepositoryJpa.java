/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.repository.MessageRepository;

/**
 * <pre>
 * BCM 메시지 JPA Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Repository
public interface MessageRepositoryJpa extends MessageRepository, JpaRepository<Message, String> {

}
