/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.dto.ProgramSearchDTO;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 프로그램 JPA Repository Extend Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Repository
public class ProgramRepositoryJpaImpl implements ProgramRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<Program> findQueryBySearch(ProgramSearchDTO search, Pageable pageable) {

        /*
         * =====조건처리=====
         */

        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(a.programName) LIKE :keyword OR UPPER(a.programId) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 미사용 포함
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(a.useYn) = 'Y' ";
        }

        // 상세 검색
        String detailSearch = "";

        // 프로그램 유형
        if (search.getProgramType() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(a.programType) = :programType ";
            paramMap.put("programType", search.getProgramType());
        }

        // 기본검색 및 상세검색 조합
        if (detailSearch != "") {
            detailSearch = "(" + detailSearch + ") ";
            if (where != "") {
                where += "AND ";
            }
            where += detailSearch;
        }

        // 조건이 하나라도 있는지 검사
        if (where != "") {
            where = "WHERE " + where;
        }

        /*
         * =====정렬처리=====
         */
        Sort sort = pageable.getSort();
        String orderBy = "";

        String[] orderByText = { "" };
        sort.forEach((Order arg) -> orderByText[0] = "a." + arg.getProperty() + " " + arg.getDirection() + " ");

        if (orderByText[0] != "") {
            orderBy += "ORDER BY ";
            orderBy += orderByText[0];
        }

        /*
         * =====Query처리=====
         */

        // Query 생성
        String query = "" + "SELECT a " + "FROM TB_BCM_PRGM a " + where + orderBy;

        TypedQuery<Program> typedQuery = em.createQuery(query, Program.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        PageDTO<Program> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        // 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setPage(pageable);

        result.setList(typedQuery.getResultList());

        return result;
    }

    @Override
    public List<Program> findQueryBySearch(ProgramSearchDTO search, Sort sort) {
        /*
         * =====조건처리=====
         */

        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(a.programName) LIKE :keyword OR UPPER(a.programId) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 미사용 포함
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(a.useYn) = 'Y' ";
        }

        // 상세 검색
        String detailSearch = "";

        // 프로그램 유형
        if (search.getProgramType() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(a.programType) = :programType ";
            paramMap.put("programType", search.getProgramType());
        }

        // 기본검색 및 상세검색 조합
        if (detailSearch != "") {
            detailSearch = "(" + detailSearch + ") ";
            if (where != "") {
                where += "AND ";
            }
            where += detailSearch;
        }

        // 조건이 하나라도 있는지 검사
        if (where != "") {
            where = "WHERE " + where;
        }

        /*
         * =====정렬처리=====
         */
        String orderBy = "";

        String[] orderByText = { "" };
        sort.forEach((Order arg) -> orderByText[0] = "a." + arg.getProperty() + " " + arg.getDirection() + " ");

        if (orderByText[0] != "") {
            orderBy += "ORDER BY ";
            orderBy += orderByText[0];
        }

        /*
         * =====Query처리=====
         */

        // Query 생성
        String query = "" + "SELECT a " + "FROM TB_BCM_PRGM a " + where + orderBy;

        TypedQuery<Program> typedQuery = em.createQuery(query, Program.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        return typedQuery.getResultList();

    }

}
