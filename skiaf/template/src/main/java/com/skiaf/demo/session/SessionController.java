/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <pre>
 * 암호화 데모 컨트롤러
 * 
 * History
 * - 2018. 8. 27. | in01868 | 최초작성.
 * </pre>
 */
@Profile({"default","dev"})       // default 와 dev 프로파일에서만 동작.
@Api(tags = "데모-세션")
@RestController
public class SessionController {
    
   /* @Autowired
    RedisOperationsSessionRepository sessionRepository;*/
    
    /*@Autowired
    HttpSessionre httpSessionSecurityContextRepository;*/
    
    @Value("${server.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @ApiOperation(value = "현재 세션 조회")
    @GetMapping("/api/demo/session")
    public RestResponse getSession(@ApiIgnore HttpSession session) {

        Enumeration<String> attributeNames = session.getAttributeNames();
        
        Map<String, Object> attributeList = new HashMap<>();
        while(attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            attributeList.put(attributeName, attributeValue);
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", session.getId());
        map.put("session maxInactiveInterval", session.getMaxInactiveInterval());
        map.put("attributeList", attributeList);
        
        return new RestResponse(map);
    }
    
    @ApiOperation(value = "다른 세션 조회")
    @GetMapping("/api/demo/session/{sessionId}")
    public RestResponse getSessionDetail(@ApiIgnore HttpSession session, @PathVariable String sessionId) {
        
        Map<String, Object> map = new HashMap<>();
        map.put("now sessionId", session.getId());
        map.put("another sessionId", sessionId);
        
        /*if(sessionRepository == null) {
            return new RestResponse(map);
        }
        
        ExpiringSession anotherSession = sessionRepository.getSession(sessionId);
        
        if (anotherSession == null) {
            map.put("another expireTime", "expired");    
        }else {
            Date date = new Date();
            int diffTime = (int)(maxInactiveIntervalInSeconds -( ((date.getTime() - anotherSession.getLastAccessedTime())) / 1000));
            map.put("another expireTime", diffTime);
            
            Set<String> attributeNames = anotherSession.getAttributeNames();
            Map<String, Object> attributeList = new HashMap<>();
            attributeNames.forEach(attributeName -> {
                Object attributeValue = session.getAttribute(attributeName);
                attributeList.put(attributeName, attributeValue);
            });
            map.put("another attributeList", attributeList);
        }*/
        
        return new RestResponse(map);
    }

}
