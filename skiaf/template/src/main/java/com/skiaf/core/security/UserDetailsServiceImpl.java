/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${bcm.password.change-cycle-day}")
    private long passwordChangeCycleDay;

    @Autowired
    private UserService userService;

    /**
     * <pre>
     * 사용자 조회 오버라이드
     * </pre>
     * 
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        User user = userService.findByLoginId(id);

        if (user == null) {
            throw new UsernameNotFoundException(id);
        } 

        UserInfoDTO userInfo = userService.getUserInfoByUser(user);

        return new UserDetailsImpl(userInfo);
    }
}
