/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.menu.domain.model.Menu;
import com.skiaf.bcm.menu.domain.repository.MenuRepository;
import com.skiaf.bcm.menu.domain.service.dto.MenuSaveDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeUpdateDTO;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.role.domain.model.MenuRoleMap;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.role.domain.service.RoleService;
import com.skiaf.bcm.role.domain.service.dto.MenuRoleMapDTO;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.constant.MenuType;
import com.skiaf.core.constant.RoleType;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCM 메뉴 관리 ServiceImpl
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
    private static final String MENU_TREE_NOAUTH_CACHE_NAME = "MENU_TREE_NOAUTH_CACHE";
    private static final String MENU_TREE_AUTH_CACHE_NAME = "MENU_TREE_AUTH_CACHE";

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProgramService programService;

    @Autowired
    RoleMapService roleMapService;

    @Autowired
    RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String ROOT_MENU_ID = CommonConstant.ROOT_MENU_ID;

    @Override
    public List<Menu> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "menuSortSeq").and(new Sort(Sort.Direction.ASC, "menuId"));
        return menuRepository.findAll(sort);
    }

    @Override
    @Transactional
    public Menu create(MenuSaveDTO menuSaveDTO) {
        if (!menuSaveDTO.getMenuType().equals(MenuType.PROGRAM.toString())) {
            menuSaveDTO.setProgramId(null);
        }
        Menu menu = modelMapper.map(menuSaveDTO, Menu.class);
        String menuType = menu.getMenuType();

        // 메뉴 유형별 set
        if (MenuType.FOLDER.toString().equals(menuType)) {
            menu.setMenuType(MenuType.FOLDER.toString());
            menu.setProgram(null);
            menu.setUrlAddr(null);
        } else if (MenuType.PROGRAM.toString().equals(menuType)) {
            menu.setMenuType(MenuType.PROGRAM.toString());
            menu.setUrlAddr(null);
            if (!StringUtils.isBlank(menuSaveDTO.getProgramId())) {
                Program program = programService.findOne(menuSaveDTO.getProgramId());
                if (program == null) {
                    throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND")
                            .withSystemMessage("program == null").build();
                } else {
                    menu.setProgram(program);
                }
            }
            menu.setOpenType(menuSaveDTO.getOpenType());
        } else if (MenuType.URL.toString().equals(menuType)) {
            menu.setMenuType(MenuType.URL.toString());
            menu.setProgram(null);
            if (!StringUtils.isBlank(menu.getUrlAddr())) {
                menu.setUrlAddr(menu.getUrlAddr());
            }
            menu.setOpenType(menuSaveDTO.getOpenType());
        }

        return menuRepository.save(menu);
    }

    @Override
    public Menu findOne(String menuId) {
        return menuRepository.findOne(menuId);
    }

    @Override
    @Transactional
    public Menu update(String menuId, MenuSaveDTO menu) {

        Menu updateMenu = menuRepository.findOne(menuId);
        if (updateMenu == null) {
            throw ValidationException.withUserMessageKey("bcm.EMPTY").withSystemMessage("updateMenu == null").build();
        }

        // 선택한 부모메뉴ID가 자식노드에 있을 경우
        if (menu.isSelectedParentIdIsChildren()) {
            // 선택한 부모메뉴에 자식이 있을 경우 자식은 부모의 원래 부모ID로 부모ID를 변경
            List<Menu> childrenList = menuRepository.findByParentMenuId(menuId);
            childrenList.forEach((Menu child) -> {
                child.setParentMenuId(updateMenu.getParentMenuId());
                menuRepository.save(child);
            });
        }

        if (!StringUtils.isBlank(menu.getMenuName1())) {
            updateMenu.setMenuName1(menu.getMenuName1());
        }

        updateMenu.setMenuName2(menu.getMenuName2());
        updateMenu.setMenuName3(menu.getMenuName3());
/* 4번째 언어 추가시, 주석 해제
        updateMenu.setMenuName4(menu.getMenuName4());
*/
        updateMenu.setMenuDesc(menu.getMenuDesc());

        // 메뉴 유형별 set
        if (menu.getMenuType().equals(MenuType.FOLDER.toString())) {
            updateMenu.setMenuType(MenuType.FOLDER.toString());
            updateMenu.setProgram(null);
            updateMenu.setUrlAddr(null);
        } else if (menu.getMenuType().equals(MenuType.PROGRAM.toString())) {
            updateMenu.setMenuType(MenuType.PROGRAM.toString());
            updateMenu.setUrlAddr(null);
            if (!StringUtils.isBlank(menu.getProgramId())) {
                Program program = programService.findOne(menu.getProgramId());
                if (program == null || StringUtils.isBlank(program.getProgramId())) {
                    throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND")
                            .withSystemMessage("program == null").build();
                } else {
                    updateMenu.setProgram(program);
                }
            }
            updateMenu.setOpenType(menu.getOpenType());
        } else if (menu.getMenuType().equals(MenuType.URL.toString())) {
            updateMenu.setMenuType(MenuType.URL.toString());
            updateMenu.setProgram(null);
            if (!StringUtils.isBlank(menu.getUrlAddr())) {
                updateMenu.setUrlAddr(menu.getUrlAddr());
            }
            updateMenu.setOpenType(menu.getOpenType());
        }

        updateMenu.setParentMenuId(menu.getParentMenuId());
        updateMenu.setMenuSortSeq(menu.getMenuSortSeq());
        updateMenu.setUseYn(menu.isUseYn());

        return menuRepository.save(updateMenu);
    }

    @Override
    public Boolean duplicateCheck(String menuId) {
        Boolean isDuplicate = false;
        Menu menu = menuRepository.findByMenuId(menuId);
        if (menu != null) {
            isDuplicate = true;
        }
        return isDuplicate;
    }

    @Override
    public List<MenuRoleMapDTO> findMenuRoleMapByMenuId(String menuId) {
        List<MenuRoleMapDTO> menuRoleMapList = new ArrayList<>();
        Menu menu = menuRepository.findByMenuId(menuId);

        menu.getMenuRoleMap().forEach((MenuRoleMap menuRoleMap) -> {
            MenuRoleMapDTO dto = modelMapper.map(menuRoleMap, MenuRoleMapDTO.class);

            Menu m = menuRoleMap.getMenu();
            dto.setMenuId(m.getMenuId());
            dto.setMenuName1(m.getMenuName1());
            dto.setMenuName2(m.getMenuName2());
            dto.setMenuName3(m.getMenuName3());
/* 4번째 언어 추가시, 주석 해제
            dto.setMenuName4(m.getMenuName4());
*/

            Role r = menuRoleMap.getRole();
            dto.setRoleId(r.getRoleId());
            dto.setRoleName(r.getRoleName());
            dto.setRoleDesc(r.getRoleDesc());

            dto.setUpdateBy(menuRoleMap.getUpdateBy());
            dto.setUpdateDate(menuRoleMap.getUpdateDate());

            menuRoleMapList.add(dto);
        });

        return menuRoleMapList;
    }

    @Override
    @Transactional
    public int updateMenuTree(List<MenuTreeUpdateDTO> updateList) {
        int updateCnt = 0;

        MenuTreeUpdateDTO dto = null;
        Menu menu = null;
        for (int i = 0, max = updateList.size(); i < max; i++) {
            dto = updateList.get(i);

            menu = menuRepository.findOne(dto.getMenuId());
            menu.setMenuSortSeq(dto.getMenuSortSeq());
            menu.setParentMenuId(dto.getParentMenuId());

            menuRepository.save(menu);
            updateCnt++;
        }

        return updateCnt;
    }


    /**
     * <pre>
     * 권한관리를 적용받지 않는 메뉴트리 가져오기.
     * </pre>
     */
    @Cacheable(cacheNames = MENU_TREE_NOAUTH_CACHE_NAME)
    @Override
    public List<MenuTreeDTO> findMenuTreeNoAuthCached() {
        log.info("findMenuTreeNoAuthCached");

        Sort sort = new Sort(Sort.Direction.ASC, "parentMenuId").and(new Sort(Sort.Direction.ASC, "menuSortSeq"));

        // 사용여부 'Y'인 메뉴 목록
        List<Menu> menuList = menuRepository.findByUseYn(sort, true);

        // 메뉴 트리 DTO 목록
        List<MenuTreeDTO> dtoList = new ArrayList<>();

        // 로그인 하지 않았을 경우, 권한매핑이 안되어있는 메뉴만 노출
        Menu menu = null;
        List<MenuRoleMap> roleList = null;
        MenuTreeDTO dto = new MenuTreeDTO();
        for (int i = 0; i < menuList.size(); i++) {
            menu = menuList.get(i);
            roleList = menu.getMenuRoleMap();
            dto = modelMapper.map(menu, MenuTreeDTO.class);

            if (roleList == null || roleList.isEmpty()) {
                dto.setAccessible(true);
            } else {
                dto.setAccessible(false);
            }

            dtoList.add(dto);
        }

        // 트리로 변환
        List<MenuTreeDTO> convertorTree = convertorTree(dtoList, ROOT_MENU_ID);

        // 권한있는 메뉴만 노출
        List<MenuTreeDTO> filteredTree = filterAccessableMenu(convertorTree);

        return filteredTree;
    }

    /**
     * <pre>
     * 로그인 유저가 접근가능한 메뉴 트리 가져오기.
     * </pre>
     */
    @Cacheable(cacheNames = MENU_TREE_AUTH_CACHE_NAME, key = "#loginUserInfo.user.userId")
    @Override
    public List<MenuTreeDTO> findMenuTreeAuthCached(UserInfoDTO loginUserInfo) {
        log.info("findMenuTreeAuthCached, user.userId={}, user.loginId={}", loginUserInfo.getUser().getUserId(), loginUserInfo.getUser().getLoginId());

        Sort sort = new Sort(Sort.Direction.ASC, "parentMenuId").and(new Sort(Sort.Direction.ASC, "menuSortSeq"));

        // 사용여부 'Y'인 메뉴 목록
        List<Menu> menuList = menuRepository.findByUseYn(sort, true);

        // 메뉴 트리 DTO 목록
        List<MenuTreeDTO> dtoList = new ArrayList<>();

        // 권한 있는 메뉴 ID 목록
        List<String> menuRoleIds = new ArrayList<>();

        // 사용자가 로그인한 상태일 경우 사용자에 권한매핑되어있는 메뉴만 노출

        // 로그인한 사용자의 메뉴 권한 목록
        List<Role> userRoleList = loginUserInfo.getRoleList();
        for (int i = 0, iLen = userRoleList.size(); i < iLen; i++) {
            if (userRoleList.get(i) != null &&
                    StringUtils.equals(userRoleList.get(i).getRoleType(), RoleType.MENU.toString())) {
                menuRoleIds.add(userRoleList.get(i).getRoleId());
            }
        }

        Menu menu = null;
        List<MenuRoleMap> roleList = null;
        MenuTreeDTO dto = new MenuTreeDTO();
        for (int i = 0; i < menuList.size(); i++) {
            menu = menuList.get(i);

            // 메뉴의 권한 매핑 목록
            roleList = menu.getMenuRoleMap();
            dto = new MenuTreeDTO();

            if (roleList == null || roleList.isEmpty()) {
                // 메뉴에 매핑된 권한이 없을 경우 그 메뉴는 모든 사용자에게 노출
                dto = modelMapper.map(menu, MenuTreeDTO.class);
                dto.setAccessible(true);
                dtoList.add(dto);
            } else {
                // 메뉴 접근 가능여부 : true일 경우에만 메뉴 노출
                boolean accessable = false;

                // 메뉴에 매핑된 권한이 있을 경우 로그인한 사용자가 권한이 있을 경우에 노출
                for (int j = 0, jLen = roleList.size(); j < jLen; j++) {
                    if (menuRoleIds.contains(roleList.get(j).getRole().getRoleId())) {
                        accessable = true;
                    }
                }
                dto = modelMapper.map(menu, MenuTreeDTO.class);
                dto.setAccessible(accessable);
                dtoList.add(dto);
            }
        }

        // 트리로 변환
        List<MenuTreeDTO> convertorTree = convertorTree(dtoList, ROOT_MENU_ID);

        // 권한있는 메뉴만 노출
        List<MenuTreeDTO> filteredTree = filterAccessableMenu(convertorTree);

        return filteredTree;
    }

    /**
     * <pre>
     * 메뉴 트리 캐시를 모두 제거함.
     * </pre>
     */
    @CacheEvict(cacheNames = {MENU_TREE_NOAUTH_CACHE_NAME,MENU_TREE_AUTH_CACHE_NAME}, allEntries = true)   // 메뉴 트리 캐시 내의 모든 항목을 제거함.
    @Override
    public void clearMenuTreeCache() {
        log.info("clearMenuTreeCache");
    }

    /**
     * <pre>
     * 메뉴목록 트리로 변환
     * </pre>
     */
    private List<MenuTreeDTO> convertorTree(List<MenuTreeDTO> list, String rootMenuId) {

        List<MenuTreeDTO> treeList = new ArrayList<>(); // 최종 트리

        int listLength = list.size();
        int loopLength = 0;
        final int[] treeLength = new int[] { 0 };

        while (treeLength[0] != listLength && listLength != loopLength++) {

            MenuTreeDTO item = null;
            for (int i = 0; i < list.size(); i++) {

                item = list.get(i);

                // 최상위 메뉴일 경우
                if (StringUtils.equals(rootMenuId, item.getParentMenuId())) {
                    treeList.add(item);
                    list.remove(i);
                    treeLength[0]++;

                    Collections.sort(treeList, new Comparator<MenuTreeDTO>() {
                        @Override
                        public int compare(MenuTreeDTO arg0, MenuTreeDTO arg1) {
                            return arg0.getMenuSortSeq().compareTo(arg1.getMenuSortSeq());
                        }
                    });
                    break;
                } else {
                    new InnerClass() {
                        @Override
                        public void getParentNode(List<MenuTreeDTO> children, MenuTreeDTO item) {
                            MenuTreeDTO child = null;
                            for (int j = 0; j < children.size(); j++) {

                                child = children.get(j);

                                if (StringUtils.equals(child.getMenuId(), item.getParentMenuId())) {
                                    child.addChild(item);
                                    treeLength[0]++;
                                    list.remove(list.indexOf(item));

                                    Collections.sort(child.getChildren(), new Comparator<MenuTreeDTO>() {
                                        @Override
                                        public int compare(MenuTreeDTO arg0, MenuTreeDTO arg1) {
                                            return arg0.getMenuSortSeq().compareTo(arg1.getMenuSortSeq());
                                        }
                                    });
                                    break;
                                } else {
                                    if (!child.getChildren().isEmpty()) {
                                        getParentNode(child.getChildren(), item);
                                    }
                                }
                            }
                        }
                    }.getParentNode(treeList, item);
                }
            }
        }
        return treeList;
    }

    private interface InnerClass {
        public void getParentNode(List<MenuTreeDTO> list, MenuTreeDTO item);
    }

    /**
     * <pre>
     * 메뉴 권한에 따라서 메뉴 필터링
     * </pre>
     */
    private List<MenuTreeDTO> filterAccessableMenu(List<MenuTreeDTO> topMenuList) {
        if (topMenuList == null || topMenuList.isEmpty()) {
            return topMenuList;
        } else {
            List<MenuTreeDTO> filterMenuList = new ArrayList<>();
            for (MenuTreeDTO menu : topMenuList) {
                // 해당 유저에 대한 권한이 있는 경우
                if (isAccessable(menu)) {
                    filterMenuList.add(menu);
                }
            }
            return filterMenuList;
        }
    }

    /**
     * <pre>
     * 자식 중 권한있는 메뉴있을 경우 true 리턴
     * </pre>
     */
    private boolean isAccessable(MenuTreeDTO menu) {

        if (menu == null) {
            return false;
        }

        if (menu.getChildren() == null || menu.getChildren().isEmpty()) {
            // 메뉴 접근 가능하면 true 리턴
            if (menu.isAccessible()) {
                return true;
            }
        } else {
            List<MenuTreeDTO> filteredChildren = new ArrayList<>();
            for (MenuTreeDTO child : menu.getChildren()) {
                // 재귀 호출
                if (isAccessable(child)) {
                    filteredChildren.add(child);
                }
            }
            menu.setChildren(filteredChildren);
            // 필터된 자식이 있을 경우 true 리턴
            if (!filteredChildren.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
