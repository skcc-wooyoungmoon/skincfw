/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.hello.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hello.model.Hello;
import com.hello.repository.HelloMapper;
import com.hello.repository.HelloRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Profile("default") // Profile 속성을 사용한 이유는 샘플성 코드 이기 때문이다.
@Api(tags = "Hello Controller")
@RestController // RestController 어노테이션
public class HelloController {

    @Autowired
    HelloRepository helloRepository;

    @Autowired
    HelloMapper helloMapper;

    @ApiOperation(value = "헬로우 월드")
    @GetMapping("/api/hello") // Get Method 방식, 주소 입력
    public String hello() {
        return "Hello World"; // 출력값
    }

    @ApiOperation(value = "헬로우 리스트")
    @GetMapping("/api/hellolist")
    public List<Hello> helloList() {

        return helloRepository.findAll();
    }

    @ApiOperation(value = "헬로우 추가")
    @PostMapping("/api/helloadd") // Post Method 방식
    public Hello helloName(Hello hello) {

        Hello helloData = helloRepository.save(hello);

        return helloData;
    }

    /** SQL Mapper 관련 내용 추가 START  ***/
    @ApiOperation(value = "myBatis 헬로우 추가")
    @PostMapping("/api/mybatisadd")
    public Hello insertHello(Hello hello) {

        helloMapper.insertHello(hello);

        return hello;

    }

    @ApiOperation(value = "myBatis 헬로우 리스트")
    @GetMapping("/api/mybatislist")
    public List<Hello> hellolist() {

        List<Hello> hello = helloMapper.selectHelloList();

        return hello;
    }
    /** SQL Mapper 관련 내용 추가 END  ***/

    @GetMapping("/view/jpahellolist") // view 는 GetMapping을 사용한다.
    public ModelAndView jpaHelloListView() {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("data", helloRepository.findAll()); //jpa의 findAll을 이용해 리스트 가져오기
        modelAndView.setViewName("hello/view/hello-view");

        return modelAndView;
    }

    @GetMapping("/view/mybatishellolist") // view 는 GetMapping을 사용한다.
    public ModelAndView mybatisHelloListView() {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("data", helloMapper.selectHelloList()); //mybatis mapper의 selectHelloList를 이용해 리스트 가져오기
        modelAndView.setViewName("hello/view/hello-view");

        return modelAndView;
    }
}
