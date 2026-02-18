/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository.jpa;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 게시판 JPA Custom Repository
 * 
 * History
 * - 2018. 9. 18. | in01871 | 최초작성.
 * </pre>
 */
public interface BoardRepositoryJpaExtend {
    
    public PageDTO<Board> findQueryBySearch(BoardSearchDTO search, Pageable pageable);

}
