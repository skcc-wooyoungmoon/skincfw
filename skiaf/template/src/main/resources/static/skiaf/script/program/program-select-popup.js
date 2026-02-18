/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 선택 팝업
 */
"use strict";
var ProgramPopupModule = $a.page(function() {
    
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

    // 그리드 컬럼
    var gridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],  key : 'check',       align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.program.id'],     key : 'programId',   align : 'left',   tooltip : true, width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.name'],   key : 'programName', align : 'left',   tooltip : true, width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.type'],   key : 'programType', align : 'center', tooltip : true, width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.desc'],   key : 'programDesc', align : 'left',   tooltip : true, width : '200px'},
        {title : SKIAF.i18n.messages['bcm.program.method'], key : 'httpMethod',  align : 'center', tooltip : true, width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.path'],   key : 'programPath', align : 'left',   tooltip : true, width : '200px'}
    ];
    
    // state
    var isProgramCommonReady = false;
    var isDomReady = false;
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // program-common.js import
    if (typeof ProgramCommonModule === 'undefined') {
        $.getScript(SKIAF.contextPath + '/static/skiaf/script/program/program-common.js', function() {
            isProgramCommonReady = true;
            SKIAF.console.info('program common js import');
            ProgramPopupModule.searchList();
        });
    } else {
        isProgramCommonReady = true;
        SKIAF.console.info('program common js import already');
    }
    
    // Dom Ready
    this.init = function(id, param) {
        
        isDomReady = true;
        
        // 프로그램 타입 선택
        var programType = SKIAF.ENUM.PROGRAM_TYPE.VIEW;
        if (param.programType) {
            programType = param.programType;
        }

        var inputData = {
                programtype: [
                    {value: '', text: SKIAF.i18n.messages['bcm.common.all']},
                    {value: SKIAF.ENUM.PROGRAM_TYPE.SERVICE, text: SKIAF.ENUM.PROGRAM_TYPE.SERVICE},
                    {value: SKIAF.ENUM.PROGRAM_TYPE.VIEW, text: SKIAF.ENUM.PROGRAM_TYPE.VIEW}
                ],
                selectedtype: programType
        };

        if (param.keyword) {
            inputData.keyword = param.keyword;
        } else {
            inputData.keyword = '';
        }
        
        if (param.programTypeShow) {
            $('#programPopupSearchArea .searchbox').show();
        }

        $('#programPopupSearchArea').setData(inputData);

        ProgramPopupModule.addEvent();

        var listGridObj = {
            height: 394,
            autoColumnIndex : true,
            columnFixUpto : 1,
            fitTableWidth : true,
            pager : true,
            rowSelectOption: {
                clickSelect: true,
                singleSelect: true,
                disableSelectByKey : true
            },
            paging : {
                perPage : paging.size,
                pagerCount : 10,
                pagerSelect : true
            },
            defaultColumnMapping : {
                resizing : true
            },
            columnMapping : gridColumns,
            data : []
        };
        $('#programPopupGrid').alopexGrid(listGridObj);
        
        // 최초 검색
        ProgramPopupModule.searchList();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {

        // 확인 버튼
        $('#programPopupConfirm').on('click', function(e) {
            // 선택된 데이터 리턴
            var selectedData = $('#programPopupGrid').alopexGrid('dataGet', { _state : { selected : true } });
            if (selectedData.length <= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.select']);
                return;
            }
            
            $a.close(selectedData[0]);
        });
        
        // Row 더블 클릭
        $('#programPopupGrid').on('dblclick', '.bodycell', function(e) {

            // 선택된 데이터 리턴
            var dataObj = AlopexGrid.parseEvent(e).data;
            
            $a.close(dataObj);
        });

        // 취소 버튼
        $('#programPopupCancle').on('click', function(e) {
            $a.close(false);
        });
        
        // 페이지 사이즈 변경 이벤트
        $('#programPopupGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            
            paging.page = 1;
            ProgramPopupModule.searchList();
        });
        
        // 페이징 변경 이벤트
        $('#programPopupGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            ProgramPopupModule.searchList();
        });
        
        // 검색 버튼 클릭
        $('#programPopupSearchArea .skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            ProgramPopupModule.searchList();
        });

        // 검색 입력영역 엔터 이벤트
        $('#programPopupSearchArea .skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            ProgramPopupModule.searchList();
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 프로그램 목록 검색
     */
    this.searchList = function () {
        
        // Dom Ready 와 Program Common js 임포트 된 경우 실행
        if (!isProgramCommonReady || !isDomReady) {
            return;
        }
        
        // 파라메터 생성
        var params = {};
        var inputData = $('#programPopupSearchArea').getData();
        var keyword = inputData.keyword;
        
        params.page = paging.page;
        params.size = paging.size;

        var type = inputData.selectedtype;
        if (type == SKIAF.ENUM.PROGRAM_TYPE.VIEW || type == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
            params.programType = type;            
        }

        if (keyword) {
            params.keyword = keyword;
        }
        params.isUnusedInclude = inputData.isunusedinclude.length > 0;

        // 프로그램 리스트에 정의된 검색 기능 사용
        ProgramCommonModule.search(params, false, function(result) {

            var pageinfo = {
                    dataLength : result.meta.totalCount,
                    current : params.page + 1,
                    perPage : params.size
            };

            $('#programPopupGrid').alopexGrid('dataSet', result.data, pageinfo);
        });
    };

});