/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.dto.RoleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 권한 관리 Service
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface RoleService {

    /**
     * <pre>
     * 조건에 따라 필터가 되는 권한 목록
     * </pre>
     */
    public PageDTO<Role> findQueryByKeyword(RoleSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * 권한 목록 조회
     * </pre>
     */
    public List<Role> findQueryByKeyword(RoleSearchDTO search);

    /**
     * <pre>
     * 권한 등록
     * </pre>
     */
    public Role create(Role role);

    /**
     * <pre>
     * 권한 조회
     * </pre>
     */
    public Role findOne(String roleId);

    /**
     * <pre>
     * 권한 수정
     * </pre>
     */
    public Role update(String roleId, Role role);

    /**
     * <pre>
     * 권한 삭제
     * </pre>
     */
    public Role delete(String roleId);

    /**
     * <pre>
     * 권한 ID 중복체크
     * </pre>
     */
    public Boolean duplicateCheck(String id);
}
