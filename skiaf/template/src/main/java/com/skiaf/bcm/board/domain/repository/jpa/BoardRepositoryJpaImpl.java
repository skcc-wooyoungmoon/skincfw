/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.board.domain.model.Board;
import com.skiaf.bcm.board.domain.service.dto.BoardSearchDTO;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 게시판 JPA Repository Impl
 * 
 * History
 * - 2018. 9. 18. | in01871 | 최초작성.
 * </pre>
 */
@Repository
public class BoardRepositoryJpaImpl implements BoardRepositoryJpaExtend {
    
    private static final String KEYWORD = "keyword";
    
    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<Board> findQueryBySearch(BoardSearchDTO search, Pageable pageable) {
        
        String where = "";
        Map<String, String> paramMap = new HashMap<>();
        
        // 기본 검색
        if (search.getKeyword() != null) {
            where += "(UPPER(b.boardId) LIKE :keyword or UPPER(b.boardName) LIKE :keyword) ";
            paramMap.put(KEYWORD, "%" + search.getKeyword().toUpperCase() + "%");
            
        }
        
        if (where != "") {
            where = "WHERE" + where;
            
        }
        
        Sort sort = pageable.getSort();
        String sortStr = sort.toString();
        String orderBy = "";
        
        if (StringUtils.isNotBlank(sortStr)) {
            orderBy += "ORDER BY ";
            orderBy += Util.removeChar(sortStr, ":");
            
        }
        
        String query = "" + "SELECT b " + "FROM TB_BCM_BOARD b " + where + orderBy;
        
        TypedQuery<Board> typedQuery = em.createQuery(query, Board.class);
        
        paramMap.forEach((String key, String value) -> typedQuery.setParameter(key, value));
        
        PageDTO<Board> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());
        
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        result.setList(typedQuery.getResultList());
        result.setPage(pageable);
        
        return result;
    }
    
    

}
