/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01871
 * description : 사용자그룹 리스트
 */
"use strict";
var UserGroupListModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var pageUrl = location.pathname;
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };
    
    // 그리드 컬럼
    var userGroupGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],          key : 'check',          align : 'center',   width : 50,         selectorColumn : true, headerStyleclass:'set',     resizing : false},
        {title : SKIAF.i18n.messages['bcm.usergroup.id'],           key : 'userGroupId',    align : 'left',     width : 150,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.usergroup.name'],         key : 'userGroupName',  align : 'left',     width : 150,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.usergroup.desc'],         key : 'userGroupDesc',  align : 'left',     width : 300,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.usergroup.company'],      key : 'companyName',    align : 'center',   width : 200,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.usergroup.usercount'],    key : 'userCount',      align : 'center',   width : 65,         tooltip : true,        render : {type : "link"}},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],          key : 'useYn',          align : 'center',   width : 80,         tooltip : true,        render : function(value, data) { return value ? 'Y' : 'N'}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],       key : 'updateBy',       align : 'center',   width : 85,         tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.update-date'],     key : 'updateDate',     align : 'center',   width : 150,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.create-by'],       key : 'createBy',       align : 'center',   width : 85,         tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.create-date'],     key : 'createDate',     align : 'center',   width : 150,        tooltip : true}
        
    ];
    
    // 서버
    var isAjaxAvailable = true;        // Ajax 사용가능 여부
    
    // 회사코드
    var company = null;
    
    // 그리드 초기화 
    var userGroupGridObj = {
        columnFixUpto : 1,
        headerRowHeight: 30,
        height : 404, /* 전체 height */
        numberingColumnFromZero: false,
        autoColumnIndex : true,
        fitTableWidth : true,
        rowOption : {
            defaultHeight : 30
        },
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        defaultColumnMapping: {
            resizing: true
        },
        renderMapping : {
            'link' : {
                renderer : function(value, data, render, mapping) {
                    return '<a href = "' + SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USER_GROUPS_DETAIL, data.userGroupId) + '">' + data.userCount + '</a>';
                }
            }
        },
        pager : true,
        paging : {
            perPage : paging.size,
            pagerCount : 10,
            pagerTotal : true,
            pagerSelect : true
        },
        columnMapping : userGroupGridColumns,
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        
        UserGroupListModule.setParameter();
        UserGroupListModule.setCompanyCode();
        UserGroupListModule.addEvent();
       
        $('#userGroupGrid').alopexGrid(userGroupGridObj);
        
        // 최초 검색
        UserGroupListModule.search();
    
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {

        // 등록 팝업 버튼
        $('#btnUserGroupAdd').on('click', function(e) {
        
            UserGroupListModule.userGroupCreatePopup();
        
        });
    
        // 수정 팝업 버튼
        $('#btnUserGroupUpdate').on('click', function() {

            var dataList = $('#userGroupGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if (dataList.length == 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.usergroup.valid.id.select']);
                return;
                
            } else {
                
                var userGroupId = dataList[0].userGroupId;
                                
                UserGroupListModule.userGroupUpdatePopup(userGroupId);
                
            }
        });
        
        // 그룹 사용자 관리 버튼
        $('#btnUserGroupDetail').on('click', function(e) {
            
            var dataList = $('#userGroupGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if (dataList.length == 0) {
                
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.usergroup.valid.id.select']);
                return;
                
            } else {

                var userGroupId = dataList[0].userGroupId;
                UserGroupListModule.userGroupDetail(userGroupId);
            
            }
        
        });
    
        // 페이지 변경
        $('#userGroupGrid').on('perPageChange', function(e) {
            if (!isAjaxAvailable) {
                return;
            }
            var evObj = AlopexGrid.parseEvent(e);
            
            paging.size = evObj.perPage;
            paging.page = 1;
            UserGroupListModule.search(true);
        });
        
        // 목록 갯수 변경
        $('#userGroupGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
    
            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            UserGroupListModule.search(true);
        });
        
        // 검색 버튼 클릭
        $('#defaultSearchBtn').on('click', function(e) {
            paging.page = 1;
            UserGroupListModule.search(true);
        });
        
        // 검색 버튼 엔터
        $('#defaultSearchText').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            UserGroupListModule.search(true);
        });
        
        // 브라우저 뒤로가기 
        window.addEventListener('popstate', function(e) {
            UserGroupListModule.setParameter();
            UserGroupListModule.search();
        }, false);
    
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 조회
     */
    this.search = function (isHistory){
        
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        
        // ajax 사용중인지 확인
        if (!isAjaxAvailable) {
            return;
        }
        isAjaxAvailable = false;
        
        // 파라메터 생성
        var params = {};
        var inputData = $('.skiaf-ui-contents').getData();
        
        var companyCode = inputData.optionSelected;
        var keyword = inputData.keyword;
        var isUnusedInclude = inputData.isunusedinclude.indexOf('userGroupListCheck') >= 0;
        
        params.page = paging.page;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        
        if (keyword) {
            params.keyword = keyword;
        }
        
        if (companyCode) {
            params.companyCode = companyCode;
        }
        
        if(isHistory) {
            var queryString = '?' + SKIAF.util.createParameter(params);
            
            history.pushState(null, '', pageUrl + queryString);
        }
        
        params.page = paging.page - 1;
        
        // ajax 통신
        $a.request(SKIAF.PATH.USER_GROUPS, {
            method : 'GET',
            data : params,
            success : function(result) {
                
                if (!result.data) {
                    return;
                }
                var pageinfo = {
                    dataLength : result.meta.totalCount,
                    current : paging.page,
                    perPage : paging.size
                };
                
                var data = result.data;

                $('#userGroupGrid').alopexGrid('dataSet', data, pageinfo);
            },
            after : function() {
                isAjaxAvailable = true;
            }
        });
    };
    
    /**
     * 파라미터 세팅
     */
    this.setParameter = function () {
               
        //페이지 정보
        paging.page = SKIAF.util.getParameter('page') ? SKIAF.util.getParameter('page') : defaultPage;
        paging.size = SKIAF.util.getParameter('size') ? SKIAF.util.getParameter('size') : defaultSize;

     // 미사용 체크 여부
        var isUnusedInclude = (SKIAF.util.getParameter('isUnusedInclude') == 'true') ? true : false;
        
        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') ? SKIAF.util.getParameter('keyword') : '';
        
        // 파라메터 값 매칭
        $('.skiaf-ui-contents').setData({
            isunusedinclude: (isUnusedInclude ? ['userGroupListCheck'] : []),
            keyword : keyword
        });

    };
    
    /**
     * 회사코드로 회사이름 가져오기 
     */
    this.setCompanyCode = function (){
        
        //회사그룹코드
        var companyCode = SKIAF.CONSTATNT.CODE_GROUP_COMPANY_ID;
                
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODES_DETAIL_LANG, companyCode), {
            method : 'GET',
            async : false,
            success : function(result) {

                var defaultCode = []
                defaultCode.codeId = '';
                defaultCode.codeName = SKIAF.i18n.messages['bcm.common.all'];
                
                result.data.unshift(defaultCode);
                
                company = result.data;
                
                $('#select').setData({
                    data : company
                });
            }
        });
    };
    
    /**
     * 등록팝업
     */
    this.userGroupCreatePopup = function() {
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS_CREATE,
            iframe : false,
            movable : true,
            width : 1000,
            height : 360,
            title : SKIAF.i18n.messages['bcm.usergroup.create.title'],
            center : true,
            callback : function(result) {
                
            }
        });
    };
    
    /**
     * 수정팝업
     */
    this.userGroupUpdatePopup = function(userGroupId) {
         
        var param = [];
        param.userGroupId = userGroupId;
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS_UPDATE,
            data : {
                userGroupId : userGroupId   
            },
            iframe : false,
            movable : true,
            width : 1000,
            height : 380,
            title : SKIAF.i18n.messages['bcm.usergroup.update.title'],
            center : true,
            callback : function(result) {
                
            }
        });
    };
    
    /**
     * 그룹 사용자 관리
     */
    this.userGroupDetail = function(userGroupId) {
        
        Cookies.set(SKIAF.JS_CONSTANT.USERGROUP_PREVIOUS_URL, location.href);
        
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USER_GROUPS_DETAIL, userGroupId);
        return;
    };

});