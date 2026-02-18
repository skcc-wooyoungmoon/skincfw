/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository;

import java.util.List;

import com.skiaf.bcm.role.domain.model.ElementRoleMap;

/**
 * <pre>
 * BCM 프로그램요소 권한 Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
public interface ElementRoleMapRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | JPA - default Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * element 에 role 매핑 다중 저장
     * </pre>
     */
    public <S extends ElementRoleMap> List<S> save(Iterable<S> elementRoleMapList);

    /**
     * <pre>
     * element 에 role 매핑 다중 삭제
     * </pre>
     */
    public void deleteInBatch(Iterable<ElementRoleMap> elementRoleMapList);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Custom Method 
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

}
