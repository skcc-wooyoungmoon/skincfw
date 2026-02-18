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

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * History
 * - 2018. 7. 19. | in01871 | 최초작성.
 * - 2018. 9. 11. | in01943 | 2차수정.
 * </pre>
 */
public interface ArticleRepository {

    public List<Article> findAllByBoardBoardId(String boardId);

    public Article save(Article article);

    public Article findOne(Long articleId);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 게시글 페이징, 검색 목록조회
     * </pre>
     */
    public PageDTO<Article> findQueryBySearch(ArticleSearchDTO search, Pageable pageable);

}
