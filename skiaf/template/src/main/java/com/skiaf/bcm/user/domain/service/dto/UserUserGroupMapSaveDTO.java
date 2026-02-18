/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 사용자관리의 사용자그룹 저장 DTO
 * 
 * History
 * - 2018. 9. 10. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class UserUserGroupMapSaveDTO implements Serializable {

    private static final long serialVersionUID = 1089435645573906771L;

    private List<String> addUserGroupIds;
    
    private List<String> deleteUserGroupIds;
    
}
