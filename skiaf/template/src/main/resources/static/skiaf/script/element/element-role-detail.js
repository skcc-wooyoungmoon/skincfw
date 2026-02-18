/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.20 - in01871
 * description : 프로그램 요소 권한 관리
 */
"use strict";
var ElementRoleModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var programId = SKIAF.util.getPathVariable(4);
    
    var useYnTxt = null;
    
    var elementSeq = null;
    
    var isElementDetailReady = false;
    
    // 중복 아이디 체크
    var idCheck = null;
     
    var elementRoleGridColumns = [
        {title: SKIAF.i18n.messages['bcm.common.select'],          key: 'check',            align: 'center',        width : 50,            selectorColumn : true,        headerStyleclass:'set',    resizing: false},
        {title: SKIAF.i18n.messages['bcm.element.element-key'],    key: 'elementKey',       align: 'left',          width : 150},
        {title: SKIAF.i18n.messages['bcm.element.element-desc'],   key: 'elementDesc',      align: 'left',          width : 200},
        {title: SKIAF.i18n.messages['bcm.element.role-id'],        key: 'roleId',           align: 'center',        width : 150},
        {title: SKIAF.i18n.messages['bcm.element.role-name'],      key: 'roleName',         align: 'left',          width : 150},
        {title: SKIAF.i18n.messages['bcm.element.role.evmode'],    key: 'evMode',           align: 'center',        width : 190,
            render : function (value, data) {
                if (value === SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE) {
                    
                    return SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
                } else if (value === SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE) {
                    
                    return SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE;
                }
                return value;
            },
            editable : {
                type : 'radio',
                rule : [{value : SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE, text : SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE}, {value : SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE, text : SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE}]
                    
            },
            value : function (value, data) {
                if (value === SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE) {
                    return SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
                } else if (value === SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE) {
                    return SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE;
                }
                return value;
            }
        },
        {title: SKIAF.i18n.messages['bcm.common.update-by'],              key: 'updateBy',        align: 'center',        width: 85},
        {title: SKIAF.i18n.messages['bcm.common.update-date'],            key: 'updateDate',      align: 'center',        width: 150}
    ];
        
    var elementRoleGridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        headerRowHeight: 30,
        columnFixUpto: 1,
        height:344,
        cellInlineEdit: true,
        cellInlineEditOption: {
             startEvent: 'click',
             
        },
        instantInlineEditType: ['radio'],
        rowSelectOption : {clickSelect : true},
        message: {
            nodata: SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping: {
            resizing: true,
            tooltip: true
        },
        rowOption : {
            styleclass : function(data, rowOption) {
                   if (data._state.added && !data._state.selected) {
                       return 'highlight-add highlight-blur';
                   }
                   if (data._state.added) {
                       return 'highlight-add';
                   } else if(data._state.edited) {
                       data._state.selected = true;
                       data.status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE;
                       ElementRoleModule.btnCheck();
                       return 'highlight-edit';
                   }
                   return '';
               }
        },
        readonlyRender: false,
        columnMapping: elementRoleGridColumns,
        data: []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
                
        ElementRoleModule.search(programId);
        
        $('#elementRoleGrid').alopexGrid(elementRoleGridObj);
        
        ElementRoleModule.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        $('#inputProgramSearch').val(programId);
        
        $('#inputProgramSearch').focus();
        
        // 프로그램 검색 버튼
        $('#btnProgramSearch').on('click', function() {
            ElementRoleModule.programSearchPopup();
        });
        
        // 권한 추가 버튼
        $('#btnRoleAdd').on('click', function() {
            ElementRoleModule.roleAddPopup();
        });

        // 저장 버튼
        $('#btnElementRoleSave').on('click', function() {
            ElementRoleModule.save();
        });

        // 삭제 버튼
        $('#btnElementRoleDelete').on('click', function() {
            ElementRoleModule.deleteElementRole();
        });

        // 이전 버튼
        $('#btnElementRolePrevious').on('click', function() {
            
            var previousUrl = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.ELEMENT_PREVIOUS_URL);
            
            if(previousUrl) {
                window.location.href = previousUrl;
            } else {
                ElementRoleModule.goList();
            }
            
        });

        // 검색 입력영역 엔터 이벤트
        $('#inputProgramSearch').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            
            ElementRoleModule.programSearchPopup();
        });
        
        // 적용 모드 변경 처리
        $('#elementRoleGrid').on('click', '.bodycell', function(e) {
           var evObj = AlopexGrid.parseEvent(e);
           var index = evObj.data._index;
           var key = evObj.mapping.key;
           
           var changeData = {};
           changeData._state = {};
           
           if(key == 'evMode'){
               changeData.evMode = e.target.value === SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE ? SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE : SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
               changeData._state.edited = true;
               
               $('#elementRoleGrid').alopexGrid('dataEdit', changeData, {_index : index});
               
           }
        });
        
        // 사용자 처리(added, edited)
        $('#elementRoleGrid').on('dataSelectEnd', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            var added = evObj.datalist[0]._state.added;
            var selected = evObj.datalist[0]._state.selected;
            var edited = evObj.datalist[0]._state.edited;
            var row = evObj.datalist[0]._index.row;
            
            if(added){
                
                if(selected) {
                    $('#elementRoleGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-blur').addClass('highlight-add');
                } else {
                    $('#elementRoleGrid').alopexGrid('rowElementGet',{_index : {row : row}}).addClass('highlight-blur').addClass('highlight-add');
                }
            }
            
            if(edited) {
                evObj.datalist[0].status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE;
                
            }
            
            if (!selected) {
                
                var indexInfo = {
                        _index: evObj.datalist[0]._index
                }
                
                $(e.currentTarget).alopexGrid('dataRestore', indexInfo);
                
            }

            ElementRoleModule.btnCheck();

        });

        // 그리드 값 변경에 따른 상태값 변경
        $('#elementRoleGrid').on('dataChanged', function(e) {
            var dataList = AlopexGrid.parseEvent(e).datalist;
            var evObj = AlopexGrid.parseEvent(e);
            var selected = null;
            var state = null;
            var indexInfo = null;
            var changeData = {};
                        
            if(evObj.type != 'empty') {
            
                selected = evObj.datalist[0]._state.selected;
                state = dataList[0]._state;
                indexInfo = {
                        _index : dataList[0]._index
                };
            }
                        
            if(evObj.type == 'edit') {
                var isDiff = true;
                
                if(state == null){
                    return;
                }
              
                if(dataList[0].evMode == dataList[0].oriEvMode) {
                    isDiff = false;
                }
                  
                if(isDiff && !state.selected) {
                    changeData = {
                            _state : {
                                selected : true
                            }
                    };
                      
                    $(e.currentTarget).alopexGrid('dataEdit', changeData, indexInfo);
                      
                } else if (!isDiff && (state.selected || state.edited)) {
                      
                    changeData = {
                            _state : {
                                selected : false,
                                edited : false
                            }
                    };
                    
                    dataList[0].status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL;
                    
                    $(e.currentTarget).alopexGrid('dataEdit', changeData, indexInfo);
                }
            }
            
            ElementRoleModule.btnCheck();
            
        });
        
    };
        
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.goSearch = function (programId) {
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ELEMENT_ROLE, programId);
        return;
    };
    
    this.goList = function () {
        window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_ELEMENT;
        return;
    };

    /**
     * 저장, 삭제 버튼 활성화/비활성화 처리
     */
    this.btnCheck = function () {
        
        var selectGridDataList = $('#elementRoleGrid').alopexGrid('dataGet', {_state : {selected: true}});
        
        var btnCheck = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_NEITHER;
        
        var selectGridStatus = [];
 
        for(var i = 0; i < selectGridDataList.length; i++){
            if($.inArray(selectGridDataList[i].status, selectGridStatus == -1)){
                selectGridStatus.push(selectGridDataList[i].status);
            }
        }
        
        if(selectGridStatus.length != 0){
            
            if($.inArray(SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE, selectGridStatus) == -1){
                btnCheck = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL;
                
            } else if ($.inArray(SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE, selectGridStatus) != -1){
                btnCheck = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE;
                
            }
        } else {
            btnCheck = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_NEITHER;
            
        }

        if(btnCheck == SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_NEITHER){
            $('#btnElementRoleDelete').setEnabled(false);
            $('#btnElementRoleSave').setEnabled(false);
            
        } else if (btnCheck == SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL) {
            $('#btnElementRoleDelete').setEnabled(true);
            $('#btnElementRoleSave').setEnabled(false);
            
        } else if (btnCheck == SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE) {
            $('#btnElementRoleDelete').setEnabled(false);
            $('#btnElementRoleSave').setEnabled(true);
            
        }
    };

    /**
     * 요소 검색
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

                    var elementList = res.data.elementList;
                    var elementRoleMapList = res.data.elementRoleMapList;
                    var elementRoleList = [];
                    
                    for(var i = 0;i < elementRoleMapList.length; i++){

                        if(elementRoleMapList[i].enableYn == true) {
                            elementRoleMapList[i].status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL;
                            elementRoleMapList[i].evMode = SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE;
                            elementRoleMapList[i].oriEvMode = SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE;
                            
                        } else {
                            elementRoleMapList[i].status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL;
                            elementRoleMapList[i].evMode = SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
                            elementRoleMapList[i].oriEvMode = SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
                            
                        }
                    }
                    
                    $('#elementRoleGrid').alopexGrid('dataSet', elementRoleMapList);
                    $('#elementRoleGrid').alopexGrid('dataSort', 'updateDate', 'desc');
                }
            });
        }
    };
    
    /**
     * 프로그램 검색 팝업
     */
    this.programSearchPopup = function() {
            
        var params = {};
            
        programId = $('#inputProgramSearch').val();
            
        params.keyword = programId;
        
        $a.popup({
            url : SKIAF.PATH.VIEW_PROGRAMS_SELECT,
            data : params,
            movable : true,
            iframe : false,
            width : 1000,
            height : 600,
            title : SKIAF.i18n.messages['bcm.element.program-select'],
            center : true,
            callback : function(data) {
                
                $('#inputProgramSearch').val(data.programId);
                
                ElementRoleModule.goSearch(data.programId);
            }
        });
    };
    
    /**
     * 권한 추가 팝업
     */
    this.roleAddPopup = function() {
        
        var gridData = $('#elementRoleGrid').alopexGrid('dataGet');
        
        var gridElementSeq = [];
        
        for (var i = 0; i < gridData.length; i++) {
            
            if($.inArray(gridData[i].elementSeq, gridElementSeq) == -1){
                gridElementSeq.push(gridData[i].elementSeq);
            }
        }
        
        var param = {};
        
        param.programId = programId;
                
        $a.popup({
            url : SKIAF.PATH.VIEW_ELEMENT_ROLE_POPUP,
            data : param,
            movable : true,
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            title : SKIAF.i18n.messages['bcm.element.role.add'],
            callback : function(result) {

                var elementRoleList = result.data;
                
                var cnt = 0;

                for(var i = 0; i < elementRoleList.length; i++) {

                    var gridRoleId = [];
                    
                    elementRoleList[i].status = SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE;
                    elementRoleList[i].evMode = SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_VISIBLE;
                    elementRoleList[i].updateBy = '';
                    elementRoleList[i].updateDate = '';
                    
                    if($.inArray(elementRoleList[i].elementSeq, gridElementSeq) != -1){
                        
                        for(var j = 0; j < gridData.length; j++){
                            
                            if(elementRoleList[i].elementSeq == gridData[j].elementSeq){

                                if($.inArray(elementRoleList[i].roleId, gridRoleId) == -1){
                                    gridRoleId.push(gridData[j].roleId);
                                
                                }
                            }
                        }
                    }
                    
                    if($.inArray(elementRoleList[i].roleId, gridRoleId) == -1){
                        $('#elementRoleGrid').alopexGrid('dataAdd', $.extend({_state : {selected : true}}, elementRoleList[i]));
                        cnt = cnt+1;
                    }
                }
                
                if(cnt == 0){
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.valid.no-add']);
                    return;
                }
                
                ElementRoleModule.btnCheck();
            }
        });
    };
    
    /**
     * 저장
     */
    this.save = function() {
        
        var selectedData = $('#elementRoleGrid').alopexGrid('dataGet', {_state : {selected: true}});
        
        var saveData = [];
        
        for(var i = 0, iLen = selectedData.length; i < iLen; i++){
            if (selectedData[i].status == SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_SAVE){
                saveData.push(selectedData[i]);
            }
        }
        

        if (saveData.length != 0) {
            for (var j = 0, jLen = saveData.length; j < jLen; j++) {
                if (saveData[j].evMode == SKIAF.JS_CONSTANT.ELEMENT_ROLE_EVMODE_ENABLE) {
                    saveData[j].visibleYn = false;
                    saveData[j].enableYn = true;
                } else {
                    saveData[j].visibleYn = true;
                    saveData[j].enableYn = false;
                }
            }

            $a.request(SKIAF.util.urlWithParams(
                    SKIAF.PATH.ELEMENT_ROLE_MAPS, programId), {
                method : 'POST',
                array : saveData,
                contentType : 'application/json',
                success : function(result) {

                    ElementRoleModule.search(programId);

                }
            });

        } else {
            SKIAF.popup
                    .alert(SKIAF.i18n.messages['bcm.common.select-option']);
            return;

        }
    };
    
    /**
     * 요소 권한 삭제
     */
    this.deleteElementRole = function () {
        
        SKIAF.popup.confirm((SKIAF.i18n.messages['bcm.common.delete-confirm']), function callback(){
            
            var selectedData = $('#elementRoleGrid').alopexGrid('dataGet', {status : SKIAF.JS_CONSTANT.ELEMENT_ROLE_BTN_CHECK_DEL, _state : {selected: true}});
            
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT_ROLE_MAPS, programId), {
                method : 'DELETE',
                array : selectedData,
                contentType : 'application/json',
                success : function(result) {
                    ElementRoleModule.search(programId);
                    
                }
            });
        });
    };

});