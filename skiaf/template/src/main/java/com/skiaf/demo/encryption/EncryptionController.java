/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.encryption;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.util.encription.PublicKeyEncryption;
import com.skiaf.core.util.encription.SymmetricKeyEncryption;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 암호화 데모 컨트롤러
 * 
 * History
 * - 2018. 8. 27. | in01868 | 최초작성.
 * </pre>
 */
@Api(tags = "데모-암호화")
@RestController
public class EncryptionController {

    private static final String VALUE = "value";
    private static final String ENCRYPTED_VALUE = "encryptedValue";
    private static final String DECRYPTED_VALUE = "decryptedValue";

    private static final String SYMMETRIC_KEY = "f16da33a765bbc4e";
    private static final String SALT = "1f737595d72a3d32";

    private static final String PUBLIC_KEY_PATH = "static/skiaf/crypto/keystore-public.cert";
    private static final String KEY_FILE_PATH = "static/skiaf/crypto/keystore.jks";
    private static final String PRIVATE_KEY = "mypassword";
    private static final String KEY_ALIAS = "mykeystore";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "단방향 암호화")
    @GetMapping("/api/demo/encryption/hashing/{value}")
    public RestResponse passwordEncoder(
            @ApiParam(name = "value", required = true, value = "인코딩될 벨류") @PathVariable String value) {

        String encryptedValue = passwordEncoder.encode(value);

        Map<String, String> map = new HashMap<>();
        map.put(VALUE, value);
        map.put(ENCRYPTED_VALUE, encryptedValue);
        map.put("isMatch", passwordEncoder.matches(value, encryptedValue) + "");

        return new RestResponse(map);
    }

    @ApiOperation(value = "공개키 암호화")
    @GetMapping("/api/demo/encryption/public/{value}")
    public RestResponse publicKeyEncryption(
            @ApiParam(name = "value", required = true, value = "인코딩될 벨류") @PathVariable String value) {

        PublicKeyEncryption publicKeyEncryption = new PublicKeyEncryption(PUBLIC_KEY_PATH, KEY_FILE_PATH, PRIVATE_KEY, KEY_ALIAS);

        String encryptedValue = publicKeyEncryption.encrypt(value);

        Map<String, String> map = new HashMap<>();
        map.put(VALUE, value);
        map.put(ENCRYPTED_VALUE, encryptedValue);
        map.put(DECRYPTED_VALUE, publicKeyEncryption.decrypt(encryptedValue));

        return new RestResponse(map);
    }

    @ApiOperation(value = "양방향 암호화")
    @GetMapping("/api/demo/encryption/symmetric/{value}")
    public RestResponse symmetricKeyEncryption(
            @ApiParam(name = "value", required = true, value = "인코딩될 벨류") @PathVariable String value) {

        SymmetricKeyEncryption symmetricKeyEncryption = new SymmetricKeyEncryption(SYMMETRIC_KEY, SALT);
        String encryptedValue = symmetricKeyEncryption.encrypt(value);

        Map<String, String> map = new HashMap<>();
        map.put(VALUE, value);
        map.put(ENCRYPTED_VALUE, encryptedValue);
        map.put(DECRYPTED_VALUE, symmetricKeyEncryption.decrypt(encryptedValue));

        return new RestResponse(map);
    }

}
