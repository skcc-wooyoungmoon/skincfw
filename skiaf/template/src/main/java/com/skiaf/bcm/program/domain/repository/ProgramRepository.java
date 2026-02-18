/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.dto.ProgramSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 프로그램 Repository
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface ProgramRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 프로그램 ID 앞 문자열이 일치하는 프로그램이 있는지 체크
     * </pre>
     */
    public boolean existsByProgramIdStartingWith(String programIdPrefix);

    /**
     * <pre>
     * 프로그램 조회
     * </pre>
     */
    public Program findOne(String programId);

    /**
     * <pre>
     * 대소문자 구분 없이 Http Method, Base path 가 일치하는 프로그램 조회
     * </pre>
     */
    public Program findTopByHttpMethodIgnoreCaseAndBasePathIgnoreCase(String httpMethod, String basePath);

    /**
     * <pre>
     * 프로그램 전체 목록 조회
     * </pre>
     */
    public List<Program> findAll();

    /**
     * <pre>
     * 프로그램 아이디 목록에 있는 프로그램 조회
     * </pre>
     */
    public List<Program> findByProgramIdIn(List<String> programIdList);

    /**
     * <pre>
     * 프로그램 ID 앞 문자열이 일치하는 프로그램 조회
     * </pre>
     */
    public List<Program> findByProgramIdStartingWithOrderByUpdateDateDesc(String programIdPrefix);

    /**
     * <pre>
     * 프로그램 저장/수정
     * </pre>
     */
    public Program save(Program program);

    /**
     * <pre>
     * 프로그램 목록 저장/수정
     * </pre>
     */
    public <S extends Program> List<S> save(Iterable<S> programList);

    /**
     * <pre>
     * 프로그램 제거
     * </pre>
     */
    public void delete(Program program);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 프로그램 페이징, 검색 목록 조회
     * </pre>
     */
    public PageDTO<Program> findQueryBySearch(ProgramSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * 프로그램 페이징, 검색 목록 조회
     * </pre>
     */
    public List<Program> findQueryBySearch(ProgramSearchDTO search, Sort sort);
}
