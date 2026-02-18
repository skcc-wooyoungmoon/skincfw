/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.11 - in01943
 * description : 게시글 리스트
 */
 var ArticleListModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    // URL 파라메타로 받음
    var initGetBoardId = SKIAF.util.getPathVariable(3);
     
    // 페이지 정보
    var pageUrl = location.pathname;
           
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };

    // 게시글 목록 Grid
    var articleGridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        headerRowHeight: 30, 
        message : { nodata : SKIAF.i18n.messages['bcm.common.nodata'] },
        height:404,
        pager : true,
        paging: {perPage : 10, pagerCount: paging.size, pagerSelect : true },
        defaultColumnMapping: { resizing: true },
        rowSelectOption: {clickSelect: true, singleSelect : true},
        readonlyRender: false,
        columnMapping : [
            {title : SKIAF.i18n.messages['bcm.common.no'],          key : 'articleId',     align : 'center', headerStyleclass : 'set',    resizing : false,    width : '50px', selectorColumn : false, inlineStyle: {'line-height' : '30px'}},
            {title : SKIAF.i18n.messages['bcm.common.title'],       key : 'articleTitle',  align : 'left',   tooltip : false,    width : '470px', 
                render : function(value, data) {
                                        
                    var today = new Date();
                    var createDate = new Date(data.createDate);
                    var diff = (today.getTime() - createDate.getTime()) / (1000*60*60); 

                    var returnTitle = '<a class="boardlist" onclick="javascript:ArticleListModule.callArticleDetail(' + data.articleId + ');">' + data.articleTitle  +'</a>';

                    if(data.emgcYn == true) {
                        return '<span class="Label urgent">' + SKIAF.i18n.messages['bcm.board.emergency'] + '</span>' + returnTitle; //긴급   <!-- 0928 수정 <a class="boardlist" href="#"> 추가 -->
                    } else if (diff < 24){
                        return '<span class="Label new">' + SKIAF.i18n.messages['bcm.board.new'] + '</span>' + returnTitle; //new   <!-- 0928 수정 <a class="boardlist" href="#"> 추가 -->            
                    } else {
                        return returnTitle;
                    }
                }
            },
            {title : SKIAF.i18n.messages['bcm.common.attachment'],  key : 'attachCount',   align : 'center', tooltip : false,    width : '60px'},
            {title : SKIAF.i18n.messages['bcm.board.comment'],      key : 'commentCount',  align : 'center', tooltip : false,    width : '60px'}, 
            {title : SKIAF.i18n.messages['bcm.common.views'],       key : 'hitCnt',        align : 'center', tooltip : false,    width : '60px'}, 
            {title : SKIAF.i18n.messages['bcm.common.create-by'],   key : 'createBy',      align : 'center', tooltip : false,    width : '90px'}, 
            {title : SKIAF.i18n.messages['bcm.common.create-date'], key : 'createDate',    align : 'center', tooltip : false,    width : '150px', render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}}
        ],
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {

        
        ArticleListModule.setParameter();
        
        // 그리드 초기화
        $('#articleGrid').alopexGrid(articleGridObj);
        
        // 이벤트 바인딩
        ArticleListModule.addEvent();
        
        // 그리드 데이터 바인딩
        ArticleListModule.search();
        
    };    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        // 페이지 사이즈 변경 이벤트
        $('#articleGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.size = evObj.perPage;

            paging.page = 1;
            ArticleListModule.search(true);
        });
        
        // 페이징 변경 이벤트
        $('#articleGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            ArticleListModule.search(true);
        });
        
        // 검색 버튼 클릭
        $('.skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            ArticleListModule.search(true);
        });

        // 검색 영역 엔터키
        $('.skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            ArticleListModule.search(true);
        });
        
        // 게시글등록
        $('#articleCreateBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ARTICLES_CREATE_POPUP, initGetBoardId), 
                title : $('#boardName').text(),
                data : {
                    boardId: initGetBoardId
                },
                iframe : false,
                width : 1000,
                height : 685,
                center : true,
                callback : function(data) {
                    SKIAF.console.info("call back ok :: data :: ", data); 
                    
                    if (data != null) {
                        ArticleListModule.search();
                    }
                }
            });
        });

        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {

            ArticleListModule.setParameter();
            
            // 브라우저 뒤로가기 후 최초 검색
            ArticleListModule.search();
        }, false);
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 게시글 검색
     */
    this.search = function(isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 파라메터 생성
        var params = {};

        var searchData = $('#searchBox').getData();
        var keyword = searchData.keyword;
        
        var termUseYn = null;
        
        // 게시판 정보 가져오기
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.BOARDS_DETAIL, initGetBoardId), {
            method : 'GET',
            async : false,
            success : function(result){
                if (!result.data) {
                    return;
                }

                termUseYn = result.data.termUseYn;

            }
        })

        params.page = paging.page;
        params.size = paging.size;
        params.termUseYn = termUseYn;

        params.boardId = initGetBoardId;
        if (keyword) {
            params.keyword = keyword;
        }
        
        // 히스토리에 저장
        if (isHistory) {

            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);
            
            // 히스토리 푸쉬
            history.pushState(null, '', pageUrl + queryString);
        }

        params.page = params.page - 1;

        $a.request(SKIAF.PATH.ARTICLES, {
            method : 'GET',
            data : params,
            success : function(res) {
                SKIAF.console.info('select :: res ::', res);
                
                if (!res.data) {
                    return;
                }
                
//                for(var i = 0; i < res.data.length; i++) {
//                	var articleEndDtm = res.data[i].articleEndDtm;
//                	
//                	if(articleEndDtm < today){
//                		res.data.splice(i, 1);
//                	}
//                	
//                }
                
                var pageinfo = {
                    dataLength : res.meta.totalCount,
                    current : params.page + 1,
                    perPage : params.size
                };
                
                $('#articleGrid').alopexGrid('dataSet', res.data, pageinfo);
            }
        });

    };

    /**
     * 게시판 상세
     */
    this.callArticleDetail = function(articleId) {
        var searchData = $('#searchBox').getData();
        var boardType = searchData.boardType;

        if (initGetBoardId && articleId && boardType) {
            // main type
            if (boardType == "M") {
                window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_BOARD_ARTICLES_DETAIL, initGetBoardId, articleId);
                return;
            } else {
                $a.popup({
                    url : SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ARTICLES_UPDATE_POPUP_DETAIL, initGetBoardId, articleId), 
                    title : $('#boardName').text(),
                    data : {
                        boardId: initGetBoardId,
                        articleId: articleId
                    }, 
                    iframe : false,
                    width : 1000,
                    height : 685,
                    center : true,
                    callback : function(data) {
                        SKIAF.console.info("callArticleDetail :: ", data);
                        ArticleListModule.search();
                    }
                });
            } 
        }
    };

    /**
     * 파라미터 셋팅
     */
    this.setParameter = function() {
        
        // 페이지 정보
        paging.page = SKIAF.util.getParameter('page') ? SKIAF.util.getParameter('page') : defaultPage;
        paging.size = SKIAF.util.getParameter('size') ? SKIAF.util.getParameter('size') : defaultSize;

        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') ? SKIAF.util.getParameter('keyword') : '';
        
        $('#searchBox').setData({
            keyword: keyword
        });
    };
    
    this.boardInfo = function() {
    	
    	
    	
    };

});