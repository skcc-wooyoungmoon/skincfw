/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.code.domain.model.Code;

/**
 * <pre>
 * BCM 코드 JPA Repository Extend Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 24. | in01866 | 2차 수정.
 * </pre>
 */
@Repository
public class CodeRepositoryJpaImpl implements CodeRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Code> findQueryByCodeGroupId(String codeGroupId, Sort sort) {

        /*
         * 조건 처리
         */
        // 기본 검색
        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 검색어
        if (!StringUtils.isBlank(codeGroupId)) {
            where += "(a.codeGroup.codeGroupId = :codeGroupId) ";
            paramMap.put("codeGroupId", codeGroupId);
        }

        // 조건이 하나라도 있는지 검사
        if (!StringUtils.isBlank(where)) {
            where = "WHERE " + where;
        }

        /*
         * 정렬 처리
         */

        String orderBy = "";

        String[] orderByText = {""};
        sort.forEach((Order arg) -> orderByText[0] = "a." + arg.getProperty() + " " + arg.getDirection() + " ");

        if (!StringUtils.isBlank(orderByText[0])) {
            orderBy += "ORDER BY ";
            orderBy += orderByText[0];
        }

        /*
         * Query 처리
         */

        // Query 생성
        String query = ""
                + "SELECT a "
                + "FROM TB_BCM_CDDTL a "
                + "INNER JOIN a.codeGroup "
                + where
                + orderBy;

        TypedQuery<Code> typedQuery = em.createQuery(query, Code.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        return typedQuery.getResultList();
    }
}
