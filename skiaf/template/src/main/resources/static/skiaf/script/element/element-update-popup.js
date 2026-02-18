/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01871
 * description : 프로그램 요소 수정 팝업
 */
"use strict";
var ElementUpdateModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var programId = null;

    var useYnTxt = null;

    var elementSeq = null;

    var isElementDetailReady = false;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
 
    if (typeof ElementDetailModule === 'undefined') {
        $.getScript(SKIAF.contextPath
                + '/static/skiaf/script/element/element-detail.js', function() {
            isElementDetailReady = true;
        });

    } else {
        isElementDetailReady = true;

    }

    this.init = function(id, param) {
        elementSeq = param.elementSeq;

        ElementUpdateModule.dataSet(param);
        ElementUpdateModule.addEvent();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 요소 수정 버튼
        $('#elementUpdateBtn').on('click', function(e) {
            ElementUpdateModule.update(elementSeq);
        });

        // 취소 버튼
        $('#elementUpdateCancelBtn').on('click', function(e) {
            ElementUpdateModule.cancel();
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 요소 수정
     */
    this.update = function(elementSeq) {

        var updateData = $('#updateElementTable').getData();

        var programId = updateData.programId;

        if (updateData.useYn == 'Y') {

            updateData.useYn = true;
        } else {

            updateData.useYn = false;
        }
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT_DETAIL, elementSeq, programId),{
            method : 'PUT',
            data : updateData,
            success : function(result){
                ElementDetailModule.search();
                $a.close();
            }
        });
    };

    /**
     * 취소
     */
    this.cancel = function() {
        $a.close();
    };

    /**
     * 데이터 세팅
     */
    this.dataSet = function(param) {
        if (param.gridData.useYn == true) {
            param.gridData.useYn = 'Y';
        } else {
            param.gridData.useYn = 'N';
        }

        $('#updateProgramId').setData({
            programId : param.tableData.programId
        });
        $('#updateProgramName').setData({
            programName : param.tableData.programName
        });
        $('#updateElementKey').setData({
            elementKey : param.gridData.elementKey
        });
        $('#updateElementDesc').setData({
            elementDesc : param.gridData.elementDesc
        });
        $('.Radio').setData({
            useYn : param.gridData.useYn
        });
    };

});