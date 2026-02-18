/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * 댓글
 *
 * History
 * - 2018. 7. 17. | in01871 | 최초작성.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_COMMENT")
public class Comment extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 1L;

    /** 댓글 아이디 */
    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_ID", nullable = false)
    private Long commentId;

    /** 게시글 아이디 */
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    @JsonBackReference
    private Article article;

    /** 댓글 내용 */
    @Column(name = "COMMENT_CTNT", length = 1000, nullable = false)
    private String commentCtnt;

}
