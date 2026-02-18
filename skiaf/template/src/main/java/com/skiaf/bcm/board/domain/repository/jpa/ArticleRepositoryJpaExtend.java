/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository.jpa;

import org.springframework.data.domain.Pageable;

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 권한 JPA Repository Extend
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 *
 * </pre>
 */
public interface ArticleRepositoryJpaExtend {

    public PageDTO<Article> findQueryBySearch(ArticleSearchDTO search, Pageable pageable);

}
