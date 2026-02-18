/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util.encription;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * <pre>
 * 대칭키(양방향) 암호화 SpringSecurity - TextEncryptor
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public class SymmetricKeyEncryption {
    
    private TextEncryptor encryptor;
    
    /**
     * <pre>
     * 생성자
     * </pre>
     * @param symmetricKey 대칭키의 암호 
     * @param salt salt값 
     */
    public SymmetricKeyEncryption(String symmetricKey, String salt) {
        this.encryptor = Encryptors.text(symmetricKey, salt); 
    }
    
    /**
     * <pre>
     * 암호화
     * </pre>
     */
    public String encrypt(String plainText) {
        return encryptor.encrypt(plainText);
    }

    /**
     * <pre>
     * 복호화
     * </pre>
     */
    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }

}
