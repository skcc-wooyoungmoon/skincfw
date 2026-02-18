/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModel;
import com.skiaf.bcm.program.domain.model.Program;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 프로그램 <=> 권한 매핑 Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_PRGM_ROLE_MAP")
public class ProgramRoleMap extends BaseModel {

    private static final long serialVersionUID = 3701270441027161431L;

    @EmbeddedId
    private ProgramRoleMapId mapId;

    /** 프로그램 */
    @MapsId("programId")
    @ManyToOne
    @JoinColumn(name = "PRGM_ID")
    @JsonBackReference
    private Program program;

    /** 권한 */
    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    @JsonBackReference
    private Role role;

    /** 생성자 */
    public ProgramRoleMap() {

    }

    public ProgramRoleMap(String programId, String roleId) {
        this.mapId = new ProgramRoleMapId(programId, roleId);

        Program programInit = new Program();
        programInit.setProgramId(programId);
        this.program = programInit;

        Role roleInit = new Role();
        roleInit.setRoleId(roleId);
        this.role = roleInit;
    }
}
