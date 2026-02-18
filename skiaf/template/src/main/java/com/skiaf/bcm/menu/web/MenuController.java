/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.web;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.menu.domain.service.MenuService;
import com.skiaf.bcm.menu.domain.service.dto.MenuSaveDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeDTO;
import com.skiaf.bcm.menu.domain.service.dto.MenuTreeUpdateDTO;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * BCM 메뉴 관리 Controller
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Api(tags = "메뉴 관리")
@RestController
public class MenuController {

    private static final String BCM_MENU_ASTERISK = "bcm.menu.*";

    @Autowired
    RoleMapService roleMapService;

    @Autowired
    private MenuService menuService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "메뉴관리 화면")
    @GetMapping(value = Path.VIEW_MENUS)
    public ModelAndView menuList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-list");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "메뉴관리 메뉴 등록 팝업 화면")
    @GetMapping(value = Path.VIEW_MENUS_CREATE)
    public ModelAndView menuCreatePopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-create-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "메뉴관리 메뉴 상세 수정 팝업 화면")
    @GetMapping(value = Path.VIEW_MENUS_UPDATE)
    public ModelAndView menuDetailPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-update-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "메뉴관리 메뉴 권한 등록 팝업 화면")
    @GetMapping(value = Path.VIEW_MENUS_ROLE)
    public ModelAndView menuRolePopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-role-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "메뉴관리 상위메뉴선택 레이어팝업 화면")
    @GetMapping(value = Path.VIEW_MENUS_SELECT)
    public ModelAndView menuSelectPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-select-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "메뉴관리 메뉴트리변경 레이어팝업 화면")
    @GetMapping(value = Path.VIEW_MENUS_CHANGE)
    public ModelAndView menuChangePopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/menu/menu-change-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_MENU_ASTERISK);
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "메뉴 목록 조회")
    @GetMapping(value = Path.MENUS)
    public RestResponse findAll() {
        return new RestResponse(menuService.findAll());
    }

    @ApiOperation(value = "메뉴 등록")
    @PostMapping(value = Path.MENUS)
    public RestResponse create(
            @ApiParam(name = "created menu object", required = true, value = "생성하는 메뉴 정보") @Validated @RequestBody MenuSaveDTO menuSaveDTO) {
        
        RestResponse result = new RestResponse(menuService.create(menuSaveDTO));
        
        // 새로운 메뉴가 생성되었으므로, 기존 캐시 초기화
        menuService.clearMenuTreeCache();
        
        return result;
    }

    @ApiOperation(value = "메뉴 상세 조회")
    @GetMapping(value = Path.MENUS_DETAIL)
    public RestResponse findOne(
            @ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId) {

        return new RestResponse(menuService.findOne(menuId));
    }

    @ApiOperation(value = "메뉴 수정")
    @PatchMapping(value = Path.MENUS_DETAIL)
    public RestResponse update(@ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId,
            @ApiParam(name = "update menu object", required = true, value = "메뉴 수정 정보") @Validated @RequestBody MenuSaveDTO menuSaveDTO) {

        menuSaveDTO.setMenuId(menuId);
        
        RestResponse result = new RestResponse(menuService.update(menuId, menuSaveDTO));
        
        //메뉴가 수정되었으므로, 기존 캐시 초기화
        menuService.clearMenuTreeCache();
        
        return result;
    }

    @ApiOperation(value = "메뉴ID 중복체크")
    @GetMapping(value = Path.MENUS_DUPLICATE)
    public RestResponse duplicateCheck(
            @ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId) {

        return new RestResponse(menuService.duplicateCheck(menuId));
    }

    @ApiOperation(value = "메뉴 ID 로 Role List 조회")
    @GetMapping(value = Path.MENUS_ROLE_MAP)
    public RestResponse findRoleByMenuId(
            @ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId) {

        return new RestResponse(menuService.findMenuRoleMapByMenuId(menuId));
    }

    @ApiOperation(value = "해당 메뉴 ID에 권한 목록 매핑(저장)")
    @PostMapping(value = Path.MENUS_ROLE_MAP)
    public RestResponse saveRolesByMenuId(
            @ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId,
            @RequestBody String[] roleId) {
        
        RestResponse result = new RestResponse(roleMapService.saveRolesByMenuId(menuId, roleId));
        
        // 메뉴권한이 수정되었으므로, 기존 캐시 초기화
        menuService.clearMenuTreeCache();

        return result;
    }

    @ApiOperation(value = "해당 메뉴 ID에 매핑된 권한 목록 삭제")
    @DeleteMapping(value = Path.MENUS_ROLE_MAP)
    public RestResponse deleteRolesByMenuId(
            @ApiParam(name = "menuId", required = true, value = "메뉴 ID") @PathVariable String menuId,
            @RequestBody String[] roleId) {
        
        RestResponse result = new RestResponse(roleMapService.deleteRolesByMenuId(menuId, roleId));
        
        // 메뉴권한이 수정되었으므로, 기존 캐시 초기화
        menuService.clearMenuTreeCache();

        return result;
    }

    @ApiOperation(value = "메뉴 트리 수정")
    @PatchMapping(value = Path.MENUS_TREE)
    public RestResponse treeUpdate(
            @ApiParam(name = "updated menu list object", required = true, value = "수정하는 메뉴 목록") @RequestBody List<MenuTreeUpdateDTO> updateList) {

        RestResponse result = new RestResponse(menuService.updateMenuTree(updateList));
        
        // 메뉴가 수정되었으므로, 기존 캐시 초기화
        menuService.clearMenuTreeCache();
        
        return result;
    }

    @ApiOperation(value = "메뉴 트리 목록 조회")
    @GetMapping(value = Path.MENUS_TREE)
    public RestResponse findMenuTree() {
        
        List<MenuTreeDTO> menuTree = null;
        
        // 로그인 하지 않았을 경우, 권한매핑이 안되어있는 메뉴만 노출
        if (SessionUtil.getLoginUser() == null) {
            menuTree = menuService.findMenuTreeNoAuthCached();
        }
        // 사용자가 로그인한 상태일 경우 사용자에 권한매핑되어있는 메뉴만 노출
        else {
            menuTree = menuService.findMenuTreeAuthCached(SessionUtil.getLoginUserInfo());
        }
        return new RestResponse(menuTree);
    }

    /**
     * <pre>
     * bean init
     * </pre>
     */
    @PostConstruct
    public void init() {
        // 메뉴 트리 캐시를 초기화. redis를 쓰는 경우를 위해 의도적으로 초기화.
        menuService.clearMenuTreeCache();
    }
}
