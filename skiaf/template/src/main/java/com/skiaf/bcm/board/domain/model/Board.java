/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.core.constant.BoardAttachFileType;
import com.skiaf.core.constant.BoardType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * 게시판
 *
 * History
 * - 2018. 7. 17. | in01871 | 최초작성.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_BOARD")
public class Board extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 4273773746993665959L;

    /** 게시판 아이디 */
    @ApiModelProperty(required = true, example = "notice")
    @Id
    @NotBlank
    @Ids
    @Column(name = "BOARD_ID", nullable = false, unique = true)
    private String boardId;

    /** 게시판 명 */
    @ApiModelProperty(required = true, example = "게시판 명")
    @NotBlank
    @Length(max = 128)
    @Column(name = "BOARD_NM", length = 128, nullable = false)
    private String boardName;

    /** 게시판 설명 */
    @ApiModelProperty(required = false, example = "게시판 설명입니다.")
    @Length(max = 2000)
    @Column(name = "BOARD_DESC", length = 2000, nullable = true)
    private String boardDesc;

    /** 게시판 유형 */
    @ApiModelProperty(required = true, example = "메인 Type = MAIN, 팝업 Type = POPUP")
    @Enum(enumClass = BoardType.class, ignoreCase = true)
    @Column(name = "BOARD_TYP", length = 50, nullable = false)
    private String boardType;

    /** 첨부파일 유형 */
    @ApiModelProperty(required = true, example = "사용안함 = NO, 1개 = SINGLE, 복수 = MULTI")
    @Enum(enumClass = BoardAttachFileType.class, ignoreCase = true)
    @Column(name = "ATTACH_FILE_TYP", length = 50, nullable = false)
    private String attachFileType;

    /** 댓글 사용 여부 */
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "COMMENT_USE_YN", length = 1, nullable = false)
    private boolean commentUseYn = true;

    /** 긴급 사용 여부 */
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "EMGC_USE_YN", length = 1, nullable = false)
    private boolean emergencyUseYn = true;

    /** 기간 사용 여부 */
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "TERM_USE_YN", length = 1, nullable = false)
    private boolean termUseYn = true;

    /** 기준 경로 */
    @ApiModelProperty(hidden = true)
    @NotBlank
    @Length(max = 1000)
    @Column(name = "BASE_PATH", length = 1000, nullable = false)
    private String basePath;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Article> article;

}
