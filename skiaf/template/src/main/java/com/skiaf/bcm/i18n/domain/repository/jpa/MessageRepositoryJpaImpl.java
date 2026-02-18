/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.i18n.domain.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.i18n.domain.model.Message;
import com.skiaf.bcm.i18n.domain.service.dto.MessageSearchDTO;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 메시지 JPA Repository Extend Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Repository
public class MessageRepositoryJpaImpl implements MessageRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<Message> findQueryBySearch(MessageSearchDTO search, Pageable pageable) {

        /*
         * 조건 처리
         */

        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(m.messageKey) LIKE :keyword ";
            where += "OR UPPER(m.messageName1) LIKE :keyword ";
            where += "OR UPPER(m.messageName2) LIKE :keyword ";
            where += "OR UPPER(m.messageName3) LIKE :keyword ";
            where += "OR UPPER(m.target) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 사용만 조회하는지 확인
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(m.useYn) = 'Y' ";
        }

        // 조건이 하나라도 있는지 검사
        if (where != "") {
            where = "WHERE " + where;
        }

        /*
         * 정렬 처리
         */

        Sort sort = pageable.getSort();
        String sortStr = sort.toString();
        String orderBy = "";
        if (StringUtils.isNotBlank(sortStr)) {
            orderBy += "ORDER BY ";
            orderBy += Util.removeChar(sortStr, ":");
        }

        /*
         * Query 처리
         */

        // Query 생성
        String query = "" + "SELECT m " + "FROM TB_BCM_MSG m " + where + orderBy;

        TypedQuery<Message> typedQuery = em.createQuery(query, Message.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        PageDTO<Message> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        // 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setPage(pageable);
        result.setList(typedQuery.getResultList());

        return result;
    }

}
