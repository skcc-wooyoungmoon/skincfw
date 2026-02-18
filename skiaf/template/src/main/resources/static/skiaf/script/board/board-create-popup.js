/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.19 - in01871
 * description : 게시판 저장 팝업
 */
"use strict";
var BoardCreatePopupModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var idCheck = null;
    
    var boardIdLabel = SKIAF.i18n.messages['bcm.board.id'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], boardIdLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], boardIdLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], boardIdLabel);
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {

        BoardCreatePopupModule.addEvent();
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /**
         * input변경시 처리
         */
        $('#inputBoardId').on('keyup', function() {
            idCheck = false;
            
            $('#boardIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg).show();
        });
        
        /**
         * 취소 버튼
         */
        $('#boardCancelBtn').on('click', function() {
            
            BoardCreatePopupModule.cancel();
            
        });
                
        /**
         * 등록 버튼
         */
        $('#boardSaveBtn').on('click', function() {
            
            if (!$('#inputBoardId').validate()) {
                $('#inputBoardId').validator();
                return;
            }
            
            if (idCheck == null || idCheck == false) {
//                SKIAF.popup.alert(idCheckMsg);
                return;
                
            } else {
                BoardCreatePopupModule.save();
                
            }
            
        });
        
        /**
         * 중복체크 버튼
         */
        $('#btnDuplicateCheck').on('click', function(){

            if (!$('#inputBoardId').validate()) {
                $('#inputBoardId').validator();
                return;
            }

            BoardCreatePopupModule.duplicateCheck(); 
        });

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    // 저장
    this.save = function() {
        
        var createData = $('#createBoardTable').getData();
        
        $a.request(SKIAF.PATH.BOARDS, {
            method : 'POST',
            data : createData,
            success : function(result){
                $a.close(result);
            }
        });
        
    };
    
    // 취소
    this.cancel = function() {
        
        $a.close();
    };
    
    // 중복체크
    this.duplicateCheck = function() {
        var boardId = $('#createBoardTable').getData().boardId;
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.BOARDS_DUPLICATE, boardId),{
           method : 'GET',
           success : function (result){
               if(result.data == true) {
                   idCheck = false;
                   
                   $('#boardIdCheckMsg')
                       .addClass('Color-danger')
                       .removeClass('Color-confirm')
                       .text(idDuplicationMsg).show();
                   
               } else {
                   idCheck = true;
                   
                   $('#boardIdCheckMsg')
                       .addClass('Color-confirm')
                       .removeClass('Color-danger')
                       .text(idSuccessMsg).show();
               }
           }
        });
    };

});