/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.repository.UserRepository;

/**
 * <pre>
 * BCM 사용자 JPA Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
public interface UserRepositoryJpa extends UserRepository, JpaRepository<User, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_BCM_USER SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT +1 WHERE LOGIN_ID = :loginId", nativeQuery = true)
    public void incrementLoginFailCountByLoginId(@Param("loginId") String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_BCM_USER SET LOGIN_FAIL_CNT = 0 WHERE LOGIN_ID = :loginId", nativeQuery = true)
    public void initLoginFailCountByLoginId(@Param("loginId") String userId);

}
