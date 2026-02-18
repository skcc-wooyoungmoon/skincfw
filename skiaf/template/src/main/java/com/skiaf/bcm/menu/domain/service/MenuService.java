/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.service;

import java.util.List;

import com.skiaf.bcm.menu.domain.model.Menu;
import com.skiaf.bcm.menu.domain.service.dto.MenuSaveDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeUpdateDTO;
import com.skiaf.bcm.role.domain.service.dto.MenuRoleMapDTO;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;

/**
 * <pre>
 * BCM 메뉴 관리 Service
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public interface MenuService {

    /**
     * <pre>
     * 메뉴 목록 조회
     * </pre>
     */
    public List<Menu> findAll();

    /**
     * <pre>
     * 메뉴 생성
     * </pre>
     */
    public Menu create(MenuSaveDTO menuSaveDTO);

    /**
     * <pre>
     * 메뉴 상세 조회
     * </pre>
     */
    public Menu findOne(String menuId);

    /**
     * <pre>
     * 메뉴 수정
     * </pre>
     */
    public Menu update(String menuId, MenuSaveDTO menuSaveDTO);

    /**
     * <pre>
     * 메뉴 ID 중복 체크
     * </pre>
     */
    public Boolean duplicateCheck(String menuId);

    /**
     * <pre>
     * 메뉴 권한 목록
     * </pre>
     */
    public List<MenuRoleMapDTO> findMenuRoleMapByMenuId(String menuId);

    /**
     * <pre>
     * 메뉴 전체 트리 수정
     * </pre>
     */
    public int updateMenuTree(List<MenuTreeUpdateDTO> updateList);

    
    /**
     * <pre>
     * 권한관리를 적용받지 않는 메뉴트리 가져오기 (캐시됨)
     * </pre>
     */
    public List<MenuTreeDTO> findMenuTreeNoAuthCached();
    
    /**
     * <pre>
     * 로그인 유저가 접근가능한 메뉴 트리 가져오기 (캐시됨)
     * </pre>
     */
    public List<MenuTreeDTO> findMenuTreeAuthCached(UserInfoDTO loginUserInfo);    
    
    /**
     * <pre>
     * 메뉴 트리 캐시를 모두 제거함.
     * </pre>
     */
    public void clearMenuTreeCache();
}
