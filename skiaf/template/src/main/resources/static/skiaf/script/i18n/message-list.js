/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.17 - in01866
 * description : 메시지 목록
 */
"use strict";
var MessageListModule = $a.page(function() {
    
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

    // 메시지 그룹 그리드 컬럼
    var messageGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],          key : 'check',      align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.message.key'],            key : 'messageKey', align : 'left',   tooltip : true, width : '170px'},
        {title : SKIAF.i18n.messages['bcm.message.type'],           key : 'target',     align : 'center', tooltip : true, width : '100px', resizing : false}
    ];
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        messageGridColumns.push({title : SKIAF.i18n.messages['bcm.message.name'] + ' (' + langCode + ')', key : 'messageName' + (index + 1), align : 'left', tooltip : true, width: '150px'});
    });
    messageGridColumns.push(
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],          key : 'useYn',      align : 'center', tooltip : false, width : '65px', resizing : false, render : function(value, data) { return value ? 'Y' : 'N';}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],       key : 'updateBy',   align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.update-date'],     key : 'updateDate', align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}
        },
        {title : SKIAF.i18n.messages['bcm.common.create-by'],       key : 'createBy',   align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.create-date'],     key : 'createDate', align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}
        }
    );
    
    // 그리드 설정
    var messageGridObj = {
        numberingColumnFromZero: false,
        columnFixUpto : 1,
        autoColumnIndex : true,
        fitTableWidth : true,
        headerRowHeight: 30,
        height:404, /* 전체 height */
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
        columnMapping : messageGridColumns,
        data : []
    };

    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        
        // 파라메터 셋팅
        MessageListModule.setParameter();
        
        // 이벤트 설정
        MessageListModule.addEvent();
        
        // 그리드 초기화
        $('#messageGrid').alopexGrid(messageGridObj);
        
        // 최초 검색
        MessageListModule.searchMessage();

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 등록 버튼 클릭 이벤트
        $('#addBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.PATH.VIEW_MESSAGES_SAVE,
                data : {
                    messageId : ''
                },
                movable: true,
                iframe : false,
                width: 1000,
                height: 500,
                center : true,
                title : SKIAF.i18n.messages['bcm.message.create'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        MessageListModule.searchMessage();
                        $('#messageGrid').alopexGrid();                        
                    }
                }
            });
        });
        
        // 수정 버튼 클릭 이벤트
        $('#editBtn').on('click', function(e) {
            var dataList = $('#messageGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if(dataList.length == 0){
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.message.valid.update-select']);
                return;
            }
            
            var data = {};
            var isEditMode = false; 
            if (dataList && dataList.length == 1) {
                data = dataList[0];
                isEditMode = true;
            }
            
            $a.popup({
                url : SKIAF.PATH.VIEW_MESSAGES_SAVE,
                data : {
                    message : data,
                    editMode : isEditMode
                },
                movable: true,
                iframe : false,
                width: 1000,
                height: 500,
                center : true,
                title : SKIAF.i18n.messages['bcm.message.edit'],
                callback : function(data) {
                    
                    // 저장이 되었으면 목록 새로고침
                    if (data) {
                        MessageListModule.searchMessage();
                        $('#messageGrid').alopexGrid();
                    }
                }
            });
        });
        
        $('#messagesDownload').on('click', function(e) {
            
            var params = {};
            var inputData = $('#messageArea').getData();
            var keyword = inputData.keyword;
            var isUnusedInclude = inputData.isunusedinclude.indexOf('messageListCheck') >= 0;

            params.page = 0;
            params.isUnusedInclude = isUnusedInclude;
            if (keyword) {
                params.keyword = keyword;
            }
            
            var excelParm = {
                    url : SKIAF.PATH.MESSAGES,
                    method : 'GET',
                    data : params,
                    resultListName : 'messages',
                    columnNames : messageGridColumns,
                    fileName : "SKIAF_excel_messages_" + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss')
            }
            
            SKIAF.excelUtil.excelExportUrl(excelParm);

        });
        
        // 메시지 그룹 페이지 사이즈 변경 이벤트
        $('#messageGrid').on('perPageChange', function(e) {

            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            
            paging.page = 1;
            MessageListModule.searchMessage(true);
        });
        
        // 메시지 그룹 페이징 변경 이벤트
        $('#messageGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;

            MessageListModule.searchMessage(true);
        });
        
        // 메시지 그룹 검색 버튼 클릭
        $('#messageArea .skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            MessageListModule.searchMessage(true);
        });

        // 메시지 그룹 검색영역 엔터키 이벤트
        $('#messageArea .skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            MessageListModule.searchMessage(true);
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            SKIAF.console.info('state', e.state);
            MessageListModule.setParameter();
            
            // 브라우저 뒤로가기 후 최초 검색
            MessageListModule.searchMessage();
        }, false);
        
        // 체크시 메시지쪽 그룹정보 반영
        $('#messageGrid').on('dataChanged', function(e) {
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

            var rowData = $('#messageGrid').alopexGrid('dataGetByIndex', {data : dataList[0]._index.data});
//            if (rowData._state.selected) {
//                $('#editBtn').setEnabled(true);
//
//            } else {
//                $('#editBtn').setEnabled(false);
//
//            }
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 메시지 페이지 목록 조회
     */
    this.searchMessage = function (isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        
        // 파라메터 생성
        var params = {};
        var inputData = $('#messageArea').getData();
        var keyword = inputData.keyword;
        var isUnusedInclude = inputData.isunusedinclude.indexOf('messageListCheck') >= 0;

        params.page = paging.page;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        if (keyword) {
            params.keyword = keyword;
        }
        
        // 히스토리에 저장
        if (isHistory) {
            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);
            // 히스토리 푸쉬
            history.pushState(null, pageTitle, pageUrl + queryString);
        }

        // 그리드 페이지에서 서버 페이지로 값 변환
        params.page = params.page - 1;
        
        // ajax 통신
        // 메시지 검색
        $a.request(SKIAF.PATH.MESSAGES, {
            method : 'GET',
            data : params,
            success : function(result) {
                
                if (!result.data) {
                    return;
                }
                
                var pageinfo = {
                    dataLength : result.meta.totalCount,
                    current : params.page + 1,
                    perPage : params.size
                };
                
//                $('#editBtn').setEnabled(false);

                $('#messageGrid').alopexGrid('dataSet', result.data, pageinfo);

            }
        });

    };
    
    /**
     * 파라미터 셋팅
     */
    this.setParameter = function() {
        
        // 페이지 정보
        paging.page = SKIAF.util.getParameter('page') || defaultPage;
        paging.size = SKIAF.util.getParameter('size') || defaultSize;
        
        // 미사용 체크 여부
        var isUnusedInclude = (SKIAF.util.getParameter('isUnusedInclude') == 'true') ? true : false;
        
        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') || '';
        
        $('#messageArea').setData({
            isunusedinclude: (isUnusedInclude ? ['messageListCheck'] : []),
            keyword: keyword,
        });
    };
});
