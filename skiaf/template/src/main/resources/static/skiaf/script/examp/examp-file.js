/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * file test
 * 
 * History
 * - 2018.08.01 | in01866 | 최초작성.
 */

var FileTestModule = $a.page(function() {


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var targetId = 'skiaf_test';
    var targetType = SKIAF.ENUM.FILE_TYPE.COMMON;

    // 파일 첨부 모듈
    var attachFileModule = new AttachFileModule();

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {
        
        FileTestModule.addEvent();

        attachFileModule.setFileUpload($('#fileuploader'), targetId, targetType);
        

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 저장 버튼 클릭
        $('#saveBtn').on('click', function(e) {
            attachFileModule.saveFile(targetId, targetType, function(result) {
                SKIAF.console.info('result', result);
                location.reload();
            });
        });
        
        // 전제 삭제 버튼 클릭
        $('#deleteAllBtn').on('click', function(e) {
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'], function() {
                attachFileModule.deleteFileAll(targetId, targetType, function(result) {
                    SKIAF.console.info('result', result);
                    location.reload();
                });
            });
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


});