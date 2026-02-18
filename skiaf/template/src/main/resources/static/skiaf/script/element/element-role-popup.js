/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.21 - in01871
 * description : 프로그램 요소 권한 팝업
 */
"use strict";
var ElementRolePopupModule = $a.page(function() {
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
     
     var programId = null;
     
     var useYnTxt = null;
     
     var isElementDetailReady = false;
     
     var selectedRoleId = null;
     
     var selectedRoleName = null;
     
     var roleId = null;
     
     var roleName = null;
     
     // 중복 아이디 체크
     var idCheck = null;
      
     var elementGridColumns = [
         {title: SKIAF.i18n.messages['bcm.common.select'],            key: 'check',            align: 'center',        width : 50,            selectorColumn : true,        headerStyleclass:'set', resizing:false},
         {title: SKIAF.i18n.messages['bcm.element.element-key'],         key: 'elementKey',        align: 'center',        width : 100,            tooltip: true},
         {title: SKIAF.i18n.messages['bcm.element.element-desc'],       key: 'elementDesc',        align: 'center',        width : 150,            tooltip: true}
     ];
     
     var roleGridColumns = [
         {title: SKIAF.i18n.messages['bcm.common.select'],            key: 'check',           align: 'center',        width : 50,         selectorColumn : true,      headerStyleclass:'set',       resizing:false},
         {title: SKIAF.i18n.messages['bcm.element.role-id'],         key: 'roleId',          align: 'center',        width : 70,         tooltip: true},
         {title: SKIAF.i18n.messages['bcm.element.role-name'],         key: 'roleName',        align: 'center',        width : 70,         tooltip: true},
         {title: SKIAF.i18n.messages['bcm.element.role-desc'],       key: 'roleDesc',        align: 'center',        width : 100,         tooltip: true}
     ];
     
     var elementGridObj = {
             autoColumnIndex: true,
             fitTableWidth: true,
             headerRowHeight: 30,
             height: 334,
             rowSelectOption : {clickSelect : true},
             message: {
                 nodata: SKIAF.i18n.messages['bcm.common.nodata']
             },
             defaultColumnMapping: {
                 resizing: true
             },
             readonlyRender: false,
             columnMapping: elementGridColumns,
             data: []
     };
     
     var roleGridObj = {
             autoColumnIndex: true,
             fitTableWidth: true,
             headerRowHeight: 30,
             height: 334,
             rowSelectOption : {clickSelect : true},
             rowOption: {
                 defaultHeight: 30
             },
             defaultColumnMapping: {
                 resizing: true
             },
             message: {
                 nodata: SKIAF.i18n.messages['bcm.common.nodata']
             },
             columnMapping: roleGridColumns,
             data: []
         };
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
     
     this.init = function(id, param) {
         
         programId = param.programId;
         
         ElementRolePopupModule.addEvent();
         
         ElementRolePopupModule.elementSearch(programId);
         
         ElementRolePopupModule.roleSearch();
         
         $('#elementGrid').alopexGrid(elementGridObj);
         $('#roleGrid').alopexGrid(roleGridObj);
        
     };
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
     
    this.addEvent = function() {
        
        // 권한 검색 버튼
        $('#btnRoleSearch').on('click', function() {
            ElementRolePopupModule.roleSearch();
        });
        
        // 추가 버튼
        $('#roleAddBtn').on('click', function() {
            ElementRolePopupModule.addRoleElements();
            
        });
        
        // 취소 버튼
        $('#roleCancelBtn').on('click', function() {
            ElementRolePopupModule.cancel();
        });
                        
    };
         
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 프로그램 요소 검색
     */
    this.elementSearch = function(programId) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ELEMENT, programId), {
            method : 'GET',
            success : function(res) {
                var elementList = res.data.elementList;
                
                $('#elementGrid').alopexGrid('dataSet', elementList);
                
            }
        });
    };

    /**
     * 권한 검색
     */
    this.roleSearch = function() {
        
        var keyword = $('#searchRole').val();
        var roleType = SKIAF.ENUM.ROLE_TYPE.ELEMENT;
        var isUnusedInclude = false;
        var list = true;
        
        var params = {};
        
        params.keyword = keyword;
        params.roleType = roleType;
        params.isUnusedInclude = isUnusedInclude;
        params.list = list;
                
        $a.request(SKIAF.PATH.ROLES, {
            method : 'GET',
            data : params,
            success : function (result) {
                
                var roleList = result.data;
                
                $('#roleGrid').alopexGrid('dataSet', roleList);
            }
        });
    };
    
    /**
     * 요소 권한 추가
     */
    this.addRoleElements = function () {
        
        var elementList = $('#elementGrid').alopexGrid('dataGet', {_state : {selected: true}});
        var roleList = $('#roleGrid').alopexGrid('dataGet', {_state : {selected: true}});
        
        if(elementList.length == 0){
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.valid.no-element-select']);
            return;
        } else if (roleList.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.element.valid.no-role-select']);
            return;
        }
        
        var data = [];
        
        for (var i = 0; i < elementList.length; i++) {

            for (var j = 0; j < roleList.length; j++) {
                var elementRoleList = [];
                elementRoleList.elementSeq = elementList[i].elementSeq;
                elementRoleList.elementKey = elementList[i].elementKey; 
                elementRoleList.elementDesc = elementList[i].elementDesc;
                elementRoleList.roleId = roleList[j].roleId; 
                elementRoleList.roleName = roleList[j].roleName;
                                                
                data.push(elementRoleList);
            }
        }
        
        $a.close({
            data : data
        });
    };
    
    /**
     * 취소
     */
    this.cancel = function () {
        
        $a.close();
    };

});