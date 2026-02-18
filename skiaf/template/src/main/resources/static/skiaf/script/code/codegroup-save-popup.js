/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 코드 등록
 */
"use strict";
var CodeGroupSaveModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var isEditMode = false;
    
    var codeGroupIdLabel = SKIAF.i18n.messages['bcm.code.codegroup.id'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], codeGroupIdLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], codeGroupIdLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], codeGroupIdLabel);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init 
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
        SKIAF.console.info('param', param);

        // 입력 또는 편집 모드 판단
        isEditMode = param['editMode'];
        if (isEditMode) {
            $('#codeGroupIdInput').setEnabled(false);
            $('#codeGroupIdCheck').setEnabled(false);
        } else {
            $('#codeGroupIdInput').setEnabled(true);
        }
        
        if (param['codeGroup']) {
            param.codeGroup.useYn = param.codeGroup.useYn ? 'true' : 'false';
            $('#codeGroupTable').setData(param['codeGroup']);
        }
        
        
        // 이벤트 설정
        CodeGroupSaveModule.addEvent();
        
        // 코드 아이디 포커스 이동
        $('#codeGroupIdInput').focus();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        $('#codeGroupIdInput').on('keyup', function(e) {
            $('#codeGroupIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg).show();
        });
        
        // 아이디 입력 변경 이벤트
        $('#codeGroupIdInput').on('change', function(e) {
            $('#codeGroupIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg);
        });
        
        // 중복 체크 Event
        $('#codeGroupIdCheck').on('click', function(e) {
            
            // Validation ID Check
            if (!$('#codeGroupIdInput').validate()) {
                $('#codeGroupIdInput').validator();
                return;
            }
            
            var codeGroupId = $('#codeGroupIdInput').val();
            
            // ajax 통신
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODE_GROUPS_DETAIL, SKIAF.util.encodeUrlAndBase64(codeGroupId)), {
                method : 'GET',
                success : function(result) {
                    SKIAF.console.info(result);

                    if (result.data && result.data.codeGroupId) {
                        $('#codeGroupIdCheckMsg')
                            .removeClass('Color-confirm')
                            .addClass('Color-danger')
                            .text(idDuplicationMsg).show();
                        
                    } else {
                        $('#codeGroupIdCheckMsg')
                            .removeClass('Color-danger')
                            .addClass('Color-confirm')
                            .text(idSuccessMsg).show();
                    }
                    
                }
            });
        });

        // 저장
        $('#codeGroupSaveBtn').on('click', function(e) {
            var data = $('#codeGroupTable').getData();
            
            // Validation Check
            if (!$('#codeGroupTable').validate()) {
                $('#codeGroupTable').validator();
                return;
            }
            
            // id check
            var isIdConfirm = $('#codeGroupIdCheckMsg').hasClass('Color-confirm');
            if (!isEditMode && !isIdConfirm) {
                $('#codeGroupIdCheckMsg').show();
                return;
            }
            
            // 저장 및 수정을 위한 데이터
            var params = {
                codeGroupId : data.codeGroupId,
                codeGroupDesc: data.codeGroupDesc,
                useYn: data.useYn
            };
            SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
                if (data['codeGroupName' + (index + 1)]) {
                    params['codeGroupName' + (index + 1)] = data['codeGroupName' + (index + 1)];                     
                }
            });
            
            var url = (
                    isEditMode
                    ? SKIAF.util.urlWithParams(SKIAF.PATH.CODE_GROUPS_DETAIL, SKIAF.util.encodeUrlAndBase64(data.codeGroupId))
                    : SKIAF.PATH.CODE_GROUPS 
            );
            
            // ajax 통신
            $a.request(url, {
                method : isEditMode ? 'PUT' : 'POST',
                data : params,
                success : function(result) {
                    SKIAF.console.info(result);
                    
                    if (!result.data) {
                        return;
                    }
                    
                    $a.close(true);
                }
            });
        });
        
        // 취소
        $('#codeGroupCancleBtn').on('click', function(e) {
            SKIAF.console.info('close');
            $a.close(false);
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

});
