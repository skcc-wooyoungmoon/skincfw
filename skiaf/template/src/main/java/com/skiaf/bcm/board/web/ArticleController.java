/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.model.Comment;
import com.skiaf.bcm.board.domain.service.ArticleService;
import com.skiaf.bcm.board.domain.service.BoardService;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 
 * History
 * - 2018. 07. 20. | in01871 | 최초작성.
 * - 2018. 09. 11. | in01943 | 2차수정
 * </pre>
 */
@Api(tags = "게시글 관리")
@RestController
public class ArticleController {

    private static final String BCM_BOARD_ASTERISK = "bcm.board.*";
    private static final String BCM_CODE_ASTERISK = "bcm.code.*";

    @Autowired
    private BoardService boardService;

    @Autowired
    private ArticleService articleService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "게시글 목록")
    @GetMapping(value = Path.VIEW_BOARD_ARTICLES)
    public ModelAndView viewArticleList(
            @ApiParam(name = "boardId", required = true, value = "게시판ID") @PathVariable String boardId) {

        Board board = boardService.findOne(boardId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("board", board);
        modelAndView.setViewName("skiaf/view/board/article-list");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_BOARD_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "게시글 상세(main type)")
    @GetMapping(value = Path.VIEW_BOARD_ARTICLES_DETAIL)
    public ModelAndView viewArticleDetail(
            @ApiParam(name = "boardId", required = true, value = "게시판ID") @PathVariable String boardId) {

        Board board = boardService.findOne(boardId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("board", board);
        modelAndView.setViewName("skiaf/view/board/article-detail");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_BOARD_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "게시글 수정 popup(main type)")
    @GetMapping(value = Path.VIEW_ARTICLES_SAVE_POPUP_DETAIL)
    public ModelAndView viewArticleDeteilSavePopup(@PathVariable String boardId) {

        Board board = boardService.findOne(boardId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("board", board);
        modelAndView.setViewName("skiaf/view/board/article-detail-save-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_CODE_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "게시글 조회-수정 popup(popup type)")
    @GetMapping(value = Path.VIEW_ARTICLES_UPDATE_POPUP_DETAIL)
    public ModelAndView viewArticleDeteilViewSavePopup(@PathVariable String boardId) {

        Board board = boardService.findOne(boardId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("board", board);
        modelAndView.setViewName("skiaf/view/board/article-detail-update-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_CODE_ASTERISK);
        return modelAndView;
    }

    @ApiOperation(value = "게시글 등록 popup")
    @GetMapping(value = Path.VIEW_ARTICLES_CREATE_POPUP)
    public ModelAndView viewArticleCreatePopup(@PathVariable String boardId) {

        Board board = boardService.findOne(boardId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("board", board);
        modelAndView.setViewName("skiaf/view/board/article-create-popup");
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_CODE_ASTERISK);
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "게시글 검색 페이징 목록 조회")
    @GetMapping(value = Path.ARTICLES)
    public RestResponse findQueryBySearch(@Valid ArticleSearchDTO search,
            @PageableDefault(sort = { "emgcYn", "articleId" }, direction = Sort.Direction.DESC) Pageable pageable) {
        return new RestResponse(articleService.findQueryByKeyword(search, pageable));
    }

    @ApiOperation(value = "게시글 상세조회")
    @GetMapping(value = Path.ARTICLES_DETAIL)
    public RestResponse getArticle(@PathVariable Long articleId) {
        return new RestResponse(articleService.getArticle(articleId));
    }

    @ApiOperation(value = "게시글 수정")
    @PutMapping(value = Path.ARTICLES_DETAIL)
    public RestResponse updateComment(@PathVariable(required = true) Long articleId, @RequestBody Article article) {
        return new RestResponse(articleService.updateArticle(article, articleId));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping(value = Path.ARTICLES_DETAIL)
    public RestResponse deleteArticle(@PathVariable(required = true) Long articleId) {
        return new RestResponse(articleService.deleteArticle(articleId));
    }

    @ApiOperation(value = "게시글 등록")
    @PostMapping(value = Path.ARTICLES_CREATE)
    public RestResponse createArticle(@PathVariable(required = true) String boardId, @RequestBody  Article article) {
        return new RestResponse(articleService.createArticle(article, boardId));
    }

    @ApiOperation(value = "댓글 등록")
    @PostMapping(value = Path.ARTICLES_MAP_COMMENTS)
    public RestResponse createComment(@PathVariable(required = true) Long articleId, @RequestBody Comment comment) {
        return new RestResponse(articleService.createComment(comment, articleId));
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping(value = Path.COMMENTS_DETAIL)
    public RestResponse updateComment(@PathVariable(required = true) Long commentsId, @RequestBody Comment comment) {
        return new RestResponse(articleService.updateComment(comment, commentsId));
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping(value = Path.COMMENTS_DETAIL)
    public RestResponse deleteComment(@PathVariable(required = true) Long commentsId) {
        return new RestResponse(articleService.deleteComment(commentsId));
    }

}
