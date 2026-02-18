/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.bcm.user.domain.service.dto.UserUserGroupMapSaveDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 사용자 관리 Service
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface UserService {

    /**
     * <pre>
     * 조건에 따라 필터가 되는 사용자 목록
     * </pre>
     */
    public PageDTO<User> findQueryByKeyword(UserSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * 로그인 실패 카운트 증가
     * </pre>
     */
    public void incrementLoginFailCountByLoginId(String loginId);

    /**
     * <pre>
     * 로그인 실패 카운트 초기화
     * </pre>
     */
    public void initLoginFailCountByLoginId(String loginId);

    /**
     * <pre>
     * 로그인 아이디로 사용자 조회
     * </pre>
     */
    public User findByLoginId(String loginId);

    /**
     * <pre>
     * 로그인 아이디로 SSO 로그인 사용자 조회
     * </pre>
     */
    public User findByLoginIdAndSsoYnTrue(String loginId);

    /**
     * <pre>
     * 로그인 아이디로 일반로그인 사용자 조회
     * </pre>
     */
    public User findByLoginIdAndSsoYnFalse(String loginId);

    /**
     * <pre>
     * 사용자정보 + 사용자그룹정보 + 사용자 권한정보 조회
     * </pre>
     */
    public UserInfoDTO getUserInfoByUser(User user);

    /**
     * <pre>
     * 사용자 정보 수정
     * </pre>
     */
    public User update(String userId, User user);

    /**
     * <pre>
     * 사용자 정보 등록
     * </pre>
     */
    public User create(User user);

    /**
     * <pre>
     * 사용자 정보 조회
     * </pre>
     */
    public User findOne(String userId);

    /**
     * <pre>
     * 로그인 ID 중복체크
     * </pre>
     */
    public Boolean duplicateCheck(String loginId);
    
    /**
     * <pre>
     * 비밀번호 변경
     * </pre>
     */
    public User passWordChange(String userId, String prePassword, String newPassword, Boolean firstLoginYn);
    
    
    /**
     * <pre>
     * 비밀번호 리셋
     * </pre>
     */
    public User passWordReset(String userId);
    
    
    /**
     * <pre>
     * 사용자 그룹 매핑목록
     * </pre>
     */
    public List<UserGroup> findUserGroupMap(String userId);
    
    /**
     * <pre>
     * 사용자에 사용자 그룹 매핑저장
     * </pre>
     */
    public UserUserGroupMapSaveDTO saveUserGroupMap(String userId, UserUserGroupMapSaveDTO userUserGroupMapSaveDTO);

}
