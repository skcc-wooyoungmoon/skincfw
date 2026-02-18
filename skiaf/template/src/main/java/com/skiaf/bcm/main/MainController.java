/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;

/**
 * <pre>
 * 메인 컨트롤러
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Controller
public class MainController {
    
    @Value("${bcm.ui.home.url}")
    private String bcmUIHomeUrl;
    
    /**
     * <pre>
     * 루트 페이지
     * </pre>
     */
    @GetMapping(value = Path.VIEW_ROOT)
    public ModelAndView root() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/index");
        modelAndView.addObject("bcmUIHomeUrl", bcmUIHomeUrl);
        return modelAndView;
    }
    
    /**
     * <pre>
     * BCM 메인페이지
     * </pre>
     */
    @GetMapping(value = "${bcm.home.url}")
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.main.*");
        modelAndView.setViewName("skiaf/view/main");
        return modelAndView;
    }
}
