/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01878
 * description : 로그인
 */
"use strict";
var LoginFormModel = $a.page(function() {

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
        LoginFormModel.getInitData();
        LoginFormModel.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        $("#loginBtn").on('click', function(e) {
            
            if (!$('#loginValidationArea').validate()) {
                $('#loginValidationArea').validator();
                return;
            }
            
            var params = {
                'id' : $('#id').val(),
                'password' : $('#password').val(),
                'languageCode' : $('#languageCode').val()
            }
            LoginFormModel.login(params);
        });

        $("#ssoLoginBtn").on('click', function(e) {
            
            if(SKIAF.useSsoLoginBtn){
                var params = {
                    'ssoTestLoginId' : $('input[name=ssoTestLoginId]').val(),
                    'ssoStatus' : $('input[name=ssoStatus]').val(),
                    'nextSsoStatus' : $('input[name=nextSsoStatus]').val(),
                }
                
                Cookies.set(SKIAF.CONSTATNT.TEMP_LANGUAGE_CODE,$('#languageCode').val());
                
                LoginFormModel.ssoLogin(params);
            }else{
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.login.sso.error.block-login']);
            }
        });

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.getInitData = function() {
        // 언어 코드 조회 및 설정
        var optionSelectedValue = SKIAF.languageSupportList[0].codeId;
        $('#languageCode').setData({
            options : SKIAF.languageSupportList
            , optionSelected : optionSelectedValue
        });
    };
    
    this.login = function(params) {

        $a.request(SKIAF.PATH.LOGIN, {
            method : 'POST',
            data : params,
            success : function(res) {
                SKIAF.loginUtil.loginSuccess(SKIAF.returnUrlAfterLogin);
            },
            error : function(res) {
                var errorRes = JSON.parse(res.response)
                var code = errorRes.meta.code;
                if (code != null) {
                    switch (code) {
                    // LOGIN_REQUIRED_PASSWORD_CHANGE = "0101"; // 패스워드 변경 필요
                    case SKIAF.CONSTATNT.LOGIN_REQUIRED_PASSWORD_CHANGE :
                        location.replace(SKIAF.contextPath
                                + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_CHANGE_PASSWORD, params.id));
                        break;
                    }
                }
            }
        });
    };

    this.ssoLogin = function(params) {
       
        $a.request(SKIAF.PATH.SSO_LOGIN, {
            method : 'GET',
            data : params,
            success : function(res) {
                SKIAF.loginUtil.loginSuccess(SKIAF.returnUrlAfterLogin);
            },
            error : function(res) {
                var errorRes = JSON.parse(res.response)
                var code = errorRes.meta.code;
                if (code != null) {
                    switch (code) {
                    // LOGIN_SSO_FIRST_ACCESS = "0202"; // SSO 최초 로그인
                    case SKIAF.CONSTATNT.LOGIN_SSO_FIRST_ACCESS :
                        location.replace(SKIAF.contextPath + SKIAF.PATH.VIEW_TRY_SSO);
                        break;
                    // LOGIN_SSO_EXPIRE_TIMEOUT = "0203"; // SSO 인증토큰 만료
                    case SKIAF.CONSTATNT.LOGIN_SSO_EXPIRE_TIMEOUT :
                        location.replace(SKIAF.contextPath + SKIAF.PATH.VIEW_SSO_LOGOFF);
                        break;
                    default:
                        Cookies.set(SKIAF.JS_CONSTANT.CODE, code)
                        Cookies.set(SKIAF.JS_CONSTANT.USER_MESSAGE, errorRes.meta.userMessage)
                        Cookies.set(SKIAF.JS_CONSTANT.SYSTEM_MESSAGE, errorRes.meta.systemMessage)
                        location.replace(SKIAF.contextPath + SKIAF.PATH.VIEW_LOGIN_ERROR);
                        break;
                    }
                } else {
                    alert(errorRes.meta.userMessage);
                }
            }
        });
    };
    
});
