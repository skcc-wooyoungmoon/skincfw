/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.core.validation.annotation.Ids;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * 첨부파일
 *
 * History
 * - 2018. 7. 17. | in01871 | 최초작성.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_ATTACH_FILE")
public class AttachFile extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 4134687661750263540L;

    /** 파일 아이디 */
    @Id
    @Ids
    @Column(name = "FILE_ID", nullable = false)
    private String fileId;

    /** 사용처 ID */
    @Column(name = "TARGET_ID", length = 50, nullable = false)
    private String targetId;

    /** 사용처 유형 */
    @Column(name = "TARGET_TYP", length = 50, nullable = false)
    private String targetType;

    /** 원본 파일 이름 */
    @Column(name = "ORI_FILE_NM", length = 200, nullable = false)
    private String originalFileName;

    /** 서버 파일 이름 */
    @Column(name = "SVR_FILE_NM", length = 200, nullable = false)
    private String severFileName;

    /** 파일 경로 */
    @Column(name = "FILE_PATH", length = 200, nullable = false)
    private String filePath;

    /** 확장자 */
    @Column(name = "FILE_EXT", length = 10, nullable = false)
    private String fileExtension;

    /** 파일 사이즈 */
    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;

}
