/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01878
 * description : 에러 공통
 */
"use strict";
var ErrorModel = $a.page(function() {

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
        
        // 새창에서 오픈시 닫기 버튼 노출
        if (window.opener != null) {
            $('.error-btn').children().hide();
            $('#closeBtn').show();
        }
        
        ErrorModel.addEvent();

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        try {
            $("#ssoLoginBtn").on('click',function(e) {
                location.replace(SKIAF.contextPath + SKIAF.ssoRedirectLoginUrl);
            });
        } catch (err) {}

        try {
            $("#homeBtn").on('click', function(e) {
                location.replace(SKIAF.contextPath + SKIAF.bcmHomeUrl);
            });
        } catch (err) {}

        try {
            $("#prevPageBtn").on('click', function(e) {
                
                // A권한을 가지고 있는 사용자가 A권한을 요구하는 페이지에서 로그아웃 하고 B라는 사용자로 로그인 할 경우
                // 403에러페이지로 보내고 이전페이지로 이동 시 로그인 페이지로 이동한다. 이를 막기위한 로직.
                if(document.referrer.indexOf(SKIAF.PATH.VIEW_LOGIN) > -1){
                    location.href = SKIAF.contextPath + SKIAF.bcmHomeUrl; 
                }else{
                    history.back(1);
                }
            });
        } catch (err) {}

        try {
            $("#goLoginBtn").on('click',function(e) {
                location.replace(SKIAF.contextPath + SKIAF.PATH.VIEW_LOGIN);
            });
        } catch (err) {}
        
        try {
            $("#closeBtn").on('click',function(e) {
                window.close();
            });
        } catch (err) {}

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

});
