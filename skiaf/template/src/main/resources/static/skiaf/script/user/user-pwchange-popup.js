/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01876
 * description : 사용자 비번변경
 */
"use strict";
var UserPwChangeModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        if(param.user == null || param.user == ""){
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.user.valid.no-update-user'], function(){ $a.close(); });
        }else{
            $('#userInfo').setData(param.user);
            UserPwChangeModule.addEvent();
        }
        
        
    };
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        
        //input 값 변경시 validation text 제거
        $('#userInfo').find('input:password').on('keydown ', function (e){
            $(e.target).parent().removeClass('success').removeClass('danger');
        });
        
    
        //취소 버튼
        $('#cancleBtn').on('click', function(e) {
            $a.close();
        });
        
        
        //저장 버튼
        $('#saveBtn').on('click', function(e){
            let userData = $('#userInfo').getData();
            if(UserPwChangeModule.validationCheck(userData)){
                UserPwChangeModule.passwordChange(userData);
            }
        
        });

    };
    
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    

    this.validationCheck = function(userData){

        if(userData.prePassword == null || userData.prePassword == ""){
            // 현재 비밀번호를 입력하십시오.
            $('#prePasswordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.now-password.null']).addClass('Color-danger');
            $('#prePasswordMessage').show();
            $('#prePassword').find('input').focus();
            return false;
        } else {
            $('#prePasswordMessage').hide();
        }
        
        if(userData.password == null || userData.password == ""){
            // 새 비밀번호를 입력하십시오.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-password.null']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(userData.passwordCheck == null || userData.passwordCheck == ""){
            // 비밀번호 확인을 입력하십시오.
            $('#passwordCheckMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-password-check.null']).addClass('Color-danger');
            $('#passwordCheckMessage').show();
            $('#passwordCheck').find('input').focus();
            return false;
        } else {
            $('#passwordCheckMessage').hide();
        }
        
        if(userData.password !== userData.passwordCheck){
            // 새 비밀번호와 비밀번호 확인이 일치하지 않습니다.
            $('#passwordCheckMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.new-check.not-equal']).addClass('Color-danger');
            $('#passwordCheckMessage').show();
            $('#passwordCheck').find('input').focus();
            return false;
        } else {
            $('#passwordCheckMessage').hide();
        }
        
        if(userData.prePassword == userData.password){
            // 이전 비밀번호와 같을 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.pre-new.equal']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.unionRule(userData.password)){
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
        
        if(!SKIAF.passwordUtil.oneStringRepetition(userData.password)){
            // 동일한 글자가 연속 3번 들어갈 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.one-string-repetition']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.twoStringRepetition(userData.password)){
            // 두자 이상의 동일 문자가 연속 2번 들어갈 수 없습니다.
            $('#passwordMessage').text(SKIAF.i18n.messages['bcm.common.password-change.valid.two-string-repetition']).addClass('Color-danger');
            $('#passwordMessage').show();
            $('#password').find('input').focus();
            return false;
        } else {
            $('#passwordMessage').hide();
        }
        
        if(!SKIAF.passwordUtil.keyboardContinuity(userData.password)){
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
    
    
    //비번변경
    this.passwordChange = function(userData) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_DETAIL_PWCHANGE_MY), {
            method : 'PATCH',
            data: userData,
            success: function(res) {
                $a.close(res);
            }
        });
    };

});