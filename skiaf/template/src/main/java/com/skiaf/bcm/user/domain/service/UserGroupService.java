/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 *
 * BCM 사용자그룹 관리 Service
 * 
 * History
 * - 2018. 7. 19. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface UserGroupService {

    /**
     * <pre>
     * 사용자그룹 목록 조회
     * </pre>
     */
    public List<UserGroup> findAll();

    /**
     * <pre>
     * 사용자그룹 등록
     * </pre>
     */
    public UserGroup create(UserGroup userGroup);

    /**
     * <pre>
     * 사용자그룹 상세조회
     * </pre>
     */
    public UserGroup findOne(String id);

    /**
     * <pre>
     * 사용자그룹 수정
     * </pre>
     */
    public UserGroup update(String id, UserGroup userGroup);


    /**
     * <pre>
     * 사용자그룹 아이디 중복체크
     * </pre>
     */
    public Boolean duplicateCheck(String id);

    /**
     * <pre>
     * 사용자그룹 조건에 따라 목록 조회
     * </pre>
     */
    public PageDTO<UserGroup> findQueryBySearch(UserGroupSearchDTO search, Pageable pageable);
    
    /**
     * <pre>
     * 페이징 없는 사용자 그룹목록 조회
     * </pre>
     */
    public List<UserGroup> findByUseYn(Sort sort, boolean useYn);

}
