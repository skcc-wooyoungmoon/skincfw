/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.element.domain.repository.ElementRepository;

/**
 * <pre>
 *
 * BCM 프로그램요소 JPA Repository
 *
 * History
 * - 2018. 7. 25. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface ElementRepositoryJpa extends ElementRepository, JpaRepository<Element, Long> {

}
