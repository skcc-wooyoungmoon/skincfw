/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01871
 * description : 사용자그룹 등록
 */
"use strict";
var UserGroupCreateModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 서버
    var isAjaxAvailable = true; // Ajax 사용가능 여부
    
    // 중복 아이디 체크
    var idCheck = null;
    
    var userGroupIdLabel = SKIAF.i18n.messages['bcm.usergroup.id'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], userGroupIdLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], userGroupIdLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], userGroupIdLabel);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
        
        UserGroupCreateModule.setCompanyCode();
        
        UserGroupCreateModule.addEvent();        
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function () {
        
        // input변경시 처리
        $('#inputUserGroupId').on('keyup', function() {
            idCheck = false;
            $('#userGroupIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg).show();

        });

        // 저장 버튼
        $('#btnCreateUserGroup').on('click', function() {
            
            // Validation Check
            if (!$('#userGroupCreateTable').validate()) {
                $('#userGroupCreateTable').validator();
                return;
            }
            
            if (idCheck == null || idCheck == false) {
                return;
                
            } else {
                UserGroupCreateModule.userGroupCreate();
                
            }
        });
        
        // 중복 체크 버튼
        $('#btnDuplicateCheck').on('click', function() {

         // Validation ID Check
            if (!$('#inputUserGroupId').validate()) {
                $('#inputUserGroupId').validator();
                return;
            }
            
            UserGroupCreateModule.duplicateCheck();

        });
        
        // 취소 버튼
        $('#btnCreateUserGroupCancel').on('click', function() {
            $a.close();
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 회사 코드 가져오기
     */
    this.setCompanyCode = function (){
        
        //회사그룹코드
        var companyCode = SKIAF.CONSTATNT.CODE_GROUP_COMPANY_ID;
        
        var companyName = null;
                
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODES_DETAIL_LANG, companyCode), {
            method : 'GET',
            async : false,
            success : function(result) {

                var defaultCode = []
                defaultCode.codeId = '';
                defaultCode.codeName = SKIAF.i18n.messages['bcm.common.select'];
                
                result.data.unshift(defaultCode);
                
                companyName = result.data;
                
                $('#selectPopup').setData({
                    dataPopup : companyName
                });
            }
        });
    };    
    
    this.goDetail = function (userGroupId) {
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USER_GROUPS_DETAIL, userGroupId);
        return;
    };
    
    this.goList = function (){
        window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS;
        return;
    };

    /**
     * 사용자 그룹 생성
     */
    this.userGroupCreate = function() {
        
        var createData = $('#userGroupCreate').getData();
        
        createData.useYn = true;
        
        createData.companyCode = createData.optionSelected;
        
        $a.request(SKIAF.PATH.USER_GROUPS, {
            method : 'POST',
            data : createData,
            success : function(result) {

                UserGroupCreateModule.goList();
                
            },
            after : function() {
                isAjaxAvailable = true;
            }
        });
    };
    
    /**
     * 사용자 그룹 중복체크
     */
    this.duplicateCheck = function() {
        var userGroupId = $('#userGroupCreate').getData().userGroupId;
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUPS_DUPLICATE, userGroupId), {
            method : 'GET',
            
            success : function(result){
                
                if (!result) {
                    return;
                }
                if(result.data == true){
                    
                    idCheck = false;
                    $('#userGroupIdCheckMsg')
                        .addClass('Color-danger')
                        .removeClass('Color-confirm')
                        .text(idDuplicationMsg).show();
                } else {
                    
                    idCheck = true;
                    $('#userGroupIdCheckMsg')
                        .addClass('Color-confirm')
                        .removeClass('Color-danger')
                        .text(idSuccessMsg).show();
                    
                }
            },
            after : function() {
                isAjaxAvailable = true;
            }
        });
    };

});