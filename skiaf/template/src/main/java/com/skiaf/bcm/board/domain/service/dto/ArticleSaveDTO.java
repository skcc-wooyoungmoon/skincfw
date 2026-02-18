/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.board.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleSaveDTO implements Serializable {
    
    private static final long serialVersionUID = -3427902307396202162L;

    private String articleId;
    
    private String articleTitle;

    private String articleCtnt;
    
    private String articleBeginDtm;
    
    private String articleEndDtm;
    
    private boolean emgcYn;
    
    private int hitCnt;
}
