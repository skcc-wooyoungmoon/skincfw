/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util.encription;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <pre>
 * 단방향 암호화 (BCrypt)
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public class PasswordHashing implements PasswordEncoder {

    private PasswordEncoder passwordEncoder;

    /**
     * <pre>
     * 생성자
     * </pre>
     */
    public PasswordHashing() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * <pre>
     * 생성자
     * </pre>
     */
    public PasswordHashing(int strength) {
        // 가중치를 주면 엄청 무거워진다... encode하는데 7초..
        // this.passwordEncoder = new BCryptPasswordEncoder(16);
        this.passwordEncoder = new BCryptPasswordEncoder(strength);
    }

    /**
     * <pre>
     * 생성자
     * </pre>
     */
    public PasswordHashing(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <pre>
     * 인코딩
     * </pre>
     * @see org.springframework.security.crypto.password.PasswordEncoder#encode(java.lang.CharSequence)
     */
    @Override
    public String encode(CharSequence plainText) {
        return passwordEncoder.encode(plainText);
    }

    /**
     * <pre>
     * 비밀번호 일치 체크
     * </pre>
     * @see org.springframework.security.crypto.password.PasswordEncoder#matches(java.lang.CharSequence, java.lang.String)
     */
    @Override
    public boolean matches(CharSequence plainText, String chipherText) {
        return passwordEncoder.matches(plainText, chipherText);
    }

}
