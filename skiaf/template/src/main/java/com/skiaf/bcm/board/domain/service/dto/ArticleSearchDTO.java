/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 게시글 검색 DTO
 * 
 * History
 * - 2018. 9. 11. | in01943 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class ArticleSearchDTO implements Serializable {

    private static final long serialVersionUID = 6673191829153649949L;

    /** 게시판 아이디 */
    private String boardId;

    /** 기본검색 */
    @Length(max = 20)
    private String keyword;

    /** 미사용 포함여부 */
    private boolean isUnusedInclude = false;

    /** 리스트 타입 결과 사용 */
    private boolean isList = false;
    
    /** 게시판 기간 사용 여부 */
    private boolean termUseYn = true;

    public boolean isUnusedInclude() {
        return isUnusedInclude;
    }

    public void setIsUnusedInclude(boolean isUnusedInclude) {
        this.isUnusedInclude = isUnusedInclude;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

}
