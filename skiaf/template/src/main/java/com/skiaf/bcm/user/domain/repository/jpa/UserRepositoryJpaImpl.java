/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 사용자 JPA Repository ExtendImpl
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
@Repository
public class UserRepositoryJpaImpl implements UserRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public PageDTO<User> findQueryByKeyword(UserSearchDTO search, Pageable pageable) {

        /*
         * =====조건처리=====
         */

        String where = "";
        Map<String, String> paramMap = new HashMap<>();

        // 기본 검색
        if (search.getKeyword() != null) {
            // where += "(UPPER(u.loginId) LIKE :keyword OR UPPER(u.userName) LIKE :keyword
            // OR UPPER(u.email) LIKE :keyword) ";
            where += "(UPPER(u.loginId) LIKE :keyword OR UPPER(u.userName) LIKE :keyword) ";
            paramMap.put("keyword", "%" + search.getKeyword().toUpperCase() + "%");
        }

        // 상세 검색
        String detailSearch = "";

        // 회사코드
        if (search.getCompanyCode() != null) {
            detailSearch += "UPPER(u.companyCode) LIKE :companyCode ";
            paramMap.put("companyCode", "%" + search.getCompanyCode().toUpperCase() + "%");
        }

        // 부서명
        if (search.getDepartmentName() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(u.departmentName) LIKE :departmentName ";
            paramMap.put("departmentName", "%" + search.getDepartmentName().toUpperCase() + "%");
        }

        // 직급
        if (search.getPositionNm() != null) {
            if (detailSearch != "") {
                detailSearch += "AND ";
            }
            detailSearch += "UPPER(u.positionName) LIKE :positionName ";
            paramMap.put("positionName", "%" + search.getPositionNm().toUpperCase() + "%");
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
        String query = "" + "SELECT u " + "FROM TB_BCM_USER u " + where + orderBy;

        TypedQuery<User> typedQuery = em.createQuery(query, User.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, String value) -> typedQuery.setParameter(key, value));

        PageDTO<User> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());
        // 조건 페이징 추가
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setPage(pageable);
        result.setList(typedQuery.getResultList());

        return result;
    }

}
