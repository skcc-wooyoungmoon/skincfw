/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.13 - in01871
 * description : 프로그램 요소 관리
 */
"use strict";
var ElementDetailModule = $a.page(function() {
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
     
    var programId = null;
     
    // 중복 아이디 체크
    var idCheck = null;
     
    var gridColumns1 = [
         {title: SKIAF.i18n.messages['bcm.common.select'],              key: 'check',           align: 'center',        width : 50,         resizing: false,        selectorColumn : true,        headerStyleclass:'set'},
         {title: SKIAF.i18n.messages['bcm.element.element-key'],        key: 'elementKey',      align: 'left',          width : 150,        tooltip: true},
         {title: SKIAF.i18n.messages['bcm.element.element-desc'],       key: 'elementDesc',     align: 'left',          width : 300,        tooltip: true},
         {title: SKIAF.i18n.messages['bcm.element.role-count'],         key: 'roleCount',       align: 'center',        width : 100,        tooltip: false},
         {title: SKIAF.i18n.messages['bcm.common.use-yn'],              key: 'useYn',           align: 'center',        width : 70,         tooltip: false,         render : function(value, data) { return value ? 'Y' : 'N'}},
         {title: SKIAF.i18n.messages['bcm.common.update-by'],           key: 'updateBy',        align: 'center',        width : 85,         tooltip: true},
         {title: SKIAF.i18n.messages['bcm.common.update-date'],         key: 'updateDate',      align: 'center',        width : 150,        tooltip: true},
         {title: SKIAF.i18n.messages['bcm.common.create-by'],           key: 'createBy',        align: 'center',        width : 85,         tooltip: true},
         {title: SKIAF.i18n.messages['bcm.common.create-date'],         key: 'createDate',      align: 'center',        width : 150,        tooltip: true}
     ];
     
     var elementsGridObj = {
             autoColumnIndex: true,
             fitTableWidth: true,
             columnFixUpto: 1,
             height:404,
             headerRowHeight: 30,
             message: {
                 nodata: SKIAF.i18n.messages['bcm.common.nodata']
             },
             rowSelectOption : {
                clickSelect : true,
                singleSelect : true,
                disableSelectByKey : true
             },
             defaultColumnMapping: {
                 resizing: true
             },
             columnMapping: gridColumns1,
             data: []
         };
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
     
    this.init = function(id, param) {
        
        $('#inputProgramSearch').focus();
        
        $('.skiaf-inputbox input').on('keyup', function() {
            idCheck = false;
            $('.skiaf-inputbox').removeClass('success');
            $('.skiaf-inputbox').removeClass('danger');
            
        });
        
        $('#linkGrid1').alopexGrid(elementsGridObj);
        
        ElementDetailModule.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 프로그램 검색 팝업
        $('#programSearchBtn').on('click', function() {
            
            ElementDetailModule.programSearchPopup();
            
        });

        // 프로그램 요소 등록 버튼
        $('#elementCreatePopupBtn').on('click', function() {
            
            var programId = $('#programId').text();
            
            if(programId != '') {
                
                ElementDetailModule.elementCreatePopup();                
                
            } else {
                
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.search-alert']);
                return;
                
            }
            
        });
        
        // 프로그램 요소 수정 버튼
        $('#elementUpdatePopupBtn').on('click', function() {
            
            var dataList = $('#linkGrid1').alopexGrid('dataGet', {_state : {selected : true}});
            
            if (dataList.length == 0) {
                
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.valid.element-select']);
                return;
                
            } else {
                
                var elementSeq = dataList[0].elementSeq;
                var dataObj = dataList[0];
                
                ElementDetailModule.elementUpdatePopup(elementSeq, dataObj);
                
            }
        });
        
        // 권한 관리 버튼
        $('#elementRoleDetailBtn').on('click', function() {
            
            var programId = $('#programId').text();
            
            if(programId != ''){
                
                // URL 기억
                Cookies.set(SKIAF.JS_CONSTANT.ELEMENT_PREVIOUS_URL, location.href);
                
                window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ELEMENT_ROLE, programId);
                return;
            
            } else {
                
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.search-alert']);
                return;
                
            }
        });
           
        // 검색 입력영역 엔터 이벤트
        $('#inputProgramSearch').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            
            ElementDetailModule.programSearchPopup();
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 검색
     */
    this.search = function(programId) {
        
        if(programId == null) {
            programId = $('#inputProgramSearch').val();
        }

        if(programId) {

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT, programId), {

                method : 'GET',
                success : function(res) {
                    
                    $('#programId').setData({programId : res.data.program.programId});
                    $('#programName').setData({programName : res.data.program.programName});
                    
                    if (res.data.program.useYn == true){
                        res.data.program.useYnTxt = 'Y';
                    } else {
                        res.data.program.useYnTxt = 'N';
                    }
                    $('#useYnTxt').setData({useYnTxt : res.data.program.useYnTxt});

                    $('#inputProgramId').setData({programId : res.data.program.programId});
                    $('#inputProgramName').setData({programName : res.data.program.programName});
                                        
                    $('#elementUpdatePopupBtn').setEnabled(true);
                    $('#elementCreatePopupBtn').setEnabled(true);
                    $('#elementRoleDetailBtn').setEnabled(true);
                    
                    for(var i=0; i < res.data.elementList.length; i++){
                        res.data.elementList[i].roleCount = res.data.elementList[i].elementRoleMap.length;
                    }

                    $('#linkGrid1').alopexGrid('dataSet', res.data.elementList);
                }
            });
        }
    };
    
    /**
     * 검색 팝업
     */
    this.programSearchPopup = function() {
        
        var params = {};
        var programType = SKIAF.ENUM.PROGRAM_TYPE.VIEW;
        
        programId = $('#inputProgramSearch').val();
        
        params.keyword = programId;
        params.programType = programType;
        
        $a.popup({
            url : SKIAF.PATH.VIEW_PROGRAMS_SELECT,
            data : params,
            movable: true,
            iframe : false,
            width : 1000,
            height : 600,
            title : SKIAF.i18n.messages['bcm.element.program-search'],
            center : true,
            callback : function(data) {
                
                $('#inputProgramSearch').val(data.programId);
                
                ElementDetailModule.search();
            }
        });
    };

    /**
     * 등록 팝업
     */
    this.elementCreatePopup = function() {
        
        var tableData = $('.skiaf-table').getData();
        
        var params = {};
        
        params.tableData = tableData;
        
        $a.popup({
            url : SKIAF.PATH.VIEW_ELEMENT_CREATE,
            data : params,
            movable: true,
            iframe : false,
            width : 1000,
            height : 370,
            title : SKIAF.i18n.messages['bcm.element.element-create'],
            center : true,
            callback : function(result) {
                                                
                ElementDetailModule.search();
            }
        });
    };
    
    /**
     * 수정 팝업
     */
    this.elementUpdatePopup = function(elementSeq, dataObj) {
        
        var tableData = $('.skiaf-table').getData();
        
        var params = {};
        
        params.elementSeq = elementSeq;
        params.tableData = tableData;
        params.gridData = dataObj;
        
        $a.popup({
            url : SKIAF.PATH.VIEW_ELEMENT_UPDATE,
            data : params,
            movable: true,
            iframe : false,
            width : 1000,
            height : 370,
            title : SKIAF.i18n.messages['bcm.element.element-modify'],
            
            center : true,
            callback : function(result) {
                
                if (data != null && data != undefined) {
                    ElementDetailModule.search();
                    
                }
            }
        });
    };
});