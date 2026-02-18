/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.role;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 *
 * History
 * - 2018. 8. 27. | in01876 | 최초작성.
 * </pre>
 */
@Profile({"default","dev"})       // default 와 dev 프로파일에서만 동작.
@Api(tags = "권한관리(TEST)")
@RestController
public class RoleTestController {

    private static final String BCM_COMMON_SUCCESS = "bcm.common.SUCCESS";
    private static final String BCM_COMMON_EXCEPTION_BIZ = "bcm.common.exception.biz";
    private static final String BCM_COMMON_EXCEPTION_UNAUTHORIZED = "bcm.common.exception.unauthorized";

    @Autowired
    RoleMapService roleMapService;

    @Autowired
    private MessageComponent messageComponent;

    @ApiOperation(value = "[TEST] 활성화 상태의 RoleMap List 조회")
    @GetMapping(value = "/api/test/users/{userId}/active/roles")
    public RestResponse activeRoleList(
            @ApiParam(name = "userId", required = true, value = "User ID", defaultValue = "sungnam-cuid-1") @PathVariable String userId,
            @ApiParam(name = "betweenDate", required = true, value = "betweenDate", defaultValue = "20180801") @RequestParam String betweenDate) {
        if (StringUtils.isBlank(betweenDate)) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            betweenDate = currentDate.toString(fmt);
        }
        return new RestResponse(roleMapService.findActivationRoleMapByUserId(userId, betweenDate));
    }

    @ApiOperation(value = "[TEST] 업무 처리 상세 권한 테스트")
    @GetMapping(value = "/api/test/roles/biz-role")
    public RestResponse testBizProcess() {

        // 권한이 있는지 확인
        if (!SessionUtil.hasUserAuth("ABBCM00001")) {
            throw BizException.withUserMessageKey(BCM_COMMON_EXCEPTION_BIZ).build();
        }
        return new RestResponse(messageComponent.getMessage(BCM_COMMON_SUCCESS));
    }

}
