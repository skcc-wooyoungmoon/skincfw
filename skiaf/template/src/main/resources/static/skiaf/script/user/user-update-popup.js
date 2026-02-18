/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01876
 * description : 사용자 수정
 */
"use strict";
var UserUpdateModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.userId = null;
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {

        if(param.userId == null || param.userId == ""){
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.user.valid.no-update-user'], function(){ $a.close(); });
        }else{
            
            UserUpdateModule.userId = param.userId;
            //회사목록 조회
            UserUpdateModule.getComponyList();
            //이벤트 등록
            UserUpdateModule.addEvent();
        }
    };
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        //취소 버튼
        $('#cancleBtn').on('click', function(e) {
            $a.close();
        });
        
        
        //저장 버튼
        $('#saveBtn').on('click', function(e){
            
            var userData = $('#userUpdateForm').getData();
            if(UserUpdateModule.validationCheck(userData)){
                
                userData.email = userData.email1 + "@" + userData.email2;
                userData.telNo = userData.telNo1 + "-" + userData.telNo2 + '-' + userData.telNo3;
                userData.mobileNo = userData.mobileNo1 + "-" + userData.mobileNo2 + '-' + userData.mobileNo3;
                userData.ssoYn = userData.ssoYn == 'Y' ? true : false;
                userData.useYn = userData.useYn == 'Y' ? true : false;
                $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_DETAIL, userData.userId), {
                    method : 'PUT',
                    data : userData,
                    success : function(res) {
                        $a.close(res);
                    }
                
                });
            }
        });
        
        //비밀번호 초기화 버튼
        $('#pwResetBtn').on('click', function(e){
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.user.confirm-password-reset'] , function callback(){
                var userData = $('#userUpdateForm').getData();
                $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_DETAIL_PWRESET, userData.userId), {
                    method : 'PUT',
                    data : null,
                    success : function(res) {
                        $a.close(res);
                    }
                
                });
            });
        });
        
        //잠김해제 버튼
        $('#failCountResetBtn').on('click', function(e){
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.user.confirm-lock-release'] , function callback(){
                var userData = $('#userUpdateForm').getData();
                $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_DETAIL_FAILRESET, userData.userId), {
                    method : 'PUT',
                    data : null,
                    success : function(res) {
                        $a.close(res);
                    }
                
                });
            });
        });
        
        // 이메일 선택 이벤트
        $('#email1').parent('td').find('select').on('change', function(){
            if($.trim($(this).val()) != '') {
                $('#email2').val($(this).val());
            }
        });
    };
    
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     *  회사목록 조회
     */
    this.getComponyList = function(){
        //회사 목록 조회
        var companyCode = SKIAF.CONSTATNT.CODE_GROUP_COMPANY_ID;
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODES_DETAIL_LANG, companyCode), {
            method : 'GET',
            async : false,
            success : function(res) {
                
                let companyOptions = [{TEXT: SKIAF.i18n.messages['bcm.common.select-option'] ,VALUE:"NONE"}];
                res.data.forEach(function(item){
                    companyOptions.push({TEXT:item.codeName, VALUE:item.codeId})
                });
                let companyObj = {companyOptions : companyOptions, companyCode : 'NONE'};
                $('#companyTr').setData(companyObj);
                
                UserUpdateModule.getUserInfo(UserUpdateModule.userId);
            }
        });
    };
    
    this.getUserInfo = function(userId){
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_DETAIL, userId), {
            method : 'GET',
            async : false,
            success : function(res) {
                
                let userData = res.data;
                if(userData.email){
                    userData.email1 = res.data.email.split('@')[0];
                    userData.email2 = res.data.email.split('@')[1];
                }
                if(userData.telNo){
                    userData.telNo1 = res.data.telNo.split('-')[0];
                    userData.telNo2 = res.data.telNo.split('-')[1];
                    userData.telNo3 = res.data.telNo.split('-')[2];
                }
                if(userData.mobileNo){
                    userData.mobileNo1 = res.data.mobileNo.split('-')[0];
                    userData.mobileNo2 = res.data.mobileNo.split('-')[1];
                    userData.mobileNo3 = res.data.mobileNo.split('-')[2];
                }
                userData.ssoYn = userData.ssoYn ? 'Y' : 'N';
                userData.useYn = userData.useYn ? 'Y' : 'N';
                $('#userUpdateForm').setData(userData);
            }
        });
    };
    
    
    /**
     *  벨리데이션 체크
     */
    this.validationCheck = function(userData){
            
        //2. 사용자 이름
        if(userData.userName == null || userData.userName.length < 2){
            $('#userNameMessage').text(SKIAF.i18n.messages['bcm.user.valid.user-name-length']).addClass('Color-danger');
            $('#userNameMessage').show();
            $('#userName').find('input').focus();
            return false;
        } else {
            $('#userNameMessage').hide();
        }
        
        //3. 이메일
        if(userData.email1 != null && userData.email2 != null){
            if(!SKIAF.util.isEmail(userData.email1 + "@" + userData.email2)){
                $('#email1Message').text(SKIAF.i18n.messages['bcm.user.valid.email-check']).addClass('Color-danger');
                $('#email1Message').show();
                $('#email1').find('input').focus();
                return false;
            } else {
                $('#email1Message').hide();
            }
        }else{
            $('#email1Message').text(SKIAF.i18n.messages['bcm.user.valid.email']).addClass('Color-danger');
            $('#email1Message').show();
            $('#email1').find('input').focus();
            return false;
        }
        
        //4. 회사구분 
        if(userData.companyCode == 'NONE'){
            $('#companyMessage').text(SKIAF.i18n.messages['bcm.user.valid.company-select']).addClass('Color-danger');
            $('#companyMessage').show();
            $('#companyTr').find('select').focus();
            return false;
        } else {
            $('#companyMessage').hide();
        }

        return true;
    };

});