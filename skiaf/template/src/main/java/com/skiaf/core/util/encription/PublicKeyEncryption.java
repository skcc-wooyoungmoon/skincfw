/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util.encription;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.rsa.crypto.RsaSecretEncryptor;

import com.skiaf.core.exception.BizException;

/**
 * <pre>
 * 공개키(비대칭키) 암호화
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */
public class PublicKeyEncryption {

    private TextEncryptor encryptor;
    private TextEncryptor decryptor;

    /**
     * <pre>
     * 생성자
     * </pre>
     * @param publicKeyPath  공개키(.cert)의 위치 
     */
    public PublicKeyEncryption(String publicKeyPath) {
        PublicKey publicKey = null;

        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream is = new FileInputStream(new ClassPathResource(publicKeyPath).getFile());
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(is);
            publicKey = cert.getPublicKey();
            this.encryptor = new RsaSecretEncryptor(publicKey);
        } catch (Exception e) {
            throw BizException.withUserMessageKey("bcm.common.FAIL").withSystemMessage("PublicKeyEncryption(publicCertPath) err : " + e.toString()).build();
        }
    }
    
    /**
     * <pre>
     * 생성자
     * </pre>
     * @param publicKeyPath  공개키(.cert)의 위치 
     * @param keyFilePath  키파일(.jks)의 위치
     * @param privateKey  공개키의 암호
     * @param keyAlias  공개키 생성시, 키파일 별명
     */
    public PublicKeyEncryption(String publicKeyPath, String keyFilePath, String privateKey, String keyAlias) {
        PublicKey publicKey = null;

        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream is = new FileInputStream(new ClassPathResource(publicKeyPath).getFile());
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(is);
            publicKey = cert.getPublicKey();
            this.encryptor = new RsaSecretEncryptor(publicKey);
            
            char[] password = privateKey.toCharArray();
            KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyFilePath), password);
            KeyPair keyPair = keyFactory.getKeyPair(keyAlias);
            this.decryptor = new RsaSecretEncryptor(keyPair);
        } catch (Exception e) {
            throw BizException.withUserMessageKey("bcm.common.FAIL").withSystemMessage("PublicKeyEncryption(...) err : " + e.toString()).build();
        }
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
        
        if(decryptor == null) {
            return null;
        }
        return decryptor.decrypt(encryptedText);
    }

}
