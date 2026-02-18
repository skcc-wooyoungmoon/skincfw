/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 코드 그룹 팝업
 */
"use strict";
var CodeGroupPopupModule = $a.page(function() {
    
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

    // 그리드 컬럼
    var popupGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],       key : 'check',         align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.code.codegroup.id'],   key : 'codeGroupId',   align : 'center', tooltip : true,  width : '115px'}
    ];
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        popupGridColumns.push({title : SKIAF.i18n.messages['bcm.code.codegroup.name'] + ' (' + langCode + ')',       key : 'codeGroupName' + (index + 1), align : 'center', tooltip : true, width : '150px'});        
    });
    popupGridColumns.push(
        {title : SKIAF.i18n.messages['bcm.code.codegroup.desc'], key : 'codeGroupDesc', align : 'center', tooltip : true,  width : '200px'},
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
    
    // 그리드 설정
    var popupGridObj = {
        numberingColumnFromZero: false,
        columnFixUpto : 1,
        autoColumnIndex : true,
        fitTableWidth : true,
        height: 404,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping: {
            resizing: true
        },
        pager : true,
        paging : {
            perPage : paging.size,
            pagerCount : 10,
            pagerTotal : true,
            pagerSelect : true
        },
        rowSelectOption: {
            clickSelect: true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : popupGridColumns,
        data : []
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        
        SKIAF.console.info(param);
        if (param['keyword']) {
            $('#codeGroupPopupSearchArea').setData({keyword : param['keyword']});
        }
        
        // 이벤트 설정
        CodeGroupPopupModule.addEvent();
        
        SKIAF.console.info('event');
        
        // 그리드 초기화
        $('#codeGroupPopupGrid').alopexGrid(popupGridObj);

        // 최초 검색
        CodeGroupPopupModule.searchCodeGroup();
        
        // 검색 영역으로 포커스 이동
        $('#codeGroupPopupSearchInput').focus();

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 확인 버튼
        $('#codeGroupPopupConfirmBtn').on('click', function(e) {
            // 선택된 데이터 리턴
            var selectedData = $('#codeGroupPopupGrid').alopexGrid('dataGet', { _state : { selected : true } });
            if (selectedData.length != 1) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.select']);
                return;
            }
            
            $a.close(selectedData[0]);
        });    

        // 취소 버튼
        $('#codeGroupPopupCancleBtn').on('click', function(e) {
            $a.close();
        });
        
        // 코드 그룹 페이지 사이즈 변경 이벤트
        $('#codeGroupPopupGrid').on('perPageChange', function(e) {

            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            
            paging.page = 1;
            CodeGroupPopupModule.searchCodeGroup();
        });
        
        // 코드 그룹 페이징 변경 이벤트
        $('#codeGroupPopupGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;

            CodeGroupPopupModule.searchCodeGroup();
        });
        
        // 검색 버튼 클릭
        $('#codeGroupPopupSearchBtn').on('click', function(e) {
            paging.page = 1;
            CodeGroupPopupModule.searchCodeGroup();
        });

        // 검색영역 엔터키 이벤트
        $('#codeGroupPopupSearchInput').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            CodeGroupPopupModule.searchCodeGroup();
        });
        
        // Row 더블 클릭
        $('#codeGroupPopupGrid').on('dblclick', '.bodycell', function(e) {

            // 선택된 데이터 리턴
            var dataObj = AlopexGrid.parseEvent(e).data;
            
            $a.close(dataObj);
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 코드 그룹 페이지 목록 조회
     */
    this.searchCodeGroup = function (isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 파라메터 생성
        var params = {};
        var isPaging = true;
        var inputData = $('#codeGroupPopupSearchArea').getData();
        var keyword = inputData.keyword;
        var isUnusedInclude = inputData.isunusedinclude.indexOf('codeGroupPopupCheck') >= 0;

        params.isPaging = isPaging;
        params.page = paging.page;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        params.isCodeInclude = false;
        if (keyword) {
            params.keyword = keyword;
        }

        // 코드 그룹 검색
        CodeCommonModule.search(params, false, function(result) {

            SKIAF.console.info('codegroup select popup result :: ', result);
            var pageinfo = {
                dataLength : result.meta.totalCount,
                current : params.page + 1,
                perPage : params.size
            };

            $('#codeGroupPopupGrid').alopexGrid('dataSet', result.data, pageinfo);
        });

    };

});
