/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01869
 * description : 권한 리스트
 */
"use strict";
var RoleListModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 페이지 정보
    var pageUrl = location.pathname;
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };

    // 권한 목록 Grid
    var roleGridObj = {
        height : 404,
        autoColumnIndex : true,
        fitTableWidth : true,
        useClassHovering : false,
        columnFixUpto : 2,
        pager : true,
        paging : { perPage : 10, pagerCount : 10, pagerSelect : true},
        defaultColumnMapping : { resizing : true },
        columnMapping : [
            {title : SKIAF.i18n.messages['bcm.common.select'],      key : 'check',      align : 'center', headerStyleclass : 'set',    resizing : false, selectorColumn : true, width: '50px', excludeFitWidth : true},
            {title : SKIAF.i18n.messages['bcm.role.id'],            key : 'roleId',     align : 'left',   tooltip : false,    width : '115px'}, 
            {title : SKIAF.i18n.messages['bcm.role.name'],          key : 'roleName',   align : 'left',   tooltip : false,    width : '150px'},
            {title : SKIAF.i18n.messages['bcm.role.type'],          key : 'roleType',   align : 'center', tooltip : false,    width : '110px'}, 
            {title : SKIAF.i18n.messages['bcm.role.desc'],          key : 'roleDesc',   align : 'left',   tooltip : true,     width : '200px'}, 
            {title : SKIAF.i18n.messages['bcm.common.use-yn'],      key : 'useYn',      align : 'center', tooltip : false,    width : '65px',   render : function(value, data) {return data.useYn == true ? 'Y' : 'N';}}, 
            {title : SKIAF.i18n.messages['bcm.common.update-by'],   key : 'updateBy',   align : 'center', tooltip : false,    width : '85px'}, 
            {title : SKIAF.i18n.messages['bcm.common.update-date'], key : 'updateDate', align : 'center', tooltip : false,    width : '135px',  render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}},
            {title : SKIAF.i18n.messages['bcm.common.create-by'],   key : 'createBy',   align : 'center', tooltip : false,    width : '85px'}, 
            {title : SKIAF.i18n.messages['bcm.common.create-date'], key : 'createDate', align : 'center', tooltip : false,    width : '135px',  render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}}
        ],
        rowSelectOption: {
            clickSelect: true,
            singleSelect : true,
            disableSelectByKey : true
        },
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        RoleListModule.setParameter();
        
        // 그리드 초기화
        $('#roleGrid').alopexGrid(roleGridObj);
        
        // 이벤트 바인딩
        RoleListModule.addEvent();
        
        // 그리드 데이터 바인딩
        RoleListModule.search();
        
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 취소 버튼 클릭 이벤트
        $('.skiaf-ui-centerbtn .Typeb').on('click', function(e) {
            $a.close({
                type : 'cancel'
            });
        });

        // 페이지 사이즈 변경 이벤트
        $('#roleGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.size = evObj.perPage;

            paging.page = 1;
            RoleListModule.search(true);
        });
        
        // 페이징 변경 이벤트
        $('#roleGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            RoleListModule.search(true);
        });
        
        // 검색 버튼 클릭
        $('.skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            RoleListModule.search(true);
        });

        // 검색 영역 엔터키
        $('.skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            RoleListModule.search(true);
        });
        
        // 권한등록
        $('#roleCreateBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES_CREATE,
                title : SKIAF.i18n.messages['bcm.role.create'],
                data : null,
                movable: true,
                iframe : false,
                width : 1000,
                height : 508,
                center : true,
                callback : function(data) {
                    
                    if (data != null) {
                        RoleListModule.search();
                    }
                }
            });
        });
        
        // 권한별 사용자관리 상세 이동
        $('#roleMapBtn').on('click', function(e) {
            var selectRoleItem = {};
            var dataList = $('#roleGrid').alopexGrid('dataGet', {
                _state : {
                    selected : true
                }
            });
            if (dataList && dataList.length >= 1) {
                selectRoleItem = dataList[0];
            }
            
            // 현재 Url 기억 
            Cookies.set(SKIAF.JS_CONSTANT.ROLE_PREVIOUS_URL, location.href);

            // 이동
            if (selectRoleItem.hasOwnProperty('roleId')) {
                window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES_DETAIL + '?roleId=' + selectRoleItem.roleId;
            } else {
                window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES_DETAIL;
            }
            return;
        });
        
        // 권한수정
        $('#roleEditBtn').on('click', function(e) {
            var selectRoleItem = {};
            var dataList = $('#roleGrid').alopexGrid('dataGet', {_state : {selected : true}});
            if (dataList && dataList.length < 1) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            selectRoleItem = dataList[0];
            $a.popup({
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES_EDIT,
                title : SKIAF.i18n.messages['bcm.role.edit'],
                data : selectRoleItem,
                movable: true,
                iframe : false,
                width : 1000,
                height : 447,
                center : true,
                callback : function(data) {
                    
                    if (data != null) {
                        RoleListModule.search();
                    }
                }
            });
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {

            RoleListModule.setParameter();
            
            // 브라우저 뒤로가기 후 최초 검색
            RoleListModule.search();
        }, false);
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 권한 검색
     */
    this.search = function(isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 파라메터 생성
        var params = {};

        var searchData = $('#searchBox').getData();
        var keyword = searchData.keyword;
        var isUnusedInclude = searchData.isunusedinclude.indexOf('roleListCheck') >= 0;
        
        params.page = paging.page;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        
        if($('#roleTypeSelect').val() != 'ALL'){
            params.roleType = $('#roleTypeSelect').val();
        }

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

        // 그리드 페이지에서 서버 페이지로 값 변환
        params.page = params.page - 1;

        $a.request(SKIAF.PATH.ROLES, {
            method : 'GET',
            data : params,
            success : function(res) {
                
                if (!res.data) {
                    return;
                }
                var pageinfo = {
                    dataLength : res.meta.totalCount,
                    current : params.page + 1,
                    perPage : params.size
                };
                
                var roleList = res.data;


                $('#roleGrid').alopexGrid('dataSet', roleList, pageinfo);
            }
        });

    };
    
    /**
     * 파라미터 셋팅
     */
    this.setParameter = function() {
        
        // 페이지 정보
        paging.page = SKIAF.util.getParameter('page') ? SKIAF.util.getParameter('page') : defaultPage;
        paging.size = SKIAF.util.getParameter('size') ? SKIAF.util.getParameter('size') : defaultSize;
        
        // 미사용 체크 여부
        var isUnusedInclude = SKIAF.util.getParameter('isUnusedInclude') == 'true' ? true : false;
        
        // 검색어
        var keyword = SKIAF.util.getParameter('keyword') ? SKIAF.util.getParameter('keyword') : '';
        
        $('#searchBox').setData({
            isunusedinclude: (isUnusedInclude ? ['roleListCheck'] : []),
            keyword: keyword
        });
        
        // 권한 타입
        var roleType = SKIAF.util.getParameter('roleType') ? SKIAF.util.getParameter('roleType') : 'ALL';
        $('#roleTypeSelect').val(roleType);
    };

});