/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01869
 * description : 권한 검색 레이어팝업 js
 */
"use strict";
var RoleSearchModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 멀티선택 여부(기본 true)
    var multiSelected = true;
    
    // 부모창 선택되어있는 권한 ID 목록
    var parentSelectedRoleIds = [];

    // 선택한 권한 ID 목록
    var selectedRoleIds = [];
    
    // 선택한 권한 목록
    var selectedRoles = [];
    
    // 페이징
    var defaultPage = 1;
    var defaultSize = 10;
    var paging = {
        page : defaultPage,            // 현재 페이지
        size : defaultSize,            // 페이지 사이즈
    };

    // 권한 목록 Grid
    var roleSearchGridObj = {
        height : 394,
        autoColumnIndex : true,
        fitTableWidth : true,
        useClassHovering : true,
        pager : true,
        paging : {
            perPage : 10,
            pagerCount : 10,
            pagerSelect : true
        },
        defaultColumnMapping : {
            resizing : true
        },
        columnMapping : [ 
            {title : SKIAF.i18n.messages['bcm.common.select'],      key : 'check',      align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true}, 
            {title : SKIAF.i18n.messages['bcm.role.type'],          key : 'roleType',   align : 'center', tooltip : false},
            {title : SKIAF.i18n.messages['bcm.role.id'],            key : 'roleId',     align : 'center', tooltip : false}, 
            {title : SKIAF.i18n.messages['bcm.role.name'],          key : 'roleName',   align : 'center', tooltip : false}, 
            {title : SKIAF.i18n.messages['bcm.common.use-yn'],      key : 'useYn',      align : 'center', tooltip : false, render : function(value, data) {return data.useYn == true ? 'Y' : 'N';}}, 
            {title : SKIAF.i18n.messages['bcm.common.update-date'], key : 'updateInfo', align : 'center', tooltip : false, render : function(value, data) {return data.updateBy + ' ' + SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}} 
        ],
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

        // 파라미터값 셋팅
        if (param['roleType'] != null && param['roleType'] != '') {
            $('#roleTypeSelect').val(param['roleType']);
        } else {
            $('#roleTypeSelect').val('ALL');
        }
        
        $('#roleTypeSelect').parents('.Divselect').refresh();
        
        if ($('#roleTypeSelect').val() != 'ALL') {
            $('#roleTypeSelect').parents('.Divselect').setEnabled(false);
        }

        if (!param['multiSelected']) {
            multiSelected = false;
            roleSearchGridObj.rowSelectOption.singleSelect = true;
            roleSearchGridObj.rowSelectOption.disableSelectByKey = true;
        }

        if (param['searchKeyword'] != null && param['searchKeyword'] != '') {
            $('.searchbox-left').setData({
                keyword : param['searchKeyword']
            });
        }

        if (param['selectedRoleIds'] != null && param['selectedRoleIds'] != '') {
            parentSelectedRoleIds = param['selectedRoleIds'];
        }
        
        $('#roleSearchGrid').alopexGrid(roleSearchGridObj);
        
        // 이벤트 바인딩
        RoleSearchModule.addEvent();
        
        // 그리드 데이터 바인딩
        RoleSearchModule.search();
        
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        /**
         * 취소 버튼 클릭 이벤트
         */
        $('#roleCancelBtn').on('click', function(e) {
            $a.close({
                type : 'cancel'
            });
        });
        
        /**
         * 그리드 체크박스 선택 이벤트
         */
        $('#roleSearchGrid').on('dataSelectEnd', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            var selectObj = evObj.datalist[0];

            if(selectObj._state.selected) {
                //단일선택 모드인 경우 기존 선택되어있는 아이템 제거
                if(!multiSelected){
                    selectedRoleIds = [];    
                    selectedRoles = [];
                }
                // 체크박스 선택시 선택된 권한목록에 추가
                if($.inArray(selectObj.roleId, selectedRoleIds) == -1) {
                    selectedRoleIds.push(selectObj.roleId);    
                    selectedRoles.push(selectObj);
                }
            } else {
                // 체크박스 해제시 선택된 권한목록에서 삭제
                if($.inArray(selectObj.roleId, selectedRoleIds) != -1) {                    
                    selectedRoleIds.splice(selectedRoleIds.indexOf(selectObj.roleId), 1);
                    selectedRoles.splice(selectedRoles.indexOf(selectObj), 1);
                }
            }            
        });
        

        /**
         * 페이지 사이즈 변경 이벤트
         */
        $('#roleSearchGrid').on('perPageChange', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.size = evObj.perPage;

            paging.page = 1;
            RoleSearchModule.search();
        });
        
        /**
         * 페이징 변경 이벤트
         */
        $('#roleSearchGrid').on('pageSet', function(e) {
            var evObj = AlopexGrid.parseEvent(e);

            paging.page = evObj.page;
            paging.size = evObj.pageinfo.perPage;
                    
            RoleSearchModule.search();
        });
        
        /**
         * 검색 버튼 클릭
         */
        $('.skiaf-ui-search button.search').on('click', function(e) {
            paging.page = 1;
            RoleSearchModule.search();
        });

        /**
         * 검색 영역 엔터키
         */
        $('.skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            paging.page = 1;
            RoleSearchModule.search();
        });
        
        /**
         * [확인] 버튼 클릭 이벤트
         */
        $('#roleSelectBtn').on('click', function(e) {

            // 페이지 변경 없이 확인버튼 눌렀을 경우 선택된 항목 전달
            var selectedList = $('#roleSearchGrid').alopexGrid('dataGet', {_state : {selected : true}});
            
            if (selectedList.length <= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            for(var i = 0; i < selectedList.length; i++) {
                if($.inArray(selectedList[i].roleId, selectedRoleIds) == -1) {
                    selectedRoleIds.push(selectedList[i].roleId);    
                    selectedRoles.push(selectedList[i]);
                }
            }
            
            $a.close({
                type : 'confirm',
                selectedRoles : selectedRoles
            }); // 데이터를 팝업을 띄운 윈도우의 callback에 전달

        });
        
        if(!multiSelected) {
            /**
             * 그리드 더블클릭
             */
            $('#roleSearchGrid').on('dblclick', '.bodycell', function(e){
                var evObj = AlopexGrid.parseEvent(e);
                $a.close({
                    type : 'confirm',
                    selectedRoles : [evObj.data]
                });
                
            });
        }
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 권한 검색
     */
    this.search = function() {
        
        // 선택되어있는 권한 목록에 추가
        var selectedList = $('#roleSearchGrid').alopexGrid('dataGet', {_state : {selected : true}});
        for(var i = 0; i < selectedList.length; i++) {
            if($.inArray(selectedList[i].roleId, selectedRoleIds) == -1) {
                selectedRoleIds.push(selectedList[i].roleId);    
                selectedRoles.push(selectedList[i]);
            }
        }

        // 파라메터 생성
        var params = {};

        var searchData = $('#searchBox').getData();
        var keyword = searchData.keyword;
        var isUnusedInclude = searchData.isunusedinclude.length > 0;

        params.page = paging.page - 1;
        params.size = paging.size;
        params.isUnusedInclude = isUnusedInclude;
        if($('#roleTypeSelect').val() != 'ALL'){
            params.roleType = $('#roleTypeSelect').val();
        }


        if (keyword) {
            params.keyword = keyword;
        }

        $a.request(SKIAF.PATH.ROLES, {
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
                
                var roleList = res.data;

                //부모창에서 선택된 권한 체크박스 Disabled
                if(parentSelectedRoleIds.length > 0) {
                    for(var i = 0; i < roleList.length; i++) {
                        if($.inArray(roleList[i].roleId, parentSelectedRoleIds) != -1) {
                            roleList[i].action = 'parentSelected';
                        } else {
                            roleList[i].action = '';
                        }
                    }                
                }
                
                $('#roleSearchGrid').alopexGrid('dataSet', roleList, pageinfo);
                
                
                //페이징시 선택되어있는 권한 체크박스 선택처리 ===> 'dataSet' 처리 후 해야한다..
                for(var i = 0; i < roleList.length; i++) {
                    if($.inArray(roleList[i].roleId, selectedRoleIds) != -1) {
                        roleList[i]._state.selected = true;
                    }
                }
                
                $('#roleSearchGrid').alopexGrid('rowSelect', {_state: {selected: true}}, true);
            },

            fail : function(res) {
                // 통신은 성공적으로 이루어 졌으나, 서버오류가 발생한 경우 호출되는 콜백함수
            },
            error : function(errObject) {
                // 통신이 실패한 경우 호출되는 콜백함수
            }
        });

    };

});