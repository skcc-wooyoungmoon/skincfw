/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.service;

import java.util.List;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.element.domain.service.dto.ElementDetailDTO;
import com.skiaf.bcm.element.domain.service.dto.ElementRoleListDTO;

/**
 * <pre>
 *
 * BCM 프로그램요소 관리 Service
 *
 * History
 * - 2018. 8. 13. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface ElementService {

    /**
     * <pre>
     * 프로그램요소 목록 전체 조회
     * </pre>
     */
    public List<Element> findAll();

    /**
     * <pre>
     * 프로그램요소 등록
     * </pre>
     */
    public Element create(String programId, ElementDetailDTO elementDetailDTO);

    /**
     * <pre>
     * 프로그램요소 상세조회
     * </pre>
     */
    public Element findOne(Long elementSeq);

    /**
     * <pre>
     * 프로그램요소 수정
     * </pre>
     */
    public Element update(String programId, Long elementSeq, ElementDetailDTO elementDetailDTO);

    /**
     * <pre>
     * 프로그램아이디로 목록 조회
     * </pre>
     */
    public ElementRoleListDTO findByProgramProgramId(String programId);

    /**
     * <pre>
     * 프로그램요소 중복 체크
     * </pre>
     */
    public Boolean duplicateElement(String programId, String elementKey);

}
