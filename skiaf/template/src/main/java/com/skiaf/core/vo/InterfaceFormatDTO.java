/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFormatDTO implements Serializable {
    private static final long serialVersionUID = 6243441464183985973L;

    private String parsingId; // 전문 타입
    private String rowSeparator; // 전문 가변길이인경우 ROW 구분자
    private String colSeparator; // 전문 가변길이인경우 COLUMN 구분자
    private String loggingId;
    private String bizAppId;
    private String bizAppName;
    private String loggingValue;

    private int statusCode; // 상태코드
    private String statusMessage; // 상태 메시지

    private int columnCount; // config상 컬럼 개수
    private int totalSize; // config상 컴럼의 전체 size
    private int dataTotalCount; // 변환 후 전체 size
    private ArrayList<HashMap<String, String>> configList; // config List
    private ArrayList<HashMap<String, String>> dataList; // data List
}
