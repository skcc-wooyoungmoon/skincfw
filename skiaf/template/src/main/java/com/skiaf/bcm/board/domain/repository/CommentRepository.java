/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository;

import com.skiaf.bcm.board.domain.model.Comment;

/**
 * <pre>
 * 
 * History
 * - 2018. 7. 19. | in01871 | 최초작성.
 * </pre>
 */
public interface CommentRepository {

    public Comment save(Comment comment);

    public Comment findOne(Long commentId);

    public void delete(Long commentId);

}
