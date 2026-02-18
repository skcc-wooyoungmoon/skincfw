/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * History
 * - 2018. 7. 18. | in01871 | 최초작성.
 * </pre>
 */
public interface BoardRepository {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * <pre>
     * 게시판 전체 조회
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
     * 게시판 생성 / 수정
     * </pre>
     */
    public Board save(Board board);
    
    /*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    public PageDTO<Board> findQueryBySearch(BoardSearchDTO Search, Pageable pageable);
}
