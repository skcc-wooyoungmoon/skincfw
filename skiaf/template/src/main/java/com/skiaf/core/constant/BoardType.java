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
 * 
 * History
 * - 2018. 10. 16. | in01871 | 최초작성.
 * </pre>
 */
public enum BoardType {
    
    MAIN("M"), POPUP("P");
    
    private final String name;
    
    private BoardType(String name) {
        this.name = name;
    }
            
    public String getName() {
        return this.name;
    }
    
}
