/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.model.Comment;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * History
 * - 2018. 9. 11. | in01943 | 최초작성.
 * </pre>
 */
public interface ArticleService {

    /**
     * <pre>
     * 조건에 따라 필터가 되는 게시글 목록
     * </pre>
     */
    public PageDTO<Article> findQueryByKeyword(ArticleSearchDTO search, Pageable pageable);

    /**
     * <pre>
     * 게시판별 게시글 조회
     * </pre>
     */
    public List<Article> getArticleList(String boardId);

    /**
     * <pre>
     * 게시글 상세 조회
     * </pre>
     */
    public Article getArticle(Long articleId);

    /**
     * <pre>
     * 게시글 생성
     * </pre>
     */
    public Article createArticle(Article article, String boardId);

    /**
     * <pre>
     * 게시글 수정
     * </pre>
     */
    public Article updateArticle(Article article, Long articleId);

    /**
     * <pre>
     * 게시글 삭제
     * </pre>
     */
    public Article deleteArticle(Long articleId);

    /**
     * <pre>
     * 게시글 생성
     * </pre>
     */
    public Comment createComment(Comment comment, Long articleId);

    /**
     * <pre>
     * 게시글 수정
     * </pre>
     */
    public Comment updateComment(Comment comment, Long commentId);

    /**
     * <pre>
     * 게시글 삭제
     * </pre>
     */
    public Comment deleteComment(Long commentId);

}
