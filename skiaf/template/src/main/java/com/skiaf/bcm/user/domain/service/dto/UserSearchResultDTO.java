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

import com.skiaf.bcm.user.domain.model.User;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 사용자 상세검색 결과 DTO
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class UserSearchResultDTO implements Serializable {

    private static final long serialVersionUID = -3284967341447689642L;

    private List<User> users;

    private Pageable page;

    private int total;

}
