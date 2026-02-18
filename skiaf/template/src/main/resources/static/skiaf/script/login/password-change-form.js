/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01878
 * description : 비밀번호 변경
 */
"use strict";
var PasswordChangeFormModel = $a.page(function() {
    
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
        PasswordChangeFormModel.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        $("#changeBtn").on('click', function(e) {
            
            var prePassword = $("#prePassword").val();
            var password = $("#password").val();
            var passwordCheck = $("#passwordCheck").val();
            var userData = SKIAF.loginUser;
            
            if(!PasswordChangeFormModel.validCheck(prePassword, password, passwordCheck, userData)){
                return;
            }
            
            var params = {
                'prePassword' : prePassword,'password' : password, 'passwordCheck' : passwordCheck 
            }
            PasswordChangeFormModel.passwordChange(userData.userId, params);
        });
        
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.validCheck = function(prePassword, password, passwordCheck, userData) {
        
        if(prePassword == null || prePassword == ""){
            // 현재 비밀번호를 입력하십시오.
            $('#prePasswordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.now-password.null']).addClass('Color-danger');
            $('#prePasswordMessage').show();
            $('#prePassword').find('input').focus();
            return false;
        } else {
            $('#prePasswordMessage').hide();
        }
        
        if(password == null || password == ""){
            // 새 비밀번호를 입력하십시오.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-password.null']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(passwordCheck == null || passwordCheck == ""){
            // 비밀번호 확인을 입력하십시오.
            $('#passwordCheckMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-password-check.null']).addClass('Color-danger');
            $('#passwordCheckMessage').show();
            $('#passwordCheck').find('input').focus();
            return false;
        } else {
            $('#passwordCheckMessage').hide();
        }
        
        if(password !== passwordCheck){
            // 새 비밀번호와 비밀번호 확인이 일치하지 않습니다.
            $('#passwordCheckMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-check.not-equal']).addClass('Color-danger');
            $('#passwordCheckMessage').show();
            $('#passwordCheck').find('input').focus();
            return false;
        } else {
            $('#passwordCheckMessage').hide();
        }
        
        if(prePassword == password){
            // 이전 비밀번호와 같을 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.pre-new.equal']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.unionRule(password)){
            // 비밀번호 문자 조합 규칙에 맞지 않습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.union-rule-simple']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.personalInformation(userData)){
            // 비밀번호에는 개인정보가 포함 되어서는 안됩니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.personal-infomation-simple']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.oneStringRepetition(password)){
            // 동일한 글자가 연속 3번 들어갈 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.one-string-repetition']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.twoStringRepetition(password)){
            // 두자 이상의 동일 문자가 연속 2번 들어갈 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.two-string-repetition']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.keyboardContinuity(password)){
            // 키보드의 연속된 문자 및 숫자를 사용해서는 안됩니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.keyboard-continuity-simple']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        return true;
    };
    
    this.passwordChange = function(id, params) {

        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.PASSWORD_CHANGE, id), {
            method : 'PATCH',
            data: params,
            success: function(res) {
                SKIAF.loginUtil.loginSuccess(SKIAF.returnUrlAfterLogin);
            }
        });
    };
});
