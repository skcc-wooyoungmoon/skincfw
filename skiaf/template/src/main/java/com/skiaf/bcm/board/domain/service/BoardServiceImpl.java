/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.repository.BoardRepository;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 게시판 관리 ServiceImpl
 * 
 * History
 * - 2018. 8.  7. | in01868 | 최초작성.
 * - 2018. 9. 18. | in01871 | 2차 수정.
 * </pre>
 */
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * <pre>
     * 게시판 목록 조회
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#findAll()
     */
    @Override
    public List<Board> findAll() {

        return boardRepository.findAll();
    }

    /**
     * <pre>
     * 게시판 상세 조회
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#findOne(java.lang.Long)
     */
    @Override
    public Board findOne(String boardId) {

        Board board = boardRepository.findOne(boardId);

        if (board == null) {
            throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND")
                    .withSystemMessage("board == null")
                    .build();
        }

        return board;
    }

    /**
     * 
     * <pre>
     * 게시판 등록
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#create(com.skiaf.bcm.board.domain.service.dto.BoardSaveDTO)
     */
    @Override
    public Board create(Board board) {
        
        board.setBasePath("/boards/");
        
        Board firstSave = boardRepository.save(board);
        
        firstSave.setBasePath("/boards/" + firstSave.getBoardId());
        
        Board secondSave = boardRepository.save(firstSave);
        
        return secondSave;
    }

    /**
     * <pre>
     * 게시판 수정
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#update(com.skiaf.bcm.board.domain.model.Board, java.lang.Long)
     */
    @Override
    public Board update(String boardId, Board board) {

        // 1. boardEntity select
        Board updateBoard = boardRepository.findOne(boardId);

        if (updateBoard == null) {
            throw NotFoundException.withUserMessageKey("bcm.common.NOT_FOUND")
                    .withSystemMessage("updateBoard == null")
                    .build();
        }

        // 2. update data put
        updateBoard.setBoardName(board.getBoardName());
        updateBoard.setBoardDesc(board.getBoardDesc());
        updateBoard.setBoardType(board.getBoardType());
        updateBoard.setAttachFileType(board.getAttachFileType());
        updateBoard.setEmergencyUseYn(board.isEmergencyUseYn());
        updateBoard.setTermUseYn(board.isTermUseYn());
        updateBoard.setCommentUseYn(board.isCommentUseYn());
        updateBoard.setUseYn(board.isUseYn());

        // 3. save
        boardRepository.save(updateBoard);

        return updateBoard;
    }

    /**
     * <pre>
     * 검색 조건에 따른 게시판 목록 조회
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#findQueryBySearch(com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO, org.springframework.data.domain.Pageable)
     */
    @Override
    public PageDTO<Board> findQueryBySearch(BoardSearchDTO search, Pageable pageable) {
        
        return boardRepository.findQueryBySearch(search, pageable);
    }

    /**
     * <pre>
     * 게시판 ID 중복체크
     * </pre>
     * @see com.skiaf.bcm.board.domain.service.BoardService#duplicateBoardId(java.lang.String)
     */
    @Override
    public Boolean duplicateBoardId(String boardId) {
        
        Boolean isDuplicate = false;
        
        Board board = boardRepository.findOne(boardId);
        if (board != null) {
            isDuplicate = true;
        }

        return isDuplicate;
    }

}
