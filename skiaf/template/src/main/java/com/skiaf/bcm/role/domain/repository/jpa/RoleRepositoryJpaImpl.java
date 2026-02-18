/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.dto.RoleSearchDTO;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 권한 JPA Repository ExtendImpl
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 *
 * </pre>
 */
public class RoleRepositoryJpaImpl implements RoleRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<Role> findQueryBySearch(RoleSearchDTO search, Pageable pageable) {

        /*
         * =====조건처리=====
         */

        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(r.roleName) LIKE :keyword OR UPPER(r.roleId) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 사용만 조회하는지 확인
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(r.useYn) = 'Y' ";
        }

        // 상세 검색
        String detailSearch = "";

        // 권한 유형
        if (search.getRoleType() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(r.roleType) = :roleType ";
            paramMap.put("roleType", search.getRoleType());
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
        String sortStr = sort.toString();
        String orderBy = "";
        if (StringUtils.isNotBlank(sortStr)) {
            orderBy += "ORDER BY ";
            orderBy += Util.removeChar(sortStr, ":");
        }

        /*
         * =====Query처리=====
         */

        // Query 생성
        String query = "" + "SELECT r " + "FROM TB_BCM_ROLE r " + where + orderBy;

        TypedQuery<Role> typedQuery = em.createQuery(query, Role.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        PageDTO<Role> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());
        // 조건 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setPage(pageable);
        result.setList(typedQuery.getResultList());

        return result;
    }

    @Override
    public List<Role> findQueryBySearch(RoleSearchDTO search) {

        /*
         * =====조건처리=====
         */

        String where = "";
        Map<String, Object> paramMap = new HashMap<>();

        // 기본 검색

        // 검색어
        if (search.getKeyword() != null) {
            where += "(UPPER(r.roleName) LIKE :keyword OR UPPER(r.roleId) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 사용만 조회하는지 확인
        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";
            }
            // 사용만 조회
            where += "UPPER(r.useYn) = 'Y' ";
        }

        // 상세 검색
        String detailSearch = "";

        // 권한 유형
        if (search.getRoleType() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(r.roleType) = :roleType ";
            paramMap.put("roleType", search.getRoleType());
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
        String orderBy = "ORDER BY r.updateDate DESC ";


        /*
         * =====Query처리=====
         */

        // Query 생성
        String query = "SELECT r " + "FROM TB_BCM_ROLE r " + where + orderBy;

        TypedQuery<Role> typedQuery = em.createQuery(query, Role.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        List<Role> result = new ArrayList<>();

        result.addAll(typedQuery.getResultList());

        return result;

    }
}
