/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.hello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data // lombok 어노테이션, getter, setter 등을 자동으로 생성해줌
@Entity (name = "HELLO")// JPA에서 테이블과 매핑할 클래스에 필수로 넣어야하는 어노테이션, name은 생성 할 테이블 이름
public class Hello {

    @ApiModelProperty(hidden = true) //스웨거 모델 설정(값이 자동으로 생성되므로 숨기기처리)
    @Id // 키값
    @GeneratedValue(strategy = GenerationType.AUTO) // 시퀀스를 자동으로 생성해주는 어노테이션
    private int id;

    @ApiModelProperty(required = true, example = "이름") //스웨거 모델 설정(example은 예시)
    private String name;

}