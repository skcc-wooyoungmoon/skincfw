/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.UserGroupService;
import com.skiaf.bcm.user.domain.service.UserGroupUserMapService;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 
 * BCM 사용자그룹 관리 Controller
 * 
 * History
 * - 2018. 7. 19. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Api(tags = "사용자그룹 관리")
@RestController
public class UserGroupController {

    private static final String BCM_USERGROUP_ASTERISK = "bcm.usergroup.*";

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserGroupUserMapService userGroupUserMapService;

    @Autowired
    private RoleMapService roleMapService;
    
    @Autowired
    private MessageComponent messageComponent;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_USER_GROUPS)
    public ModelAndView userGroupList() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USERGROUP_ASTERISK);
        modelAndView.setViewName("skiaf/view/usergroup/usergroup-list");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_USER_GROUPS_DETAIL)
    public ModelAndView userGroupDetail(@PathVariable String userGroupId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userGroupId", userGroupId);
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USERGROUP_ASTERISK);
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.user.*");
        modelAndView.setViewName("skiaf/view/usergroup/usergroup-detail");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_USER_GROUPS_CREATE)
    public ModelAndView userGroupCreate() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USERGROUP_ASTERISK);
        modelAndView.setViewName("skiaf/view/usergroup/usergroup-create-popup");

        return modelAndView;
    }
    
    @GetMapping(value = Path.VIEW_USER_GROUPS_UPDATE)
    public ModelAndView userGroupUpdate() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USERGROUP_ASTERISK);
        modelAndView.setViewName("skiaf/view/usergroup/usergroup-update-popup");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_USER_GROUPS_SEARCH)
    public ModelAndView userGroupSelectPopup() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USERGROUP_ASTERISK);
        modelAndView.setViewName("skiaf/view/usergroup/usergroup-search-popup");

        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 사용자그룹 목록 조회
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 목록 조회")
    @GetMapping(value = Path.USER_GROUPS)
    public RestResponse findQueryByKeyword(UserGroupSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        return new RestResponse(userGroupService.findQueryBySearch(search, pageable));
    }

    /**
     * <pre>
     * 사용자그룹 아이디 중복체크
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 아이디 중복체크")
    @GetMapping(value = Path.USER_GROUPS_DUPLICATE)
    public RestResponse duplicateCheck(
            @ApiParam(name = "userGroupId", required = true, value = "그룹 ID") @PathVariable String userGroupId) {

        RestResponse restResponse = new RestResponse();
        Boolean isDuplicate = userGroupService.duplicateCheck(userGroupId);
        restResponse.setData(isDuplicate);
        if (isDuplicate) {
            restResponse.setUserMessage(messageComponent.getMessage("bcm.common.DUPLICATE"));
        }
        return restResponse;
    }

    /**
     * <pre>
     * 사용자그룹 등록
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 등록")
    @PostMapping(value = Path.USER_GROUPS)
    public RestResponse create(
            @ApiParam(name = "userGroup", required = true, value = "사용자그룹 정보") @RequestBody UserGroup userGroup) {

        return new RestResponse(userGroupService.create(userGroup));
    }

    /**
     * <pre>
     * 사용자그룹 상세 조회
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 상세 조회")
    @GetMapping(value = Path.USER_GROUPS_DETAIL)
    public RestResponse findOne(
            @ApiParam(name = "userGroupId", required = true, value = "그룹 ID") @PathVariable String userGroupId) {

        RestResponse restResponse = new RestResponse();
        
        UserGroup userGroup = userGroupService.findOne(userGroupId);
        List<User> userGroupMapUserList = userGroupUserMapService.findByMapIdUserGroupId(userGroupId);

        Map<String, Object> data = new HashMap<>();

        data.put("userGroup", userGroup);
        data.put("userGroupMapUserList", userGroupMapUserList);

        restResponse.setData(data);

        return restResponse;
    }

    /**
     * <pre>
     * 사용자그룹 수정
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 수정")
    @PutMapping(value = Path.USER_GROUPS_DETAIL)
    public RestResponse update(
            @ApiParam(name = "userGroupid", required = true, value = "사용자그룹 ID") @PathVariable String userGroupId,
            @ApiParam(name = "update userGroup object", required = true, value = "사용자그룹 정보") @RequestBody UserGroup userGroup) {

        return new RestResponse(userGroupService.update(userGroupId, userGroup));
    }

    /**
     * <pre>
     * 사용자그룹 사용자 매핑 등록
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 사용자 매핑 등록")
    @PostMapping(value = Path.USER_GROUP_USERS_ROLE_MAPS)
    public RestResponse saveUsersByUserGroupId(@PathVariable String userGroupId, @RequestBody String[] userIdList) {

        return new RestResponse(userGroupUserMapService.saveUsersByUserGroupId(userGroupId, userIdList));
    }

    /**
     * <pre>
     * 사용자그룹 사용자 매핑 삭제
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 사용자 매핑 삭제")
    @DeleteMapping(value = Path.USER_GROUP_USERS_ROLE_MAPS)
    public RestResponse deleteUsersByUserGroupId(@PathVariable String userGroupId, @RequestBody String[] userIdList) {

        return new RestResponse(userGroupUserMapService.deleteUsersByUserGroupId(userGroupId, userIdList));
    }

    /**
     * <pre>
     * 사용자그룹 권한 매핑 조회
     * </pre>
     */
    @ApiOperation(value = "사용자그룹 권한 매핑 조회")
    @GetMapping(value = Path.USER_GROUP_USERS_ROLE_MAPS)
    public RestResponse findRoleByUserGroupId(@PathVariable String userGroupId) {

        return new RestResponse(roleMapService.findRoleMapByUserGroupId(userGroupId));
    }
}
