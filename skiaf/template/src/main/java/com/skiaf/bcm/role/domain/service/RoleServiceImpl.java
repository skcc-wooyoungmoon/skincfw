/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.repository.RoleRepository;
import com.skiaf.bcm.role.domain.service.dto.RoleSearchDTO;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 권한 관리 ServiceImpl
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public PageDTO<Role> findQueryByKeyword(RoleSearchDTO search, Pageable pageable) {
        PageDTO<Role> result = roleRepository.findQueryBySearch(search, pageable);
        result.setPage(pageable);
        return result;
    }

    @Override
    public List<Role> findQueryByKeyword(RoleSearchDTO search) {

        return roleRepository.findQueryBySearch(search);
    }

    @Override
    public Role create(Role role) {
        // 1. one more duplicate check
        if (this.duplicateCheck(role.getRoleId())) {
            throw ValidationException.withUserMessageKey("bcm.common.DUPLICATE")
                    .withSystemMessage("this.duplicateCheck(role.getRoleId())").build();
        }
        // 3. save
        Role createRole = roleRepository.save(role);
        // 4. result return
        return createRole;
    }

    @Override
    public Role findOne(String roleId) {

        Role role = roleRepository.findByRoleId(roleId);
        if (role == null) {
            throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND").withSystemMessage("role == null").build();
        }
        return role;
    }

    @Override
    public Role update(String roleId, Role role) {

        // 1. RoleEntity select
        Role updateRole = roleRepository.findByRoleId(roleId);

        // 2. null check
        if (updateRole == null) {
            throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND").withSystemMessage("updateRole == null").build();
        }

        // 3. update data put
        updateRole.setRoleName(role.getRoleName());
        updateRole.setRoleDesc(role.getRoleDesc());
        updateRole.setUseYn(role.isUseYn());

        // 4. managed Entity save
        roleRepository.save(updateRole);

        // 5. 성공 결과 return
        return updateRole;
    }

    @Override
    public Role delete(String roleId) {

        Role role = roleRepository.findByRoleId(roleId);
        role.setUseYn(false);
        roleRepository.save(role);
        return role;
    }

    @Override
    public Boolean duplicateCheck(String roleId) {
        Boolean isDuplicate = false;
        Role role = roleRepository.findByRoleId(roleId);
        if (role != null) {
            isDuplicate = true;
        }
        return isDuplicate;
    }

}
