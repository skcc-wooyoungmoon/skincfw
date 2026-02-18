/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.hello.repository.jpa;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.hello.model.Hello;
import com.hello.repository.HelloRepository;
 
public interface HelloRepositoryJpa extends HelloRepository, JpaRepository<Hello, Integer> {
 
}