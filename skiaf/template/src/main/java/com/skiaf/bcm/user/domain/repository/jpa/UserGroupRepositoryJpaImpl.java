/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 사용자그룹 JPA RepositoryImpl
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Repository
public class UserGroupRepositoryJpaImpl implements UserGroupRepositoryJpaExtend {

    private static final String KEYWORD = "keyword";

    @PersistenceContext
    EntityManager em;

    @Override
    public List<UserGroup> findQueryByKeyword(String keyword) {

        String query = "" + "SELECT grp " + "FROM TB_BCM_USER_GRP grp " + "WHERE grp.userGroupId LIKE :keyword "
                + "OR grp.userGroupName LIKE :keyword ";

        return em.createQuery(query, UserGroup.class).setParameter(KEYWORD, "%" + keyword + "%").getResultList();
    }

    @Override
    public PageDTO<UserGroup> findQueryBySearch(UserGroupSearchDTO search, Pageable pageable) {

        String where = "";
        Map<String, String> paramMap = new HashMap<>();

        // 기본 검색

        if (search.getCompanyCode() != null) {
            where += "UPPER(grp.companyCode) = :companyCode ";
            paramMap.put("companyCode", search.getCompanyCode().toUpperCase());

            if (search.getKeyword() != null) {
                where += " AND (UPPER(grp.userGroupId) LIKE :keyword OR UPPER(grp.userGroupName) LIKE :keyword) ";
                paramMap.put(KEYWORD, "%" + search.getKeyword().toUpperCase() + "%");
            }
        } else {
            if (search.getKeyword() != null) {
                where += "(UPPER(grp.userGroupId) LIKE :keyword OR UPPER(grp.userGroupName) LIKE :keyword) ";
                paramMap.put(KEYWORD, "%" + search.getKeyword().toUpperCase() + "%");
            }
        }

        if (!search.isUnusedInclude()) {
            if (where != "") {
                where += "AND ";

            }
            // 사용만 조회
            where += "UPPER(grp.useYn) = 'Y' ";
        }

        // 조건이 하나라도 있는지 검사
        if (where != "") {
            where = "WHERE " + where;

        }

        Sort sort = pageable.getSort();
        String sortStr = sort.toString();
        String orderBy = "";

        if (StringUtils.isNotBlank(sortStr)) {
            orderBy += "ORDER BY ";
            orderBy += Util.removeChar(sortStr, ":");
        }

        String query = "" + "SELECT grp " + "FROM TB_BCM_USER_GRP grp " + where + orderBy;

        TypedQuery<UserGroup> typedQuery = em.createQuery(query, UserGroup.class);

        paramMap.forEach((String key, String value) -> typedQuery.setParameter(key, value));

        PageDTO<UserGroup> result = new PageDTO<>();
        result.setTotalCount(typedQuery.getResultList().size());

        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        result.setList(typedQuery.getResultList());
        result.setPage(pageable);

        return result;
    }

}
