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

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.user.domain.model.UserGroup;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 사용자그룹 목록 검색결과 DTO
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class UserGroupResultDTO implements Serializable {

    private static final long serialVersionUID = -3116945603138635825L;

    private List<UserGroup> userGroupList;

    private Pageable page;

    private int total;

}
