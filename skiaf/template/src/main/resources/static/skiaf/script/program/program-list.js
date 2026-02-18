/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01866
 * description : 프로그램 리스트
 */
"use strict";
var ProgramListModule = $a.page(function() {
    
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
        {title : SKIAF.i18n.messages['bcm.common.select'],      key : 'check',       align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.program.id'],         key : 'programId',   align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.name'],       key : 'programName', align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.type'],       key : 'programType', align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.desc'],       key : 'programDesc', align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.program.method'],     key : 'httpMethod',  align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.path'],       key : 'programPath', align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],      key : 'useYn',       align : 'center', tooltip : false, width : '65px', render : function(value, data) { return value ? 'Y' : 'N';}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],   key : 'updateBy',    align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.update-date'], key : 'updateDate',  align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}
        },
        {title : SKIAF.i18n.messages['bcm.common.create-by'],   key : 'createBy',    align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.create-date'], key : 'createDate',  align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}
        }
    ];

    // 그리드 초기화
    var listGridObj = {
        height : 404,
        columnFixUpto : 1,
        autoColumnIndex : true,
        fitTableWidth : true,
        defaultColumnMapping : {
            resizing : true
        },
        pager : true,
        paging : {
            perPage : paging.size,
            pagerCount : 10,
            pagerSelect : true
        },
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : gridColumns,
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        
        // 파라메터 셋팅
        ProgramListModule.setParameter();
        
        // 이벤트 설정
        ProgramListModule.addEvent();
        
        $('#programGrid').alopexGrid(listGridObj);
        
        // 최초 검색
        ProgramListModule.searchList();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 페이지 사이즈 변경 이벤트
        $('#programGrid').on('perPageChange', function(e) {

            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            
            paging.page = 1;
            ProgramListModule.searchList(true);
        });
        
        // 페이징 변경 이벤트
        $('#programGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            ProgramListModule.searchList(true);
        });
        
        // 검색 버튼 클릭
        $('#programListArea .skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            ProgramListModule.searchList(true);
        });

        // 검색영역 엔터키 이벤트
        $('#programListArea .skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            ProgramListModule.searchList(true);
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            SKIAF.console.info('state', e.state);
            ProgramListModule.setParameter();
            ProgramListModule.searchList();
        }, false);
        
        // 등록 버튼 클릭 이벤트
        $('#createBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_SAVE,
                data : {
                    program: {},
                    editMode : false
                },
                movable: true,
                iframe : false,
                width : 1000,
                height : 600,
                center : true,
                title : SKIAF.i18n.messages['bcm.program.add'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        ProgramListModule.searchList();
                        $('#programGrid').alopexGrid();
                    }
                }
            });
        });
        
        // 수정 버튼 클릭 이벤트
        $('#editBtn').on('click', function(e) {
            var dataList = $('#programGrid').alopexGrid('dataGet', {_state : {selected : true}});
            var data = {};
            var isEditMode = false; 
            if (dataList && dataList.length >= 1) {
                data = dataList[0];
                isEditMode = true;
            } else {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.select']);
                return;
            }
            
            $a.popup({
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_SAVE,
                data : {
                    program : data,
                    editMode : isEditMode
                },
                movable: true,
                iframe : false,
                width : 1000,
                height : 600,
                center : true,
                title : SKIAF.i18n.messages['bcm.program.edit'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        ProgramListModule.searchList();
                        $('#programGrid').alopexGrid();
                    }

                }
            });
        });
        
        // 권한 관리 버튼 클릭 이벤트
        $('#roleBtn').on('click', function(e) {
            var dataList = $('#programGrid').alopexGrid('dataGet', {_state : {selected : true}});
            var programId = '';
            if (dataList && dataList.length == 1) {
                programId = dataList[0].programId;
            }
            ProgramListModule.goProgramRole(programId);
            return;
        });
        
        // 엑셀 다운로드
        $('#download').on('click', function(e) {
            var params = ProgramListModule.makeParameter();
            params.isList = 'true';
            
            var excelParm = {
                    url     : SKIAF.contextPath + SKIAF.PATH.PROGRAMS,
                    method    : 'GET',
                    data    : params,
                    fileName : 'skiaf-excel-program-' + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss'),
                    columnNames : gridColumns
            }
            SKIAF.excelUtil.excelExportUrl(excelParm);
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 페이지 목록 조회
     */
    this.searchList = function (isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        var params = ProgramListModule.makeParameter();
        
        ProgramCommonModule.search(params, isHistory, function(result) {

            var pageinfo = {
                    dataLength : result.meta.totalCount,
                    current : params.page + 1,
                    perPage : params.size
                };

            $('#programGrid').alopexGrid('dataSet', result.data, pageinfo);
        });
    };
    
    /**
     * 파라메터 생성
     */
    this.makeParameter = function() {

        var params = {};
        var inputData = $('#programListArea').getData();
        var keyword = inputData.keyword;
        var isUnusedInclude = inputData.isunusedinclude.length > 0;
        var programType = inputData.selectedtype;

        params.page = paging.page;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        if (keyword) {
            params.keyword = keyword;
        }
        if (programType) {
            params.programType = programType.toUpperCase();
        }
        return params;
    };

    /**
     * 파라메터 셋팅
     */
    this.setParameter = function() {
        
        // 페이지 정보
        paging.page = SKIAF.util.getParameter('page') ? SKIAF.util.getParameter('page') : defaultPage;
        paging.size = SKIAF.util.getParameter('size') ? SKIAF.util.getParameter('size') : defaultSize;
        
        // 미사용 체크 여부
        var isUnusedInclude = SKIAF.util.getParameter('isUnusedInclude') == 'true' ? true : false;
        
        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') ? SKIAF.util.getParameter('keyword') : '';
        
        // 상세 검색
        var programPath = SKIAF.util.getParameter('programPath') ? SKIAF.util.getParameter('programPath') : '';
        var httpMethod = SKIAF.util.getParameter('httpMethod') ? SKIAF.util.getParameter('httpMethod') : '';
        var programType = SKIAF.util.getParameter('programType') ? SKIAF.util.getParameter('programType') : '';
        programType = programType.toUpperCase();
        
        // 마라메터 값 매칭
        $('#programListArea ').setData({
            isunusedinclude: (isUnusedInclude ? ['isUnusedInclude'] : []),
            keyword: keyword,
            programpath: programPath,
            httpmethod: httpMethod,
            programtype: [
                {value: '', text: SKIAF.i18n.messages['bcm.common.all']},
                {value: SKIAF.ENUM.PROGRAM_TYPE.SERVICE, text: SKIAF.ENUM.PROGRAM_TYPE.SERVICE},
                {value: SKIAF.ENUM.PROGRAM_TYPE.VIEW, text: SKIAF.ENUM.PROGRAM_TYPE.VIEW}
            ],
            selectedtype: programType
        });

    };
    
    /**
     * 권한 관리 이동
     */
    this.goProgramRole = function(programId) {
        // 현재 Url 기억 
        Cookies.set(SKIAF.JS_CONSTANT.PROGRAM_PREVIOUS_URL, location.href);
        
        if (programId) {
            window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_ROLE + '?programId=' + programId;
        } else {
            window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_ROLE;                
        }
    };

});
