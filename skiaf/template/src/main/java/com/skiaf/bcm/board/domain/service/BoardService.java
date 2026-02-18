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

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 게시판 관리 Service
 * 
 * History
 * - 2018. 7. 23. | in01871 | 최초작성.
 * </pre>
 */
public interface BoardService {

    /**
     * <pre>
     * 게시판 목록 조회
     * </pre>
     */
    public List<Board> findAll();

    /**
     * <pre>
     * 게시판 단일 조회
     * </pre>
     */
    public Board findOne(String boardId);

    /**
     * <pre>
     * 게시판 등록
     * </pre>
     */
    public Board create(Board board);
    
    /**
     * <pre>
     * 게시판 수정
     * </pre>
     */
    public Board update(String boardId, Board board);
    
    /**
     * <pre>
     * 검색 조건에 따른 게시판 목록 조회
     * </pre>
     */
    public PageDTO<Board> findQueryBySearch(BoardSearchDTO search, Pageable pageable);
    
    /**
     * <pre>
     * 게시판 ID 중복 체크
     * </pre>
     */
    public Boolean duplicateBoardId(String boardId);

}
