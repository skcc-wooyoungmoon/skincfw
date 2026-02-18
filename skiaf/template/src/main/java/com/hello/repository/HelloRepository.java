/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.hello.repository;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.hello.model.Hello;

@Profile("default") // Profile 속성을 사용한 이유는 샘플성 코드 이기 때문이다.
@Repository //Repository 어노테이션
public interface HelloRepository {

    public List<Hello> findAll(); // 전체 목록 검색

    public Hello save(Hello hello); // 저장

}