/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.log.domain.service.LoggingService;
import com.skiaf.bcm.log.domain.service.dto.LoggingSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * BCM 로그 보기 Controller
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Api(tags = "로그 보기")
@RestController
public class LoggingController {
    
    @Autowired
    LoggingService loggingService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "기본 로그 화면")
    @GetMapping(value = Path.VIEW_SYSTEM_LOGS)
    public ModelAndView systemLogList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/log/system-log-list");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.log.system.*");
        return modelAndView;
    }

    @ApiOperation(value = "기본 로그 상세 화면")
    @GetMapping(value = Path.VIEW_SYSTEM_LOGS_POPUPS_DETAIL)
    public ModelAndView systemLogDetail() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/log/system-log-detail-popup");  
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.log.system.*");
        return modelAndView;
    }

    @ApiOperation(value = "이벤트 로그 화면")
    @GetMapping(value = Path.VIEW_EVENT_LOGS)
    public ModelAndView eventLogList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/log/event-log-list");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.log.event.*");
        return modelAndView;
    }

    @ApiOperation(value = "이벤트 로그 상세 화면")
    @GetMapping(value = Path.VIEW_EVENT_LOGS_POPUPS_DETAIL)
    public ModelAndView eventLogDetail() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/log/event-log-detail-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.log.event.*");
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 로그 목록 조회
     * </pre>
     */
    @ApiOperation(value = "로그 목록 조회", notes = "로그 목록 조회")
    @GetMapping(value = Path.LOGS)
    public RestResponse findQueryBySearch(
            @ApiParam(name = "search object", required = true, value = "로그 검색 정보") LoggingSearchDTO loggingSearchDTO,
            @PageableDefault(sort = { "event_id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        
        if (loggingSearchDTO.isList()) {
            return new RestResponse(loggingService.findQueryBySearch(loggingSearchDTO, pageable.getSort()));
        } else {
            return new RestResponse(loggingService.findQueryBySearch(loggingSearchDTO, pageable));
        }
    }
}
