/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.file;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Profile(value = {"default", "dev"})
@Api(tags = "파일(TEST)")
@RestController
public class FileTestController {

    @ApiOperation(value = "파일 테스트 화면")
    @GetMapping(value = "/view/examp/examp-file")
    public ModelAndView viewExampFile() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/examp/examp-file");
        return modelAndView;
    }
}
