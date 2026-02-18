/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.dto.ProgramDetailDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramRoleListDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramSearchDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramUpdateDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 프로그램 Service
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface ProgramService {

    /**
     * <pre>
     * 프로그램 전체 목록 조회
     * </pre>
     */
    public List<Program> findAll();

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
    public Program findTopByHttpMethodAndPath(String httpMethod, String basePath, String requestPath);

    /**
     * <pre>
     * 프로그램 ID 앞 문자열이 일치하는 프로그램이 있는지 체크
     * </pre>
     */
    public boolean existsByProgramIdStartingWith(String programIdPrefix);

    /**
     * <pre>
     * 프로그램 ID 앞 문자열이 일치하는 프로그램 조회
     * </pre>
     */
    public ProgramRoleListDTO findByProgramIdStartingWith(String programIdPrefix);

    /**
     * <pre>
     * 프로그램 저장
     * </pre>
     */
    public List<Program> create(List<ProgramDetailDTO> programDetailDTOList);

    /**
     * <pre>
     * 프로그램 수정
     * </pre>
     */
    public List<Program> update(ProgramUpdateDTO programDetailDTOList);

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
