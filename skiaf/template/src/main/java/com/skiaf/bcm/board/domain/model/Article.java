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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.dto.AttachFileUpdateDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *
 * History
 * - 2018. 07. 17. | in01871 | 최초작성.
 * - 2018. 07. 17. | in01943 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_ARTICLE")
public class Article extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 1L;

    /** 게시글 아이디 */
    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ARTICLE_ID", nullable = false)
    private Long articleId;

    /** 게시판 아이디 */
    @ApiModelProperty(required = false, example = "게시판 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    @JsonBackReference
    private Board board;

    /** 게시글 제목 */
    @ApiModelProperty(required = true, example = "공지사항 입니다.")
    @Column(name = "ARTICLE_TITLE", length = 128, nullable = false)
    private String articleTitle;

    /** 게시글 내용 */
    @ApiModelProperty(required = true, example = "내용 입니다.")
    @Column(name = "ARTICLE_CTNT", nullable = false)
    @Lob
    private String articleContent;

    /** 게시글 타입  */
    @Column(name = "ARTICLE_TYP", length = 50, nullable = false)
    private String articleType;

    /** 게시글 시작일자 */
    @ApiModelProperty(required = false, example = "20180719")
    @Column(name = "ARTICLE_BEGIN_DT", length = 8, nullable = true)
    private String articleBeginDtm;

    /** 게시글 종료일자 */
    @ApiModelProperty(required = false, example = "99991231")
    @Column(name = "ARTICLE_END_DT", length = 8, nullable = true)
    private String articleEndDtm;

    /** 긴급 */
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "EMGC_YN", length = 1, nullable = false)
    private boolean emgcYn = true;

    /** 조회수 */
    @ApiModelProperty(required = true, example = "0")
    @Column(name = "HIT_CNT", nullable = false)
    private int hitCnt;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comment;

    /** 첨부 수 */
    @Transient
    private int attachCount;

    /** 첨부 수 */
    @Transient
    private List<AttachFile> attachFile;

    /** 답글 수*/
    @Transient
    private int commentCount;

    /** 저장/삭제 할 첨부 파일 list*/
    @Transient
    private AttachFileUpdateDTO attachFileUpdateDTO;

}
