/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.19 - in01871
 * description : 게시판 수정 팝업
 */
"use strict";
var BoardUpdatePopupModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var boardId = null;
        
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
        
        boardId = param.boardId;
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.BOARDS_DETAIL, boardId), {
            method : 'GET',
            data : param,
            success : function(result){
                if (!result.data) {
                    return;
                }
                
                var data = result.data;
                
                BoardUpdatePopupModule.setData(data);
                
            }
        });

        BoardUpdatePopupModule.addEvent();
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /**
         * 취소 버튼
         */
        $('#boardCancelBtn').on('click', function() {
            
            BoardUpdatePopupModule.cancel();
            
        });
                
        /**
         * 등록 버튼
         */
        $('#boardSaveBtn').on('click', function() {
            
            BoardUpdatePopupModule.save();
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 저장
    this.save = function() {
        
        var updateData = $('#updateBoardTable').getData();
                        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.BOARDS_DETAIL, boardId), {
            method : 'PUT',
            data : updateData,
            success : function(result){
                $a.close(result);
            }
        });
        
    };
    
    // 취소
    this.cancel = function() {
        $a.close();
    };
    
    // 값 매칭
    this.setData = function(data) {
        
        if(data.commentUseYn) {
            data.commentUseYn = 'true';
        } else {
            data.commentUseYn = 'false';
        }
        
        if(data.emergencyUseYn) {
            data.emergencyUseYn = 'true';
        } else {
            data.emergencyUseYn = 'false';
        }
        
        if(data.termUseYn) {
            data.termUseYn = 'true';
        } else {
            data.termUseYn = 'false';
        }
        
        if(data.useYn) {
            data.useYn = 'true';
        } else {
            data.useYn = 'false';
        }
                
        $('#updateBoardTable').setData(data);
        
    };

});