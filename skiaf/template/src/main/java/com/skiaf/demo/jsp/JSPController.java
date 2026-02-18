/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * JSP 데모 컨트롤러
 * 
 * History
 * - 2018. 10. 17. | in01868 | 최초작성.
 * </pre>
 */
@Controller
public class JSPController {

    @GetMapping(value = "/jsp/test")
    public ModelAndView jspView1(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "JSP demo");
        modelAndView.setViewName("jsp/jsp-page");
        return modelAndView;
    }
}
