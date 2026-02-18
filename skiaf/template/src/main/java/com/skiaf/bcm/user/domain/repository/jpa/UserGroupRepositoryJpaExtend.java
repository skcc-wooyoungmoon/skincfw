/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 사용자그룹 JPA Custom Repository
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupRepositoryJpaExtend {

    public List<UserGroup> findQueryByKeyword(String keyword);

    public PageDTO<UserGroup> findQueryBySearch(UserGroupSearchDTO search, Pageable pageable);

}
