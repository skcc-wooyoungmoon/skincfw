/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.element.domain.service.ElementService;
import com.skiaf.bcm.menu.domain.model.Menu;
import com.skiaf.bcm.menu.domain.service.MenuService;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.role.domain.model.ElementRoleMap;
import com.skiaf.bcm.role.domain.model.ElementRoleMapId;
import com.skiaf.bcm.role.domain.model.MenuRoleMap;
import com.skiaf.bcm.role.domain.model.MenuRoleMapId;
import com.skiaf.bcm.role.domain.model.ProgramRoleMap;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;
import com.skiaf.bcm.role.domain.repository.ElementRoleMapRepository;
import com.skiaf.bcm.role.domain.repository.MenuRoleMapRepository;
import com.skiaf.bcm.role.domain.repository.ProgramRoleMapRepository;
import com.skiaf.bcm.role.domain.repository.RoleUserGroupUserMapRepository;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;
import com.skiaf.bcm.role.domain.service.dto.ProgramRoleMapDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.UserGroupService;
import com.skiaf.bcm.user.domain.service.UserGroupUserMapService;
import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.core.constant.RoleType;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCM 권한 관리 Service
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Service
@Slf4j
public class RoleMapServiceImpl implements RoleMapService {

    private static final String USER = "USER";
    private static final String USERGROUP = "USERGROUP";
    private static final String USER_MESSAGE_KEY_NOT_FOUND = "bcm.common.NOT_FOUND";
    private static final String USER_MESSAGE_KEY_DATE_PERIOD = "bcm.role.valid.date-period";
    private static final String SYSTEM_MESSAGE_ROLE = "Role : ";
    private static final String SYSTEM_MESSAGE_IS_NULL = "== null ";
    private static final String SYSTEM_MESSAGE_DATE_PERIOD = "start > end";
    private static final String SYSTEM_MESSAGE_DATE_FORMAT = "date format error";

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @Autowired
    ProgramService programService;

    @Autowired
    ElementService elementService;

    @Autowired
    UserService userService;

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    UserGroupUserMapService userGroupUserMapService;

    @Autowired
    RoleUserGroupUserMapRepository roleUserGroupUserMapRepository;

    @Autowired
    MenuRoleMapRepository menuRoleMapRepository;

    @Autowired
    ProgramRoleMapRepository programRoleMapRepository;

    @Autowired
    ElementRoleMapRepository elementRoleMapRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Role> findRoleMapByUserId(String userId) {
        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.findByUserUserId(userId);
        List<Role> roleList = new ArrayList<>();

        if (roleMapList != null) {
            roleMapList.forEach((RoleUserGroupUserMap roleMap) -> {
                Role role = roleMap.getRole();
                if (role != null) {
                    role.setRoleMapId(roleMap.getUser().getUserId());
                    role.setRoleMapType(USER);
                    role.setRoleMapBeginDt(roleMap.getRoleBeginDt());
                    role.setRoleMapEndDt(roleMap.getRoleEndDt());
                    role.setRoleMapUseYn(roleMap.isUseYn());

                    roleList.add(role);

                    entityManager.detach(role);
                }
            });
        }

        return roleList;
    }

    @Override
    public List<Role> findRoleMapByUserGroupList(List<String> userGroupIdList) {
        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.findByUserGroupUserGroupIdIn(userGroupIdList);
        List<Role> roleList = new ArrayList<>();

        if (roleMapList != null) {
            roleMapList.forEach((RoleUserGroupUserMap roleMap) -> {
                Role role = roleMap.getRole();
                if (role != null) {
                    role.setRoleMapId(roleMap.getUserGroup().getUserGroupId());
                    role.setRoleMapType(USERGROUP);
                    role.setRoleMapBeginDt(roleMap.getRoleBeginDt());
                    role.setRoleMapEndDt(roleMap.getRoleEndDt());
                    role.setRoleMapUseYn(roleMap.isUseYn());

                    roleList.add(role);

                    entityManager.detach(role);
                }
            });
        }

        return roleList;
    }

    @Override
    public List<Role> findRoleMapByUserGroupId(String userGroupId) {
        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.findByUserGroupUserGroupId(userGroupId);
        List<Role> roleList = new ArrayList<>();

        if (roleMapList != null) {
            roleMapList.forEach((RoleUserGroupUserMap roleMap) -> {
                Role role = roleMap.getRole();
                if (role != null) {
                    role.setRoleMapId(roleMap.getUserGroup().getUserGroupId());
                    role.setRoleMapType(USERGROUP);
                    role.setRoleMapBeginDt(roleMap.getRoleBeginDt());
                    role.setRoleMapEndDt(roleMap.getRoleEndDt());
                    role.setRoleMapUseYn(roleMap.isUseYn());

                    roleList.add(role);
                }
            });
        }

        return roleList;
    }

    @Override
    public List<Role> findRoleMapByMenuId(String menuId) {
        List<MenuRoleMap> menuRoleMapList = menuRoleMapRepository.findByMapIdMenuId(menuId);

        List<Role> roleList = new ArrayList<>();

        menuRoleMapList.forEach(item -> roleList.add(item.getRole()));

        return roleList;
    }

    @Override
    @Transactional
    public List<Role> saveRolesByMenuId(String menuId, String[] roleId) {
        List<MenuRoleMap> menuRoleMapList = new ArrayList<>();
        Menu menu = menuService.findOne(menuId);

        MenuRoleMap menuRoleMap = null;
        Role role = null;
        for (int i = 0, iLen = roleId.length; i < iLen; i++) {
            role = roleService.findOne(roleId[i]);

            if (role == null) {
                throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                        .withSystemMessage(SYSTEM_MESSAGE_ROLE + roleId[i] + SYSTEM_MESSAGE_IS_NULL).build();
            }
            if (!RoleType.MENU.toString().equals(role.getRoleType())) {
                throw BizException.withSystemMessage("!RoleType.MENU.getName().equals(role.getRoleType())").build();
            }
            menuRoleMap = new MenuRoleMap();
            menuRoleMap.setMapId(new MenuRoleMapId(menu.getMenuId(), role.getRoleId()));
            menuRoleMap.setMenu(menu);
            menuRoleMap.setRole(role);

            menuRoleMapList.add(menuRoleMap);
        }

        menuRoleMapList = menuRoleMapRepository.save(menuRoleMapList);

        List<Role> roleList = new ArrayList<>();

        menuRoleMapList.forEach((MenuRoleMap item) -> roleList.add(item.getRole()));

        return roleList;
    }

    @Override
    public Boolean deleteRolesByMenuId(String menuId, String[] roleId) {
        List<MenuRoleMap> menuRoleMapList = new ArrayList<>();
        Menu menu = menuService.findOne(menuId);

        MenuRoleMap menuRoleMap;
        for (int i = 0; i < roleId.length; i++) {
            Role role = roleService.findOne(roleId[i]);
            if (role != null) {
                menuRoleMap = new MenuRoleMap();
                menuRoleMap.setMapId(new MenuRoleMapId(menu.getMenuId(), role.getRoleId()));
                menuRoleMap.setMenu(menu);
                menuRoleMap.setRole(role);

                menuRoleMapList.add(menuRoleMap);
            }
        }
        menuRoleMapRepository.deleteInBatch(menuRoleMapList);
        return true;
    }

    @Override
    public List<Role> findRoleMapByProgramId(String programId) {
        List<ProgramRoleMap> programRoleMapList = programRoleMapRepository.findByMapIdProgramId(programId);

        List<Role> roleList = new ArrayList<>();

        programRoleMapList.forEach(item -> roleList.add(item.getRole()));

        return roleList;
    }

    @Override
    public List<ProgramRoleMap> saveRolesByProgramId(List<ProgramRoleMapDTO> programRoleMapDTOList) {
        List<ProgramRoleMap> programRoleMapList = new ArrayList<>();
        programRoleMapDTOList.forEach((ProgramRoleMapDTO programRoleMapDTO) -> {
            ProgramRoleMap programRoleMap = new ProgramRoleMap(programRoleMapDTO.getProgramId(), programRoleMapDTO.getRoleId());

            programRoleMapList.add(programRoleMap);
        });

        return programRoleMapRepository.save(programRoleMapList);
    }

    @Override
    public Boolean deleteRolesByProgramId(List<ProgramRoleMapDTO> programRoleMapDTOList) {
        List<ProgramRoleMap> programRoleMapList = new ArrayList<>();
        programRoleMapDTOList.forEach((ProgramRoleMapDTO programRoleMapDTO) -> {
            ProgramRoleMap programRoleMap = new ProgramRoleMap(programRoleMapDTO.getProgramId(), programRoleMapDTO.getRoleId());

            programRoleMapList.add(programRoleMap);
        });

        programRoleMapRepository.deleteInBatch(programRoleMapList);
        return true;
    }

    @Override
    public List<Role> findRoleMapByUserIdAndUserGroupId(String userId, List<String> userGroupIdList) {

        List<RoleUserGroupUserMap> roleUserGroupUserMapList = roleUserGroupUserMapRepository.findByUserUserIdOrUserGroupUserGroupIdIn(userId, userGroupIdList);

        List<Role> roleList = new ArrayList<>();

        roleUserGroupUserMapList.forEach(item -> roleList.add(item.getRole()));

        return roleList;
    }

    @Override
    public List<Element> saveElementsByMapIdRoleId(List<ElementRoleMapDTO> elementRoleMapDTO) {
        List<ElementRoleMap> elementRoleMapList = new ArrayList<>();

        Role role = null;
        Element element = null;
        ElementRoleMap elementRoleMap = null;
        for (int i = 0, iLen = elementRoleMapDTO.size(); i < iLen; i++) {
            role = roleService.findOne(elementRoleMapDTO.get(i).getRoleId());
            element = elementService.findOne(elementRoleMapDTO.get(i).getElementSeq());

            elementRoleMap = new ElementRoleMap();
            elementRoleMap.setMapId(new ElementRoleMapId(element.getElementSeq(), role.getRoleId()));
            elementRoleMap.setElement(element);
            elementRoleMap.setRole(role);
            elementRoleMap.setVisibleYn(elementRoleMapDTO.get(i).isVisibleYn());
            elementRoleMap.setEnableYn(elementRoleMapDTO.get(i).isEnableYn());

            elementRoleMapList.add(elementRoleMap);
        }

        elementRoleMapList = elementRoleMapRepository.save(elementRoleMapList);

        List<Element> elementList = new ArrayList<>();

        elementRoleMapList.forEach(item -> elementList.add(item.getElement()));

        return elementList;
    }

    @Override
    public Boolean deleteElementsByMapIdRoleId(List<ElementRoleMapDTO> elementRoleMapDTO) {
        List<ElementRoleMap> elementRoleMapList = new ArrayList<>();

        Role role = null;
        for (int i = 0; i < elementRoleMapDTO.size(); i++) {
            role = roleService.findOne(elementRoleMapDTO.get(i).getRoleId());

            Element element = null;
            ElementRoleMap elementRoleMap = null;
            for (int j = 0; j < elementRoleMapDTO.size(); j++) {
                element = elementService.findOne(elementRoleMapDTO.get(j).getElementSeq());

                elementRoleMap = new ElementRoleMap();
                elementRoleMap.setMapId(new ElementRoleMapId(element.getElementSeq(), role.getRoleId()));
                elementRoleMap.setElement(element);
                elementRoleMap.setRole(role);

                elementRoleMapList.add(elementRoleMap);
            }
        }

        elementRoleMapRepository.deleteInBatch(elementRoleMapList);
        return true;
    }

    @Override
    public List<UserGroup> findRoleMapUserGroupByRoleId(String roleId) {
        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.findByRoleRoleId(roleId);
        List<UserGroup> userGroupList = new ArrayList<>();

        roleMapList.forEach((RoleUserGroupUserMap item) -> {
            if (item != null && item.getUserGroup() != null) {
                UserGroup userGroup = item.getUserGroup();
                userGroup.setRoleMapId(item.getRoleSeq());
                userGroup.setRoleUseYn(item.isUseYn());
                userGroup.setRoleBeginDt(item.getRoleBeginDt());
                userGroup.setRoleEndDt(item.getRoleEndDt());

                entityManager.detach(userGroup);

                userGroupList.add(userGroup);
            }
        });

        return userGroupList;
    }

    @Override
    public List<User> findRoleMapUserByRoleId(String roleId) {
        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.findByRoleRoleId(roleId);
        List<User> userList = new ArrayList<>();

        roleMapList.forEach((RoleUserGroupUserMap item) -> {
            if (item != null && item.getUser() != null) {
                User user = item.getUser();
                user.setRoleMapId(item.getRoleSeq());
                user.setRoleUseYn(item.isUseYn());
                user.setRoleBeginDt(item.getRoleBeginDt());
                user.setRoleEndDt(item.getRoleEndDt());

                entityManager.detach(user);

                userList.add(user);
            }
        });

        return userList;
    }

    @Override
    public List<RoleUserGroupUserMap> saveRoleMapByUsers(String roleId, List<User> userList) {
        List<RoleUserGroupUserMap> roleUserMapList = new ArrayList<>();
        Role role = roleService.findOne(roleId);

        if (role == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                    .withSystemMessage(SYSTEM_MESSAGE_ROLE + roleId + SYSTEM_MESSAGE_IS_NULL).build();
        }

        RoleUserGroupUserMap roleUserMap = null;
        User user = null;
        for (int i = 0; i < userList.size(); i++) {
            user = userService.findOne(userList.get(i).getUserId());

            if (user == null) {
                throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                        .withSystemMessage("User : " + userList.get(i).getUserId() + SYSTEM_MESSAGE_IS_NULL).build();
            }

            this.checkPeriod(userList.get(i).getRoleBeginDt(), userList.get(i).getRoleEndDt());

            roleUserMap = new RoleUserGroupUserMap();

            if (userList.get(i).getRoleMapId() != null) {
                // update
                roleUserMap.setRoleSeq(userList.get(i).getRoleMapId());
            } else {
                // insert
            }
            roleUserMap.setRole(role);
            roleUserMap.setUser(user);
            roleUserMap.setUseYn(userList.get(i).getRoleUseYn());
            roleUserMap.setRoleBeginDt(userList.get(i).getRoleBeginDt());
            roleUserMap.setRoleEndDt(userList.get(i).getRoleEndDt());

            roleUserMapList.add(roleUserMap);
        }

        roleUserMapList = roleUserGroupUserMapRepository.save(roleUserMapList);

        return roleUserMapList;
    }

    @Override
    public List<RoleUserGroupUserMap> saveRoleMapByUserGroups(String roleId, List<UserGroup> userGroupList) {
        List<RoleUserGroupUserMap> roleUserMapList = new ArrayList<>();
        Role role = roleService.findOne(roleId);

        if (role == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                    .withSystemMessage(SYSTEM_MESSAGE_ROLE + roleId + SYSTEM_MESSAGE_IS_NULL).build();
        }

        RoleUserGroupUserMap roleUserMap = null;
        UserGroup userGroup = null;
        for (int i = 0; i < userGroupList.size(); i++) {
            userGroup = userGroupService.findOne(userGroupList.get(i).getUserGroupId());

            if (userGroup == null) {
                throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                        .withSystemMessage("UserGroup : " + userGroupList.get(i).getUserGroupId() + SYSTEM_MESSAGE_IS_NULL)
                        .build();
            }

            this.checkPeriod(userGroupList.get(i).getRoleBeginDt(), userGroupList.get(i).getRoleEndDt());

            roleUserMap = new RoleUserGroupUserMap();

            if (userGroupList.get(i).getRoleMapId() != null) {
                // update
                roleUserMap.setRoleSeq(userGroupList.get(i).getRoleMapId());
            } else {
                // insert
            }
            roleUserMap.setRole(role);
            roleUserMap.setUserGroup(userGroup);
            roleUserMap.setUseYn(userGroupList.get(i).getRoleUseYn());
            roleUserMap.setRoleBeginDt(userGroupList.get(i).getRoleBeginDt());
            roleUserMap.setRoleEndDt(userGroupList.get(i).getRoleEndDt());

            roleUserMapList.add(roleUserMap);
        }

        roleUserMapList = roleUserGroupUserMapRepository.save(roleUserMapList);

        return roleUserMapList;
    }

    @Override
    public List<Role> findActivationRoleMapByUserId(String userId, String betweenDate) {

        //사용자가 매핑되어있는 userGroupList 조회
        List<UserGroup> userGroupList = userGroupUserMapService.findByMapIdUserId(userId);

        //userGroup 사용여부가 true 의 userGroupId 추출
        List<String> userGroupIds = new ArrayList<>();
        userGroupList.forEach((UserGroup userGroup) -> {
            if(userGroup.isUseYn()) {
                userGroupIds.add(userGroup.getUserGroupId());
            }
        });

        List<RoleUserGroupUserMap> roleMapList = roleUserGroupUserMapRepository.activationRoleMap(userId, userGroupIds, betweenDate);

        List<Role> roleList = new ArrayList<>();

        roleMapList.forEach((RoleUserGroupUserMap roleMap) -> {
            if (roleMap.getRole().isUseYn() && roleMap.isUseYn()) {

                Role role = new Role();
                role.setRoleId(roleMap.getRole().getRoleId());
                role.setRoleName(roleMap.getRole().getRoleName());
                role.setRoleType(roleMap.getRole().getRoleType());
                role.setRoleDesc(roleMap.getRole().getRoleDesc());
                role.setUseYn(roleMap.getRole().isUseYn());

                if (roleMap.getUserGroup() != null) {
                    role.setRoleMapId(roleMap.getUserGroup().getUserGroupId());
                    role.setRoleMapType(USERGROUP);
                } else if (roleMap.getUser() != null) {
                    role.setRoleMapId(roleMap.getUser().getLoginId());
                    role.setRoleMapType(USER);
                }
                role.setRoleMapUseYn(roleMap.isUseYn());
                role.setRoleMapBeginDt(roleMap.getRoleBeginDt());
                role.setRoleMapEndDt(roleMap.getRoleEndDt());

                roleList.add(role);
            }
        });

        return roleList;
    }

    /**
     * <pre>
     * 날짜 기간 유효성 검증
     * </pre>
     */
    private void checkPeriod(String startDate, String endDate) {

        try {
            int start = Integer.parseInt(startDate);
            int end = Integer.parseInt(endDate);
            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd");
            dateFormatParser.setLenient(false);

            dateFormatParser.parse(startDate);
            dateFormatParser.parse(endDate);

            if (start > end) {
                throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DATE_PERIOD)
                        .withSystemMessage(SYSTEM_MESSAGE_DATE_PERIOD).build();
            }

        } catch (Exception e) {
            log.error(SYSTEM_MESSAGE_DATE_FORMAT, e);
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DATE_PERIOD)
                    .withSystemMessage(SYSTEM_MESSAGE_DATE_FORMAT).build();
        }

    }

}
