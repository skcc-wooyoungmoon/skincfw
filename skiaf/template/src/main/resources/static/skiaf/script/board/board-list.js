/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.18 - in01871
 * description : 게시판 목록
 */
"use strict";
var BoardListModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };

    var gridColumns = [
        {title: SKIAF.i18n.messages['bcm.common.select'],           align: 'center',        headerStyleclass:'set',     width: 50,                 selectorColumn : true,     resizing : false},
        {title: SKIAF.i18n.messages['bcm.board.id'],                key: 'boardId',         align: 'center',            width: 100,                tooltip: false},
        {title: SKIAF.i18n.messages['bcm.board.board-title'],       key: 'boardName',       align: 'left',              width: 200,                tooltip: false,          render : {type : 'link'}},
        {title: SKIAF.i18n.messages['bcm.board.desc'],              key: 'boardDesc',       align: 'left',              width: 300,                tooltip: false},
        {title: SKIAF.i18n.messages['bcm.board.comment'],           key: 'commentUseYn',    align: 'center',            width: 50,                 tooltip: false,
            render : function(value, data) {
                return value ? 'Y' : 'N'
            }
        },
        {title: SKIAF.i18n.messages['bcm.board.emergency'],         key: 'emergencyUseYn',  align: 'center',            width: 50,                 tooltip: false,
            render : function(value, data) {
                return value ? 'Y' : 'N'
            }
        },
        {title: SKIAF.i18n.messages['bcm.board.attach'],            key: 'attachFileType',  align: 'center',            width: 70,                 tooltip: false,
            render : function(value, data) {
                if(value === SKIAF.ENUM.BOARD_ATTACH_FILE_TYPE.NO){
                    return SKIAF.i18n.messages['bcm.board.no']
                } else if (value === SKIAF.ENUM.BOARD_ATTACH_FILE_TYPE.SINGLE) {
                    return SKIAF.i18n.messages['bcm.board.attach-file-single']
                } else if (value === SKIAF.ENUM.BOARD_ATTACH_FILE_TYPE.MULTI) {
                    return SKIAF.i18n.messages['bcm.board.attach-file-multi']
                }
            }
        },
        {title: SKIAF.i18n.messages['bcm.board.view-type'],         key: 'boardType',       align: 'center',            width: 90,                 tooltip: false,
            render : function(value, data) {
                if(value === SKIAF.ENUM.BOARD_TYPE.MAIN) {
                    return SKIAF.i18n.messages['bcm.board.view-type-main'];
                } else if (value === SKIAF.ENUM.BOARD_TYPE.POPUP) {
                    return SKIAF.i18n.messages['bcm.board.view-type-popup'];
                }
            }
        },
        {title: SKIAF.i18n.messages['bcm.common.use-yn'],           key: 'useYn',           align: 'center',            width: 90,                 tooltip: false,
            render : function(value, data) {
                return value ? 'Y' : 'N'
            }
        },
        {title: SKIAF.i18n.messages['bcm.common.update-by'],        key: 'updateBy',        align: 'center',            width: 85,                 tooltip: false},
        {title: SKIAF.i18n.messages['bcm.common.update-date'],      key: 'updateDate',      align: 'center',            width: 150,                tooltip: false},
        {title: SKIAF.i18n.messages['bcm.common.create-by'],        key: 'createBy',        align: 'center',            width: 85,                 tooltip: false},
        {title: SKIAF.i18n.messages['bcm.common.create-date'],      key: 'createDate',      align: 'center',            width: 150,                tooltip: false}
    ];

    var boardGridObj = {
        autoColumnIndex : true,
        fitTableWidth : true,
        headerRowHeight : 30,
        height : 404,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.no-data']
        },
        pager : true,
        paging : {
            perPage : paging.size,
            pagerCount : 10,
            pagerTotal : true,
            pagerSelect : true
        },
        defaultColumnMapping: {
            resizing: true
        },
        rowSelectOption:{
            clickSelect : true,
            singleSelect: true,
            disableSelectByKey : true
        },
        renderMapping : {
            'link' : {
                renderer : function(value, data, render, mapping) {
                    return '<a href = "' + SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_BOARD_ARTICLES, data.boardId) + '">' + data.boardName + '</a>';
                }
            }
        },
        readonlyRender: false,
        columnMapping : gridColumns,
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
        
        $('#boardGrid').alopexGrid(boardGridObj);
       
        BoardListModule.search();
        BoardListModule.addEvent();
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /**
         * 수정 버튼
         */
        $('#boardEditBtn').on('click', function() {
            
            var data = $('#boardGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if(data.length == 0) {
                
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.board.valid.select-board']);
                return;
                
            } else {
                
                var boardId = data[0].boardId;
                
                BoardListModule.boardUpdatePopup(boardId);
            }
            
        });
                
        /**
         * 등록 버튼
         */
        $('#boardCreateBtn').on('click', function() {            
            BoardListModule.boardCreatePopup();
        });
        
        /**
         * 검색 버튼
         */
        $('#boardSearchBtn').on('click', function() {
            BoardListModule.search();
        });
        
        /**
         * 페이지 변경
         */
        $('#boardGrid').on('perPageChange', function(e) {
            if (!isAjaxAvailable) {
                return;
            }
            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            paging.page = 1;
            BoardListModule.search(true);
        });
        
        /**
         * 목록 갯수 변경
         */
        $('#boardGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
    
            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            BoardListModule.search(true);
        });
        
        /**
         * 검색 입력영역 엔터 이벤트
         */ 
        $('#boardSearchInput').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            
            BoardListModule.search();
        });
        
        /**
         * 브라우저 뒤로가기
         */
        window.addEventListener('popstate', function(e) {
            UserGroupListModule.setParameter();
            UserGroupListModule.search();
        }, false);
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 검색
    this.search = function(isHistory){
        
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        
        var params = {};
        var keyword = $('#boardSearchInput').val();
        
        params.page = paging.page;
        params.size = paging.size;
        
        if(keyword) {
            params.keyword = keyword;
        }
        
        if(isHistory) {
            var queryString = '?' + SKIAF.util.createParameter(params);
            
            history.pushState(null, '', pageUrl + queryString);
        }
        
        params.page = paging.page - 1;
        
        $a.request(SKIAF.PATH.BOARDS, {
            method : 'GET',
            data : params,
            success : function(result){
                if (!result.data) {
                    return;
                }
                var pageinfo = {
                    dataLength : result.meta.totalCount,
                    current : paging.page,
                    perPage : paging.size
                };
                
                var data = result.data;

                $('#boardGrid').alopexGrid('dataSet', data, pageinfo);
            }
        });
        
    };
    
    // 등록 팝업
    this.boardCreatePopup = function (){
        
        $a.popup({
            url : SKIAF.PATH.VIEW_BOARDS_CREATE,
            iframe : false,
            movable : true,
            width : 1000,
            height : 600,
            center : true,
            title : SKIAF.i18n.messages['bcm.board.board-create'],
            callback : function (result) {
                
                BoardListModule.search();
                
            }
        });
    };
    
    // 수정 팝업
    this.boardUpdatePopup = function (boardId) {
        
        $a.popup({
            url : SKIAF.PATH.VIEW_BOARDS_UPDATE,
            iframe : false,
            data : {
                boardId : boardId
            },
            movable : false,
            width : 1000,
            height : 600,
            center : true,
            title : SKIAF.i18n.messages['bcm.board.board-update'],
            callback : function (result) {
                
                BoardListModule.search();
                
            }
        });
    };

});