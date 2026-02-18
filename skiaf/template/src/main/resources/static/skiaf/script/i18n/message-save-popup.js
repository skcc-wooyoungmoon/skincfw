/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.17 - in01866
 * description : 메시지 저장 팝업
 */
"use strict";
var MessageCreateModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var isEditMode = false;
    
    // 탭 정보
    var targetEnum = Object.freeze({
        program: 'PROGRAM',
        common: 'COMMON',
        system: 'SYSTEM'
    });
    
    var messageKeyLabel = SKIAF.i18n.messages['bcm.message.key'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], messageKeyLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], messageKeyLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], messageKeyLabel);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
        SKIAF.console.info('param', param);
        
        $('.dialog_header').parent().attr('title', "");

        // 입력 또는 편집 모드 판단
        isEditMode = param.editMode;
        if (isEditMode) {
            $('#messageKeyInput').setEnabled(false);
            $('#messageKeyCheck').setEnabled(false);

        } else {
            $('#messageKeyInput').setEnabled(true);
        
        }
        
        if (param.message) {
            
            // 사용여부 String 데이터로 변환
            param.message.useYn = param.message.useYn ? 'true' : 'false';
            
            // 타겟이 프로그램인 경우 프로그램 선택처리
            if (param.message.target) {
                var isProgram = (
                    param.message.target != targetEnum.common &&
                    param.message.target != targetEnum.system
                );
                if (isProgram) {
                    $('#messageProgramArea').show();
                    param.message.programId = param.message.target;
                    param.message.target = targetEnum.program;
                }
            }
            $('#messageTable').setData(param['message']);
        }
        
        
        // 이벤트 설정
        MessageCreateModule.addEvent();

        // 메시지 키 입력으로 포커스 이동 
        $('#messageKeyInput').focus();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 키 입력 변경 이벤트
        $('#messageKeyInput').on('keyup', function(e) {
            $('#messageKeyCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg).show();
        });
        
        // 중복 체크 Event
        $('#messageKeyCheck').on('click', function(e) {
            
            // Validation ID Check
            if (!$('#messageKeyInput').validate()) {
                $('#messageKeyInput').validator();
                return;
            }
            
            var messageKey = $('#messageKeyInput').val();
            
            // ajax 통신
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MESSAGES_DETAIL, SKIAF.util.encodeUrlAndBase64(messageKey)), {
                method : 'GET',
                success : function(result) {

                    if (result.data && result.data.messageKey) {
                        $('#messageKeyCheckMsg')
                            .removeClass('Color-confirm')
                            .addClass('Color-danger')
                            .text(idDuplicationMsg).show();
                        
                    } else {
                        $('#messageKeyCheckMsg')
                            .removeClass('Color-danger')
                            .addClass('Color-confirm')
                            .text(idSuccessMsg).show();
                    }
                    
                }
            });
        });

        // 저장
        $('#messageSaveBtn').on('click', function(e) {
            var data = $('#messageTable').getData();
            
            var isProgramTarget = (data.target == targetEnum.program);
            var $validation;
            
            // Validation 범위 지정
            if (isProgramTarget) {
                $validation = $('#messageTable');
            } else {
                $validation = $('#messageKeyInput, #messageNameRequiredInput');
            }
            
            // Validation Check
            if (!$validation.validate()) {
                $('#messageTable').validator();
                return;
            }
            
            // Id check
            var isIdConfirm = $('#messageKeyCheckMsg').hasClass('Color-confirm');
            if (!isEditMode && !isIdConfirm) {
                $('#messageKeyCheckMsg').show();
                return;
            }
            
            // 저장 및 수정을 위한 데이터
            var params = {
                messageKey : data.messageKey,
                messageDesc: data.messageDesc,
                target: (isProgramTarget ? data.programId : data.target),
                useYn: data.useYn
            };
            SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
                if (data['messageName' + (index + 1)]) {
                    params['messageName' + (index + 1)] = data['messageName' + (index + 1)];                     
                }
            });
            
            var url = (
                    isEditMode
                    ? SKIAF.util.urlWithParams(SKIAF.PATH.MESSAGES_DETAIL, SKIAF.util.encodeUrlAndBase64(data.messageKey))
                    : SKIAF.PATH.MESSAGES 
            );
            
            // ajax 통신
            $a.request(url, {
                method : isEditMode ? 'PUT' : 'POST',
                data : params,
                success : function(result) {
                    
                    if (!result.data) {
                        return;
                    }

                    $a.close(true);
                }
            });
        });
        
        // 취소
        $('#messageCanclesBtn').on('click', function(e) {
            SKIAF.console.info('close');
            $a.close(false);
        });
        
        // 프로그램 표시
        $('#messageTargetArea input[type="radio"]').on('change', function(e) {
            var $target = $(e.currentTarget);
            var targetValue = $target.val();
            
            if (targetValue == targetEnum.program) {
                $('#messageProgramArea').show();
            } else {
                $('#messageProgramArea').hide();
            }

        });
        
        // 프로그램 팝업 처리
        $('#messageProgramPopupBtn').on('click', function(e) {

            $a.popup({
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_SELECT,
                movable: true,
                iframe : false,
                width : 1000,
                height : 600,
                center : true,
                title : '프로그램 검색',
                data : {
                    keyword: ''
                },
                callback : function(data) {
                    if (data == null) {
                        return;
                    }
                    var inputData = $('#messageTable').getData();
                    inputData.programId = data.programId;
                    $('#messageTable').setData(inputData);
                    $('#messageProgramInput').trigger('click');
                }
            });
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


});
