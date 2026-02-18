/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.user.domain.model.UserGroupUserMap;
import com.skiaf.bcm.user.domain.model.UserGroupUserMapId;
import com.skiaf.bcm.user.domain.repository.UserGroupUserMapRepository;

/**
 * <pre>
 * 
 * BCM 사용자그룹 사용자 매핑 JPA Repository
 * 
 * History
 * - 2018. 7. 18. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupUserMapRepositoryJpa
        extends UserGroupUserMapRepository, JpaRepository<UserGroupUserMap, UserGroupUserMapId> {

    // @Transactional
    // @Modifying
    // @Query("DELETE FROM TB_BCM_USER_GRP_USER_MAP m where m.mapId.userId =
    // :userId")
    // public void deleteAllUserIdInQuery(@Param("userId") String userId);

}
