/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 사용자 Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
public interface UserRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 사용자 정보 저장
     * </pre>
     */
    public User save(User user);

    /**
     * <pre>
     * userId 로 사용자 정보 조회
     * </pre>
     */
    public User findByUserId(String userId);

    /**
     * <pre>
     * loginId로 사용자 정보 조회
     * </pre>
     */
    public User findByLoginId(String loginId);

    /**
     * <pre>
     * loginId로 사용여부 true 인 사용자 정보 조회
     * </pre>
     */
    public User findByLoginIdAndSsoYnTrue(String loginId);

    /**
     * <pre>
     * loginId로 사용여부 false 인 사용자 정보 조회
     * </pre>
     */
    public User findByLoginIdAndSsoYnFalse(String loginId);

    /**
     * <pre>
     * ID(loginId)로 사용자 정보 조회
     * </pre>
     */
    public User findOne(String userId);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * ID(loginId)로 사용자 정보 조회
     * </pre>
     */
    public PageDTO<User> findQueryByKeyword(UserSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * ID(loginId)의 로그인 실패횟수 증가
     * </pre>
     */
    public void incrementLoginFailCountByLoginId(String loginId);

    /**
     * <pre>
     * ID(loginId)의 로그인 실패횟수 초기화
     * </pre>
     */
    public void initLoginFailCountByLoginId(String loginId);

}
