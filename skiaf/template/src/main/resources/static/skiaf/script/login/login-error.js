/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01878
 * description : 로그인 에러 페이지
 */
"use strict";
var LoginErrorModel = $a.page(function() {
    
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
        
        LoginErrorModel.pageInit();
        
        LoginErrorModel.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        $("#ssoLoginBtn").on('click', function(e) {
            location.replace(SKIAF.contextPath + SKIAF.ssoRedirectLoginUrl);
        });
        
        $("#homeBtn").on('click', function(e) {
            location.replace(SKIAF.contextPath + SKIAF.bcmHomeUrl);
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.pageInit = function(params) {
        var code = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.CODE);
        var userMessage = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.USER_MESSAGE);
        var systemMessage = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.SYSTEM_MESSAGE);

        var title = '';
        switch(code) {
            case SKIAF.CONSTATNT.LOGIN_SSO_ACCESS_DENIED : title = SKIAF.i18n.messages['bcm.login.sso.error.sso-access-denied.label'];  break;// LOGIN_SSO_ACCESS_DENIED              = "0204";   // SSO 접근 거부
            case SKIAF.CONSTATNT.LOGIN_SSO_UNAVAILABLE   : title = SKIAF.i18n.messages['bcm.login.sso.error.sso-unavilable.label'];     break;// LOGIN_SSO_UNAVAILABLE                = "0205";   // SSO 장애
            case SKIAF.CONSTATNT.LOGIN_SSO_NOT_LOGIN     : title = SKIAF.i18n.messages['bcm.login.sso.error.sso-not-login.label'];      break;// LOGIN_SSO_NOT_LOGIN                  = "0206";   // SSO 미로그인
            // LOGIN_SSO_NOT_REGISTRAION            = "0207";   // SSO 사용자가 등록되어 있지 않음
            // LOGIN_SSO_NOT_USE                    = "0208";   // SSO 사용자의 userYn = N
            case SKIAF.CONSTATNT.LOGIN_SSO_NOT_REGISTRAION :
            case SKIAF.CONSTATNT.LOGIN_SSO_NOT_USE :
                title = SKIAF.i18n.messages['bcm.login.sso.error.not-registraion.label'];
                break;
            default :
                title = SKIAF.i18n.messages['bcm.login.sso.error.sso-fail.label'];
                break;
        }
                
        $("#title").text(title);        
        $("#userMessage").html(userMessage);
        if(systemMessage != null){
            $("#systemMessage").text(systemMessage);
        }else{
            $("#systemMessage").remove();
        }
    };
    
});
