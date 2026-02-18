/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.17 - in01876
 * description : 권한별 사용자관리
 */
"use strict";
var RoleMapModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 사용자그룹 그리드
    var userGroupMapGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],           key : 'check',          align : 'center',     headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.role.user.group.id'],      key : 'userGroupId',    align : 'center',     tooltip : false, width : '115px'},
        {title : SKIAF.i18n.messages['bcm.role.user.group.name'],    key : 'userGroupName',  align : 'left',     tooltip : true, width : '115px'},
        {title : SKIAF.i18n.messages['bcm.role.user.group.company'], key : 'companyName',    align : 'left',     tooltip : true, width : '100px'},
        {title : SKIAF.i18n.messages['bcm.role.user.group.desc'],    key : 'userGroupDesc',  align : 'left',     tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],           key : 'roleUseYn',      align : 'center',     tooltip : false,  width : '90px',
            render : function(value, data) { return value ? 'Y' : 'N'; },
            editable: {type: 'radio', rule: [{value: true, text: 'Y'}, {value: false, text: 'N'}]},
            value: function (value, data) {
                if (typeof value === 'undefined') {
                    return true;
                }
                if (value === 'true') {
                    return true;
                } else if (value === 'false') {
                    return false;
                }
                return value;
            }
        },
        {title : SKIAF.i18n.messages['bcm.role.begin'],              key : 'roleBeginDt',    align : 'center',    tooltip : true,   width : '120px', render:  {type : 'dateinput'}, editable: {type: 'dateinput'}},
        {title : SKIAF.i18n.messages['bcm.role.end'],                key : 'roleEndDt',      align : 'center',    tooltip : true,   width : '120px', render:  {type : 'dateinput'}, editable: {type: 'dateinput'}}
        
    ];
    
    
    //사용자 그리드
    var userMapGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],               key : 'check',          align : 'center',     headerStyleclass:'set', selectorColumn : true, resizing : false, width: '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.role.user.person.id'],       key : 'loginId',        align : 'center',     tooltip : true},
        {title : SKIAF.i18n.messages['bcm.role.user.person.name'],       key : 'userName',          align : 'center',     tooltip : true},
        {title : SKIAF.i18n.messages['bcm.role.user.person.company'],             key : 'companyName',      align : 'center',     tooltip : true},
        {title : SKIAF.i18n.messages['bcm.role.user.person.dept'],               key : 'departmentName', align : 'center',     tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],           key : 'roleUseYn',            align : 'center',     tooltip : false, 
            render : function(value, data) { return value ? 'Y' : 'N'; },
            editable: {type: 'radio', rule: [{value: true, text: 'Y'}, {value: false, text: 'N'}]},
            value: function (value, data) {
                if (typeof value === 'undefined') {
                    return true;
                }
                if (value === 'true') {
                    return true;
                } else if (value === 'false') {
                    return false;
                }
                return value;
            }},
        {title : SKIAF.i18n.messages['bcm.role.begin'],            key : 'roleBeginDt',    align : 'center',    tooltip : true,  render:  {type : 'dateinput'}, editable: {type: 'dateinput'}},
        {title : SKIAF.i18n.messages['bcm.role.end'],            key : 'roleEndDt',        align : 'center',    tooltip : true,  render:  {type : 'dateinput'}, editable: {type: 'dateinput'}}
        
    ];
    
    // 달력 팝업 타켓
    var $datePicker = null;

    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        
        //userGroupMapEidtGrid 초기화
        var userGroupGrid = RoleMapModule.getRoleMapGridObj();
        userGroupGrid.columnMapping = userGroupMapGridColumns;
        $('#userGroupMapEditGrid').alopexGrid(userGroupGrid);
        
        //userMapEidtGrid 초기화
        var userGrid = RoleMapModule.getRoleMapGridObj();
        userGrid.columnMapping = userMapGridColumns;
        $('#userMapEditGrid').alopexGrid(userGrid);
    
        
        RoleMapModule.addEvent();
        
        var roleId = SKIAF.util.getParameter('roleId') ? SKIAF.util.getParameter('roleId') : '';
        $('#selectedRoleInfo').setData({
            roleId : roleId
        });
        if (roleId) {
            RoleMapModule.getRoleInfo(false);
        }
        
    };
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 이벤트 등록
     */
    this.addEvent = function() {
        
        
        //권한선택 팝업버튼
        $('#roleSearchBtn').on('click', function(){
            RoleMapModule.popupRoleSearch();
        });
        
        //tab 선택시 그리드 초기화.
        $('#tab1').on('click', function(){
            $('#userGroupMapEditGrid').alopexGrid();
        });
        
        $('#tab2').on('click', function(){
            $('#userMapEditGrid').alopexGrid();
        });
        
        
        
        //==============================================USERGroup Mapping=============================================
        
        /**
         * userGroup 추가 팝업
         */
        $('#addUserGroupBtn').on('click', function(){
            var roleId = $('#selectedRoleInfo').getData().roleId;
            if (!roleId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            RoleMapModule.popupUserGroupSearch();
        });

        
        
        /**
         * roleMapUserGroup 저장버튼
         */
        $('#saveRoleUserGroupMapBtn').on('click', function(){
            
            var roleId = $('#selectedRoleInfo').getData().roleId;
            if (!roleId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            var userGroupList = $('#userGroupMapEditGrid').alopexGrid('dataGet', {_state : {selected : true}});
            for (var index in userGroupList) {
                var userGroup = userGroupList[index];
                if (!userGroup) {
                    continue;
                }
                
                // dash(-) 제거
                if (userGroup.roleBeginDt) {
                    userGroup.roleBeginDt = userGroup.roleBeginDt.split('-').join('');
                }
                if (userGroup.roleEndDt) {
                    userGroup.roleEndDt = userGroup.roleEndDt.split('-').join('');
                }
                if (parseInt(userGroup.roleBeginDt) > parseInt(userGroup.roleEndDt)) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.date-period']);
                    return;
                }
            }

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ROLES_MAP_USERGROUPS, roleId), {
                method : 'POST',
                array : userGroupList,
                success : function(res) {
                    //새로고침
                    RoleMapModule.getRoleInfo(false);
                }
            });
        });
        
        
        //==============================================USER Mapping=============================================
        
        
        /**
         * user 추가 팝업
         */
        $('#addUserBtn').on('click', function(){
            var roleId = $('#selectedRoleInfo').getData().roleId;
            if (!roleId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            RoleMapModule.popupUserSearch();
        });
        
        
        
        /**
         * userMapUser 저장버튼
         */
        $('#saveRoleUserMapBtn').on('click', function(){
            
            var roleId = $('#selectedRoleInfo').getData().roleId;
            if (!roleId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.select']);
                return;
            }
            var userList = $('#userMapEditGrid').alopexGrid('dataGet', {_state : {selected : true}});
            for (var index in userList) {
                var user = userList[index];
                if (!user) {
                    continue;
                }
                
                // dash(-) 제거
                if (user.roleBeginDt) {
                    user.roleBeginDt = user.roleBeginDt.split('-').join('');
                }
                if (user.roleEndDt) {
                    user.roleEndDt = user.roleEndDt.split('-').join('');
                }
                if (parseInt(user.roleBeginDt) > parseInt(user.roleEndDt)) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.date-period']);
                    return;
                }
            }
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ROLES_MAP_USERS, roleId), {
                method : 'POST',
                array : userList,
                success : function(res) {
                    RoleMapModule.getRoleInfo(false);
                }
            });
        });

        
        //==============================================공통 처리=============================================
        
        // 편집 그리드 이벤트 처리
        $('#userGroupMapEditGrid, #userMapEditGrid').on('click', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            if (!evObj.data) {
                return;
            }
            var index = evObj.data._index;

            var key = evObj.mapping.key;

            var $cell = evObj.$cell;
            var changeData = {};
            var $target = $(e.target);
            var isInputTag = ($target.prop('tagName').toUpperCase() === 'INPUT');
            var isDateInputTag = (
                    $target.hasClass('dateinput') || $target.parents('button').eq(0).hasClass('dateinput')
            );
            changeData._state = {};

            // 그리드 체크박스 선택 한 경우 종료.
            if (key === 'check') {
                return;
            }
            
            // 수동으로 편집모드로 전환
            if (evObj.data._state && evObj.data._state.editing === false) {
                $(e.currentTarget).alopexGrid('endRowInlineEdit');
                $(e.currentTarget).alopexGrid('startRowInlineEdit', {_index : index});
                
            } else {
                // 수동으로 편집모드 종료
                if (!isInputTag && !isDateInputTag) {
                    $(e.currentTarget).alopexGrid('endRowInlineEdit', {_index : index});                    
                }
            }
            
            if (key !== 'roleBeginDt' && key !== 'roleEndDt') {
                return;
            }

            var value = evObj.data[key];
            
            // dash(-) 제거
            value = value.split('-').join('');

            var valueObj = '';
            if (value && value != '') {
                valueObj = {year: value.substr(0,4), month: value.substr(4,2), day: value.substr(6,2)};
            }

            if (!isDateInputTag) {
                return;
            }
            $datePicker = $cell;
            $cell.showDatePicker(function(date, dateStr) {
                changeData._state.edited = true;
                changeData._state.selected = true;
                if (evObj.mapping.key == 'roleBeginDt') {
                    changeData.roleBeginDt = dateStr;                        
                } else if (evObj.mapping.key == 'roleEndDt') {
                    changeData.roleEndDt = dateStr;
                }
                $(e.currentTarget).alopexGrid('dataEdit', changeData, {_index: index});
            }, {date: valueObj, format: 'yyyyMMdd'});                

        }).on('dataSelectEnd', function(e) {    /* check 버튼 이벤트 */
            
            // 그리드 편집 모드 종료
            $(e.currentTarget).alopexGrid('endEdit');

            var dataList = AlopexGrid.parseEvent(e).datalist;
            if (!dataList) {
                return;
            }
            
            // 선택 해제시 기존의 데이터로 변경
            dataList.forEach(function(dataObj, index) {
                var state = dataObj._state;
                
                // 선택 된 아이템이면 처리안함
                if (state.added || state.selected) {
                    return;
                }
                var indexInfo = {
                        _index: dataObj._index
                }
                $(e.currentTarget).alopexGrid('dataRestore', indexInfo);
                
            });
            
            // 그리드 새로고침
            $(e.currentTarget).alopexGrid();
            
            // 저장버튼 활성화 처리
            RoleMapModule.btnStateControll($(e.currentTarget));

        }).on('dataChanged', function(e) {    /* 데이터 변경시 선택 처리 */

            var dataList = AlopexGrid.parseEvent(e).datalist;
            var eventType = AlopexGrid.parseEvent(e).type;
            if (!dataList) {
                return;
            }
            if (!eventType) {
                return;
            }
            if (eventType != 'edit') {
                
                // 저장버튼 활성화 처리
                RoleMapModule.btnStateControll($(e.currentTarget));
                return;
            }
            
            SKIAF.console.info('dataList', dataList);
            dataList.forEach(function(dataObj, index) {
                var state = dataObj._state;
                var chageData = {};
                var indexInfo = {
                        _index: dataObj._index
                }
                
                // 추가된 데이터인지 확인
                if (state.added) {
                    if (!state.selected) {
                        
                        // 데이터 자동 체크 처리
                        chageData = {
                                _state: {
                                    selected: true
                                }
                        };
                        $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                    }
                    
                    return;
                }
                
                var isDiff = SKIAF.util.isDataDiff(dataObj._original, dataObj);
                
                // 원본 데이터와 차이가 있는지 확인
                if (isDiff && !state.selected) {
                    
                    // 데이터 자동 체크 처리
                    chageData = {
                            _state: {
                                selected: true
                            }
                    };
                    $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                
                } else if (!isDiff && (state.selected || state.edited)) {

                    // 데이터 자동 체크해제 처리
                    chageData = {
                            _state: {
                                selected: false,
                                edited: false
                            }
                    };
                    
                    $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                }

            });
            
            // 저장버튼 활성화 처리
            RoleMapModule.btnStateControll($(e.currentTarget));
            
        });
        
        $('#prevBtn1, #prevBtn2').on('click', function(e){
            var previousUrl = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.ROLE_PREVIOUS_URL);
            if (previousUrl) {
                window.location.href = previousUrl;
            } else {
                window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES;
            }
            return;
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            SKIAF.console.info('state', e.state);
            var roleId = SKIAF.util.getParameter('roleId') ? SKIAF.util.getParameter('roleId') : '';
            $('#selectedRoleInfo').setData({
                roleId : roleId
            });
            if (roleId) {
                RoleMapModule.getRoleInfo(false);
            }


        }, false);
        
    };
 
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * Role 상세정보 조회 (매핑정보 포함)
     */
    this.getRoleInfo = function(isHistory){
        var roleId = $('#selectedRoleInfo').getData().roleId;
        if (!roleId) {
            // 그리드 초기화
            $('#userGroupMapEditGrid').alopexGrid('dataSet', []);
            $('#userMapEditGrid').alopexGrid('dataSet', []);
            return;
        }
        
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }
        if (isHistory) {
            history.pushState(null, null, location.origin + SKIAF.PATH.VIEW_ROLES_DETAIL + '?roleId=' + roleId);            
        }
        

        
        // 버튼 초기화
        $('#addUserGroupBtn').setEnabled(false).removeClass('Default');
        $('#addUserBtn').setEnabled(false).removeClass('Default');

        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ROLES_DETAIL_INFO, roleId), {
            method : 'GET',
            success : function(res) {
                if(res.data.roleInfo){
                    $('#addUserGroupBtn').setEnabled(true).addClass('Default');
                    $('#addUserBtn').setEnabled(true).addClass('Default');
                }else{
                    $('#addUserGroupBtn').setEnabled(false).removeClass('Default');
                    $('#addUserBtn').setEnabled(false).removeClass('Default');
                }
                res.data.roleInfo.useYn = res.data.roleInfo.useYn ? 'Y' : 'N';
                $('#selectedRoleInfo').setData(res.data.roleInfo);
                $('#userGroupMapEditGrid').alopexGrid('dataSet', res.data.roleMapUserGroup);
                $('#userMapEditGrid').alopexGrid('dataSet', res.data.roleMapUser);
            }
        });
    };
    
    
    /**
     * role 선택 레이어 팝업
     */
    this.popupRoleSearch = function() {
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_ROLES_SEARCH,
            title : SKIAF.i18n.messages['bcm.role.select'],
            data : {
                roleType : 'ALL',
                searchKeyword : $('#roleSearchText').val(),
                selectedRoleIds : [],
                multiSelected : false
            },
            movable: true,
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                
                if(data.type == 'confirm' && data.selectedRoles[0]) {
                    $('#selectedRoleInfo').setData(data.selectedRoles[0]);
                    //상세정보 조회
                    var roleId = data.selectedRoles[0].roleId;
                    $('#selectedRoleInfo').setData({
                        roleId : roleId
                    });
                    RoleMapModule.getRoleInfo(true);
                }
                
            }
        });
    };
    
    /**
     * userGroup 선택 레이어 팝업
     */
    this.popupUserGroupSearch = function() {
        
        
        var selectedUserGroupIds = [];
        var userGroupEditList = $('#userGroupMapEditGrid').alopexGrid('dataGet');    
        for(var i = 0; i<userGroupEditList.length; i++) {
            // 선택되어있는 유저 ID 추출
            selectedUserGroupIds.push(userGroupEditList[i].userGroupId);
        }
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS_SEARCH,
            title : SKIAF.i18n.messages['bcm.role.user.group.search'],
            data : {
                multiSelected : true,
                searchText : '',
                selectedIds : selectedUserGroupIds
            },
            movable: true,
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                
                if(data.type == 'confirm' && data.selectedItems.length > 0){
                    //선택된 아이템 추가
                    for(var i = 0; i<data.selectedItems.length; i++) {
                        
                        let currentDate = new Date();
                        //권한 시작일 - 현재날짜
                        data.selectedItems[i].roleBeginDt = SKIAF.util.dateFormatReplace(currentDate, 'yyyyMMdd');
                        
                        //권한 종료일 - 9999-12-31
                        currentDate.setMonth(currentDate.getMonth()+3)
                        data.selectedItems[i].roleEndDt = '99991231';
                        data.selectedItems[i].roleUseYn = true;
                        
                        data.selectedItems[i]._state.added = true;
                        data.selectedItems[i]._state.selected = true;
                        $('#userGroupMapEditGrid').alopexGrid('dataAdd', data.selectedItems[i]);                    
                    }
                }
            }
        });
        
    };
    
    
    /**
     * user 선택 레이어 팝업
     */
    this.popupUserSearch = function() {
        
        var selectedUserIds = [];
        var userEditList = $('#userMapEditGrid').alopexGrid('dataGet');    
        for(var i = 0; i<userEditList.length; i++) {
            // 선택되어있는 유저 ID 추출
            selectedUserIds.push(userEditList[i].userId);
        }

        
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_USERS_SEARCH,
            title : SKIAF.i18n.messages['bcm.role.user.person.search'],
            data : {
                multiSelected : true,
                searchText : '',
                selectedUserIds : selectedUserIds
            },
            movable: true,
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                
                if(data.type == 'confirm' && data.selectedUsers.length > 0){
                    //선택된 아이템 추가
                    for(var i = 0; i<data.selectedUsers.length; i++) {
                        
                        let currentDate = new Date();
                        //권한 시작일 - 현재날짜
                        data.selectedUsers[i].roleBeginDt = SKIAF.util.dateFormatReplace(currentDate, 'yyyyMMdd');
                        
                        //권한 종료일 - 현재부터 3개월
                        currentDate.setMonth(currentDate.getMonth()+3)
                        data.selectedUsers[i].roleEndDt = '99991231';
                        data.selectedUsers[i].roleUseYn = true;
                        
                        data.selectedUsers[i]._state.added = true;
                        data.selectedUsers[i]._state.selected = true;
                        $('#userMapEditGrid').alopexGrid('dataAdd', data.selectedUsers[i]);                        
                    }
                }
            }
        });
    };
    
    /**
     * 추가(ADD), 변경(EDIT), 취소(ADD-CANCEL)에 따른 버튼 활성/비활성 처리
     */
    this.btnStateControll = function($grid){
        
        var isSave = false;
        var gridData = $grid.alopexGrid('dataGet', {_state:{selected:true}});

        //체크된 아이템중 추가및 수정된 아이템 이 있는경우 저장버튼 활성화.
        gridData.some(function(item){
            if(item._state.added || item._state.edited){
                isSave = true;
                return true;
            } 
        });
            
        var contentId = $('.Tabs').getCurrentTabContent();
        var $saveBtn = $(contentId).find('[id^=saveRoleUser]');
        
        if(isSave){
            $saveBtn.setEnabled(true);
        }else{
            $saveBtn.setEnabled(false);
        }
        
    };
    
    this.getRoleMapGridObj = function() {
        return {
            height : 334,
            autoColumnIndex : true, useClassHovering : false, fitTableWidth : true,
            message : { nodata : SKIAF.i18n.messages['bcm.common.nodata']},
            rowInlineEdit : true,
            endInlineEditByOuterClick : true,
            readonlyRender : false,
            defaultColumnMapping : {resizing : true},
            pager : false,
            renderMapping : { 
                'dateinput' : {
                    renderer:function(value, data, render, mapping, grid) {
                        value = value.split('-').join('');
                        var dateValue = value.substr(0, 4) + '-' + value.substr(4, 2) + '-' + value.substr(6, 2);

                        // 날짜 형식에 맞는지 검사
                        if (isNaN(parseInt(value)) || value.length != 8 || isNaN((new Date(dateValue)).getTime())) {
                            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.role.valid.date']);
                            
                            // 유효하지 않은 데이터이기 때문에, 원본 날짜로 복구
                            if (data._original && mapping.key) {
                                var changeData = {};
                                value = data._original[mapping.key];
                                changeData[mapping.key] = value;
                                dateValue = value.substr(0, 4) + '-' + value.substr(4, 2) + '-' + value.substr(6, 2);
                                $(grid.root).alopexGrid('dataEdit', changeData, {_index: data._index});
                            }
                            
                        }
                        if (!data._state) {
                            return;
                        }
                        
                        // date picker 버튼
                        var $button = $('<button>', {
                            'class' : 'dateinput',
                            'html' : $('<span>', {
                                'class' : 'Icon Calendar'
                            })
                        });

                        var $div = $('<div>');

                        // date 영역 편집모드
                        if (data._state.editing && typeof data._state.editing[mapping.columnIndex] !== 'undefined') {
                            $div.html($('<input>', {
                                'type' : 'text',
                                'value' : dateValue,
                                'style' : 'width: 85%'
                            }));
                            $div.append($button);

                        } else { /* Readonly 모드 */
                            
                            // 열려 있는 datepicker 닫기
                            if ($datePicker) {
                                $datePicker.closeDatePicker();
                                $datePicker = null;
                            }
                            
                            $div.html(dateValue);
                        }
                        return $div;
                    }
                }
            },
            rowOption : {
                styleclass : function(data, rowOption) {
                    if (data._state.added && !data._state.selected) {
                        return 'highlight-add highlight-blur';
                    }
                    if (data._state.added) {
                        return 'highlight-add';
                    } else if(data._state.edited) {
                        return 'highlight-edit';
                    }
                    return '';
                }
            },
            columnMapping : []
        };
    };

});