/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.web;

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

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.MessageService;
import com.skiaf.bcm.i18n.domain.service.dto.MessageSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * BCM 메시지 관리 Controller
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Api(tags = "메시지 관리")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_MESSAGES)
    public ModelAndView messageList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.message.*");
        modelAndView.setViewName("skiaf/view/i18n/message-list");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_MESSAGES_SAVE)
    public ModelAndView messageCreate() {

        return new ModelAndView("skiaf/view/i18n/message-save-popup");
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "메시지 조회")
    @GetMapping(value = Path.MESSAGES_DETAIL)
    public RestResponse findOne(@PathVariable String messageKeyBase64) {

        String messageKey = Util.decodeUrlAndBase64(messageKeyBase64);
        return new RestResponse(messageService.findOne(messageKey));
    }

    @ApiOperation(value = "메시지 목록 조회")
    @GetMapping(value = Path.MESSAGES)
    public RestResponse findQueryBySearch(MessageSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        return new RestResponse(messageService.findQueryBySearch(search, pageable));
    }

    @ApiOperation(value = "메시지 저장")
    @PostMapping(value = Path.MESSAGES)
    public RestResponse create(@RequestBody Message message) {

        RestResponse restResponse = new RestResponse(messageService.create(message));
        return restResponse;
    }

    @ApiOperation(value = "메시지 수정")
    @PutMapping(value = Path.MESSAGES_DETAIL)
    public RestResponse update(@PathVariable String messageKeyBase64, @RequestBody Message message) {
        
        String messageKey = Util.decodeUrlAndBase64(messageKeyBase64);
        RestResponse restResponse = new RestResponse(messageService.update(messageKey, message));
        return restResponse;
    }

}
