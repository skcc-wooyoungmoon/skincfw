/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.login.domain.service.dto.PasswordChangeUserDTO;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.UserGroupUserMapService;
import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.bcm.user.domain.service.dto.UserUserGroupMapSaveDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.UnauthorizedException;
import com.skiaf.core.util.BCMEventLogger;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BCM 사용자 관리 Controller
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Slf4j
@Api(tags = "사용자 관리")
@RestController
public class UserController {

    private static final String BCM_USER_ASTERISK = "bcm.user.*";

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupUserMapService userGroupUserMapService;

    @Autowired
    private RoleMapService roleMapService;

    @Autowired
    private MessageComponent messageComponent;
    
    @Value("${bcm.password.suffix}")
    private String bcmPasswordSuffix;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "사용자 상세 화면")
    @GetMapping(value = Path.VIEW_USERS_DETAIL)
    public ModelAndView userList(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-detail");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "사용자 검색 팝업(팝업)")
    @GetMapping(value = Path.VIEW_USERS_SEARCH)
    public ModelAndView userSelectPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-search-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "사용자 등록 화면(팝업)")
    @GetMapping(value = Path.VIEW_USERS_CREATE)
    public ModelAndView userCreate() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-create-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        modelAndView.addObject("bcmPasswordSuffix", bcmPasswordSuffix);
        return modelAndView;
    }

    @ApiOperation(value = "사용자 수정 화면(팝업)")
    @GetMapping(value = Path.VIEW_USERS_UPDATE)
    public ModelAndView userUpdate() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-update-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "사용자 비밀번호 변경 화면(팝업)")
    @GetMapping(value = Path.VIEW_USERS_PWCHANGE)
    public ModelAndView userPWChange() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-pwchange-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "사용자 그룹 매핑 화면(팝업)")
    @GetMapping(value = Path.VIEW_USERS_GROUP_MAP)
    public ModelAndView userGroupMap() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/user/user-group-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_USER_ASTERISK);
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "사용자 검색 페이징 목록 조회")
    @GetMapping(value = Path.USERS)
    public RestResponse findQueryByKeyword(UserSearchDTO search,
            @PageableDefault(page = 0, size = 10, sort = "updateDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return new RestResponse(userService.findQueryByKeyword(search, pageable));
    }
    
    @ApiOperation(value = "사용자 등록")
    @PostMapping(value = Path.USERS)
    public RestResponse create(
            @ApiParam(name = "created user object", required = true, value = "사용자 정보") @Valid @RequestBody User user) {

        BCMEventLogger.withLogger(log).putUserEvent("create-try");

        RestResponse restResponse = new RestResponse();
        User createdUser = userService.create(user);
        restResponse.setData(createdUser);

        BCMEventLogger.withLogger(log).putUserEvent("create-success", createdUser.getLoginId());
        return restResponse;
    }

    @ApiOperation(value = "사용자 정보 조회")
    @GetMapping(value = Path.USERS_DETAIL)
    public RestResponse findOne(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {

        return new RestResponse(userService.findOne(userId));
    }
    

    @ApiOperation(value = "사용자 수정")
    @PutMapping(value = Path.USERS_DETAIL)
    public RestResponse update(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId,
            @ApiParam(name = "update user object", required = true, value = "사용자 정보") @Valid @RequestBody User user) {

        BCMEventLogger.withLogger(log).putUserEvent("update-detail-try", userId);
        RestResponse restResponse = new RestResponse(userService.update(userId, user));

        BCMEventLogger.withLogger(log).putUserEvent("update-detail-success", userId);

        return restResponse;
    }

    @ApiOperation(value = "사용자 상세 조회(매핑정보 포함)")
    @GetMapping(value = Path.USERS_DETAIL_INFO)
    public RestResponse findOneDetail(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {

        RestResponse restResponse = new RestResponse();

        Map<String, Object> data = new HashMap<>();

        // 본인 정보
        data.put("myInfo", userService.findOne(SessionUtil.getLoginUser().getUserId()));

        // 사용자 정보
        data.put("userInfo", userService.findOne(userId));

        // 사용자가 소속되어있는 그룹정보(useYn = true)
        List<UserGroup> activeUserGroupList = new ArrayList<>();
        List<UserGroup> userGroupList = userGroupUserMapService.findByMapIdUserId(userId);
        userGroupList.forEach((UserGroup userGroup) -> {
            if (userGroup != null && userGroup.isUseYn()) {
                activeUserGroupList.add(userGroup);
            }
        });

        data.put("userGroupMapList", activeUserGroupList);

        // 활성화 상태의 권한매핑 정보만 조회한다.
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        List<Role> roleMapList = roleMapService.findActivationRoleMapByUserId(userId, currentDate.toString(fmt));

        data.put("roleMapList", roleMapList);

        restResponse.setData(data);

        return restResponse;
    }
    
    @ApiOperation(value = "사용자 본인 상세 조회(매핑정보 포함)")
    @GetMapping(value = Path.USERS_DETAIL_INFO_MY)
    public RestResponse findOneDetailMy() {
        
        if (SessionUtil.getLoginUser() == null) {
            throw UnauthorizedException.withUserMessageKey("bcm.common.exception.unauthorized")
                    .withSystemMessage("user == null").build();
        }
        
        String userId = SessionUtil.getLoginUser().getUserId();
        RestResponse restResponse = new RestResponse();

        Map<String, Object> data = new HashMap<>();

        // 본인 정보
        data.put("myInfo", userService.findOne(userId));

        // 사용자 정보
        data.put("userInfo", userService.findOne(userId));

        // 사용자가 소속되어있는 그룹정보(useYn = true)
        List<UserGroup> activeUserGroupList = new ArrayList<>();
        List<UserGroup> userGroupList = userGroupUserMapService.findByMapIdUserId(userId);
        userGroupList.forEach((UserGroup userGroup) -> {
            if (userGroup != null && userGroup.isUseYn()) {
                activeUserGroupList.add(userGroup);
            }
        });

        data.put("userGroupMapList", activeUserGroupList);

        // 활성화 상태의 권한매핑 정보만 조회한다.
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        List<Role> roleMapList = roleMapService.findActivationRoleMapByUserId(userId, currentDate.toString(fmt));

        data.put("roleMapList", roleMapList);

        restResponse.setData(data);

        return restResponse;
    }


    @ApiOperation(value = "로그인 ID 중복체크")
    @GetMapping(value = Path.USER_DUPLICATE)
    public RestResponse duplicateCheck(
            @ApiParam(name = "loginId", required = true, value = "로그인 ID") @PathVariable String loginId) {

        RestResponse restResponse = new RestResponse();
        Boolean isDuplicate = userService.duplicateCheck(loginId);
        restResponse.setData(isDuplicate);
        if (isDuplicate) {
            restResponse.setUserMessage(messageComponent.getMessage("bcm.common.DUPLICATE"));
        }
        return restResponse;
    }

    

    @ApiOperation(value = "사용자 비밀번호 RESET")
    @PutMapping(value = Path.USERS_DETAIL_PWRESET)
    public RestResponse pwReset(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {

        BCMEventLogger.withLogger(log).putUserEvent("password-reset-try", userId);
        RestResponse restResponse = new RestResponse(userService.passWordReset(userId));
        BCMEventLogger.withLogger(log).putUserEvent("password-reset-success", userId);
        
        return restResponse;
    }

    @ApiOperation(value = "사용자 로그인 실패 카운드 RESET")
    @PutMapping(value = Path.USERS_DETAIL_FAILRESET)
    public RestResponse failCountReset(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {

        BCMEventLogger.withLogger(log).putUserEvent("login-fail-count-reset-try", userId);

        User user = userService.findOne(userId);
        userService.initLoginFailCountByLoginId(user.getLoginId());

        BCMEventLogger.withLogger(log).putUserEvent("login-fail-count-reset-success", userId);

        return new RestResponse(user);
    }

    @ApiOperation(value = "사용자 비밀번호 변경")
    @PatchMapping(value = Path.USERS_DETAIL_PWCHANGE)
    public RestResponse userPWChange(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId,
            @RequestBody @Validated PasswordChangeUserDTO passwordChangeUserDTO) {
        
        BCMEventLogger.withLogger(log).putUserEvent("password-change-try", userId);
        
        // 본인여부 확인
        if (!StringUtils.equals(userId, SessionUtil.getLoginUser().getUserId())) {
            throw BizException.withUserMessageKey("bcm.common.password-change.auth").build();
        }
        // 비번변경
        RestResponse restResponse = new RestResponse(userService.passWordChange(userId,
                passwordChangeUserDTO.getPrePassword(), passwordChangeUserDTO.getPassword(), true));

        BCMEventLogger.withLogger(log).putUserEvent("password-change-success", userId);

        return restResponse;
    }
    
    @ApiOperation(value = "사용자(본인) 비밀번호 변경")
    @PatchMapping(value = Path.USERS_DETAIL_PWCHANGE_MY)
    public RestResponse myPWChange(
            @RequestBody @Validated PasswordChangeUserDTO passwordChangeUserDTO) {

        if (SessionUtil.getLoginUser() == null) {
            throw UnauthorizedException.withUserMessageKey("bcm.common.exception.unauthorized")
                    .withSystemMessage("user == null").build();
        }
        
        BCMEventLogger.withLogger(log).putUserEvent("password-change-try", SessionUtil.getLoginUser().getUserId());

        // 비번변경
        RestResponse restResponse = new RestResponse(userService.passWordChange(SessionUtil.getLoginUser().getUserId(),
                passwordChangeUserDTO.getPrePassword(), passwordChangeUserDTO.getPassword(), true));

        BCMEventLogger.withLogger(log).putUserEvent("password-change-success", SessionUtil.getLoginUser().getUserId());

        return restResponse;
    }

    @ApiOperation(value = "사용자 그룹 매핑 목록 조회")
    @GetMapping(value = Path.USERS_GROUP_MAP)
    public RestResponse findUserGroupMap(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId) {

        return new RestResponse(userService.findUserGroupMap(userId));
    }

    @ApiOperation(value = "사용자 그룹 매핑 저장")
    @PatchMapping(value = Path.USERS_GROUP_MAP)
    public RestResponse saveUserGroupMap(
            @ApiParam(name = "userId", required = true, value = "사용자 ID") @PathVariable String userId,
            @ApiParam(name = "updated user group list object", required = true, value = "저장하는 사용자그룹 목록") @RequestBody UserUserGroupMapSaveDTO userUserGroupMapSaveDTO) {

        return new RestResponse(userService.saveUserGroupMap(userId, userUserGroupMapSaveDTO));
    }

}
