/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BoardSearchDTO
 * 
 * History
 * - 2018. 9. 18. | in01871 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class BoardSearchDTO {
    
    @Length(max = 20)
    private String keyword;
    
}
