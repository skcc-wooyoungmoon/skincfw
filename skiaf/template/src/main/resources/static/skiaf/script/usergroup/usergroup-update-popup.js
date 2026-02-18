/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.29 - in01871
 * description : 사용자그룹 수정
 */
"use strict";
var UserGroupUpdateModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 서버
    var isAjaxAvailable = true; // Ajax 사용가능 여부

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, param) {
    
        SKIAF.console.info(param.userGroupId);
    
        var userGroupId = param.userGroupId;
    
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUPS_DETAIL, userGroupId), {
            method : 'GET',
            success : function(result) {
                var userGroupData = result.data.userGroup;
                var userGroupUserList = result.data.userGroupMapUserList;
    
                if (userGroupData.useYn == true) {
                    userGroupData.useYnTxt = 'Y';
                } else {
                    userGroupData.useYnTxt = 'N';
                }
    
                userGroupData.userCount = userGroupUserList.length;
    
                $('#userGroupUpdate').setData(userGroupData);
    
            }
        });

        // 저장 버튼
        $('#btnUpdateUserGroup').on('click', function() {

            // Validation Check
            if (!$('#userGroupUpdateTable').validate()) {
                $('#userGroupUpdateTable').validator();
                return;
            }

            UserGroupUpdateModule.userGroupUpdate();

        });

        // 취소 버튼
        $('#btnUpdateUserGroupCancel').on('click', function() {
            $a.close();
        });
    };

 
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    this.goList = function() {
        window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_USER_GROUPS;
        return;
    };


    /**
     * 그룹 정보 수정
     */
    this.userGroupUpdate = function() {

        var updateData = $('#userGroupUpdate').getData();

        updateData.useYn = (updateData.useYnTxt == 'Y') ? true : false;
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_GROUPS_DETAIL, updateData.userGroupId), {
            method : 'PUT',
            data : updateData,
            success : function(result) {
                UserGroupUpdateModule.goList();

            },
            after : function() {
                isAjaxAvailable = true;
            }
        });
    };
});