/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.service.BoardService;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre>
 * 
 * History
 * - 2018. 7. 20. | in01871 | 최초작성.
 * </pre>
 */
@Api(tags = "게시판 관리")
@RestController
public class BoardController {
    
    private static final String BCM_BOARD_ASTERISK = "bcm.board.*";

    @Autowired
    private BoardService boardService;
    
    @Autowired
    private MessageComponent messageComponent;
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    @GetMapping(value = Path.VIEW_BOARDS)
    public ModelAndView boardList() {
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_BOARD_ASTERISK);
        modelAndView.setViewName("skiaf/view/board/board-list");
        
        return modelAndView;
    }
    
    @GetMapping(value = Path.VIEW_BOARDS_CREATE)
    public ModelAndView boardCreate() {
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_BOARD_ASTERISK);        
        modelAndView.setViewName("skiaf/view/board/board-create-popup");
        
        return modelAndView;
    }
    
    @GetMapping(value = Path.VIEW_BOARDS_UPDATE)
    public ModelAndView boardUpdate() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_BOARD_ASTERISK);
        modelAndView.setViewName("skiaf/view/board/board-update-popup");
        
        return modelAndView;
    }
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "게시판 조회")
    @GetMapping(value = Path.BOARDS)
    public RestResponse findQueryByKeyword(BoardSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        return new RestResponse(boardService.findQueryBySearch(search, pageable));
    }

    @ApiOperation(value = "게시판 상세조회")
    @GetMapping(value = Path.BOARDS_DETAIL)
    public RestResponse findOne(@ApiParam(name = "boardId", required = true, value = "게시판 아이디") @PathVariable String boardId) {

        return new RestResponse(boardService.findOne(boardId));
    }

    @ApiOperation(value = "게시판 등록")
    @PostMapping(value = Path.BOARDS)
    public RestResponse create(@RequestBody Board board) {

        return new RestResponse(boardService.create(board));
    }

    @ApiOperation(value = "게시판 수정")
    @PutMapping(value = Path.BOARDS_DETAIL)
    public RestResponse update(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "boardId", required = true, value = "게시판 아이디") @PathVariable String boardId,
            @ApiParam(name = "update board object", required = true, value = "게시판 정보") @RequestBody Board board) {

        return new RestResponse(boardService.update(boardId, board));
    }
    
    @ApiOperation(value = "게시판 ID 중복체크")
    @GetMapping(value = Path.BOARDS_DUPLICATE)
    public RestResponse duplicateBoardId(
            @ApiParam(name = "boardId", required = true, value = "게시판 ID") @PathVariable String boardId) {
        
        RestResponse restResponse = new RestResponse();
        Boolean isDuplicate = boardService.duplicateBoardId(boardId);
        restResponse.setData(isDuplicate);
        
        if (isDuplicate) {
            restResponse.setUserMessage(messageComponent.getMessage("bcm.common.DUPLICATE"));
        }

        return restResponse;
        
    }
}
