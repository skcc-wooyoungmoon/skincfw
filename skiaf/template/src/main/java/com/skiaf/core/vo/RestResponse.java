/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.vo;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;

import com.skiaf.core.constant.MessageDisplayType;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *  
 * History
 * - 2018. 8. 7. | in01868 | 최초작성.
 * - 2018. 8. 21. | in01865 | data와 meta로 재정리.
 * </pre>
 */
public class RestResponse implements Serializable {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    private static final long serialVersionUID = -7857710275656843711L;

    // Data
    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private RestResponseMeta meta = new RestResponseMeta();

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Constructor
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 
     */
    public RestResponse() {
    }

    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    public RestResponse(Object data) {
        this.data = data;

        if (data instanceof PageDTO) {
            // PageDTO는 list가 data 자체가 되게함.
            this.data = ((PageDTO) data).getList();
            this.meta.setPage(((PageDTO) data).getPage());
            this.meta.setTotalCount(((PageDTO) data).getTotalCount());
        }
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Getter & Setter Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    /**
     * useMessage
     */
    public void setUserMessage(String message) {
        this.meta.setUserMessage(message);
    }

    /**
     * systemMessage
     */
    public void setSystemMessage(String systemMessage) {
        this.meta.setSystemMessage(systemMessage);
    }

    /**
     * displayType
     */
    public void setDisplayType(MessageDisplayType displayType) {
        this.meta.setDisplayType(displayType);
    }

    /**
     * code
     */
    public void setCode(String code) {
        this.meta.setCode(code);
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Method for chaining
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public RestResponse userMessage(String userMessage) {
        this.setUserMessage(userMessage);
        return this;
    }

    public RestResponse systemMessage(String systemMessage) {
        this.setSystemMessage(systemMessage);
        return this;
    }

    public RestResponse displayType(MessageDisplayType displayType) {
        this.setDisplayType(displayType);
        return this;
    }

    public RestResponse code(String code) {
        this.setCode(code);
        return this;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Inner Class
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    @Getter
    @Setter
    private class RestResponseMeta implements Serializable {
        private static final long serialVersionUID = 144476231638185217L;
        
        private String userMessage = "";
        private String systemMessage = "";
        private MessageDisplayType displayType;
        private String code = "";
        private Pageable page;
        private int totalCount;
    }
}
