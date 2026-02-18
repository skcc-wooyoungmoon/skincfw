/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.repository.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.skiaf.bcm.board.domain.model.Article;
import com.skiaf.bcm.board.domain.service.dto.ArticleSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 게시글 JPA Repository ExtendImpl
 * 
 * History
 * - 2018. 9. 11. | in01943 | 최초작성.
 * </pre>
 */
public class ArticleRepositoryJpaImpl implements ArticleRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<Article> findQueryBySearch(ArticleSearchDTO search, Pageable pageable) {

        Long articleId;

        StringBuilder query = new StringBuilder("");
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색
        //query.append("SELECT r FROM TB_BCM_ARTICLE r WHERE UPPER(r.useYn) = 'Y' ");

        query.append("SELECT r FROM TB_BCM_ARTICLE r ");
        query.append("INNER JOIN r.board ");
        query.append("WHERE (r.board.boardId = :boardId) AND UPPER(r.useYn) = 'Y' ");
        paramMap.put("boardId", search.getBoardId());


        // 검색어
        if (search.getKeyword() != null) {
            // 검색어가 게기글 번호인지 check
            try {
                articleId = Long.parseLong(search.getKeyword());
            } catch (Exception e) {
                articleId = null;
            }

            if (articleId == null) {
                query.append("AND UPPER(r.articleTitle) LIKE :keyword ");
                paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
            } else {
                query.append("AND (r.articleId = :articleId OR UPPER(r.articleTitle) LIKE :keyword) AND UPPER(r.useYn) = 'Y' ");
                paramMap.put("articleId", Long.parseLong(search.getKeyword()));
                paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
            }
        }
        
        if (search.isTermUseYn()) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        	Calendar c1 = Calendar.getInstance();
        	
        	String today = sdf.format(c1.getTime());
        	
        	query.append("AND r.articleBeginDtm <= :today AND r.articleEndDtm >= :today ");
    		paramMap.put("today", today);
    	}

        Sort sort = pageable.getSort();
        String sortStr = sort.toString();
        
        if (StringUtils.isNotBlank(sortStr)) {
            query.append(" ORDER BY ");
            /** table join이 없을 경우 사용 : query.append(Util.removeChar(sortStr, ":")); */   
            int queryLength = query.toString().length();
            sort.forEach((Order arg) -> query.append(((queryLength == query.toString().length()) ? "r." : ", r.") + arg.getProperty() + " " + arg.getDirection() + " "));
        }

        // Query 생성
        TypedQuery<Article> typedQuery = em.createQuery(query.toString(), Article.class);

        // 조건 파라메터 추가
        paramMap.forEach(typedQuery::setParameter);
        
        PageDTO<Article> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        // 조건 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setPage(pageable);
        result.setList(typedQuery.getResultList());

        return result;
    }
}
