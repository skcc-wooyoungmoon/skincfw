/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.core.constant.Path;

/**
 * <pre>
 * 에러  컨트롤러
 * 
 * History
 * - 2018. 9. 10. | in01868 | 최초작성.
 * </pre>
 */
@Controller
public class ErrorController {
    
    /**
     * <pre>
     * 401 에러 페이지
     * </pre>
     */
    @GetMapping(value = Path.ERROR_401)
    public ModelAndView error401() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/error/error-401");
        return modelAndView;
    }
    
    /**
     * <pre>
     * 403 에러 페이지
     * </pre>
     */
    @GetMapping(value = Path.ERROR_403)
    public ModelAndView error403() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/error/error-403");
        return modelAndView;
    }

}
