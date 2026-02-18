/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;

/**
 * <pre>
 * BCM 권한 <=> 사용자, 사용자그룹 매핑 JPA Repository Implements
 *
 * History
 * - 2018. 10. 11. | in01866 | 최초작성.
 *
 * </pre>
 */
@Repository
public class RoleUserGroupUserMapRepositoryJpaImpl implements RoleUserGroupUserMapRepositoryJpaExtend {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<RoleUserGroupUserMap> activationRoleMap(String userId, List<String> userGroupIds, String betweenDate) {

        /*
         * =====조건처리=====
         */
        String where = "";

        Map<String, Object> paramMap = new HashMap<>();

        // 유저 ID OR 유저그룹
        String userOrUserGroup = "";
        if (!StringUtils.isBlank(userId)) {
            userOrUserGroup += "a.user.userId = :userId ";
            paramMap.put("userId", userId);
        }
        if (userGroupIds != null && userGroupIds.size() > 0) {
            if (userOrUserGroup != "") {
                userOrUserGroup += "OR ";
            }
            userOrUserGroup += "a.userGroup.userGroupId IN (:userGroupIds) ";
            paramMap.put("userGroupIds", userGroupIds);
        }

        // 유저 ID OR 유저그룹 조건 추가
        if (userOrUserGroup != "") {
            where += "(" + userOrUserGroup + ") ";
        }

        // 권한적용일
        if (!StringUtils.isBlank(betweenDate)) {
            if (where != "") {
                where += "AND ";
            }
            where += "a.roleBeginDt <= :betweenDate AND a.roleEndDt >= :betweenDate ";
            paramMap.put("betweenDate", betweenDate);
        }

        // 미사용 제외
        if (where != "") {
            where += "AND ";
        }
        where += "UPPER(a.useYn) = 'Y' ";

        // 조건이 하나라도 있는지 검사
        if (!StringUtils.isBlank(where)) {
            where = "WHERE " + where;
        }

        /*
         * Query 처리
         */

        // Query 생성
        String query = ""
                + "SELECT a "
                + "FROM TB_BCM_ROLE_USER_MAP a "
                + "LEFT JOIN a.user "
                + "LEFT JOIN a.userGroup "
                + where;

        TypedQuery<RoleUserGroupUserMap> typedQuery = em.createQuery(query, RoleUserGroupUserMap.class);

        // 조건 파라메터 추가
        paramMap.forEach((String key, Object value) -> typedQuery.setParameter(key, value));

        return typedQuery.getResultList();
    }


}
