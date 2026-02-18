/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSaveDTO implements Serializable {

    private static final long serialVersionUID = 201878214821081498L;

    /** 게시판 아이디 */
    private String boardId;

    /** 게시판 명 */
    private String boardName;

    /** 게시판 설명 */
    private String boardDesc;

    /** 게시판 유형 */
    private String boardType;

    /** 첨부파일 유형 */
    private String attachFileType;

    /** 댓글 사용 여부 */
    private boolean commentUseYn = true;

    /** 긴급 사용 여부 */
    private boolean emergencyUseYn = true;

    /** 기간 사용 여부 */
    private boolean termUseYn = true;

    /** 기준 경로 */
    private String basePath;
}
