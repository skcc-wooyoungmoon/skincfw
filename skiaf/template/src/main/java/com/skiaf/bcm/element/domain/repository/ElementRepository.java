/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.repository;

import java.util.List;

import com.skiaf.bcm.element.domain.model.Element;

/**
 * <pre>
 *
 * BCM 프로그램요소 Repository
 *
 * History
 * - 2018. 7. 25. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
public interface ElementRepository {

    /**
     * <pre>
     * 프로그램요소 전체 조회
     * </pre>
     */
    public List<Element> findAll();

    /**
     * <pre>
     * 프로그램요소 순번으로 프로그램요소 정보 조회
     * </pre>
     */
    public Element findByElementSeq(Long elementSeq);

    /**
     * <pre>
     * 프로그램아이디 프로그램요소순번으로 프로그램요소 조회
     * </pre>
     */
    public Element findByProgramProgramIdAndElementSeqOrderByUpdateDateDesc(String programId, Long elementSeq);

    /**
     * <pre>
     * 프로그램 아이디로 프로그램 요소 목록 조회
     * </pre>
     */
    public List<Element> findByProgramProgramIdOrderByUpdateDateDesc(String programId);

    /**
     * <pre>
     * 프로그램 요소 저장
     * </pre>
     */
    public Element save(Element element);

    /**
     * <pre>
     * 프로그램요소 프로그램요소키로 프로그램요소 조회
     * </pre>
     */
    public Element findByProgramProgramIdAndElementKey(String programId, String elementKey);

}
