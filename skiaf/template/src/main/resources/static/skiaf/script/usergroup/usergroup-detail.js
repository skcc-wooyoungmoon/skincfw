/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01871
 * description : 사용자그룹 상세
 */
"use strict";
var UserGroupDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var userGroupInit = {
        userGroupId : null,
        userGroupName : null,
        userGroupDesc : null,
        companyName : null,
        useYnTxt : null,
        createInfo : null,
        updateInfo : null
    };

    // 그리드 컬럼
    var userGroupDetailGridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],          align : 'center',           key : 'check',          width : 50,         selectorColumn : true,            resizing : false,            headerStyleclass : 'set'},
        {title : SKIAF.i18n.messages['bcm.user.login-id'],          key : 'loginId',            align : 'center',       width : 100,        tooltip : true,     render : {type : "link"}},
        {title : SKIAF.i18n.messages['bcm.user.user-name'],         key : 'userName',           align : 'center',       width : 100,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.user.company-name'],      key : 'companyName',        align : 'center',       width : 100,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.user.department-name'],   key : 'departmentName',     align : 'center',       width : 100,        tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],       key : 'updateBy',           align : 'center',       width : 85,         tooltip : true},
        {title : SKIAF.i18n.messages['bcm.common.update-date'],     key : 'updateDate',         align : 'center',       width : 150,        tooltip : true}
    ];
    
    // 그리드 초기화
    var userGroupDetailGridObj = {
        autoColumnIndex : true,
        useClassHovering : true,
        fitTableWidth : true,
        headerRowHeight: 30, /* 컬럼헤더 height */
        height: 394, /* 전체 height */
        rowSelectOption : {
            clickSelect : true
        },
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping: {
            resizing: true
        },
        renderMapping : {
            'link' : {
                renderer : function(value, data, render, mapping) {
                    return '<a href = "' + SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USERS_DETAIL, data.userId) + '">' + data.loginId + '</a>';
                }
            }
        },
        pager : false,
        rowOption : {
            defaultHeight : 30,
            styleclass : function(data, rowOption){
                if(data["status"] === ''){
                    return ''
                } else if(data["status"] === 'add') {
                    return 'highlight-add'
                } else if(data["status"] === 'add-cancel') {
                    return 'highlight-add highlight-blur'
                }
            }
        },
        columnMapping : userGroupDetailGridColumns,
        data : []
    };

    // 서버
    var isAjaxAvailable = true; // Ajax 사용가능 여부

    var userGroupId = SKIAF.util.getPathVariable(3);
    
    var companyCode = null;
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
       
        UserGroupDetailModule.initData(userGroupId);
        
        UserGroupDetailModule.addEvent();
        
        $('#userGroupDetailGrid').alopexGrid(userGroupDetailGridObj);
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function () {
        
        // 사용자 검색 팝업
        $('#btnUserGroupUserAdd').on("click", function(e) {
            UserGroupDetailModule.popupUserAdd();
        });
        
        // 그룹에 사용자 추가
        $('#btnUserGroupUserSave').on("click", function(e) {
            
            var gridData = $('#userGroupDetailGrid').alopexGrid('dataGet', {status : 'add', _state : {selected : true}});
           
            if (gridData != 0) {
                UserGroupDetailModule.userGroupUserSave();  
            }
        });
        
        // 그룹 내 사용자 삭제
        $('#btnUserGroupUserDelete').on("click", function(e) {
            UserGroupDetailModule.userGroupUserDelete();
        });
        
        // 이전 버튼
        $('#btnUserGroupList').on("click", function(e) {
            
            var previousUrl = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.USERGROUP_PREVIOUS_URL);
            if(previousUrl) {
                window.location.href = previousUrl;
            } else {
                UserGroupDetailModule.goList();
            }
            
        });
        
        // 브라우저 뒤로가기
        window.addEventListener('popstate', function(e) {
            setParameter();
            search(true);
        }, false);
        
        // 사용자 처리
        $('#userGroupDetailGrid').on('dataSelectEnd', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            
            var selectGridData = evObj.datalist;
            
            if(selectGridData[0].addData == true) {
            
                if(selectGridData[0]._state.selected == true) {
                    
                    $('#userGroupDetailGrid').alopexGrid('dataEdit', selectGridData[0].status = "add");
                                        
                } else {

                    $('#userGroupDetailGrid').alopexGrid('dataEdit', selectGridData[0].status = "add-cancel");

                }
            }
            
            UserGroupDetailModule.btnCheck();
            
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.goDetail = function (userGroupId) {
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USER_GROUPS_DETAIL, userGroupId);
        return;
    };
    
    this.goList = function () {
        window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS;
        return;
    };
    
    // 저장, 삭제 버튼 활성화/비활성화 처리    
    this.btnCheck = function() {
        
        var selectGridDataList = $('#userGroupDetailGrid').alopexGrid('dataGet', {_state : {selected: true}});
        
        var btnCheck = 'neither';
        
        selectGridDataList.every(function(item, index, array){
            if(item.status == 'del'){
                btnCheck = 'del';
                return item.status = 'del';
                
            } else if (item.status == 'add') {
                btnCheck = 'add';
                return item.status = 'add';
                
            }
            
        });
        
        // 항목 동시 선택시 버튼 disable
//        if(selectGridDataList.length > 1){
//            
//            for (var i = 1; i < selectGridDataList.length; i++){
//                if (selectGridDataList[i-1].status == 'del' && selectGridDataList[i].status == 'add'){
//                    btnCheck = 'neither';
//                    break;
//                }
//            }
//        }
        
        if(btnCheck == 'neither'){
            $('#btnUserGroupUserDelete').setEnabled(false);
            $('#btnUserGroupUserSave').setEnabled(false);
            
        } else if (btnCheck == 'del') {
            $('#btnUserGroupUserDelete').setEnabled(true);
            $('#btnUserGroupUserSave').setEnabled(false);
            
        } else if (btnCheck == 'add') {
            $('#btnUserGroupUserDelete').setEnabled(false);
            $('#btnUserGroupUserSave').setEnabled(true);
            
        }
    };
    
    this.initData = function(userGroupId) {

        // 그룹 정보 조회
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUPS_DETAIL, userGroupId), {
            method : 'GET',
            success : function(result) {
                                
                var userGroupMapUserList = result.data.userGroupMapUserList;
                var userGroup = result.data.userGroup;
                
                companyCode = userGroup.companyCode;

                var formatData = UserGroupDetailModule.setUserGroupDetailTxtFormat(userGroup);

                $('#userGroupDetail').find('td').text('');// 초기화
                $('#userGroupDetail').setData(formatData);
                
                var userList = new Array();

                for (var i = 0, len = userGroupMapUserList.length; i < len; i++) {
                    userGroupMapUserList[i].status = 'del';
                    userList.push(userGroupMapUserList[i]);
                }

                $('#userGroupDetailGrid').alopexGrid('dataSet', userList);

            },

            complete : function() {
                isAjaxAvailable = true;
            }
        });
        
    };

    this.setUserGroupDetailTxtFormat = function(data) {

        data.createInfo = data.createBy + ' ' + SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd HH:mm');
        data.updateInfo = data.updateBy + ' ' + SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd HH:mm');

        data.useYnTxt = (data.useYn) ? 'Y' : 'N';

        return data;
    };
    
    // 팝업에서 유저 가져오기
    this.popupUserAdd = function() {
        
        // 이미 그리드에 있는 유저
        var selectedUserIds = [];
                
        var userData = $('#userGroupDetailGrid').alopexGrid('dataGet');
        
        for(var i = 0, len = userData.length; i < len; i++) {
            selectedUserIds.push(userData[i].userId);
        }
        
        $a.popup({
            url : SKIAF.PATH.VIEW_USERS_SEARCH,
            data : {
                companyCode : companyCode,
                selectedUserIds : selectedUserIds,
                multiSelected : true
            },
            movable: true,
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            title : SKIAF.i18n.messages['bcm.usergroup.userselect'],
            callback : function(data) {
                
                if (data.length != 0) {

                    var addUserData = data.selectedUsers;
                    
                    for (var i = 0; i < addUserData.length; i++) {
                    
                        addUserData[i]["addData"] = true;
                        addUserData[i]["status"] = "add";
                        
                        if($.inArray(addUserData[i].userId, selectedUserIds) == -1) {
                            
                            $('#userGroupDetailGrid').alopexGrid('dataAdd', $.extend({}, addUserData[i]));
                            
                        }
                    }
                    
                    UserGroupDetailModule.btnCheck();
                }
            }
        });
    };
    
    // 유저그룹에 유저 매핑
    this.userGroupUserSave = function() {
        
        var userDataList = $('#userGroupDetailGrid').alopexGrid('dataGet', function(data){
            return data.status == 'add';
        });
        
        var userIdList = [];
        
        for(var i = 0, len = userDataList.length; i < len; i++) {
            userIdList.push(userDataList[i].userId);
        }
                
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUP_USERS_ROLE_MAPS, userGroupId), {
            method : 'POST',
            array : userIdList,
            success : function(result) {
                
                UserGroupDetailModule.goDetail(userGroupId);
            },

            after : function() {
                isAjaxAvailable = true;
            }
        });        
    };
    
    // 유저그룹에서 유저 삭제
    this.userGroupUserDelete = function() {
        
        var userDataList = $('#userGroupDetailGrid').alopexGrid('dataGet', function(data){
            return data.status != 'add' && data._state.selected == true;
        });

        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'], function callback() {
            
            var userIdList = [];
            
            for(var i = 0, len = userDataList.length; i < len; i++) {
                userIdList.push(userDataList[i].userId);
            }
            
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUP_USERS_ROLE_MAPS, userGroupId), {
                method : 'DELETE',
                array : userIdList,
                success : function(result) {

                    UserGroupDetailModule.goDetail(userGroupId);
                },

                after : function() {
                    isAjaxAvailable = true;
                }
            });
        });
    };

});