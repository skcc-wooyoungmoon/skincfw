/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.code.domain.service.CodeService;
import com.skiaf.bcm.code.domain.service.dto.CodeUpdateDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.controller.AbstractBCMController;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * BCM 코드 관리 Controller
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Api(tags = "코드 관리 - 코드")
@RestController
public class CodeController extends AbstractBCMController {

    @Autowired
    private CodeService codeService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_CODES)
    public ModelAndView codeList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.code.*");
        modelAndView.setViewName("skiaf/view/code/code-list");
        return modelAndView;
    }

//    @GetMapping(value = Path.VIEW_CODES_CREATE)
//    public ModelAndView codeCreate() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.code.*");
//        modelAndView.setViewName("skiaf/view/code/code-create");
//        return modelAndView;
//    }

    @GetMapping(value = Path.VIEW_CODESLOV_POPUP_SELECT)
    public ModelAndView viewCodeLovSelectPopup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.code.");
        modelAndView.setViewName("skiaf/view/code/codelov-select-popup");
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "현재 언어에 따른 코드값 목록 조회")
    @GetMapping(value = Path.CODES_DETAIL_LANG)
    public RestResponse findByCodeGroupAndCurrentLang(@PathVariable String codeGroupId) {

        return new RestResponse(codeService.findByCodeGroupDetail(codeGroupId));
    }

    @ApiOperation(value = "코드 저장")
    @PostMapping(value = Path.CODES)
    public RestResponse create(@Valid @RequestBody CodeUpdateDTO codeUpdateDTO) {

        return new RestResponse(codeService.createList(codeUpdateDTO.getSaveList(), codeUpdateDTO.getCodeGroupId()));
    }

    @ApiOperation(value = "코드 수정")
    @PutMapping(value = Path.CODES)
    public RestResponse update(@Valid @RequestBody CodeUpdateDTO codeUpdateDTO) {

        return new RestResponse(codeService.updateList(codeUpdateDTO));
    }

    @ApiOperation(value = "코드 목록 조회")
    @GetMapping(value = Path.CODES)
    public RestResponse findQueryBySearch(String codeGroupId,
            @SortDefault.SortDefaults({@SortDefault(sort = "codeSortSeq", direction = Sort.Direction.ASC)}) Sort sort) {

        return new RestResponse(codeService.findQueryByCodeGroupId(codeGroupId, sort));
    }
}
