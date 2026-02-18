/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 사용자 JPA Repository Extend
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
public interface UserRepositoryJpaExtend {

    public PageDTO<User> findQueryByKeyword(UserSearchDTO search, Pageable pageable);

}
