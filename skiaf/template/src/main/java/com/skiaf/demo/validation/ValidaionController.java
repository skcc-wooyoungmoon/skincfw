/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.validation;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.controller.AbstractBCMController;
import com.skiaf.core.validation.groups.Update;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 벨리데이션 데모 컨트롤러
 * 
 * History
 * - 2018. 8. 27. | in01868 | 최초작성.
 * </pre>
 */
@Profile({"default","dev"})       // default 와 dev 프로파일에서만 동작.
@Api(tags = "데모-벨리데이션")
@RestController
public class ValidaionController extends AbstractBCMController {

    @ApiOperation(value = "벨리데이션 체크")
    @PostMapping(value = "/api/demo/validations/check")
    public RestResponse postValidation(
            @ApiParam(name = "validation", value = "validation정보") @Validated @RequestBody ValidationModel validation) {
        return new RestResponse(validation);
    }
    
    @ApiOperation(value = "벨리데이션 리스트 체크")
    @PostMapping(value = "/api/demo/validations/checkList")
    public RestResponse postValidationList(
            @ApiParam(name = "validation", value = "validation정보") @Validated @RequestBody List<ValidationModel> validationList) {
        return new RestResponse(validationList);
    }
    
    @ApiOperation(value = "벨리데이션 체크 - (Update그룹)")
    @PutMapping(value = "/api/demo/validations/check")
    public RestResponse putValidation(
            @ApiParam(name = "validation", value = "validation정보") @Validated(Update.class) @RequestBody ValidationModel validation) {
        return new RestResponse(validation);
    }
    
    @ApiOperation(value = "벨리데이션 리스트 체크 - (Update그룹)")
    @PutMapping(value = "/api/demo/validations/checkList")
    public RestResponse putValidationList(
            @ApiParam(name = "validation", value = "validation정보") @Validated(Update.class) @RequestBody List<ValidationModel> validationList) {
        return new RestResponse(validationList);
    }

}
