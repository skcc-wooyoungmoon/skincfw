/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 코드 목록
 */
"use strict";
var CodeListModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 페이지 정보
    var pageTitle = '';
    var pageUrl = location.pathname;

    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };
    
    // 탭 정보
    var tabEnum = Object.freeze({
        codeGroup: 'codeGroup',
        code: 'code'
    });
    var tab = tabEnum.codeGroup;

    // 코드 그룹 그리드 컬럼
    var codeGroupGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],       key : 'check',         align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.code.codegroup.id'],   key : 'codeGroupId',   align : 'left',   tooltip : true, width : '115px'}
    ];
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        codeGroupGridColumns.push({title : SKIAF.i18n.messages['bcm.code.codegroup.name'] + ' (' + langCode + ')',       key : 'codeGroupName' + (index + 1), align : 'left', tooltip : true, width : '150px'});        
    });
    codeGroupGridColumns.push(
        {title : SKIAF.i18n.messages['bcm.code.codegroup.desc'], key : 'codeGroupDesc', align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],       key : 'useYn',         align : 'center', tooltip : false, width : '65px', render : function(value, data) { return value ? 'Y' : 'N';}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],    key : 'updateBy',      align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.update-date'],  key : 'updateDate',    align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}
        },
        {title : SKIAF.i18n.messages['bcm.common.create-by'],    key : 'createBy',      align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.create-date'],  key : 'createDate',    align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}
        }
    );

    // 수정 여부 판단을 위한 기준 객체
    var diffObject = {
            codeId : '',
            codeSortSeq : '',
            codeDesc : '',
            useYn : '',
    };

    // 코드 상세 그리드 컬럼 정의
    var codeGridDetailColumns = [
        {title : SKIAF.i18n.messages['bcm.code.code.id'],        key : 'codeId',         align : 'left', tooltip : true, width : '115px'},
        {title : SKIAF.i18n.messages['bcm.common.seq'],          key : 'codeSortSeq',    align : 'center', tooltip : true, width : '70px'}
    ];
    // 언어별 값 컬럼
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        codeGridDetailColumns.push({title : SKIAF.i18n.messages['bcm.code.code.name'] + ' (' + langCode + ')', key : 'codeName' + (index + 1), align : 'left', tooltip : true, width : '150px'});
        diffObject['codeName' + (index + 1)] = '';
    });
    // 언어별 값 컬럼 이후 컬럼들
    codeGridDetailColumns.push(
        {title : SKIAF.i18n.messages['bcm.code.code.desc'],      key : 'codeDesc',       align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],       key : 'useYn',          align : 'center', tooltip : false, width : '65px', render : function(value, data) { return value ? 'Y' : 'N';}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],    key : 'updateBy',       align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.update-date'],  key : 'updateDate',     align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}
        },
        {title : SKIAF.i18n.messages['bcm.common.create-by'],    key : 'createBy',       align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.create-date'],  key : 'createDate',     align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}
        }
    );

    // 코드 수정 그리드 컬럼 정의
    var codeGridEditColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],       key : 'check',          align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.code.code.id'],        key : 'codeId',         align : 'left',   tooltip : true, width: '115px',
            editable : {type : 'text', attr : {maxlength : 30}},
            allowEdit: function(value, data, mapping) {
                return (data._state.added) ? true : false;
            }
        },
        {title : SKIAF.i18n.messages['bcm.common.seq'],          key : 'codeSortSeq',    align : 'center', tooltip : true, width : '70px', editable : {type : 'text', style: {width : '40px', attr : {type : 'number'}}}}
    ];
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        codeGridEditColumns.push(
            {title : SKIAF.i18n.messages['bcm.code.code.name'] + ' (' + langCode + ')', key : 'codeName' + (index + 1), align : 'left', tooltip : true, width : '150px', defaultValue: '', editable : {type : 'text', attr : {maxlength : 128}}}
        );
    });
    codeGridEditColumns.push(
        {title : SKIAF.i18n.messages['bcm.code.code.desc'],      key : 'codeDesc',       align : 'left',   tooltip : true, width : '200px', defaultValue: '', editable : {type : 'text', attr : {maxlength : 2000}}},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],       key : 'useYn',          align : 'center', tooltip : false, width : '120px',
            render : function(value, data) { return value ? 'Y' : 'N'; },
            editable: {type: 'radio', rule: [{value: true, text: 'Y'}, {value: false, text: 'N'}]},
            value: function (value, data) {
                if (typeof value === 'undefined') {
                    return true;
                }
                if (value === 'true') {
                    return true;
                } else if (value === 'false') {
                    return false;
                }
                return value;
            }
        }
    );
    
    // 그리드 설정
    var codeGroupGridObj = {
        numberingColumnFromZero : false,
        columnFixUpto : 1,
        height : 344,/* 전체 height */
        autoColumnIndex : true,
        fitTableWidth : true,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping : {
            resizing : true
        },
//        pager : true,
//        paging : {
//            perPage : paging.size,
//            pagerCount : 10,
//            pagerTotal : true,
//            pagerSelect : true
//        },
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : codeGroupGridColumns,
        data : []
    };
    var codeGridDetailObj = {
        height : 344,
        numberingColumnFromZero : false,
        columnFixUpto : 0,
        autoColumnIndex : true,
        fitTableWidth : true,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping : {
            resizing : true
        },
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : codeGridDetailColumns,
        data : []
    };
    var codeGridEditObj = {
        height : 344,
        columnFixUpto : 1,
        autoColumnIndex : true,
        fitTableWidth : true,
        rowInlineEdit : true,
        rowInlineEditOption : {
            startEvent : 'click'
        },
        endInlineEditByOuterClick : true,
        headerRowHeight : 30,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping : {
            resizing : true
        },
        rowOption : {
            styleclass : function(data, rowOption) {
                if (data._state.added && !data._state.selected) {
                    return 'highlight-add highlight-blur';
                }
                if (data._state.added) {
                    return 'highlight-add';
                } else if (data._state.edited) {
                    return 'highlight-edit';
                }
                return '';
            }
        },
        readonlyRender : false,
        columnMapping : codeGridEditColumns,
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        
        // 파라메터 셋팅
        CodeListModule.setParameter();
        
        // 이벤트 설정
        CodeListModule.addEvent();
        
        // 그리드 초기화
        $('#codeGroupGrid').alopexGrid(codeGroupGridObj);
        $('#codeEditGrid').alopexGrid(codeGridEditObj);
        $('#codeDetailGrid').alopexGrid(codeGridDetailObj);
        
        // 최초 검색
        if (tab == tabEnum.codeGroup) {
            CodeListModule.searchCodeGroup();
        } else {
            CodeListModule.searchCode();
        }
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /*
         * 코드 상세 화면
         */
        
        // 코드 수정 버튼 클릭 이벤트
        $('#codeEditBtn').on('click', function(e) {
            
            var inputData = $('#codeArea').getData();
            
            if(!inputData.codeGroupId){
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.select']);
                return;
            }
            
            CodeListModule.editMode();
        });

        // 코드 검색영역 엔터키 이벤트
        $('#codeArea .skiaf-ui-layersearch input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            $('#codeGroupSearchBtn').trigger('click', 'CodeListModule');
        });

        $('#codeGroupDownload').on('click', function(e) {
            
            var gridData = $('#codeGroupGrid').alopexGrid('dataGet');
            var params = {};
            
            params.fileName = "SKIAF_excel_codeGroup_" + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss');
            params.gridId = '#codeGroupGrid';
            
            if(gridData.length > 0){
                SKIAF.excelUtil.excelExportGrid(params);
                
            } else {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.common.excel-download-empty']);
                
            }
            
        });
        
        $('#codeDownload').on('click', function(e) {
            
            var gridData = $('#codeDetailGrid').alopexGrid('dataGet');
            var inputData = $('#codeArea').getData();
            var codeGroupId = inputData.codeGroupId;
            var params = {};
            
            params.fileName = "SKIAF_excel_" + codeGroupId + "_code_" + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss');
            params.gridId = '#codeDetailGrid';
            
            if(gridData.length > 0){
                SKIAF.excelUtil.excelExportGrid(params);
                
            } else {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.common.excel-download-empty']);
                
            }
            
        });
        
        /*
         * 코드 수정 화면
         */

        // 추가 버튼 클릭 이벤트
        $('#codeAddBtn').on('click', function(e) {
            var data = {};
            
            // 그리드 적용
            var codeEditGridData = $('#codeEditGrid').alopexGrid('dataGet');
            data.codeSortSeq = CodeListModule.generateNextSeq(codeEditGridData);
            
            // Row 상태 설정
            data._state = {};
            data._state.added = true;
            data._state.selected = true;

            $('#codeEditGrid').alopexGrid('dataAdd', data);
        });
        
        
        // 편집 그리드 이벤트 처리
        $('#codeEditGrid').on('dataSelectEnd', function(e) {    /* check 버튼 이벤트 */
            
            // 그리드 편집 모드 종료
            $(e.currentTarget).alopexGrid('endEdit');

            var dataList = AlopexGrid.parseEvent(e).datalist;
            if (!dataList) {
                return;
            }
            
            // 선택 해제시 기존의 데이터로 변경
            dataList.forEach(function(dataObj, index) {
                var state = dataObj._state;
                
                // 선택 된 아이템이면 처리안함
                if (state.added || state.selected) {
                    return;
                }
                var indexInfo = {
                        _index: dataObj._index
                }
                $(e.currentTarget).alopexGrid('dataRestore', indexInfo);
                
            });
            
            // 그리드 새로고침
            $(e.currentTarget).alopexGrid();
            
            CodeListModule.checkCodeSaveBtn();

        }).on('dataChanged', function(e) {    /* 데이터 변경시 선택 처리 */

            var dataList = AlopexGrid.parseEvent(e).datalist;
            var eventType = AlopexGrid.parseEvent(e).type;
            if (!dataList) {
                return;
            }
            if (!eventType) {
                return;
            }
            if (eventType != 'edit') {
                CodeListModule.checkCodeSaveBtn();
                return;
            }
            
            for(var index in dataList){
                var data = dataList[index];
                if (!data.codeId) {
                  SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.id']);
                  return;
                }
                if (!data.codeSortSeq) {
                  SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.seq']);
                  return;
                }
            }
            
            SKIAF.console.info('dataList', dataList);
            dataList.forEach(function(dataObj, index) {
                var state = dataObj._state;
                var chageData = {};
                var indexInfo = {
                        _index: dataObj._index
                }
                
                // 추가된 데이터인지 확인
                if (state.added) {
                    if (!state.selected) {
                        
                        // 데이터 자동 체크 처리
                        chageData = {
                                _state: {
                                    selected: true
                                }
                        };
                        $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                    }
                    
                    return;
                }
                
                var isDiff = SKIAF.util.isDataDiff(dataObj._original, dataObj, diffObject);
                
                // 원본 데이터와 차이가 있는지 확인
                if (isDiff && !state.selected) {
                    
                    // 데이터 자동 체크 처리
                    chageData = {
                            _state: {
                                selected: true
                            }
                    };
                    $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                
                } else if (!isDiff && (state.selected || state.edited)) {

                    // 데이터 자동 체크해제 처리
                    chageData = {
                            _state: {
                                selected: false,
                                edited: false
                            }
                    };
                    
                    $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                }

            });
            
            CodeListModule.checkCodeSaveBtn();
            
        });
        
        // 저장 버튼 클릭
        $('#codeSaveBtn').on('click', function(e) {
            
            // 그리드 편집 모드 종료
            $('#codeEditGrid').alopexGrid('endEdit');
            
            var editData = [];
            var saveData = [];
            
            var editedData = $('#codeEditGrid').alopexGrid('dataGet', { _state : { selected : true } });
            
            var isValid = true;
            for (var index in editedData) {
                var data = editedData[index];
                if (!data._state) {
                    continue;
                }
                if (!data._state.selected) {
                    continue;
                }
                data.codeGroupId = $('#codeEditArea').getData().codeGroupId;
                
                if (!data.codeGroupId) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.setid']);
                    return;
                }
                if (!data.codeId) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.id']);
                    return;
                }
                
                // 순번이 입력 되어 있는지 확인
                if (!data.codeSortSeq) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.seq']);
                    return;
                }
                
                // 정수 체크
                var seq = Number(data.codeSortSeq);
                if ((seq ^ 0) !== seq) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.seq-not-number']);
                    return;
                }
                
                if (data._state.added) {
                    saveData.push(data);                    
                } else {
                    editData.push(data);
                }
            }

            SKIAF.console.info('saveData', saveData);
            SKIAF.console.info('editData', editData);

            CodeListModule.updateCode(saveData, editData);
        });
        
        // 코드 취소 버튼 클릭 이벤트
        $('#codeCancleBtn').on('click', function(e) {
            
            CodeListModule.searchCode();
            $('#codeDetailGrid').alopexGrid();
        });
        
        /*
         * 코드 그룹 목록 화면
         */
        
        // 탭 선택시 이벤트
        $('#codeTab').on('click', 'li', function(e, triggerBy) {
            if (triggerBy == 'CodeListModule') {
                return;
            }
            paging.page = defaultPage;
            paging.size = defaultSize;
            
            var $target = $(e.currentTarget);
            var tabId = $target.data('content');
            
            // 선택된 탭의 정보 조회 및 그리드 새로고침
            if (tabId == '#codeGroupArea') {
                // 선택한 코드 그룹 정보 삭제
                CodeListModule.selectCodeGroupClear();

                CodeListModule.searchCodeGroup(true);
                $('#codeGroupGrid').alopexGrid();
            } else if (tabId == '#codeArea') {
                CodeListModule.searchCode(true);
                $('#codeDetailGrid').alopexGrid();
            }
        });
        
        // 등록 버튼 클릭 이벤트
        $('#codeGroupAddBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.PATH.VIEW_CODE_GROUPS_CREATE,
                data : {
                    codeGroupId : ''
                },
                movable: true,
                iframe : false,
                width : 1000,
                height : 450,
                center : true,
                title : SKIAF.i18n.messages['bcm.code.codegroup.create'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        CodeListModule.searchCodeGroup();
                        $('#codeGroupGrid').alopexGrid();
//                        $('#codeGroupEditBtn').setEnabled(false);
                    }
                }
            });
        });
        
        // 수정 버튼 클릭 이벤트
        $('#codeGroupEditBtn').on('click', function(e) {
            var dataList = $('#codeGroupGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if(dataList == 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.select']);
                return;
            }
            
            var data = {};
            var isEditMode = false; 
            if (dataList && dataList.length == 1) {
                data = dataList[0];
                isEditMode = true;
            }
            
            $a.popup({
                url : SKIAF.PATH.VIEW_CODE_GROUPS_CREATE,
                data : {
                    codeGroup : data,
                    editMode : isEditMode
                },
                movable: true,
                iframe : false,
                width : 1000,
                height : 450,
                center : true,
                title : SKIAF.i18n.messages['bcm.code.codegroup.edit'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        CodeListModule.searchCodeGroup();
                        $('#codeGroupGrid').alopexGrid();                        
//                        $('#codeGroupEditBtn').setEnabled(false);
                    }

                }
            });
        });
        
        // 코드 그룹 선택 팝업 이벤트
        $('#codeGroupSearchBtn').on('click', function(e) {
            
            $a.popup({
                url : SKIAF.PATH.VIEW_CODE_GROUPS_SELECT,
                data : {
                    keyword : $('#codeArea').getData().keyword
                },
                movable: true,
                iframe : false,
                width : 1000,
                height : 630,
                center : true,
                title : SKIAF.i18n.messages['bcm.code.codegroup.search'],
                callback : function(data) {
                    CodeListModule.selectCodeGroup(data);
                    CodeListModule.searchCode(true);
                }
            });
        });
        
//        // 코드 그룹 페이지 사이즈 변경 이벤트
//        $('#codeGroupGrid').on('perPageChange', function(e) {
//
//            var evObj = AlopexGrid.parseEvent(e);
//            
//            paging.size = evObj.perPage;
//            
//            paging.page = 1;
//            CodeListModule.searchCodeGroup(true);
//        });
//        
//        // 코드 그룹 페이징 변경 이벤트
//        $('#codeGroupGrid').on('pageSet', function(e) {
//            var evObj = AlopexGrid.parseEvent(e);
//
//            paging.page = evObj.page;
//            paging.size = evObj.pageinfo.perPage;
//
//            CodeListModule.searchCodeGroup(true);
//        });
        
        // 코드 그룹 검색 버튼 클릭
        $('#codeGroupArea .skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            CodeListModule.searchCodeGroup(true);
        });

        // 코드 그룹 검색영역 엔터키 이벤트
        $('#codeGroupArea .skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            CodeListModule.searchCodeGroup(true);
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            SKIAF.console.info('state', e.state);
            CodeListModule.setParameter();
            
            // 브라우저 뒤로가기 후 최초 검색
            if (tab == tabEnum.codeGroup) {
                CodeListModule.searchCodeGroup();
            } else {
                CodeListModule.searchCode();
            }
        }, false);
        
        // Row 더블 클릭
        $('#codeGroupGrid').on('dblclick', '.bodycell', function(e) {
            SKIAF.console.info('codeGroupGrid dblclick');
            var dataObj = AlopexGrid.parseEvent(e).data;
            var rowData = $('#codeGroupGrid').alopexGrid('dataGetByIndex', {data : dataObj._index.data});
            CodeListModule.selectCodeGroup(rowData);                
                        
            if (e.type == 'dblclick') {
                CodeListModule.searchCode(true);                
            }
        });
        
        // 체크시 코드쪽 그룹정보 반영
        $('#codeGroupGrid').on('dataChanged', function(e) {
            SKIAF.console.info('dataChanged', AlopexGrid.parseEvent(e));
            var dataList = AlopexGrid.parseEvent(e).datalist;
            var eventType = AlopexGrid.parseEvent(e).type;
            SKIAF.console.info('eventType', eventType);
            if (!dataList) {
                return;
            }
            if (!eventType) {
                return;
            }
            if (eventType != 'select') {
                return;
            }

            var rowData = $('#codeGroupGrid').alopexGrid('dataGetByIndex', {data : dataList[0]._index.data});
            if (rowData._state.selected) {
//                $('#codeGroupEditBtn').setEnabled(true);
                CodeListModule.selectCodeGroup(rowData);
            } else {
//                $('#codeGroupEditBtn').setEnabled(false);
                CodeListModule.selectCodeGroupClear();                
            }
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 선택한 코드 그룹 데이터 입력
     */
    this.selectCodeGroup = function (data) {

        if (!data) {
            return;
        }
        if (!data.codeGroupId) {
            return;
        }
        var codeGroupId = data.codeGroupId;
        var codeGroupName = '';
        var codeGroupLang = ''; 
        SKIAF.i18n.langSupportedCodes.forEach(function(lang, index) {
            if (lang == SKIAF.i18n.langCurrentCode) {
                codeGroupName = data["codeGroupName" + (index + 1)];        
                codeGroupLang = lang;
            }
        });
        
        $('#codeArea').setData({
            codeGroupId: codeGroupId,
            codeGroupLang: codeGroupLang,
            codeGroupName: codeGroupName,
            useYnText: data.useYn ? 'Y' : 'N'
        });
    };
    
    /**
     * 코드 그룹 조회
     */
    this.findOneCodeGroup = function (codeGroupId) {
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODE_GROUPS_DETAIL, SKIAF.util.encodeUrlAndBase64(codeGroupId)), {
            method : 'GET',
            success : function(result) {
                CodeListModule.selectCodeGroup(result.data);
                
            }
        });
    };
    
    /**
     * 코드 그룹 페이지 목록 조회
     */
    this.searchCodeGroup = function (isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        
        // 코드 그룹 탭으로 이동
        $('li[data-content="#codeGroupArea"]').trigger('click', 'CodeListModule');

        // 파라메터 생성
        var params = {};
        var inputData = $('#codeGroupArea').getData();
        var keyword = inputData.keyword;
        var isUnusedInclude = inputData.isunusedinclude.indexOf('codeListCheck') >= 0;
        var isPaging = false;

//        params.page = paging.page;
//        params.size = paging.size;
        
        params.isPaging = isPaging; 
        params.isUnusedInclude = isUnusedInclude;
        params.tab = tabEnum.codeGroup;
        if (keyword) {
            params.keyword = keyword;
        }
        
        // ajax 통신
        // 코드 그룹 검색
        CodeCommonModule.search(params, isHistory, function(result) {

            SKIAF.console.info('codegroup list result :: ', result);

            var pageinfo = {
                dataLength : result.meta.totalCount,
//                current : params.page + 1,
//                perPage : params.size
            };

            $('#codeGroupGrid').alopexGrid('dataSet', result.data, pageinfo);
        });

    };
    
    /**
     * 코드 저장
     */
    this.updateCode = function (saveData, editData) {
        
        
        $a.request(SKIAF.PATH.CODES, {
            method : 'PUT',
            data: {
                codeGroupId: $('#codeEditArea').getData().codeGroupId,
                saveList: saveData,
                updateList: editData
            },
            success : function(result) {
                
                // 상세 화면으로 전환
                CodeListModule.detailMode();
                
                // 상세 화면 새로고침
                CodeListModule.searchCode();
            }
        });
    };
    
    /**
     * 코드 목록 조회
     */
    this.searchCode = function (isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 코드 탭으로 이동
        $('li[data-content="#codeArea"]').trigger('click', 'CodeListModule');
        
        // 상세 화면으로 전환
        CodeListModule.detailMode();
        
        // 파라메터 생성
        var params = {};
        var inputData = $('#codeArea').getData();
        var codeGroupId = inputData.codeGroupId;

        params.tab = tabEnum.code;
        
        // 파라미터 입력
        if (codeGroupId) {
            params.codeGroupId = codeGroupId;
        }

        // 히스토리에 저장
        if (isHistory) {
            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);
            // 히스토리 푸쉬
            history.pushState(null, pageTitle, pageUrl + queryString);
        }
        
        $('#codeEditGrid').alopexGrid('dataSet', []);
        $('#codeDetailGrid').alopexGrid('dataSet', []);
//        $('#codeEditBtn').setEnabled(false);

        if (!codeGroupId) {
            return;
        }

        // ajax 통신
        $a.request(SKIAF.PATH.CODES, {
            method : 'GET',
            data : params,
            success : function(result) {
                
                if (!result.data) {
                    return;
                }
                
                // 수정 버튼 활성화
//                $('#codeEditBtn').setEnabled(true);
                
                // 상세, 편집 그리드 끼리 데이터 공유 금지
                var editData = JSON.parse(JSON.stringify(result.data));
                var detailData = JSON.parse(JSON.stringify(result.data));
                
                $('#codeEditGrid').alopexGrid('dataSet', editData);
                $('#codeDetailGrid').alopexGrid('dataSet', detailData);
            }
        });
    };

    /**
     * 파라메터 셋팅
     */
    this.setParameter = function() {
        
        // 페이지 정보
        paging.page = SKIAF.util.getParameter('page') ? SKIAF.util.getParameter('page') : defaultPage;
        paging.size = SKIAF.util.getParameter('size') ? SKIAF.util.getParameter('size') : defaultSize;
        
        // 탭 정보
        tab = SKIAF.util.getParameter('tab') ? SKIAF.util.getParameter('tab') : tabEnum.codeGroup;
        
        // 미사용 체크 여부
        var isUnusedInclude = SKIAF.util.getParameter('isUnusedInclude') == 'true' ? true : false;
        
        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') ? SKIAF.util.getParameter('keyword') : '';
        
        // 선택된 코드 그룹 정보
        var codeGroupId = SKIAF.util.getParameter('codeGroupId') ? SKIAF.util.getParameter('codeGroupId') : '';

        // 파라미터 값 매칭
        if (tab == tabEnum.codeGroup) {
            $('#codeGroupArea').setData({
                isunusedinclude: (isUnusedInclude ? ['codeListCheck'] : []),
                keyword: keyword,
            });
        } else {
            $('#codeArea').setData({
                codeGroupId: codeGroupId
            });
            CodeListModule.findOneCodeGroup(codeGroupId);
        }
    };

    /**
     * 수정화면으로 전환
     */
    this.editMode = function() {
        $('#codeDetailArea').hide();
        $('#codeEditArea').show();
        $('#codeEditGrid').alopexGrid();
        
        var search = new AlopexGrid.plugin.Search();
        search.setGrid('#codeEditGrid');
        search.setSearchWidget("#codeGridSearchArea");
        search.init();
    };
    
    /**
     * 상세화면으로 전환
     */
    this.detailMode = function() {
        $('#codeDetailArea').show();
        $('#codeEditArea').hide();
    };
    
    /**
     * 추가할 Sequence 생성
     */
    this.generateNextSeq = function(gridData) {
        var selectedSeq = 1;
        gridData.forEach(function(data, index) {
            if (!data._state) {
                return;
            }
            
            // 기존 데이터가 있는경우 기존 아이디 다음 순번부터 부여
            if (!data._state.added) {
                if (parseInt(data.codeSortSeq) >= selectedSeq) {
                    selectedSeq = data.codeSortSeq + 1;
                }
            } else {
                selectedSeq++;
            }
        });
        return selectedSeq;
    };
    
    /**
     * Sequence 자동 부여
     */
    this.generateSeq = function(gridData) {
        var selectedSeq = 1;
        gridData.forEach(function(data, index) {
            if (!data._state) {
                return;
            }
            
            // 추가했지만 선택해제 한 경우 Seq 부여 안함
            if (data._state.added && !data._state.selected) {
                return;
            }
            
            data.codeSortSeq = selectedSeq;
            selectedSeq++;
        });
        return gridData;
    };
    
    /**
     * 선택한 코드 그룹 초기화
     */
    this.selectCodeGroupClear = function() {
        // 선택한 코드 그룹 정보 삭제
        $('#codeArea').setData({
            codeGroupId: '',
            codeGroupName: '',
            codeGroupLang: '',
            keyword: ''
        });
    };
    
    this.checkCodeSaveBtn = function() {
        var isSave = false;
        var selectedData = $('#codeEditGrid').alopexGrid('dataGet', { _state : { selected : true } });

        for (var idx in selectedData) {
            var data = selectedData[idx];
            if (!data._state.added && !data._state.edited) {
                continue;
            }
            isSave = true;
            break;
        }
        
        if (isSave) {
            $('#codeSaveBtn').setEnabled(true);
        } else {
            $('#codeSaveBtn').setEnabled(false);
        }
    };

});
