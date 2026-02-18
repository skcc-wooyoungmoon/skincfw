/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.service;

import java.util.List;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.role.domain.model.ProgramRoleMap;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;
import com.skiaf.bcm.role.domain.service.dto.ProgramRoleMapDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;

/**
 * <pre>
 * BCM 권한 매핑 Service
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface RoleMapService {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 사용자
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * USER_ID로 권한 목록 조회
     * </pre>
     */
    public List<Role> findRoleMapByUserId(String userId);

    /**
     * <pre>
     * USER_GROUP_ID 목록으로 권한 조회
     * </pre>
     */
    public List<Role> findRoleMapByUserGroupList(List<String> userGroupIdList);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 사용자 그룹
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * USER_GROUP_ID로 권한 목록 조회
     * </pre>
     */
    public List<Role> findRoleMapByUserGroupId(String userGroupId);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 메뉴
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * MENU_ID로 권한 목록 조회
     * </pre>
     */
    public List<Role> findRoleMapByMenuId(String menuId);

    /**
     * <pre>
     * MENU_ID에 다중 ROLE 등록
     * </pre>
     */
    public List<Role> saveRolesByMenuId(String menuId, String[] roleId);

    /**
     * <pre>
     * MENU_ID에 다중 ROLE 삭제
     * </pre>
     */
    public Boolean deleteRolesByMenuId(String menuId, String[] roleId);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 프로그램
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * PROGRAM_ID로 권한 목록 조회
     * </pre>
     */
    public List<Role> findRoleMapByProgramId(String programId);

    /**
     * <pre>
     *  PROGRAM_ID에 다중 ROLE 매핑(등록/수정)
     * </pre>
     */
    public List<ProgramRoleMap> saveRolesByProgramId(List<ProgramRoleMapDTO> programRoleMapDTOList);

    /**
     * <pre>
     * PROGRAM_ID에 다중 ROLE 매핑 제거
     * </pre>
     */
    public Boolean deleteRolesByProgramId(List<ProgramRoleMapDTO> programRoleMapDTOList);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 프로그램 요소
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * ELEMENT_SEQ(ID)에 다중 ROLE 매핑(등록/수정)
     * </pre>
     */
//    public List<Element> saveElementSeqByRoleId(String roleId, int[] elementSeq);
    public List<Element> saveElementsByMapIdRoleId(List<ElementRoleMapDTO> elementRoleMapDTO);

    /**
     * <pre>
     * ELEMENT_SEQ에 다중 ROLE 매핑 제거
     * </pre>
     */
//    public Boolean deleteElementSeqByRoleId(List<ElementRoleMapDTO> elementRoleMapDTO);
    public Boolean deleteElementsByMapIdRoleId(List<ElementRoleMapDTO> elementRoleMapDTO);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ROLE
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * roleId 에 매핑된 userGroup 목록 조회
     * </pre>
     */
    public List<UserGroup> findRoleMapUserGroupByRoleId(String roleId);

    /**
     * <pre>
     * ROLE 에 사용자 그룹 매핑(등록/수정)
     * </pre>
     */
    public List<RoleUserGroupUserMap> saveRoleMapByUserGroups(String roleId, List<UserGroup> userGroupList);

    /**
     * <pre>
     * roleId 에 매핑된 user 목록 조회
     * </pre>
     */
    public List<User> findRoleMapUserByRoleId(String roleId);

    /**
     * <pre>
     * ROLE 에 사용자 매핑(등록/수정)
     * </pre>
     */
    public List<RoleUserGroupUserMap> saveRoleMapByUsers(String roleId, List<User> user);

    /**
     * <pre>
     * userId 와 userGroupId List 에 해당하는 Role List 조회
     * </pre>
     */
    public List<Role> findRoleMapByUserIdAndUserGroupId(String userId, List<String> userGroupIdList);

    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Aspect 전처리
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * <pre>
     * userId로 사용여부가 Y 이면서 권한기간이 만료되지 않은 Role List 조회
     * </pre>
     */
    public List<Role> findActivationRoleMapByUserId(String userId, String betweenDate);
}
