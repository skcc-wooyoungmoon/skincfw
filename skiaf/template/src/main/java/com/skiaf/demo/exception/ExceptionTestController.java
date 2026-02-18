/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.ForbiddenException;
import com.skiaf.core.exception.InterfaceException;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.UnauthorizedException;
import com.skiaf.core.exception.ValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "데모-예외처리(TEST)")
@RestController
public class ExceptionTestController {

    @ApiOperation(value = "BizException 테스트")
    @GetMapping(value = "/api/demo/exception/bizException")
    public void bizException() {
        throw BizException.withSystemMessage("!RoleType.MENU.getName().equals(role.getRoleType())").build();
    }

    @ApiOperation(value = "ForbiddenException 테스트")
    @GetMapping(value = "/api/demo/exception/forbiddenException")
    public void forbiddenException() {
        throw ForbiddenException.withSystemMessage("userRoleList == null").build();
    }

    @ApiOperation(value = "InterfaceException 테스트")
    @GetMapping(value = "/api/demo/exception/interfaceException")
    public void interfaceException() {
        throw InterfaceException.withSystemMessage("str.getBytes().length=0, str.getBytes().length=0").build();
    }

    @ApiOperation(value = "NotFoundException 테스트")
    @GetMapping(value = "/api/demo/exception/notFoundException")
    public void notFoundException() {
        throw NotFoundException.withSystemMessage("existProgramList.isEmpty").build();
    }

    @ApiOperation(value = "UnauthorizedException 테스트")
    @GetMapping(value = "/api/demo/exception/unauthorizedException")
    public void unauthorizedExceptionWithUserMessageKey() {
        throw UnauthorizedException.withSystemMessage("user == null").build();
    }

    @ApiOperation(value = "ValidationException 테스트")
    @GetMapping(value = "/api/demo/exception/validationException")
    public void validationExceptionWithUserMessageKey() {
        throw ValidationException.withSystemMessage("this.duplicateCheck(role.getRoleId())").build();
    }
}
