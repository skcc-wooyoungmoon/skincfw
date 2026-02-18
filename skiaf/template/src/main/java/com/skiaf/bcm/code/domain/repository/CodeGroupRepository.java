/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.service.dto.CodeSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 코드 그룹 Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeGroupRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 코드 그룹 목록 조회
     * </pre>
     */
    public List<CodeGroup> findAll();

    /**
     * <pre>
     * 코드 그룹 조회
     * </pre>
     */
    public CodeGroup findOne(String codeGroupId);

    /**
     * <pre>
     * 코드 그룹 ID 및 사용여부 조건을 만족하는 코드 그룹 조회
     * </pre>
     */
    public CodeGroup findByCodeGroupIdAndUseYn(String codeGroupId, boolean useYn);

    /**
     * <pre>
     * 코드 그룹 저장/수정
     * </pre>
     */
    public CodeGroup save(CodeGroup codeGroup);

    /**
     * <pre>
     * 코드 그룹 목록 저장
     * </pre>
     */
    public List<CodeGroup> save(List<CodeGroup> codeGroupList);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 코드 그룹 페이징, 검색 목록 조회
     * </pre>
     */
    public PageDTO<CodeGroup> findQueryBySearch(CodeSearchDTO search, Pageable pageable);

}
