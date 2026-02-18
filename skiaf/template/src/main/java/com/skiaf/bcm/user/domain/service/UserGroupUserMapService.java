/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.List;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;

/**
 * <pre>
 * 
 * BCM 사용자그룹 매핑 Service
 * 
 * History
 * - 2018. 7. 23. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupUserMapService {

    /**
     * <pre>
     * 사용자 아이디로 사용자그룹 목록 조회
     * </pre>
     */
    public List<UserGroup> findByMapIdUserId(String userId);

    /**
     * <pre>
     * 사용자그룹 아이디로 매핑된 사용자 목록 조회
     * </pre>
     */
    public List<User> findByMapIdUserGroupId(String userGroupId);

    /**
     * <pre>
     * 사용자 그룹에 사용자 매핑
     * </pre>
     */
    public List<User> saveUsersByUserGroupId(String userGroupId, String[] userIdList);

    /**
     * <pre>
     * 사용자그룹에 매핑된 사용자 삭제
     * </pre>
     */
    public Boolean deleteUsersByUserGroupId(String userGroupId, String[] userIdList);
    
    /**
     * <pre>
     * 사용자에 사용자그룹 매핑
     * </pre>
     */
    public List<UserGroup> saveUserGroupsByUserId(String userId, String[] userGroupIdList);
    
    /**
     * <pre>
     * 사용자 아이디로 매핑된 사용자 그룹 삭제
     * </pre>
     */
    public boolean deleteUserGroupsByUserId(String userId, String[] userGroupIdList);

}
