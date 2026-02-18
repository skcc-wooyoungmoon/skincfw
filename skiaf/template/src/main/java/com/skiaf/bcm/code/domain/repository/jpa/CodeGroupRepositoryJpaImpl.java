/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.service.dto.CodeSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 코드 그룹 JPA Repository Extend Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 24. | in01866 | 2차 수정.
 * </pre>
 */
public class CodeGroupRepositoryJpaImpl implements CodeGroupRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<CodeGroup> findQueryBySearch(CodeSearchDTO search, Pageable pageable) {

        /*
         * where 처리
         */
        // 기본 검색
        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(a.codeGroupId) LIKE :keyword ";
            where += "OR UPPER(a.codeGroupName1) LIKE :keyword ";
            where += "OR UPPER(a.codeGroupName2) LIKE :keyword ";
            where += "OR UPPER(a.codeGroupName3) LIKE :keyword ";
/* 4번째 언어 추가시, 주석 해제
            where += "OR UPPER(a.codeGroupName4) LIKE :keyword ";
*/

            // 코드까지 검색 포함
            if (!search.isCodeInclude()) {
                where += "OR UPPER(b.codeId) LIKE :keyword ";
                where += "OR UPPER(b.codeName1) LIKE :keyword ";
                where += "OR UPPER(b.codeName2) LIKE :keyword ";
                where += "OR UPPER(b.codeName3) LIKE :keyword ";
/* 4번째 언어 추가시, 주석 해제
                where += "OR UPPER(b.codeName4) LIKE :keyword ";
*/
            }

            // 검색어 조건 닫기
            where += ") ";

            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 사용만 조회하는지 확인
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(a.useYn) = 'Y' ";
        }

        // 조건이 하나라도 있는지 검사
        if (where != "") {
            where = "WHERE " + where;
        }

        /*
         * group by 처리
         */
        String groupBy = "GROUP BY a.codeGroupId "
                    + ", a.codeGroupName1 "
                    + ", a.codeGroupName2 "
                    + ", a.codeGroupName3 "
/* 4번째 언어 추가시, 주석 해제
                    + ", a.codeGroupName4 "
*/
                    + ", a.codeGroupDesc "
                    + ", a.createBy "
                    + ", a.updateBy "
                    + ", a.createDate "
                    + ", a.updateDate "
                    + ", a.useYn ";

        /*
         * order by 처리
         */
        Sort sort = pageable.getSort();
        String orderBy = "";

        String[] orderByText = {""};
        sort.forEach((Order arg) -> orderByText[0] = "a." + arg.getProperty() + " " + arg.getDirection() + " ");

        if (orderByText[0] != "") {
            orderBy += "ORDER BY ";
            orderBy += orderByText[0];
        }

        /*
         * Query 처리
         */
        // Query 생성
        String query = ""
                + "SELECT a "
                + "FROM TB_BCM_CDMST a "
                + "LEFT JOIN a.codeList b "
                + where
                + groupBy
                + orderBy;

        TypedQuery<CodeGroup> typedQuery = em.createQuery(query, CodeGroup.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        PageDTO<CodeGroup> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        // 페이징 추가
        if(search.isPaging()) {
            typedQuery.setFirstResult(pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());

            result.setPage(pageable);
        }

        result.setList(typedQuery.getResultList());

        return result;
    }
}
