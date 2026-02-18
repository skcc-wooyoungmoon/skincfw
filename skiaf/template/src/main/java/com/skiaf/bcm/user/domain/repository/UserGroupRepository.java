/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.model.UserGroupUserMap;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 사용자그룹 Repository
 * 
 * History
 * - 2018. 7. 19. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * <pre>
     *  사용자그룹 전체 조회
     * </pre>
     */
    public List<UserGroup> findAll();

    /**
     * <pre>
     * 사용자그룹 정보 저장
     * </pre>
     */
    public UserGroup save(UserGroup userGroup);

    /**
     * <pre>
     * 사용자그룹 사용자 매핑 저장
     * </pre>
     */
    public List<UserGroupUserMap> save(List<UserGroupUserMap> userGroupMap);

    /**
     * <pre>
     * 사용자그룹 아이디로 사용자그룹 목록 조회
     * </pre>
     */
    public UserGroup findByUserGroupId(String userGroupId);

    /**
     * <pre>
     * 사용자그룹 삭제
     * </pre>
     */
    public void delete(String groupId);

    /**
     * <pre>
     * 사용자그룹 정보 조회
     * </pre>
     */
    public UserGroup findOne(String groupId);

    /*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 조건에 따른 사용자 그룹 목록 조회
     * </pre>
     */
    public List<UserGroup> findQueryByKeyword(String keyword);

    /**
     * <pre>
     * 조건에 따른 사용자 그룹 목록 조회 페이지
     * </pre>
     */
//    public UserGroupResultDTO findQueryByKeyword(UserGroupSearchDTO search, Pageable pageable);
    public PageDTO<UserGroup> findQueryBySearch(UserGroupSearchDTO search, Pageable pageable);
    
    
    /**
     * <pre>
     * 페이징 없는 사용자 그룹목록 조회
     * </pre>
     */
    public List<UserGroup> findByUseYn(Sort sort, boolean useYn);
}
