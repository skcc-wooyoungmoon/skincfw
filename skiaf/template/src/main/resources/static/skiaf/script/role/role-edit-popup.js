/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.16 - in01876
 * description : 권한 수정 팝업
 */
"use strict";
var RoleEditModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {

        param.useYn = param.useYn ? 'Y' : 'N';
        $('#roleForm').setData(param);
        
        RoleEditModule.addEvent();
        
    };
    

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 저장
        $('#saveBtn').on('click', function(e){
            let roleData = $('#roleForm').getData();

            // validation 체크
            if (!$('#roleForm').validate()) {
                $('#roleForm').validator();
                return;
            }

            roleData.useYn = roleData.useYn == 'Y' ? true : false;

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ROLES_DETAIL, roleData.roleId), {
                method : 'PUT',
                data : roleData,
                success : function(res) {
                    $a.close(res);
                }
            });
        });
        
        
        // 취소
        $('#cancleBtn').on('click', function(e) {
            $a.close();
        });
        
        
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
 

});