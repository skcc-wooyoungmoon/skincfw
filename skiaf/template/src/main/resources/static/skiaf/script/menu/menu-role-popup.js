/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.05 - in01869
 * description : 메뉴 권한설정 레이어팝업 js
 */
"use strict";
var MenuRoleLayerModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var menuRoleGridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        height: 300,
        defaultColumnMapping: {
            resizing: true
        },
        rowSelectOption : {
            clickSelect : true,
            disableSelectByKey : true
        },
        columnMapping: [
            {
                align : 'center',
                title: SKIAF.i18n.messages['bcm.common.select'],
                headerStyleclass:'set',
                width: '40px',
                selectorColumn:true
            },
            {
                title: SKIAF.i18n.messages['bcm.menu.role-id'],
                key: 'roleId',
                align: 'center',
                width : '120px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-name'],
                key: 'roleName',
                align: 'center',
                width : '140px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-desc'],
                key: 'roleDesc',
                align: 'center',
                width : '140px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-create-date'],
                key: 'updateDateTxt',
                align: 'center',
                width : '170px',
                tooltip: false,
                render : function( value, data, render, mapping) {
                    return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd HH:mm');
                }
            }],
        data: []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {
        
        var menu = param['menu'];

        $('#roleEditGrid').alopexGrid(menuRoleGridObj);

        MenuRoleLayerModule.addEvent();
        MenuRoleLayerModule.viewMenuRoleInfo(menu);
        MenuRoleLayerModule.viewMenuRoleGrid(menu.menuId);
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {
        
        // 메뉴 [권한추가] 버튼 클릭 이벤트
        $('#btnRoleAdd').on('click', function() {
            MenuRoleLayerModule.popupRoleSearch();
        });

        // 권한 수정 화면 - [취소]버튼 클릭 이벤트
        $('#btnRoleCancel').on('click', function(e) {       
            $a.close();     
        });

        // 권한 수정 화면 - 메뉴 권한 Grid 체크박스 선택 이벤트
        $('#roleEditGrid').on('dataSelectEnd', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            var dataObj = evObj.datalist[0];
            var row = dataObj._index.row;
            var selected = dataObj._state.selected;

            //체크되어있는 목록
            var addCnt = $('#roleEditGrid').alopexGrid('dataGet', {_state : {selected : true}, status : 'ADD'}).length;
            var editCnt = $('#roleEditGrid').alopexGrid('dataGet', {_state : {selected : true}, status : 'EDIT'}).length;
            var currentCnt = $('#roleEditGrid').alopexGrid('dataGet', {_state : {selected : true}, status : 'CURRENT'}).length;
            
            if(selected) {
              
                // 체크박스 선택시
                // ADD, EDIT 상태 중 DELETE인것 있으면 다시 ADD, EDIT로 변경
                if(dataObj.status != 'CURRENT') {
                    $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-blur');
                }
                
                // 체크된 추가되거나 수정된 row가 있을 경우
                if((addCnt + editCnt) > 0) {
                    // [권한 추가] [저장] 버튼 활성화
                    MenuRoleLayerModule.activateRoleButton(['btnRoleAdd', 'btnRoleSave']);
                } else {
                    // [삭제] 버튼 활성화
                    MenuRoleLayerModule.activateRoleButton(['btnRoleAdd', 'btnRoleRemove']);
                }
                
            } else {
                //체크박스 해지시
                // ADD, EDIT 상태는 DELETE로 변경
                if(dataObj.status != 'CURRENT') {
                    $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : row}}).addClass('highlight-blur');
                }
                
                // 체크된 추가되거나 수정된 row가 있을 경우
                if((addCnt + editCnt) > 0) {
                    // [권한 추가] [저장] 버튼 활성화
                    MenuRoleLayerModule.activateRoleButton(['btnRoleAdd', 'btnRoleSave']);
                } else {
                    if(currentCnt > 0) {
                        // [삭제] 버튼 활성화
                        MenuRoleLayerModule.activateRoleButton(['btnRoleAdd', 'btnRoleRemove']);
                    } else {
                        // [권한 추가] [저장] 버튼 활성화
                        MenuRoleLayerModule.activateRoleButton(['btnRoleAdd']);
                    }
                }
            }
        });
        
        // 권한 수정 화면 - 권한 수정 이벤트
        $('#roleEditGrid').on('dataEditEnd', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            var dataObj = evObj.datalist[0];
            var row = dataObj._index.row;
            var selected = dataObj._state.selected;

            $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-blur');
            $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-add');
            $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : row}}).addClass('highlight-edit');
            
        });
        
        // 권한 수정 화면 - 메뉴 권한 [저장] 버튼 클릭 이벤트
        $('#btnRoleSave').on('click', function(e) {
            var node = $('#menuTree').getSelectedNode();
            var menuId = node.data.menuId;
            MenuRoleLayerModule.saveMenuRole(menuId);
        });
        
        // 권한 수정 화면 - 메뉴 권한 [삭제] 버튼 클릭 이벤트
        $('#btnRoleRemove').on('click', function(e) {
            var node = $('#menuTree').getSelectedNode();
            var menuId = node.data.menuId;
            MenuRoleLayerModule.deleteMenuRole(menuId);
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 메뉴 권한 정보 view
     */
    this.viewMenuRoleInfo = function(menu) {

        var trByMenuType = '';
        if(menu.menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
            menu.menuTypeTxt = SKIAF.i18n.messages['bcm.menu.menu-type-program'];
            trByMenuType += '<th>'+ SKIAF.i18n.messages['bcm.menu.menu-name'] +'</th><td>'+ menu.menuName1 +'</td>';
            trByMenuType += '<th>'+ SKIAF.i18n.messages['bcm.menu.program-id'] +'</th><td>'+ menu.program.programId +'</td>';
        } else if(menu.menuType == SKIAF.ENUM.MENU_TYPE.URL) {
            menu.menuTypeTxt = SKIAF.i18n.messages['bcm.menu.menu-type-url'];
            trByMenuType += '<th>'+ SKIAF.i18n.messages['bcm.menu.menu-name'] +'</th><td>'+ menu.menuName1 +'</td>';
            trByMenuType += '<th>'+ SKIAF.i18n.messages['bcm.menu.menu-type-url'] +'</th><td>'+ menu.urlAddr +'</td>';
        }
        
        menu.trByMenuType = trByMenuType;
        
        $('#menuRoleInfo').setData(menu);
    };

    /**
     * 메뉴 권한목록 그리드 view
     */
    this.viewMenuRoleGrid = function(menuId) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_ROLE_MAP, menuId), {
            method : 'GET',
            success : function(res) {

                for(var i = 0; i<res.data.length; i++){
                    // 맵핑되어있던 메뉴 권한
                    res.data[i].status = 'CURRENT';
                }
                
                $('#roleEditGrid').alopexGrid('dataSet', res.data);

                // 메뉴 권한 [권한추가] 버튼만 활성화
                MenuRoleLayerModule.activateRoleButton(['btnRoleAdd']);

            }
        });
    };
    
    /**
     * 메뉴 권한 추가 레이어 팝업
     */
    this.popupRoleSearch = function() {
        
        // 그리드에 선택되어있는 권한 ID 목록
        var selectedRoleIds = [];

        var roleEditList = $('#roleEditGrid').alopexGrid('dataGet');        
        for(var i = 0; i<roleEditList.length; i++) {
            // 선택되어있는 권한 ID 추출
            selectedRoleIds.push(roleEditList[i].roleId);
        }

        $a.popup({
            url : SKIAF.PATH.VIEW_ROLES_SEARCH,
            title : SKIAF.i18n.messages['bcm.menu.role-select'],
            data : {
                roleType : 'MENU',
                searchKeyword : '',
                selectedRoleIds : selectedRoleIds,
                multiSelected : true
            },
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                
                // 레이어팝업에서 [확인] 버튼으로 팝업 close 했을 경우에만.
                if(data.type == 'confirm') {

                    // 권한 맵핑 추가
                    for(var i = 0; i<data.selectedRoles.length; i++) {
                        selectedRoleIds.push(data.selectedRoles[i].roleId);
                        data.selectedRoles[i].status = 'ADD';
                        data.selectedRoles[i].updateDate = null; //추가된 row의 updateDate는 권한의 updateDate이기 때문에 null 처리
                        // 로우 추가
                        $('#roleEditGrid').alopexGrid('dataAdd', data.selectedRoles[i]);                        
                    }
                    
                    var addList = $('#roleEditGrid').alopexGrid('dataGet', {status : 'ADD'});
                    for(var i = 0; i<addList.length; i++) {
                        $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : addList[i]._index.row}}).addClass('highlight-add');
                        if(!addList[i]._state.selected) {
                            $('#roleEditGrid').alopexGrid('rowElementGet',{_index : {row : addList[i]._index.row}}).addClass('highlight-blur');
                        }
                    }

                    // [권한추가] [저장] 버튼 활성화
                    MenuRoleLayerModule.activateRoleButton(['btnRoleAdd', 'btnRoleSave']);
                }
            }
        });
    };
    
    /**
     * 메뉴 권한 저장
     */
    this.saveMenuRole = function(menuId) {
        
        var saveRoleIds = [];
        //체크박스 선택되어있고 status가 'ADD'인 목록
        var addList = $('#roleEditGrid').alopexGrid('dataGet', {status : 'ADD', _state : {selected : true}});
        
        if(addList.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.valid.save-role-select']);
            return false;
        }
        
        for(var i = 0; i < addList.length; i++) {
            saveRoleIds.push(addList[i].roleId);
        }
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_ROLE_MAP, menuId), {
            method : 'POST',
            array : saveRoleIds,
            success : function(res) {
                if(res.data.length > 0) {
                    $a.close({
                        type : 'success',
                        menuId : menuId
                    });
                }           
            }
        });
    };
    
    /**
     * 메뉴 권한 삭제
     */
    this.deleteMenuRole = function(menuId) {
        
        var deleteRoleIds = [];
        //체크박스 선택되어있고 status가 'CURRENT'인 목록
        var deleteList = $('#roleEditGrid').alopexGrid('dataGet', {status : 'CURRENT', _state : {selected : true}});
        
        if(deleteList.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.valid.delete-role-select']);
            return false;
        }
        
        for(var i = 0; i<deleteList.length; i++) {
            deleteRoleIds.push(deleteList[i].roleId);
        }
        
        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'], function callback(){
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_ROLE_MAP, menuId), {
                method : 'DELETE',
                array : deleteRoleIds,
                success : function(res) {
                    $a.close({
                        type : 'success',
                        menuId : menuId
                    });     
                }
            });
        });

    };
    
    /**
     * 권한필드 버튼 활성화
     * btnIds : 활성화 하는 버튼 id 배열
     */
    this.activateRoleButton = function(btnIds) {

        var roleBtnIds = [ 'btnRoleAdd', 'btnRoleSave', 'btnRoleRemove' ];

        for (var i = 0; i < roleBtnIds.length; i++) {

            if ($.inArray(roleBtnIds[i], btnIds) != -1) {
                $('#' + roleBtnIds[i]).setEnabled(true);
            } else {
                $('#' + roleBtnIds[i]).setEnabled(false);
            }

        }
    };

});