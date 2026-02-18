/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01869
 * description : 유저 목록 검색 팝업 js
 */
"use strict";
 var SearchModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 멀티선택 여부(기본 true)
    var multiSelected = true;
    
    // 부모창 선택되어있는 ID 목록
    var parentSelectedIds = [];

    // 선택한 권한 ID 목록
    var selectedIds = [];
    
    // 선택한 권한 목록
    var selectedItems = [];
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };
    
    var gridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],          key : 'check',              align : 'center',           width : 50,         headerStyleclass : 'set',        selectorColumn : true,       resizing : false}, 
        {title : SKIAF.i18n.messages['bcm.usergroup.id'],           key : 'userGroupId',        align : 'center',           tooltip : false}, 
        {title : SKIAF.i18n.messages['bcm.usergroup.name'],         key : 'userGroupName',      align : 'center',           tooltip : false}, 
        {title : SKIAF.i18n.messages['bcm.usergroup.company'],      key : 'companyName',        align : 'center',           tooltip : false}, 
        {title : SKIAF.i18n.messages['bcm.usergroup.desc'],         key : 'userGroupDesc',      align : 'center',           tooltip : false},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],          key : 'useYn',              align : 'center',           tooltip : false,    render : function(value, data) {return data.useYn == true ? 'Y' : 'N';}}
    ];
    
    // 권한 목록 Grid
    var searchGridInit = {
            autoColumnIndex : true,
            fitTableWidth : true,
            headerRowHeight: 30,
            height: 394,
            useClassHovering : true,
            pager : true,
            paging : {
                perPage : paging.size,
                pagerCount : 10,
                pagerTotal : true,
                pagerSelect : true
            },
            defaultColumnMapping : {
                resizing : true
            },
            message : {
                nodata : SKIAF.i18n.messages['bcm.common.nodata']
            },
            columnMapping : gridColumns,        
            rowSelectOption: {
                clickSelect: true
            },
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
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        
//        console.info(param);
        
        SearchModule.setCompanyCode();
        
        //파라미터값 셋팅
        /*
        if(param['roleType'] != null && param['roleType'] != ''){
            $('#roleTypeSelect').val(param['roleType']);
        }else{
            $('#roleTypeSelect').val('ALL');
        }
        
        if($('#roleTypeSelect').val() != 'ALL'){
            $('#roleTypeSelect').setEnabled(false);
        }
        */
        
        
        
        if(param['multiSelected']){
            multiSelected = true;
            searchGridInit.rowSelectOption.singleSelect = false;
        }else{
            multiSelected = false;
            searchGridInit.rowSelectOption.singleSelect = true;
            searchGridInit.rowSelectOption.disableSelectByKey = true;
        }
        
        if(param['searchKeyword'] != null && param['searchKeyword'] != ''){    
            $('.searchbox-left').setData({keyword : param['searchKeyword']});
        }
        
        if(param['selectedIds'] != null && param['selectedIds'] != ''){
            parentSelectedIds = param['selectedIds'];
        }
        
        $('#searchGrid').alopexGrid(searchGridInit);
        
        // 이벤트 바인딩
        SearchModule.addEvent();
        
        // 그리드 데이터 바인딩
        SearchModule.search();
        
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /**
         * 취소 버튼 클릭 이벤트
         */
        $('#searchCancelBtn').on('click', function(e) {
            $a.close({
                type : 'cancel'
            });
        });
        
        /**
         * 그리드 체크박스 선택 이벤트
         */
        $('#searchGrid').on('dataSelectEnd', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            var selectObj = evObj.datalist[0];

            if(selectObj._state.selected) {
                //단일선택 모드인 경우 기존 선택되어있는 아이템 제거
                if(!multiSelected){
                    selectedIds = [];    
                    selectedItems = [];
                }
                // 체크박스 선택시 선택된 권한목록에 추가
                if($.inArray(selectObj.userGroupId, selectedIds) == -1) {
                    selectedIds.push(selectObj.userGroupId);    
                    selectedItems.push(selectObj);
                }
            } else {
                // 체크박스 해제시 선택된 권한목록에서 삭제
                if($.inArray(selectObj.userGroupId, selectedIds) != -1) {                    
                    selectedIds.splice(selectedIds.indexOf(selectObj.userGroupId), 1);
                    selectedItems.splice(selectedItems.indexOf(selectObj), 1);
                }
            }            
        });
        

        /**
         * 페이지 사이즈 변경 이벤트
         */
        $('#searchGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.size = evObj.perPage;

            paging.page = 1;
            SearchModule.search();
        });
        
        /**
         * 페이징 변경 이벤트
         */
        $('#searchGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            SearchModule.search();
        });
        
        /**
         * 검색 버튼 클릭
         */
        $('.skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            SearchModule.search();
        });

        /**
         * 검색 영역 엔터키
         */
        $('.skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            SearchModule.search();
        });
        
        /**
         * [확인] 버튼 클릭 이벤트
         */
        $('#seachSelectBtn').on('click', function(e) {

            // 페이지 변경 없이 확인버튼 눌렀을 경우 선택된 항목 전달
            var selectedList = $('#searchGrid').alopexGrid('dataGet', {_state : {selected : true}});
            for(var i = 0; i < selectedList.length; i++) {
                if($.inArray(selectedList[i].userGroupId, selectedIds) == -1) {
                    selectedIds.push(selectedList[i].userGroupId);    
                    selectedItems.push(selectedList[i]);
                }
            }
            
            if(selectedItems.length == 0){
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.usergroup.valid.id.select']);
                return;
            }
            
            $a.close({
                type : 'confirm',
                selectedItems : selectedItems
            }); // 데이터를 팝업을 띄운 윈도우의 callback에 전달

        });
        
        /**
         * 그리드 더블클릭
         */
        $('#SearchGrid').on('dblclick', '.bodycell', function(e){
            
            var evObj = AlopexGrid.parseEvent(e);
            
            if(!multiSelected){
                $a.close({
                    type : 'confirm',
                    selectedItems : [evObj.data]
                });
            }
            
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 검색
     */
    this.search = function() {
        
        // 선택되어있는 권한 목록에 추가
        var selectedList = $('#searchGrid').alopexGrid('dataGet', {_state : {selected : true}});
        for(var i = 0; i < selectedList.length; i++) {
            if($.inArray(selectedList[i].userGroupId, selectedIds) == -1) {
                selectedIds.push(selectedList[i].userGroupId);    
                selectedItems.push(selectedList[i]);
            }
        }

        // 파라메터 생성
        var params = {};

        var searchData = $('#searchBox').getData();
        var companyCode = searchData.optionSelected;
        var keyword = searchData.keyword;
        var isUnusedInclude = searchData.isunusedinclude.length > 0;

        params.page = paging.page - 1;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
//        if($('#roleTypeSelect').val() != 'ALL'){
//            params.roleType = $('#roleTypeSelect').val();
//        }

        if (keyword) {
            params.keyword = keyword;
        }
        
        if (companyCode) {
            params.companyCode = companyCode;
        }

        $a.request(SKIAF.PATH.USER_GROUPS, {
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
                
                var userGroupList = res.data;

                //부모창에서 선택된 권한 체크박스 Disabled
                if(userGroupList.length > 0 ) {
                    for(var i = 0; i < userGroupList.length; i++) {
                        if($.inArray(userGroupList[i].userGroupId, parentSelectedIds) != -1) {
                            userGroupList[i].action = 'parentSelected';
                        } else {
                            userGroupList[i].action = '';
                        }
                    }                
                }
                
                $('#searchGrid').alopexGrid('dataSet', userGroupList, pageinfo);
                
                
                //페이징시 선택되어있는 권한 체크박스 선택처리 ===> 'dataSet' 처리 후 해야한다..
                for(var i = 0; i < userGroupList.length; i++) {
                    if($.inArray(userGroupList[i].userGroupId, selectedIds) != -1) {
                        userGroupList[i]._state.selected = true;
                    }
                }
                
                $('#searchGrid').alopexGrid('rowSelect', {_state: {selected: true}}, true);
            }
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
                
                var company = result.data;
                
                $('#select').setData({
                    data : company
                });
            }
        });
    };

});