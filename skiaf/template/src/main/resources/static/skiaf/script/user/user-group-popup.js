/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.0907 - in01869
 * description : 사용자 그룹 매핑 팝업
 */
"use strict";
var UserGroupMapModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var user  = {};
    var userGroupMapList = [];
    
    var listGridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        height: '334px',
        defaultColumnMapping: {
            resizing: true
        },
        rowSelectOption : {
            clickSelect : true,
            disableSelectByKey : true
        },
        columnMapping: [
            {
                title : SKIAF.i18n.messages['bcm.common.select'],
                align : 'center',
                headerStyleclass:'set',
                key : 'check',
                width: 35,
                selectorColumn:true
            },{
            title: SKIAF.i18n.messages['bcm.user.usergroup-id'],
            key: 'userGroupId',
            align: 'center'
        }, {
            title: SKIAF.i18n.messages['bcm.user.usergroup-name'],
            key: 'userGroupName',
            align: 'center'
        }, {
            title: SKIAF.i18n.messages['bcm.user.usergroup-desc'],
            key: 'userGroupDesc',
            align: 'center'
        }, {
            title: SKIAF.i18n.messages['bcm.user.mapping-date'],
            key: 'userMapDt',
            align: 'center'
        }],
        data: []
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        if(param['user'] != null) {
            user = param['user'];
        }
        
        if(param['userGroupMapList'] != null) {
            userGroupMapList = param['userGroupMapList'];
        }
        
        //사용자 정보
        $('#userGroupMapUserInfo').setData(user);
        
        $('#userGroupMapGrid').alopexGrid(listGridObj);

        UserGroupMapModule.addEvent();
        
        UserGroupMapModule.setUserGroupList();
    };
    
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        // [취소]버튼 클릭 이벤트
        $('#userGroupMapCancel').on('click', function(e) {       
            $a.close();     
        });
        
        // [저장]버튼 클릭 이벤트
        $('#userGroupMapSave').on('click', function(e) {
            UserGroupMapModule.saveUserGroupMap();
        });
        
        // Grid 체크박스 선택 이벤트
        $('#userGroupMapGrid').on('dataSelectEnd', function(e){
            var evObj = AlopexGrid.parseEvent(e);
            var dataObj = evObj.datalist[0];
            var row = dataObj._index.row;
            var selected = dataObj._state.selected;
            
            if(selected) {
                // 체크박스 선택시
                if(dataObj.status == 'CURRENT' || dataObj.status == 'DELETE') {
                    $('#userGroupMapGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-blur');
                    $('#userGroupMapGrid').alopexGrid('dataEdit', {_state : {selected : true}, status : 'CURRENT'}, {_index : {row : row}});
                } else {
                    $('#userGroupMapGrid').alopexGrid('dataEdit', {_state : {selected : true}, status : 'ADD'}, {_index : {row : row}});
                    $('#userGroupMapGrid').alopexGrid('rowElementGet',{_index : {row : row}}).addClass('highlight-add');
                }
                
            } else {
                //체크박스 해지시
                if(dataObj.status == 'CURRENT') {
                    $('#userGroupMapGrid').alopexGrid('dataEdit', {_state : {selected : false}, status : 'DELETE'}, {_index : {row : row}});
                    $('#userGroupMapGrid').alopexGrid('rowElementGet',{_index : {row : row}}).addClass('highlight-blur');                    
                } else {
                    $('#userGroupMapGrid').alopexGrid('rowElementGet',{_index : {row : row}}).removeClass('highlight-add');
                }
            }
        });
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 사용자그룹 목록
     */
    this.setUserGroupList = function() {
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_GROUP_MAP, user.userId), {
            method : 'GET',
            success : function(result) {
                
                var userGroupList = result.data;
                // 사용자그룹목록(맵핑된 사용자그룹일 경우 매핑일시 추가)
                for(var i = 0, iLen = userGroupList.length; i < iLen; i++) {
                    for(var j = 0, jLen = userGroupMapList.length; j < jLen; j++) {
                        if(userGroupMapList[j].userGroupId == userGroupList[i].userGroupId) {
                            userGroupList[i].isMapping = true;
                            userGroupList[i].userMapDt = userGroupMapList[j].userMapDt;
                        }
                    }
                }
                
                $('#userGroupMapGrid').alopexGrid('dataSet' , userGroupList);
                
                // 매핑된 사용자 selected 처리
                var mappingList = $('#userGroupMapGrid').alopexGrid('dataGet',  {isMapping : true});
                for(var i = 0; i<mappingList.length; i++) {
                    $('#userGroupMapGrid').alopexGrid('dataEdit', {_state : {selected : true}, status : 'CURRENT'}, {_index : mappingList[i]._index});
                }
            }
        });
        
    };
    
    /**
     * 사용자그룹 목록 매핑 저장
     */
    this.saveUserGroupMap = function() {
        
        // 새로 매핑하는 사용자그룹목록
        var addList = $('#userGroupMapGrid').alopexGrid('dataGet',  {_state : {selected : true},  status : 'ADD'});
        // 기존에 매핑되었던 삭제할 사용자그룹목록
        var deleteList = $('#userGroupMapGrid').alopexGrid('dataGet',  {_state : {selected : false},  status : 'DELETE'});
        
        var addUserGroupIds = [];
        for(var i = 0, iLen = addList.length; i < iLen; i++) {
            addUserGroupIds.push(addList[i].userGroupId);
        }
        
        var deleteUserGroupIds = [];
        for(var j = 0, jLen = deleteList.length; j < jLen; j++) {
            deleteUserGroupIds.push(deleteList[j].userGroupId);
        }

        var param = {
            addUserGroupIds : addUserGroupIds,
            deleteUserGroupIds : deleteUserGroupIds
        };

        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USERS_GROUP_MAP, user.userId), {
            method : 'PATCH',
            data : param,
            success : function(result) {
                $a.close({
                    data : {
                        userId : user.userId   
                    }
                });
            }
        });
    };

});