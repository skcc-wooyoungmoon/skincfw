/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * User 관련 테스트 컨트롤러
 * 
 * History
 * - 2018. 8. 27. | in01876 | 최초작성.
 * </pre>
 */
@Profile({"default","dev"})       // default 와 dev 프로파일에서만 동작.
@Api(tags = "사용자 관리(TEST)")
@RestController
public class UserTestController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "[TEST] 비밀번호 변경")
    @GetMapping(value = "/api/test/users/{userId}/change/password")
    public RestResponse activeRoleList(
            @ApiParam(name = "userId", required = true, value = "User ID", defaultValue = "sungnam-cuid-1") @PathVariable String userId,
            @ApiParam(name = "prePassword", required = true, value = "이전 비번", defaultValue = "!1q2w3e4r") @RequestParam String prePassword,
            @ApiParam(name = "newPassword", required = true, value = "새로운비번", defaultValue = "!1q2w3e4r5t") @RequestParam String newPassword) {

        // 본인여부 판단 할것
        // User user = SessionUtil.getLoginUser();
        // if(!user.getUserId().equals(userId)) {
        // throw
        // ValidationException.withUserMessageKey("bcm.common.exception.forbidden").build();
        // }

        // TEST1 - 사용자체크
        // userId = "null_userid";

        // TEST2 - 이전 패스워드 동일여부 체크
        // prePassword = "testPw";

        // TEST3 - 이전 패스워드와 변경할 패스워드가 같은지 체크
        // prePassword = "!1q2w3e4r";
        // newPassword = "!1q2w3e4r";

        // TEST4 - 패스워드 조합정책 체크
        // 1. 특수문자 없을 시 10자리 이상
        // newPassword = "2w3e4r5t";

        // 2. 특수문자 포함 시 8자리 이상
        // newPassword = "2w3e4r!";

        // TEST5 - 3자가 추측하기 쉬운 개인정보 포함 체크
        // newPassword = "core279!1q2w";

        // TEST6 - 동일한 글자 3번연속 입력 체크
        // newPassword - !1qqq2w3e4r

        // TEST7 - 두자이상의 동일문자 두번연속 입력 체크
        // newPassword - !1q2w1q3e4r

        // TEST8 - 특정 패턴을 갖는 패스워드 사용 금지
        // 1. 숫자연속성
        // newPassword = "!1q23456qf";

        // 2. 문자연속성
        // newPassword = "!1q2werty";

        return new RestResponse(userService.passWordChange(userId, prePassword, newPassword, false));
    }

}
