/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository;

import java.util.List;

import com.skiaf.bcm.user.domain.model.UserGroupUserMap;

/**
 * <pre>
 * 
 * BCM 사용자그룹 사용자 매핑 Repository
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupUserMapRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | JPA - default Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 사용자 아이디로 사용자그룹 사용자 매핑 목록 조회
     * </pre>
     */
    public List<UserGroupUserMap> findByMapIdUserId(String userId);

    /**
     * <pre>
     * 사용자그룹 아이디로 사용자그룹 사용자 매핑 목록 조회
     * </pre>
     */
    public List<UserGroupUserMap> findByMapIdUserGroupId(String userGroupId);

    /**
     * <pre>
     * 사용자그룹 사용자 매핑 다중 저장
     * </pre>
     */
    public <S extends UserGroupUserMap> List<S> save(Iterable<S> userGroupUserMapList);

    /**
     * <pre>
     * 사용자그룹 사용자 매핑 다중 삭제
     * </pre>
     */
    public void deleteInBatch(Iterable<UserGroupUserMap> userGroupUserMapList);

}
