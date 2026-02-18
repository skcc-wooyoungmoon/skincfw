/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.element.domain.service.ElementService;
import com.skiaf.bcm.element.domain.service.dto.ElementDetailDTO;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 *
 * BCM 프로그램요소 관리 Controller
 *
 * History
 * - 2018. 8. 09. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Api(tags = "프로그램요소 관리")
@RestController
public class ElementController {

    private static final String BCM_ELEMENT_ASTERISK = "bcm.element.*";

    @Autowired
    private ElementService elementService;

    @Autowired
    private RoleMapService roleMapService;
    
    @Autowired
    private MessageComponent messageComponent;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_ELEMENT)
    public ModelAndView elementDetail() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ELEMENT_ASTERISK);
        modelAndView.setViewName("skiaf/view/element/element-detail");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ELEMENT_ROLE)
    public ModelAndView elementRole(@PathVariable String programId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ELEMENT_ASTERISK);
        modelAndView.setViewName("skiaf/view/element/element-role-detail");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ELEMENT_CREATE)
    public ModelAndView elementCreate() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ELEMENT_ASTERISK);
        modelAndView.setViewName("skiaf/view/element/element-create-popup");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ELEMENT_UPDATE)
    public ModelAndView elementUpdate() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ELEMENT_ASTERISK);
        modelAndView.setViewName("skiaf/view/element/element-update-popup");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_ELEMENT_ROLE_POPUP)
    public ModelAndView elementRolePopup() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_ELEMENT_ASTERISK);
        modelAndView.setViewName("skiaf/view/element/element-role-popup");

        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 프로그램요소 등록
     * </pre>
     */
    @ApiOperation(value = "프로그램요소 등록")
    @PostMapping(value = Path.ELEMENT)
    public RestResponse create(
            @ApiParam(name = "element", required = true, value = "프로그램 요소 정보")
            @PathVariable(required = true) String programId,
            @RequestBody ElementDetailDTO elementDetailDTO) {

        return new RestResponse(elementService.create(programId, elementDetailDTO));
    }

    /**
     * <pre>
     * 프로그램 아이디로 목록 조회
     * </pre>
     */
    @ApiOperation(value = "프로그램 아이디로 목록 조회")
    @GetMapping(value = Path.ELEMENT)
    public RestResponse findByProgramProgramId(
            @ApiParam(name = "programId", required = true, value = "프로그램 아이디") @PathVariable(required = true) String programId) {

        return new RestResponse(elementService.findByProgramProgramId(programId));
    }
    
    @ApiOperation(value = "프로그램요소 순번 조회")
    @GetMapping(value = Path.ELEMENT_DETAIL)
    public RestResponse findOne(
            @ApiParam(name = "elementSeq", required = true, value = "프로그램 요소 순번") @PathVariable(required = true) Long elementSeq) {

        return new RestResponse(elementService.findOne(elementSeq));
    }

    /**
     * <pre>
     * 프로그램요소 수정
     * </pre>
     */
    @ApiOperation(value = "프로그램요소 수정")
    @PutMapping(value = Path.ELEMENT_DETAIL)
    public RestResponse update(
            @ApiParam(name = "programId", required = true, value = "프로그램 아이디") @PathVariable(required = true) String programId,
            @ApiParam(name = "elementSeq", required = true, value = "프로그램 요소 순번") @PathVariable(required = true) Long elementSeq,
            @ApiParam(name = "elementDetailDTO", required = true, value = "프로그램 요소") @RequestBody ElementDetailDTO elementDetailDTO) {

        return new RestResponse(elementService.update(programId, elementSeq, elementDetailDTO));
    }

    /**
     * <pre>
     * 프로그램요소 중복체크
     * </pre>
     */
    @ApiOperation(value = "프로그램요소 중복체크")
    @GetMapping(value = Path.ELEMENT_DUPLICATE)
    public RestResponse duplicateElement(
            @ApiParam(name = "programId", required = true, value = "프로그램 아이디") @PathVariable(required = true) String programId,
            @ApiParam(name = "elementKey", required = true, value = "프로그램 요소") @PathVariable(required = true) String elementKey) {

        RestResponse restResponse = new RestResponse();

        Boolean isDuplicate = elementService.duplicateElement(programId, elementKey);
        restResponse.setData(isDuplicate);

        if (isDuplicate) {
            restResponse.setUserMessage(messageComponent.getMessage("bcm.common.DUPLICATE"));
        }

        return restResponse;
    }

    /**
     * <pre>
     * 프로그램요소 권한 매핑 저장
     * </pre>
     */
    @ApiOperation(value = "프로그램요소 권한 매핑 저장")
    @PostMapping(value = Path.ELEMENT_ROLE_MAPS)
    public RestResponse saveElementByRoleId(@PathVariable String programId,
            @RequestBody List<ElementRoleMapDTO> elementRoleMapDTO) {

        return new RestResponse(roleMapService.saveElementsByMapIdRoleId(elementRoleMapDTO));
    }

    /**
     * <pre>
     * 프로그램요소 권한 매핑 삭제
     * </pre>
     */
    @ApiOperation(value = "프로그램요소 권한 매핑 삭제")
    @DeleteMapping(value = Path.ELEMENT_ROLE_MAPS)
    public RestResponse deleteElementByRoleId(@PathVariable String programId,
            @RequestBody List<ElementRoleMapDTO> elementRoleMapDTO) {

        return new RestResponse(roleMapService.deleteElementsByMapIdRoleId(elementRoleMapDTO));
    }

}