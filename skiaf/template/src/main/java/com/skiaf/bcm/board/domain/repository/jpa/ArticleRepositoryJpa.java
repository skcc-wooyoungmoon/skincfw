/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.repository.ArticleRepository;

/**
 * <pre>
 * 
 * History
 * - 2018. 7. 19. | in01871 | 최초작성.
 * - 2018. 7. 19. | in01943 | 2차수정.
 * </pre>
 */
@Repository
public interface ArticleRepositoryJpa extends ArticleRepository, JpaRepository<Article, Long>, ArticleRepositoryJpaExtend {

}
