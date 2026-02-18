/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.14 - in01869
 * description : 이벤트 로그 화면 js
 */
"use strict";
var EventLogModule = $a.page(function() {


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 날짜 시간 입력 mask 처리
    $a.maskedinput($("input[name=startDate]")[0], "0000-00-00");
    $a.maskedinput($("input[name=endDate]")[0], "0000-00-00");
    $a.maskedinput($("input[name=startTime]")[0], "00:00");
    $a.maskedinput($("input[name=endTime]")[0], "00:00");

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 페이지 정보
    var pageTitle = '';
    var pageUrl = location.pathname;

    // 페이징
    var defaultCurrent = 1;
    var defaultSize = 10;
    var paging = {
        current : defaultCurrent, // 현재 페이지
        size : defaultSize, // 페이지 사이즈
    };

    // 그리드 초기화
    var listGridObj = {
        numberingColumnFromZero : false,
        autoColumnIndex : true,
        columnFixUpto : 2,
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        height : '404px', /* 전체 height */
        pager : true,
        paging : {
            perPage : paging.size,
            pagerCount : 10,
            pagerSelect : true
        },
        defaultColumnMapping : {
            resizing : true
        },
        columnMapping : [
                {
                    title : SKIAF.i18n.messages['bcm.common.select'],
                    align : 'center',
                    key : 'check',
                    headerStyleclass : 'set',
                    width : '50px',
                    selectorColumn : true,
                    resizing : false
                },
                {
                    title : 'Seq',
                    key : 'eventId',
                    align : 'center',
                    width : '100px'
                },
                {
                    title : 'Timestamp',
                    key : 'timestmp',
                    align : 'center',
                    width : '160px',
                    render : function(value, data, render, mapping) {
                        var dt = new Date(data.timestmp);
                        return SKIAF.util.dateFormatReplace(dt,
                                'yyyy-MM-dd HH:mm:ss');
                    }
                }, {
                    title : 'Event Group',
                    key : 'eventGroup',
                    align : 'center',
                    width : '100px'
                }, {
                    title : 'Event Type',
                    key : 'eventType',
                    align : 'center',
                    tooltip : true,
                    width : '150px'
                }, {
                    title : 'Login ID',
                    key : 'loginId',
                    align : 'center',
                    tooltip : true,
                    width : '120px'
                }, {
                    title : 'Message',
                    key : 'formattedMessage',
                    align : 'left',
                    tooltip : true,
                    width : '250px'
                }, {
                    title : 'Logger',
                    key : 'loggerName',
                    align : 'left',
                    tooltip : true,
                    width : '200px'
                } ],
        data : []
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {
    
        // 이벤트 바인딩
        EventLogModule.addEvent();
    
        // 파라메터 셋팅
        EventLogModule.setParameter();
    
        // 그리드 초기화
        $('#listGrid').alopexGrid(listGridObj);
    
        // 최초 검색
        EventLogModule.search(true);
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        /**
         * 페이지 사이즈 변경 이벤트
         */
        $('#listGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.size = evObj.perPage;

            paging.current = 1;
            EventLogModule.search();
        });

        /**
         * 페이징 변경 이벤트
         */
        $('#listGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.current = evObj.page;
            paging.size = evObj.pageinfo.perPage;

            EventLogModule.search();
        });

        /**
         * 검색 버튼 클릭
         */
        $('#defaultSearchBtn').on('click', function(e) {
            paging.current = 1;
            EventLogModule.search();
        });

        /**
         * 검색 영역 엔터키
         */
        $('#defaultSearchText').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.current = 1;
            EventLogModule.search();
        });

        /**
         * 브라우저 뒤로가기 이벤트
         */
        window.addEventListener('popstate', function(e) {
            EventLogModule.setParameter();
            EventLogModule.search(true);
        }, false);

        /**
         * 상세검색 버튼 클릭 이벤트
         */
        $('#detailSearch').on('click', function() {
            // 상세검색 비활성화 일 경우 상세검색 필드 초기화
            if (!$(this).isChecked()) {
                $('select[name=searchLevel]').setSelected('');
                $('input[name=searchLogger]').val('');
                $('input[name=searchCaller]').val('');
            }
        });

        /**
         * 오늘 버튼 클릭 이벤트
         */
        $('#today').on('click', function() {
            EventLogModule.setDaterange('today');
        });

        /**
         * 어제 버튼 클릭 이벤트
         */
        $('#yesterday').on('click', function() {
            EventLogModule.setDaterange('yesterday');
        });

        /**
         * 최근 일주일 버튼 클릭 이벤트
         */
        $('#week').on('click', function() {
            EventLogModule.setDaterange('week');
        });

        /**
         * 최근 한달 버튼 클릭 이벤트
         */
        $('#month').on('click', function() {
            EventLogModule.setDaterange('month');
        });

        /**
         * 그리드 더블클릭
         */
        $('#listGrid')
            .on(
                'dblclick',
                '.bodycell',
                function(e) {
                    var evObj = AlopexGrid.parseEvent(e);
                    var pop = $a
                            .popup({
                                title : SKIAF.i18n.messages['bcm.log.event.event-log-detail.label'],
                                url : SKIAF.PATH.VIEW_EVENT_LOGS_POPUPS_DETAIL,
                                data : {
                                    data : evObj.data
                                },
                                iframe : false,
                                movable : true,
                                width : 1000,// width 필요
                                height : 520
                            // height 필요
                            });
                    $(pop).addClass('layerpop'); // class 필요
                });

        /**
         * 상세 버튼 클릭
         */
        $('#detailView').on('click', function() {
            var selectedObj = $('#listGrid').alopexGrid('dataGet', {'_state': {selected : true}});
            if(selectedObj.length == 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.log.event.valid.log-select']);
                return false;
            }
            var pop = $a.popup({
                title : SKIAF.i18n.messages['bcm.log.event.event-log-detail.label'],
                url : SKIAF.PATH.VIEW_EVENT_LOGS_POPUPS_DETAIL,
                data : {
                    data : selectedObj[0]
                },
                iframe: false,
                movable:true,
                width: 1000,//width 필요
                height: 520 //height 필요
            });
            $(pop).addClass('layerpop'); //class 필요
        });
        
        /**
         * 엑셀 다운로드 버튼 클릭
         */
        $('#btnExcelDownload').on('click', function() {
            
            var totalCnt = 0;
            var paging = $('#listGrid').alopexGrid('readOption').paging;
            if(paging.customPaging) {
                totalCnt = $('#listGrid').alopexGrid('readOption').paging.customPaging.dataLength;
            }
            
            if(totalCnt == 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.common.excel-download-empty']);
                return false;
            }   
            
            if(totalCnt > 1000) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.log.event.valid.excel-download-size']);
                return false;
            }
            
            // 파라메터 생성
            var params = {};
            params.loggingType = 'EVENT';
            params.sort = 'event_id,desc';

            params = EventLogModule.validate(params);
            params.list = true;

            var excelParm = {
                    url     : SKIAF.PATH.LOGS,
                    method    : 'GET',
                    data    : params,
                    resultListName  : 'eventLogList',
                    fileName : 'skiaf-excel-eventLog-' + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss'),
                    columnNames : listGridObj.columnMapping
            }
            SKIAF.excelUtil.excelExportUrl(excelParm);
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 조회
     */
    this.search = function(isHistory) {

        // 파라메터 생성
        var params = {};
        params.loggingType = 'EVENT';
        params.sort = 'event_id,desc';
        params.page = paging.current - 1;
        params.size = paging.size;

        params = EventLogModule.validate(params);

        // 히스토리에 저장
        if (isHistory == null || isHistory) {
            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);
            // 히스토리 푸쉬
            history.pushState(null, pageTitle, pageUrl + queryString);
        }

        $a.request(SKIAF.PATH.LOGS, {
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

                $('#listGrid').alopexGrid('dataSet', result.data,
                        pageinfo);

            }
        });
    };

    this.validate = function(params) {
    
        var keyword = $('#defaultSearchText').val();
        if (keyword) {
            params.keyword = keyword;
        }
    
        // 시작날짜
        var startDate = $('input[name=startDate]').val();
        // 시작시간
        var startTime = $('input[name=startTime]').val();
    
        if (startDate) {
    
            params.startDate = startDate;
    
            if ($.trim(startTime) != '') {
                if (startTime.length < 5) {
                    SKIAF.popup
                            .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-start-time']);
                    return false;
                }
                var arry1 = startTime.split(':');
                var hours1 = arry1[0] * 1;
                var minutes1 = arry1[1] * 1;
    
                if (hours1 > 23 || minutes1 > 59) {
                    SKIAF.popup
                            .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-start-time']);
                    return false;
                }
    
                params.startTime = startTime;
    
            } else {
                params.startTime = '00:00';
            }
        } else {
            if ($.trim(startTime) != '') {
                SKIAF.popup
                        .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-start-date']);
                return false;
            }
        }
    
        // 종료날짜
        var endDate = $('input[name=endDate]').val();
        // 종료시간
        var endTime = $('input[name=endTime]').val();
    
        if (endDate) {
    
            params.endDate = endDate;
    
            if ($.trim(endTime) != '') {
                if (endTime.length < 5) {
                    SKIAF.popup
                            .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-end-time']);
                    return false;
                }
                var arry2 = endTime.split(':');
                var hours2 = arry2[0] * 1;
                var minutes2 = arry2[1] * 1;
    
                if (hours2 > 23 || minutes2 > 59) {
                    SKIAF.popup
                            .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-end-time']);
                    return false;
                }
    
                params.endTime = endTime;
    
            } else {
                params.endTime = '23:59';
            }
        } else {
            if ($.trim(endTime) != '') {
                SKIAF.popup
                        .alert(SKIAF.i18n.messages['bcm.log.event.valid.search-end-date']);
                return false;
            }
        }
    
        var eventGroup = $('select[name=searchEventGroup]').val()
        if (eventGroup) {
            params.eventGroup = eventGroup;
        }
    
        var eventType = $('input[name=searchEventType]').val();
        if (eventType) {
            params.eventType = eventType;
        }
    
        var loginId = $('input[name=searchLoginId]').val();
        if (loginId) {
            params.loginId = loginId;
        }
    
        return params;
    };

    /**
     * 파라메터 셋팅
     */
    this.setParameter = function() {
        paging.current = SKIAF.util.getParameter('current') || defaultCurrent;
        paging.size = SKIAF.util.getParameter('size') || defaultSize;
    
        var keyword = SKIAF.util.getParameter('keyword') || '';
        $('#defaultSearchText').val(keyword);
    
        var eventGroup = SKIAF.util.getParameter('eventGroup') || '';
        $('select[name=searchEventGroup]').val(eventGroup);
        $('.searchbox .Divselect').refresh();
    
        var eventType = SKIAF.util.getParameter('eventType') || '';
        $('input[name=searchEventType]').val(eventType);
    
        var loginId = SKIAF.util.getParameter('loginId') || '';
        $('input[name="searchLoginId"]').val(loginId);
    };

    /**
     * 날짜 검색 기간 설정
     */
    this.setDaterange = function(type) {
    
        var startDate = '';
        var endDate = '';
    
        if (type == 'today') {
            var now = new Date();
            startDate = now.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(now.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(now.getDate(), 2);
            endDate = startDate;
        } else if (type == 'yesterday') {
            var dt1 = new Date(new Date().getTime() - (1 * 24 * 60 * 60 * 1000));
            startDate = dt1.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(dt1.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(dt1.getDate(), 2);
            endDate = startDate;
        } else if (type == 'week') {
            var dt2 = new Date();
            endDate = dt2.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(dt2.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(dt2.getDate(), 2);
            var startDt = new Date(dt2.getTime() - (6 * 24 * 60 * 60 * 1000));
            startDate = startDt.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(startDt.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(startDt.getDate(), 2);
        } else if (type == 'month') {
            var dt3 = new Date();
            endDate = dt3.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(dt3.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(dt3.getDate(), 2);
    
            var lastDayofLastMonth = (new Date(dt3.getYear(), dt3.getMonth(), 0)).getDate();
            if (dt3.getDate() > lastDayofLastMonth) {
                dt3.setDate(lastDayofLastMonth);
            }
    
            var month = dt3.getMonth() - 1;
            dt3.setMonth(month);
    
            startDate = dt3.getFullYear() + '-'
                        + SKIAF.util.padNumLeft(dt3.getMonth() + 1, 2)
                        + '-' + SKIAF.util.padNumLeft(dt3.getDate(), 2);
        }
    
        $('input[name=startDate]').val(startDate);
        $('input[name=endDate]').val(endDate);
    
        $('input[name=startTime]').val('');
        $('input[name=endTime]').val('');
    };
});