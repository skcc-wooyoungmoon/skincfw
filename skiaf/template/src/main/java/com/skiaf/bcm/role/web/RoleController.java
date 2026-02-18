/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.role.domain.service.RoleService;
import com.skiaf.bcm.role.domain.service.dto.RoleSearchDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * BCM 권한 관리 Controller
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Api(tags = "권한 관리")
@RestController
public class RoleController {

    private static final String BCM_ROLE_ASTERISK = "bcm.role.*";

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapService roleMapService;

    @Autowired
    private MessageComponent messageComponent;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_ROLES)
    public ModelAndView roleList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ROLE_ASTERISK);
        modelAndView.setViewName("skiaf/view/role/role-list");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ROLES_DETAIL)
    public ModelAndView roleUserMap() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ROLE_ASTERISK);
        modelAndView.setViewName("skiaf/view/role/role-map");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ROLES_SEARCH)
    public ModelAndView roleSearchPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ROLE_ASTERISK);
        modelAndView.setViewName("skiaf/view/role/role-search-popup");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ROLES_CREATE)
    public ModelAndView roleCreate() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/role/role-create-popup");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ROLES_EDIT)
    public ModelAndView roleEdit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/role/role-edit-popup");
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "Role 검색 페이징 목록 조회")
    @GetMapping(value = Path.ROLES)
    public RestResponse findQueryBySearch(@Valid RoleSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        if (search.isList()) {
            return new RestResponse(roleService.findQueryByKeyword(search));
        } else {
            return new RestResponse(roleService.findQueryByKeyword(search, pageable));
        }
    }

    @ApiOperation(value = "Role ID 중복체크")
    @GetMapping(value = Path.ROLES_DUPLICATE)
    public RestResponse duplicateCheck(
            @ApiParam(name = "roleId", required = true, value = "role ID") @PathVariable String roleId) {

        RestResponse restResponse = new RestResponse();
        Boolean isDuplicate = roleService.duplicateCheck(roleId);
        restResponse.setData(isDuplicate);
        if (isDuplicate) {
            restResponse.setUserMessage(messageComponent.getMessage("bcm.common.DUPLICATE"));
        }
        return restResponse;
    }

    @ApiOperation(value = "Role 등록")
    @PostMapping(value = Path.ROLES)
    public RestResponse create(
            @ApiParam(name = "created role object", required = true, value = "Role 정보") @Valid @RequestBody Role role) {

        RestResponse restResponse = new RestResponse();
        Role createRole = roleService.create(role);
        restResponse.setData(createRole);

        return restResponse;
    }

    @ApiOperation(value = "Role 단일 조회")
    @GetMapping(value = Path.ROLES_DETAIL)
    public RestResponse findOne(
            @ApiParam(name = "roleId", required = true, value = "Role ID") @PathVariable String roleId) {

        return new RestResponse(roleService.findOne(roleId));
    }

    @ApiOperation(value = "Role 상세조회(매핑정보 포함)")
    @GetMapping(value = Path.ROLES_DETAIL_INFO)
    public RestResponse findOneDetail(
            @ApiParam(name = "roleId", required = true, value = "Role ID") @PathVariable String roleId) {

        Map<String, Object> data = new HashMap<>();

        // 권한 정보
        data.put("roleInfo", roleService.findOne(roleId));

        // 권한 매핑정보(UserGroup)
        data.put("roleMapUserGroup", roleMapService.findRoleMapUserGroupByRoleId(roleId));

        // 권한 매핑정보(User)
        data.put("roleMapUser", roleMapService.findRoleMapUserByRoleId(roleId));

        return new RestResponse(data);
    }

    @ApiOperation(value = "Role 수정")
    @PutMapping(value = Path.ROLES_DETAIL)
    public RestResponse update(
            @ApiParam(name = "roleId", required = true, value = "Role ID") @PathVariable String roleId,
            @ApiParam(name = "update role object", required = true, value = "Role 정보") @RequestBody Role role) {

        return new RestResponse(roleService.update(roleId, role));
    }

    @ApiOperation(value = "Role 에 사용자 매핑")
    @PostMapping(value = Path.ROLES_MAP_USERS)
    public RestResponse saveRoleMapByUsers(
            @ApiParam(name = "roleId", required = true, value = "Role ID") @PathVariable String roleId,
            @ApiParam(name = "user list", required = true, value = "사용자 목록") @RequestBody List<User> userList) {
        return new RestResponse(roleMapService.saveRoleMapByUsers(roleId, userList));
    }

    @ApiOperation(value = "Role 에 사용자그룹 매핑")
    @PostMapping(value = Path.ROLES_MAP_USERGROUPS)
    public RestResponse saveRoleMapByUserGroups(
            @ApiParam(name = "roleId", required = true, value = "Role ID") @PathVariable String roleId,
            @ApiParam(name = "userGroup List", required = true, value = "사용자그룹 목록") @RequestBody List<UserGroup> userGroupList) {
        return new RestResponse(roleMapService.saveRoleMapByUserGroups(roleId, userGroupList));
    }

}
