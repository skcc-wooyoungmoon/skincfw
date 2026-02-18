/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.web;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
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

import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.service.CodeGroupService;
import com.skiaf.bcm.code.domain.service.dto.CodeSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.controller.AbstractBCMController;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * BCM 코드 그룹 관리 Controller
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Api(tags = "코드 관리 - 코드 그룹")
@RestController
public class CodeGroupController extends AbstractBCMController {

    @Autowired
    private CodeGroupService codeGroupService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_CODE_GROUPS_CREATE)
    public ModelAndView codeGroupCreate() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.code.*");
        modelAndView.setViewName("skiaf/view/code/codegroup-save-popup");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_CODE_GROUPS_SELECT)
    public ModelAndView codeGroupPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.code.*");
        modelAndView.setViewName("skiaf/view/code/codegroup-select-popup");
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "코드 그룹 조회")
    @GetMapping(value = Path.CODE_GROUPS_DETAIL)
    public RestResponse findOne(@PathVariable String codeGroupIdBase64) {

        String codeGroupId = Util.decodeUrlAndBase64(codeGroupIdBase64);
        return new RestResponse(codeGroupService.findOne(codeGroupId));
    }

    @ApiOperation(value = "코드 그룹 저장")
    @PostMapping(value = Path.CODE_GROUPS)
    public RestResponse create(@Valid @RequestBody CodeGroup codeGroup) {

        return new RestResponse(codeGroupService.create(codeGroup));
    }

    @ApiOperation(value = "코드 그룹 수정")
    @PutMapping(value = Path.CODE_GROUPS_DETAIL)
    public RestResponse update(@Valid @PathVariable @NotBlank(message = "bcm.code.codegroup.valid.id") String codeGroupIdBase64,
        @Valid @RequestBody CodeGroup codeGroup) {

        String codeGroupId = Util.decodeUrlAndBase64(codeGroupIdBase64);
        return new RestResponse(codeGroupService.update(codeGroupId, codeGroup));
    }

    @ApiOperation(value = "코드 목록 조회")
    @GetMapping(value = Path.CODE_GROUPS)
    public RestResponse findQueryBySearch(@Valid CodeSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        return new RestResponse(codeGroupService.findQueryBySearch(search, pageable));
    }
}
