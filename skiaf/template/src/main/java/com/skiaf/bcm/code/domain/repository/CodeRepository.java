/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.model.CodeId;

/**
 * <pre>
 * BCM 코드 Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 코드 목록 조회
     * </pre>
     */
    public List<Code> findAll();

    /**
     * <pre>
     * 코드 아이디 목록에 있는 코드 조회
     * </pre>
     */
    public List<Code> findByCodeIdIn(List<String> codeId);

    /**
     * <pre>
     * 코드 조회
     * </pre>
     */
    public Code findOne(CodeId codeId);

    /**
     * <pre>
     * 코드 저장/수정
     * </pre>
     */
    public Code save(Code code);

    /**
     * <pre>
     * 코드 목록 저장/수정
     * </pre>
     */
    public <S extends Code> List<S> save(Iterable<S> codeList);

    /**
     * <pre>
     * 코드 삭제
     * </pre>
     */
    public void delete(Code code);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 코드 페이징, 검색 목록 조회
     * </pre>
     */
    public List<Code> findQueryByCodeGroupId(String codeGroupId, Sort sort);
}
