/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01876
 * description : 사용자 검색 레이어팝업 js
 */
"use strict";
var UserSearchPopupModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 멀티선택 여부(기본 true)
    var multiSelected = true;
    
    // 부모창 선택되어있는 유저 ID 목록
    var parentSelectedUserIds = [];
    
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };
    
    var companyCode = null;
    
    var selectedUserIds = [];
    var selectedUsers = [];
    

    var gridColumns = [{
            title : SKIAF.i18n.messages['bcm.common.select'],
            align : 'center',
            key : 'check',
            headerStyleclass : 'set',
            width : '50px',
            selectorColumn : true
        }, {
            title : SKIAF.i18n.messages['bcm.user.login-id'],
            key : 'loginId',
            align : 'center',
        }, {
            title : SKIAF.i18n.messages['bcm.user.user-name'],
            key : 'userName',
            align : 'center'
        }, {
            title : SKIAF.i18n.messages['bcm.user.department-name'],
            key : 'departmentName',
            align : 'center'
        }, {
            title : SKIAF.i18n.messages['bcm.user.email'],
            key : 'email',
            align : 'center'
        }, {
            title : SKIAF.i18n.messages['bcm.common.use-yn'],
            key : 'useYn',
            align : 'center',
            render : function(value, data) {
                return value ? 'Y' : 'N';
            }
        }
    ];
    
    var listGridObj = {
            autoColumnIndex : true,
            fitTableWidth : true,
            useClassHovering: true,
            headerRowHeight: 30,
            height: 390,
            pager : true,
            paging : {perPage : paging.size, pagerCount : 10, pagerSelect : true},
            defaultColumnMapping : {resizing : true},
            rowSelectOption : {clickSelect : true},
            columnMapping : gridColumns,
            rowOption: {
                // 메뉴 row 스타일
                styleclass : function(data, rowOption){
                    if(data.action == 'parentSelected'){
                         return 'highlight-blur';
                    }
                },
                // row 선택 Disabled
                allowSelect: function(data) {
                    return (data.action == 'parentSelected') ? false : true;
                }
            },
            data : []
        };
    
    // 서버
    var isAjaxAvailable = true;        // Ajax 사용가능 여부
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        companyCode = param.companyCode;
        
        if(param.searchText != null && param.searchText != ''){
            $('#defaultSearchText').val(param.searchText);
        }
        
        if(param['multiSelected']){
            multiSelected = true;
            listGridObj.rowSelectOption.singleSelect = false;
        } else {
            multiSelected = false;
            listGridObj.rowSelectOption.singleSelect = true;
            listGridObj.rowSelectOption.disableSelectByKey = true;
        }

        if(param['selectedUserIds'] != null && param['selectedUserIds'] != ''){
            parentSelectedUserIds = param['selectedUserIds'];
        }
        
        $('#listGrid').alopexGrid(listGridObj);
        
        UserSearchPopupModule.addEvent();
        
        UserSearchPopupModule.search();
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
            paging.page = 1;
            UserSearchPopupModule.search();
        });
        
        /**
         * 페이징 변경 이벤트
         */
        $('#listGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            
            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
            
            UserSearchPopupModule.search();
        });
        
        /**
         * 검색 버튼 클릭
         */
        $('#defaultSearchBtn').on('click', function(e) {
            paging.page = 1;
            UserSearchPopupModule.search();
        });
    
        /**
         * 검색 영역 엔터키
         */
        $('#defaultSearchText').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            UserSearchPopupModule.search();
        });
        
        /**
         * 그리드 아이템 선택
         */
        $('#listGrid').on('dataSelectEnd', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            var selectObj = evObj.datalist[0];
            
            if(selectObj._state.selected) {
                
                //단일선택 모드인 경우 기존 선택되어있는 아이템 제거
                if(!multiSelected){
                    selectedUserIds = [];    
                    selectedUsers = [];
                }
                // 체크박스 선택시 선택된 목록에 추가
                if($.inArray(selectObj.userId, selectedUserIds) == -1) {
                    selectedUserIds.push(selectObj.userId);
                    selectedUsers.push(selectObj);
                }
            } else {
                if($.inArray(selectObj.userId, selectedUserIds) != -1) {
                    selectedUserIds.splice(selectedUserIds.indexOf(selectObj.userId), 1);
                    selectedUsers.splice(selectedUsers.indexOf(selectObj), 1);
                }
            }
        });
        
        /**
         * 그리드 더블클릭
         */
        $('#listGrid').on('dblclick', '.bodycell', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            $a.close({
                type : 'confirm',
                selectedUsers : [evObj.data]
            });
            
        });
        
        /**
         * 선택 확인
         */
        $('#userSelectBtn').on('click', function(e) {
            
            
            // 페이지 변경 없이 확인버튼 눌렀을 경우 선택된 항목 전달
            var selectedList = $('#listGrid').alopexGrid('dataGet', {_state : {selected : true}});
            for(var i = 0; i < selectedList.length; i++) {
                if($.inArray(selectedList[i].userId, selectedUserIds) == -1) {
                    selectedUserIds.push(selectedList[i].userId);
                    selectedUsers.push(selectedList[i]);
                }
            }
            
            if(selectedUsers.length == 0){
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.usergroup.valid.id.select']);
                return;
            }
            
            $a.close({
                type : 'confirm',
                selectedUsers : selectedUsers
            }); // 데이터를 팝업을 띄운 윈도우의 callback에 전달
            
        });
        
        /**
         * 취소
         */
        $('#userCancelBtn').on('click', function(e) {
            $a.close();
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.search = function () {
        
        // 파라메터 생성
        var params = {};
        var inputData = $('.skiaf-ui-search').getData();
        var keyword = $('#defaultSearchText').val();
        
        params.page = paging.page - 1;
        params.size = paging.size;
                
        if (keyword) {
            params.keyword = keyword;
        }
        
        if (companyCode) {
            params.companyCode = companyCode;
        }
        
        // ajax 통신
        $a.request(SKIAF.PATH.USERS, {
            method : 'GET',
            data : params,
            success : function(res) {
                
                if (!res.data) {
                    return;
                }
                var pageinfo = {
                    dataLength : res.meta.totalCount,
                    current : paging.page,
                    perPage : paging.size
                };
                
                var userList = res.data;

                for (var i = 0; i < userList.length; i++) {
                    if ($.inArray(userList[i].userId, parentSelectedUserIds) != -1) {
                        userList[i].action = 'parentSelected';
                    } else {
                        userList[i].action = '';
                    }
                }
            
                $('#listGrid').alopexGrid('dataSet', userList, pageinfo);
                

                for (var i = 0; i < userList.length; i++) {
                    if ($.inArray(userList[i].userId, selectedUserIds) != -1) {
                        userList[i]._state.selected = true;
                    }
                }
                
                $('#listGrid').alopexGrid('rowSelect', {_state: {selected: true}}, true);
            }
        });
    };
});