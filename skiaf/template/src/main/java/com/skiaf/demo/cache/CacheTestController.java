/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.bcm.i18n.domain.service.MessageService;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 
 * History
 * - 2018. 9. 12. | in01865 | 최초작성.
 * </pre>
 */
@Api(tags = "캐시관리(TEST)")
@RestController
public class CacheTestController {

    @Autowired
    MessageService messageService;

    @ApiOperation(value = "[TEST] 캐시가 작동되는 메세지 조회")
    @GetMapping(value = "/api/test/messages/{messageKey}")
    public RestResponse findOne(
            @ApiParam(
                    name = "messageKey", 
                    required = true, 
                    defaultValue = "bcm.common.login-user-info"
            ) 
            @PathVariable String messageKey ) {
        
        return new RestResponse(messageService.findOneCached(messageKey));
    }
    
    @ApiOperation(value = "[TEST] 메세지 캐시를 모두 제거")
    @GetMapping(value = "/api/test/messages/commands/delete-cache-all")
    public RestResponse deleteAll() {
        messageService.deleteCacheAll();
        
        return new RestResponse(true);
    }

}
