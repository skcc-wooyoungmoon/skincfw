/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.18 - in01866
 * description : 프로그램 도움말 공통 처리
 */
"use strict";
var CommonProgramModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        CommonProgramModule.addProgramHelp();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 프로그램 도움말 처리
     */
    this.addProgramHelp = function() {
        var programAttachId = SKIAF.program.fileId;
        if (!programAttachId) {
            return;
        }
        var attachUrl = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, programAttachId);
        var $help = $('<span>', {
            'class' : 'skiaf-help',
            'html' : $('<a>', {href : attachUrl, text : SKIAF.i18n.messages['bcm.common.manual']})
        });
        $('.skiaf-wrap-content h1').eq(0).after($help);
        $('.skiaf-wrap-content .skiaf-help').show();
    };
    
    this.addProgramPopupHelp = function($target) {
        if (typeof $target === 'undefined') {
            return;
        }
        var programAttachId = SKIAF.programPopup.fileId;
        if (!programAttachId) {
            return;
        }
        var attachUrl = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, programAttachId);
        var $help = $('<span>', {
            'class' : 'skiaf-dialog-help',
            'html' : $('<a>', {href : attachUrl, text : SKIAF.i18n.messages['bcm.common.manual']})
        });
        $target.find('.dialog_header').eq(0).after($help);
        $target.find('.skiaf-dialog-help').show();
    };


});
