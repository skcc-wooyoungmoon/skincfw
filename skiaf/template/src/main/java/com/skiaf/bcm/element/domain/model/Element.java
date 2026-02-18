/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.role.domain.model.ElementRoleMap;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *
 * BCM 프로그램요소 Entity
 *
 * History
 * - 2018. 7. 25. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_VIEW_ELEMENT")
public class Element extends BaseModelUseYnSupport {

    private static final long serialVersionUID = -1090106417814961305L;

    /** 요소 순번 */
    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long elementSeq;

    /** 프로그램 아이디 */
    @ApiModelProperty(required = true, example = "PSNEW-01")
    @ManyToOne
    @JoinColumn(name = "PRGM_ID")
    @JsonBackReference
    private Program program;

    /** 요소 키 */
    @ApiModelProperty(required = true, example = "#btn001")
    @NotBlank
    @Length(max = 128)
    @Column(length = 128, nullable = false)
    private String elementKey;

    /** 요소 설명 */
    @ApiModelProperty(required = false, example = "버튼1 입니다.")
    @Length(max = 2000)
    @Column(length = 2000, nullable = true)
    private String elementDesc;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "element", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ElementRoleMap> elementRoleMap;

}
