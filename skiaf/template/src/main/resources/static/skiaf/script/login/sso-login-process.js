/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01878
 * description : SSO 로그인 중
 */
"use strict";
var SSOLoginProcessModel = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {

        SSOLoginProcessModel.ssoLogin(param);

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.ssoLogin = function(param) {

        $a.request(SKIAF.PATH.SSO_LOGIN, {
            method : 'GET',
            data : param,
            success : function(res) {
                SKIAF.loginUtil.loginSuccess(SKIAF.returnUrlAfterLogin);
            },
            error : function(res) {
                var errorRes = JSON.parse(res.response)
                var code = errorRes.meta.code;
                if (code != null) {
                    Cookies.set(SKIAF.JS_CONSTANT.CODE, code)
                    Cookies.set(SKIAF.JS_CONSTANT.USER_MESSAGE, errorRes.meta.userMessage)
                    Cookies.set(SKIAF.JS_CONSTANT.SYSTEM_MESSAGE, errorRes.meta.systemMessage)
                    location.replace(SKIAF.contextPath + SKIAF.PATH.VIEW_LOGIN_ERROR);
                } 
            }
        });
    };
});
