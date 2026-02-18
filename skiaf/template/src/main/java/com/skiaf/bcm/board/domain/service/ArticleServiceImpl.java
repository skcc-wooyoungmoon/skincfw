/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.model.Comment;
import com.skiaf.bcm.board.domain.repository.ArticleRepository;
import com.skiaf.bcm.board.domain.repository.BoardRepository;
import com.skiaf.bcm.board.domain.repository.CommentRepository;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.repository.AttachFileRepository;
import com.skiaf.bcm.file.domain.service.AttachFileService;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.constant.ArticleType;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.constant.FileType;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.PageDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *
 * History
 * - 2018. 7. 19. | in01871 | 최초작성.
 * - 2018. 9. 11. | in01943 | 2수정.
 * </pre>
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private static final String USER_MESSAGE_KEY_NOT_FOUND = "bcm.common.NOT_FOUND";
    private static final String BCM_COMMON_EXCEPTION_BIZ_CREATOR = "bcm.common.exception.biz-creator";

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private AttachFileRepository attachFileRepository;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private LucyXssFilter lucyXssFilter;

    @Override
    public PageDTO<Article> findQueryByKeyword(ArticleSearchDTO search, Pageable pageable) {
        List<AttachFile> attachfiles;
        PageDTO<Article> result = articleRepository.findQueryBySearch(search, pageable);

        for (int i = 0; i < result.getList().size(); i++) {
            attachfiles = attachFileRepository.findByTargetIdAndTargetTypeAndUseYnTrue(
                    result.getList().get(i).getArticleId().toString(), FileType.ARTICLE.toString());
            result.getList().get(i).setCommentCount(result.getList().get(i).getComment().size());
            result.getList().get(i).setAttachCount(attachfiles.size());
        }

        return result;
    }

    @Override
    public List<Article> getArticleList(String boardId) {

        return articleRepository.findAllByBoardBoardId(boardId);
    }

    @Override
    public Article getArticle(Long articleId) {

        Article article = articleRepository.findOne(articleId);
        List<AttachFile> attachfiles = attachFileRepository
                .findByTargetIdAndTargetTypeAndUseYnTrue(articleId.toString(), FileType.ARTICLE.toString());

        if (article == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND).withSystemMessage("article == null")
                    .build();
        }

        article.setHitCnt(article.getHitCnt() + 1);
        articleRepository.save(article);

        article.setCommentCount(article.getComment().size());
        article.setAttachCount(attachfiles.size());
        article.setAttachFile(attachfiles);

        return article;
    }

    private void checkMyCreateOrAdminRole(String createId) {
        // 로그인 사용자 정보 추가
        UserInfoDTO userInfoDTO = SessionUtil.getLoginUserInfo();

        // 본인/admin 인지 check
        if (!createId.equals(userInfoDTO.getUser().getLoginId()) && !userInfoDTO.isRole(CommonConstant.BOARD_ADMIN_ROLE)) {
            throw BizException.withUserMessageKey(BCM_COMMON_EXCEPTION_BIZ_CREATOR).build();
        }   
    }
    
    @Override
    public Article createArticle(Article article, String boardId) {

        Board board = boardRepository.findOne(boardId);

        if (board == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND).withSystemMessage("board == null")
                    .build();
        }

        // HTML타입의 본문인 경우, 문제되는 마크업들을 필터링함.
        if (ArticleType.HTML.toString().equals(article.getArticleType())) {
            String content = article.getArticleContent();
            log.info("content={}", content);
            String cleanedContent = lucyXssFilter.doFilter(content);
            log.info("cleanedContent={}", cleanedContent);
            article.setArticleContent(cleanedContent);
        }

        article.setBoard(board);

        Article retArticle = articleRepository.save(article);

        attachFileService.updateList(Long.toString(retArticle.getArticleId()), FileType.ARTICLE.toString(),
                article.getAttachFileUpdateDTO());

        return retArticle;
    }

    @Override
    public Article updateArticle(Article article, Long articleId) {

        // 1. articleEntity select
        Article updateArticle = articleRepository.findOne(articleId);

        if (updateArticle == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                    .withSystemMessage("updateArticle == null").build();
        }

        // 게시글 권한자 및 본인
        checkMyCreateOrAdminRole(updateArticle.getCreateBy());

        // HTML타입의 본문인 경우, 문제되는 마크업들을 필터링함.
        if (ArticleType.HTML.toString().equals(article.getArticleType())) {
            String content = article.getArticleContent();
            log.info("content={}", content);
            String cleanedContent = lucyXssFilter.doFilter(content);
            log.info("cleanedContent={}", cleanedContent);
            article.setArticleContent(cleanedContent);
        }

        // 2. update data
        updateArticle.setArticleTitle(article.getArticleTitle());
        updateArticle.setArticleContent((article.getArticleContent() == null ? updateArticle.getArticleContent()
                : article.getArticleContent()));
        updateArticle.setArticleType(article.getArticleType());
        updateArticle.setHitCnt(updateArticle.getHitCnt());
        updateArticle.setArticleBeginDtm(article.getArticleBeginDtm());
        updateArticle.setArticleEndDtm(article.getArticleEndDtm());
        updateArticle.setEmgcYn(article.isEmgcYn());

        // 3. save
        articleRepository.save(updateArticle);

        attachFileService.updateList(Long.toString(articleId), FileType.ARTICLE.toString(),
                article.getAttachFileUpdateDTO());

        return updateArticle;
    }

    @Override
    public Article deleteArticle(Long articleId) {

        Article article = articleRepository.findOne(articleId);

        // 게시글 권한자 및 본인
        checkMyCreateOrAdminRole(article.getCreateBy());

        article.setUseYn(false);

        articleRepository.save(article);

        attachFileService.deleteList(Long.toString(articleId), FileType.ARTICLE.toString());

        return article;
    }

    @Override
    public Comment createComment(Comment comment, Long articleId) {

        Article article = articleRepository.findOne(articleId);

        if (article == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND).withSystemMessage("article == null")
                    .build();
        }

        comment.setArticle(article);

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment, Long commentId) {

        // 1. commentEntity select
        Comment updateComment = commentRepository.findOne(commentId);

        if (updateComment == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                    .withSystemMessage("updateComment == null").build();
        }

        // 권한 check
        checkMyCreateOrAdminRole(updateComment.getCreateBy());

        // 2. update data
        updateComment.setCommentCtnt(comment.getCommentCtnt());
        updateComment.setUseYn(comment.isUseYn());

        // 3. save
        commentRepository.save(updateComment);

        return updateComment;
    }

    @Override
    public Comment deleteComment(Long commentId) {

        Comment comment = commentRepository.findOne(commentId);

        if (comment == null) {
            throw NotFoundException.withUserMessageKey(USER_MESSAGE_KEY_NOT_FOUND)
                    .withSystemMessage("deleteComment == null").build();
        }

        // 권한 check
        checkMyCreateOrAdminRole(comment.getCreateBy());

        comment.setUseYn(false);

        commentRepository.delete(commentId);

        return comment;
    }

}
