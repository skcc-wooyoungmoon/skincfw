/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.22 - in01871
 * description : 프로그램 요소 등록 팝업
 */
"use strict";
var ElementCreateModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var programId = null;
    
    var useYnTxt = null;
    
    var elementSeq = null;
    
    // 중복 아이디 체크
    var idCheck = null;
    
    var elementKeyLabel = SKIAF.i18n.messages['bcm.element.element-key'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], elementKeyLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], elementKeyLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], elementKeyLabel);
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
 
    this.init = function(id, param) {
        
        ElementCreateModule.dataSet(param);
        ElementCreateModule.addEvent();
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
     // input변경시 처리
        $('#elementKeyInput').on('keyup', function() {
            idCheck = false;
            $('#elementKeyCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg).show();

        });
        
        $('#elementSaveBtn').on('click', function(e) {
            
            // Validation Check
            if (!$('#createElementTable').validate()) {
                $('#createElementTable').validator();
                return;
            }
            
            if (idCheck == null || idCheck == false) {
//                SKIAF.popup.alert(idCheckMsg);
                return;
                
            } else {
                ElementCreateModule.save();
                
            }
            
        });
        
        $('#elementDuplicateBtn').on('click', function(e) {

            if (!$('#elementKeyInput').validate()) {
                $('#elementKeyInput').validator();
                return;
            }
            
            ElementCreateModule.elementDuplicateCheck();
           
        });
       
        $('#elementCancelBtn').on('click', function(e) {
            ElementCreateModule.cancel();
           
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 프로그램 요소 중복체크
     */
    this.elementDuplicateCheck = function() {
        
        var programId = $('#createElementTable').getData().programId;
        var elementKey = $('#createElementTable').getData().elementKey;
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT_DUPLICATE, programId, encodeURIComponent(elementKey)), {
            method : 'GET',
            success : function (result) {
                
                if(result.data == true){

                    idCheck = false;

                    $('#elementKeyCheckMsg')
                        .addClass('Color-danger')
                        .removeClass('Color-confirm')
                        .text(idDuplicationMsg).show();

                } else {
                    
                    idCheck = true;
                    $('#elementKeyCheckMsg')
                        .addClass('Color-confirm')
                        .removeClass('Color-danger')
                        .text(idSuccessMsg).show();
                }
            }
        });
    };
    
    /**
     * 저장
     */
    this.save = function() {
        
        var programId = $('#createElementTable').getData().programId;
        var createData = $('#createElementTable').getData();
        
        if(createData.useYn == 'Y') {
            
            createData.useYn = true;
        } else {
            
            createData.useYn = false;
        }
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT, programId) , {
            method : 'POST',
            data : createData,
            success : function(result) {
                $a.close();
            }
            
        });
    };
    
    /**
     * 취소
     */
    this.cancel = function () {
        
        $a.close();
    };
    
    /**
     * 데이터 가져오기
     */
    this.dataSet = function(param) {
       
        $('#createElementTable').setData({
           programId : param.tableData.programId,
           programName : param.tableData.programName
           
        });
        
    };

});