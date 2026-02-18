/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01876
 * description : 권한 등록 팝업
 */
"use strict";
var RoleCreateModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    

    var menuLabel = SKIAF.i18n.messages['bcm.role.type.menu'];
    var programLabel = SKIAF.i18n.messages['bcm.role.type.program'];
    var elementLabel = SKIAF.i18n.messages['bcm.role.type.element'];
    var bizLabel = SKIAF.i18n.messages['bcm.role.type.biz'];
    
    var roleTypeObj = {
            roleOptions : [ 
                {'TEXT' : SKIAF.i18n.messages['bcm.common.select-option'], 'VALUE' : ''},
                {'TEXT' : SKIAF.i18n.messages['bcm.role.type.menu'],       'VALUE' : SKIAF.ENUM.ROLE_TYPE.MENU},
                {'TEXT' : SKIAF.i18n.messages['bcm.role.type.program'],       'VALUE' : SKIAF.ENUM.ROLE_TYPE.PROGRAM},
                {'TEXT' : SKIAF.i18n.messages['bcm.role.type.element'],    'VALUE' : SKIAF.ENUM.ROLE_TYPE.ELEMENT},
                {'TEXT' : SKIAF.i18n.messages['bcm.role.type.biz'],        'VALUE' : SKIAF.ENUM.ROLE_TYPE.BIZ}
            ],
            roleType : ''
    };
    
    var messageKeyLabel = SKIAF.i18n.messages['bcm.role.id'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], messageKeyLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], messageKeyLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], messageKeyLabel);
    var idValidationMsg = SKIAF.i18n.messages['bcm.common.id.validation'];
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        $('#roleTypeTr').setData(roleTypeObj);
        
        RoleCreateModule.addEvent();
        
    };
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /*
         * 저장
         */
        $('#saveBtn').on('click', function(e){
            var roleData = $('#roleForm').getData();

            // validation 체크
            if (!$('#roleForm').validate()) {
                $('#roleForm').validator();
                return;
            }
            
            // Id check
            var isIdConfirm = $('#roleIdCheckMsg').hasClass('Color-confirm');
            if (!isIdConfirm) {
                $('#roleIdCheckMsg').show();
                return;
            }

            // 저장 처리
            $a.request(SKIAF.PATH.ROLES, {
                method : 'POST',
                data : roleData,
                success : function(res) {
                    $a.close(res);
                }
            });
        });
        
        
        /*
         * 취소
         */
        $('#cancleBtn').on('click', function(e) {
            $a.close();
        });
        
        
        /*
         * 키 입력 변경 이벤트
         */
        $('#roleIdInput').on('change', function(e) {
            $('#roleIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg);
        });
        
        /*
         * roleId 중복체크
         */
        $('#idDuplicateBtn').on('click', function(e){
            
            // Validation ID Check
            if (!$('#roleIdInput').validate()) {
                $('#roleIdInput').validator();
                return;
            }
            
            $('#roleIdCheckMsg').show();
            
            var roleData = $('#roleForm').getData();
            if (!SKIAF.util.checkId(roleData.roleId)) {
                $('#roleIdCheckMsg').text(idValidationMsg);
                return;
            }
            
            // 권한 타입별 Validation 체크
            var idPrefix = roleData.roleId.substr(0, 2);
            if (roleData.roleType == '') {
                $('#roleIdCheckMsg').text(SKIAF.i18n.messages['bcm.role.valid.type']);
                return;
            } else if (
                (roleData.roleType == 'MENU'    && idPrefix != 'AM') || 
                (roleData.roleType == 'PROGRAM' && idPrefix != 'AP') || 
                (roleData.roleType == 'ELEMENT' && idPrefix != 'AE') || 
                (roleData.roleType == 'BIZ'     && idPrefix != 'AB') ) {

                $('#roleIdCheckMsg').text(SKIAF.i18n.messages['bcm.role.valid.type-rule']);
                return;
            }

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ROLES_DUPLICATE, roleData.roleId), {
                method : 'GET',
                success : function(res) {
                    if (res.data === true) {
                        $('#roleIdCheckMsg')
                        .removeClass('Color-confirm')
                        .addClass('Color-danger')
                        .text(idDuplicationMsg);
                        
                    } else {
                        $('#roleIdCheckMsg')
                        .removeClass('Color-danger')
                        .addClass('Color-confirm')    
                        .text(idSuccessMsg);
                        
                    }
                    
                }
            });
            
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

});
