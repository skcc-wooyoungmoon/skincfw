/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.constant;

/**
 * <pre>
 * BCM 메뉴 오픈 타입 enum 클래스
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public enum MenuOpenType {

    CURRENT_WINDOW("C"), NEW_TAB("T"), NEW_WINDOW("P");
    
    private final String name;

    private MenuOpenType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
}
