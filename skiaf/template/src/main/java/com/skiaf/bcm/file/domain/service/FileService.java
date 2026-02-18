/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.dto.AttachFileCreateDTO;

/**
 * <pre>
 *
 * History
 * - 2018. 09. 11. | in01866 | 최초작성.
 * </pre>
 */
public interface FileService {

    /**
     * <pre>
     * 첨부파일 업로드
     * </pre>
     */
    public String uploadFile(MultipartFile file);

    /**
     * <pre>
     * 임시 저장된 파일을 저장 디렉토리로 이동
     * </pre>
     */
    public AttachFile moveSaveLocation(AttachFileCreateDTO attachFileSaveDTO);

    /**
     * <pre>
     * 저장된 파일을 삭제 디렉토리로 이동
     * </pre>
     */
    public AttachFile moveDeleteLocation(AttachFile attachFile);

    /**
     * <pre>
     * 등록된 첨부파일 가져오기
     * </pre>
     */
    public File downloadFile(String filePathWithName);

    /**
     * <pre>
     * 지난 임시 파일제거
     * </pre>
     */
    public void cleanTempFile(int daysAgo);
}
