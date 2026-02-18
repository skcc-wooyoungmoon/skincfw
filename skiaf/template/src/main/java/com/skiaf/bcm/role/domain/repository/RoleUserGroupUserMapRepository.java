/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository;

import java.util.List;

import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;

/**
 * <pre>
 * BCM 권한 <=> 사용자, 사용자그룹 매핑 Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface RoleUserGroupUserMapRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | JPA - default Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * roleId 로 Role List 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> findByRoleRoleId(String roleId);

    /**
     * <pre>
     * userGrpId 로 Role List 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> findByUserGroupUserGroupId(String userGroupId);

    /**
     * <pre>
     * userId 로 Role List 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> findByUserUserId(String userId);

    /**
     * <pre>
     * userGroupId List 로 Role 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> findByUserGroupUserGroupIdIn(List<String> userGroupIdList);

    /**
     * <pre>
     * userId & userGroupId List 로 Role 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> findByUserUserIdOrUserGroupUserGroupIdIn(String userId,
            List<String> userGroupIdList);

    /**
     * <pre>
     * role 에 user,userGroup 다중 매핑
     * </pre>
     */
    public <S extends RoleUserGroupUserMap> List<S> save(Iterable<S> roleUserGroupUserMapList);

    /**
     * <pre>
     * role 에 user,userGroup 다중 매핑 제거
     * </pre>
     */
    public void deleteInBatch(Iterable<RoleUserGroupUserMap> roleUserGroupUserMapList);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Custom Method 
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * <pre>
     * userId, userGorupId, betweenDate 로 활성화 권한 목록 조회
     * </pre>
     */
    public List<RoleUserGroupUserMap> activationRoleMap(String userId, List<String> userGroupIds, String betweenDate);

}
