/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.service;

import java.util.List;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.dto.AttachFileCreateDTO;
import com.skiaf.bcm.file.domain.service.dto.AttachFileUpdateDTO;

/**
 * <pre>
 *
 * History
 * - 2018. 09. 11. | in01866 | 최초작성.
 * </pre>
 */
public interface AttachFileService {

    /**
     * <pre>
     * 파일 ID로 첨부파일 조회
     * </pre>
     */
    public AttachFile findOne(String fileId);


    /**
     * <pre>
     * 사용되는 Target 정보로 첨부파일 조회
     * </pre>
     */
    public AttachFile findTopByTargetIdAndTargetType(String targetId, String targetType);

    /**
     * <pre>
     * 첨부파일 등록
     * </pre>
     */
    public AttachFile create(AttachFileCreateDTO attachFileSaveDTO);

    /**
     * <pre>
     * 첨부파일 삭제 - 미사용으로 전환
     * </pre>
     */
    public AttachFile delete(String fileId);

    /**
     * <pre>
     * 첨부파일 목록 조회
     * </pre>
     */
    public List<AttachFile> findAll();

    /**
     * <pre>
     * 사용되는 Target 정보로 첨부파일 목록 조회
     * </pre>
     */
    public List<AttachFile> findByTargetIdAndTargetType(String targetId, String targetType);

    /**
     * <pre>
     * 첨부파일 목록 등록
     * </pre>
     */
    public List<AttachFile> createList(List<AttachFileCreateDTO> attachFileSaveDTOList);

    /**
     * <pre>
     * 사용되는 Target 정보로 첨부파일 목록 수정
     * </pre>
     */
    public List<AttachFile> updateList(String targetId, String targetType, AttachFileUpdateDTO attachFileUpdateDTO);

    /**
     * <pre>
     * 첨부파일 목록 삭제 - 미사용으로 전환
     * </pre>
     */
    public List<AttachFile> deleteList(List<String> fileIdList);

    /**
     * <pre>
     * 같은 타입정보로 연결된 파일 목록 삭제 - 미사용으로 전환
     * </pre>
     */
    public List<AttachFile> deleteList(String targetId, String targetType);

}
