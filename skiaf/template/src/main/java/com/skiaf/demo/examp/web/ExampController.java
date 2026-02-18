/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.examp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.vo.RestResponse;
import com.skiaf.demo.examp.domain.service.ExampService;
import com.skiaf.demo.examp.domain.service.dto.ExampFormatDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * 엑셀, LOV들 셈플 page 처리
 *
 * History
 * - 2018. 8. 2. | in01943 | 최초작성.
 * </pre>
 */
@Api(tags = "셈플")
@RestController
public class ExampController {

    private static final String BCM_CODE_ASTERISK  = "bcm.code.*";
    private static final String BCM_EXAMP_ASTERISK = "bcm.examp.*";
    private static final String BCM_COMMON_SUCCESS = "bcm.common.SUCCESS";

    @Autowired
    private ExampService exampService;

    @Autowired
    private MessageComponent messageComponent;

    @ApiOperation(value = "LOV 화면")
    @GetMapping(value = "/view/examp/examp-lov")
    public ModelAndView viewExampLov() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_CODE_ASTERISK);
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_EXAMP_ASTERISK);
        modelAndView.setViewName("skiaf/view/examp/examp-lov");
        return modelAndView;
    }

    @ApiOperation(value = "엑셀 테스트 화면")
    @GetMapping(value = "/view/examp/examp-excel-export")
    public ModelAndView viewExampExcelExport() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_EXAMP_ASTERISK);
        modelAndView.setViewName("skiaf/view/examp/examp-excel-export");
        return modelAndView;
    }

    @ApiOperation(value = "전문 테스트 화면")
    @GetMapping(value = "/view/examp/examp-format")
    public ModelAndView viewExampFormat() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_EXAMP_ASTERISK);
        modelAndView.setViewName("skiaf/view/examp/examp-format");
        return modelAndView;
    }

    @ApiOperation(value = "Config 파일 read")
    @PostMapping(value = "/api/examp-format-readconfig")
    public RestResponse readConfigFile(@RequestBody ExampFormatDTO dto) {
        RestResponse restResponse = new RestResponse();
        restResponse.setData(exampService.readConfigFile(dto));
        restResponse.setUserMessage(messageComponent.getMessage(BCM_COMMON_SUCCESS));
        return restResponse;
    }

    @ApiOperation(value = "data 전문 변환")
    @PostMapping(value = "/api/examp-format")
    public RestResponse convertData(@RequestBody ExampFormatDTO dto) {
        return new RestResponse(exampService.convertData(dto));
    }

}
