/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.service.dto.CodeDetailDTO;
import com.skiaf.bcm.code.domain.service.dto.CodeUpdateDTO;

/**
 * <pre>
 * BCM 코드 그룹 Service
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
public interface CodeService {

    /**
     * <pre>
     * 코드 조회
     * </pre>
     */
    public Code findOne(String codeGroupId, String codeId);

    /**
     * <pre>
     * 코드 목록 저장
     * </pre>
     */
    public List<Code> createList(List<CodeDetailDTO> codeDetailDTOList, String codeGroupId);

    /**
     * <pre>
     * 코드 목록 수정
     * </pre>
     */
    public List<Code> updateList(CodeUpdateDTO codeUpdateDTO);

    /**
     * <pre>
     * 코드 전체 목록 조회
     * </pre>
     */
    public List<Code> findAll();

    /**
     * <pre>
     * 코드 삭제
     * </pre>
     */
    public void delete(Code code);

    /**
     * <pre>
     * 코드 페이징, 검색 목록 조회
     * </pre>
     */
    public List<CodeDetailDTO> findQueryByCodeGroupId(String codeGroupId, Sort sort);

    /**
     * <pre>
     * 현재 언어 기준 코드 목록 조회
     * </pre>
     */
    public List<Code> findByCodeGroupDetail(String codeGroupId);

    /**
     * <pre>
     * 현재 언어의 코드명 조회
     * </pre>
     */
    public String findCodeNameByCurrentLanguage(Code code);

}
