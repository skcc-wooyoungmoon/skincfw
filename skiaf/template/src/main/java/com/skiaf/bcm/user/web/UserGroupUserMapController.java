/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.bcm.user.domain.service.UserGroupUserMapService;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 
 * History
 * - 2018. 7. 23. | in01876 | 최초작성.
 * </pre>
 */
@Api(tags = "UserGroupUserMapController(TEST)")
@RestController
public class UserGroupUserMapController {

    @Autowired
    UserGroupUserMapService userGroupUserMapService;

    @ApiOperation(value = "사용자그룹에 소속되어있는 사용자(목록) 조회")
    @GetMapping(value = "/api/usergroups/{userGroupId}/map/users1")
    public RestResponse findByMapIdUserGroupId(
            @ApiParam(
                name = "userGroupId", 
                required = true, 
                value = "사용자그룹 ID", 
                defaultValue = "GRP_DEV1") 
            @RequestParam(required = true) String userGroupId ) {
        return new RestResponse(userGroupUserMapService.findByMapIdUserGroupId(userGroupId));
    }

    @ApiOperation(value = "userGroupId 에 user list 매핑(저장)")
    @PostMapping(value = "/api/usergroups/{userGroupId}/map/users1")
    public RestResponse saveUsersByUserGroupId(
            @ApiParam(
                    name = "userGroupId", 
                    required = true, 
                    value = "userGroupId", 
                    defaultValue = "GRP_DEV1") 
            @PathVariable String userGroupId,
            @RequestParam String[] userIdList) {

        return new RestResponse(userGroupUserMapService.saveUsersByUserGroupId(userGroupId, userIdList));
    }

    @ApiOperation(value = " userGroupId 에 매핑된 user list 삭제")
    @DeleteMapping(value = "/api/usergroups/{userGroupId}/map/users1")
    public RestResponse deleteUsersByUserGroupId(
            @ApiParam(
                    name = "userGroupId", 
                    required = true, 
                    value = "userGroupId", 
                    defaultValue = "GRP_DEV1") 
            @PathVariable String userGroupId,
            @RequestParam String[] userIdList) {

        return new RestResponse(userGroupUserMapService.deleteUsersByUserGroupId(userGroupId, userIdList));
    }
}
