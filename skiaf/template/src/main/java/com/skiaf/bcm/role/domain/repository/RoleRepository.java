/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.dto.RoleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 권한 Repository
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface RoleRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * role 저장/수정
     * </pre>
     */
    public Role save(Role role);

    /**
     * <pre>
     * role 정보 조회
     * </pre>
     */
    public Role findByRoleId(String roleId);


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * role 페이징, 검색 목록조회
     * </pre>
     */
    public PageDTO<Role> findQueryBySearch(RoleSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * role 리스트, 검색 목록조회
     * </pre>
     */
    public List<Role> findQueryBySearch(RoleSearchDTO search);

}
