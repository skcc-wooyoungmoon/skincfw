/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01876
 * description : 사용자 상세 레이어팝업 js
 */
"use strict";
var UserDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    //사용자 상세 초기 오브젝트 (Info : 화면 출력용)
    var userData = {
        gwIfYn : null, //그룹웨어 연동여부
        ssoYn : null, //sso 연동여부
        userName : null, //사용자 이름
        loginId : null, //로그인
        companyCode : null, //회사코드
        departmentName : null, //부서
        positionName : null, //직급
        regularYn : null, //정직원 여부
        positionRegularInfo : null, //직급 + 정직원여부
        email : null, //이메일
        telNo : null, //전화번호
        mobileNo : null, //휴대폰번호
        firstLoginYn : null, //최초로그인(비번변경여부)
        lastLogin : null, //마지막로그인 -- history
        useYn : null, //사용여부
        createBy : null, //등록Id
        createDate : null, //등록일
        createInfo : null, //등록Id + 등록일
        updateBy : null, //수정Id
        updateDate : null, //수정일
        updateInfo : null, //수정Id + 수정일
        status : null //계정상태(정상, 실패, 잠김) 
    };
    var userGroupMapList = {};
    var roleMapList = {};
    var editData = {};
    var myData = {};

    //grid
    var userGroupMapGridColumns = [
        {title : SKIAF.i18n.messages['bcm.user.usergroup-id'],    key : 'userGroupId',    align : 'center',    tooltip : true, width : '115px'},
        {title : SKIAF.i18n.messages['bcm.user.usergroup-name'],    key : 'userGroupName',    align : 'center',    tooltip : true, width : '130px'},
        {title : SKIAF.i18n.messages['bcm.user.usergroup-desc'],    key : 'userGroupDesc',    align : 'center',    tooltip : true, width : '140px'},
        {title : SKIAF.i18n.messages['bcm.user.mapping-date'],        key : 'userMapDt',        align : 'center',    tooltip : true, width : '110px'}
    ];
    var userGroupMapGridInit = {
        autoColumnIndex : true, 
        useClassHovering : true, 
        fitTableWidth : true, 
        headerRowHeight: 30, 
        height:238,
        rowOption : {
            defaultHeight : 30
        },
        defaultColumnMapping : { resizing : true },
        renderMapping : {
            'link' : {
                renderer : function(value, data, render, mapping) {
                    return '<a href = "' + SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USER_GROUPS_DETAIL, data.userGroupId) + '">' + data.userGroupId + '</a>';
                }
            }
        },
        pager : false, columnMapping : userGroupMapGridColumns, data : []
    };

    var roleMapGridColumns = [
        {title : SKIAF.i18n.messages['bcm.user.role-id'],          key : 'roleId',        align : 'center',    tooltip : true,     width : '115px'},
        {title : SKIAF.i18n.messages['bcm.user.role-name'],          key : 'roleName',        align : 'center',    tooltip : true,     width : '150px'},
        {title : SKIAF.i18n.messages['bcm.user.role-type'],      key : 'roleType',        align : 'center',    tooltip : true,        width : '110px'},
        {title : SKIAF.i18n.messages['bcm.user.role-map'], key : 'roleMapId',    align : 'center',    tooltip : true,     width : '200px', render : {type : "roleMapId"}},
        {title : SKIAF.i18n.messages['bcm.user.role-bigin-dt'],      key : 'roleBeginDt',    align : 'center',    tooltip : true,        width : '110px'},    
        {title : SKIAF.i18n.messages['bcm.user.role-end-dt'],      key : 'roleEndDt',    align : 'center',    tooltip : true,        width : '110px'}
    ];

    var roleMapGridInit = {
        autoColumnIndex : true,
        useClassHovering : true,
        fitTableWidth : true,
        headerRowHeight: 30,
        rowOption : {
            defaultHeight : 30
        },
        height:240, /* 전체 height */
        defaultColumnMapping : { resizing : true },
        renderMapping : {
            'link' : {
                renderer : function(value, data, render, mapping) {
                    return '<a href = "' + SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ROLES, data.roleId) + '">' + data.roleId + '</a>';
                }
            },
            'roleMapId' : {
                renderer : function(value, data, render, mapping) {
                    if(data.roleMapType === 'USERGROUP'){
                        return SKIAF.i18n.messages['bcm.user.usergroup'] + ' : ' + data.roleMapId;
                    } else {
                        return SKIAF.i18n.messages['bcm.user.user'] + ' : ' + data.roleMapId;
                    }
                }
            }
        },
        pager : false, columnMapping : roleMapGridColumns, data : []
    };

    var userId = SKIAF.util.getPathVariable(3);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {

        // event 등록
        UserDetailModule.addEvent();

        //grid 초기화
        $('#linkGrid1').alopexGrid(userGroupMapGridInit);
        $('#linkGrid2').alopexGrid(roleMapGridInit);
        
        //button 초기화
        $('#userEditBtn').setEnabled(false);
        $('#userGroupMapBtn').setEnabled(false);
        
        if(userId !== "none"){
            UserDetailModule.getUser(userId);
        }

    };



    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        //사용자 검색
        $('#userSearchBtn').on("click", function(e) {
            UserDetailModule.popupUserSearch();
        });
        
        
        //수정버튼
        $('#userEditBtn').on("click", function(e) {
            let userId = $('#userDetail').getData().userId
            if(userId != null){
                UserDetailModule.popupUpdateUser(userId);
            }
        });

        
        //등록버튼
        $('#userCreateBtn').on("click", function(e){
            UserDetailModule.popupCreateUser();
        });
        
        
        //사용자 검색 Input - Enter Event.
        $('#searchInput').keydown( function(key){
            if($('#userSearchBtn').attr('disabled') != 'disabled') {
                if(key.keyCode == 13){
                    UserDetailModule.popupUserSearch();
                }
            }
        });
        
        //그룹정보 매핑
        $('#userGroupMapBtn').on('click', function(e){
            UserDetailModule.popupUserGroupMap();
        });
        
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 사용자 상세 정보 조회
     */
    this.getUser = function(userId){
        
        var url = '';
        
        if(userId !== "none"){
            
            if(userId == 'my') {
                url = SKIAF.PATH.USERS_DETAIL_INFO_MY;
            } else {
                url = SKIAF.PATH.USERS_DETAIL_INFO;
            }
        }
        
        //url 변경
        history.pushState(null, null, location.origin + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_USERS_DETAIL, userId));
         

        $a.request(SKIAF.util.urlWithParams(url, userId), {
            method : 'GET',
            success : function(res) {
                
                //data 포멧 변경
                myData  = res.data.myInfo;
                userData = res.data.userInfo;
                
                myData.gwIfYn = myData.gwIfYn ? "Y" : "N";
                userData.gwIfYn = userData.gwIfYn ? "Y" : "N";
                
                // 그룹웨어 수신 여부에 따라 수정버튼 활성/비활성
                if(userData.gwIfYn == 'N'){
                    $('#userEditBtn').show();
                } else {
                    $('#userEditBtn').hide();
                }
                
                //sso 인증 여부
                if(userData.ssoYn){
                    $('#ssoYnTd').text('Y');
                }else{
                    //본인의 경우에만 비밀번호 변경 가능. 서버에서 2차검증.
                    if(myData.userId == userData.userId || userId == 'my'){
                        if(userData.gwIfYn == 'N'){
                            $('#ssoYnTd').html('N<button onclick="UserDetailModule.pwChange()" class="Button basic1 small Float-right" id="passwordChange">' + SKIAF.i18n.messages['bcm.user.password-change'] + '</button>');
                            $a.convert($('#ssoYnTd'));
                        } else {
                            $('#ssoYnTd').text('N');
                        }
                    }else{
                        $('#ssoYnTd').text('N');
                    }
                    
                }
                userData.positionRegularInfo = userData.regularYn ? userData.positionName + " / " + SKIAF.i18n.messages['bcm.user.regular-y'] :  userData.positionName + " / " + SKIAF.i18n.messages['bcm.user.regular-n'] ;
                userData.useYn = userData.useYn ? "Y" : "N";
                
                userData.firstLoginYn = userData.firstLoginYn ? "Y" : "N";
                userData.createDate = SKIAF.util.dateFormat(userData.createDate, 'yyyy-MM-dd HH:mm');
                userData.updateDate = SKIAF.util.dateFormat(userData.updateDate, 'yyyy-MM-dd HH:mm');
                if(userData.loginFailCount >= 5){
                    userData.status = SKIAF.i18n.messages['bcm.user.status.locked'];
                }else if(userData.loginFailCount > 0){
                    userData.status = SKIAF.i18n.messages['bcm.user.status.fail'] + "(" + userData.loginFailCount + ")";
                }else{
                    userData.status = SKIAF.i18n.messages['bcm.user.status.normal'];
                }
                
                userGroupMapList = res.data.userGroupMapList;
                userGroupMapList.forEach(function(userGroupMap){
                    userGroupMap.userMapDt = SKIAF.util.dateFormat(userGroupMap.userMapDt, 'yyyy-MM-dd');
                });

                roleMapList = res.data.roleMapList;
                roleMapList.forEach(function(roleMap){
                    roleMap.roleBeginDt = roleMap.roleMapBeginDt.substr(0,4) + "-" + roleMap.roleMapBeginDt.substr(4,2) + "-" + roleMap.roleMapBeginDt.substr(6,2);
                    roleMap.roleEndDt = roleMap.roleMapEndDt.substr(0,4) + "-" + roleMap.roleMapEndDt.substr(4,2) + "-" + roleMap.roleMapEndDt.substr(6,2);
                });

                // data binding
                $('#userDetail').setData(userData);
                $('#linkGrid1').alopexGrid('dataSet', userGroupMapList);
                $('#linkGrid2').alopexGrid('dataSet', roleMapList);
                
                //button enabled 처리
                $('#userEditBtn').setEnabled(true);
                $('#userGroupMapBtn').setEnabled(true);
                
            }
        });
    };
    
    
    /**
     * 사용자 검색(선택) 팝업
     */
    this.popupUserSearch = function() {
        var user = null;
        
        var userData = $('#linkGrid1').alopexGrid('dataGet');
        var selectedUserIds = [];
        
        for(var i = 0, len = userData.length; i < len; i++) {
            selectedUserIds.push(userData[i].userId);
        }

        $a.popup({
            url : SKIAF.PATH.VIEW_USERS_SEARCH,
            title : SKIAF.i18n.messages['bcm.user.user-search'],
            data : {
                multiSelected : false,
                searchText : $('#searchInput').val()
            },
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                
                if (data.type == 'confirm' && data.selectedUsers.length > 0) {
                    UserDetailModule.getUser(data.selectedUsers[0].userId);
                }
            }
        });
    };
    
    /**
    * 사용자 등록 레이어 팝업
    */
    this.popupCreateUser = function() {
       $a.popup({
           url : SKIAF.PATH.VIEW_USERS_CREATE,
           title : SKIAF.i18n.messages['bcm.user.user-register'],
           data : {},
           iframe : false,
           width : 1000,
           height : 600,
           center : true,
           callback : function(res) {
               //등록 성공시 해당 상세로 이동.
               if(res.data.userId != null){
                   UserDetailModule.getUser(res.data.userId);
               }
           }
       });
    };
    
    /**
     * 사용자 수정 레이어 팝업
     */
    this.popupUpdateUser = function(userId) {
        $a.popup({
            url : SKIAF.PATH.VIEW_USERS_UPDATE,
            title : SKIAF.i18n.messages['bcm.user.user-modify'],
            data : { userId : userId },
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(res) {
                //수정 성공시 해당 상세로 이동.
                if(res.data.userId != null){
                    UserDetailModule.getUser(res.data.userId);
                }
            }
        });
    };
     
     /**
      * 개인 - 비밀번호 변경 (로그인한 본인만 가능)
      */
    this.pwChange = function(){
        $a.popup({
            url : SKIAF.PATH.VIEW_USERS_PWCHANGE,
            title : SKIAF.i18n.messages['bcm.user.password-change'],
            data : { user : $('#userDetail').getData() },
            iframe : false,
            width : 700,
            height : 600,
            center : true,
            callback : function(res) {
                //수정 성공시 해당 상세로 이동.
                if(res.data.userId != null){
                    //UserDetailModule.getUser(res.data.userId);
                    UserDetailModule.getUser('my');
                }
            }
        });
    };

    /**
     * 사용자 그룹 매핑 수정 레이어팝업
     */
    this.popupUserGroupMap = function(){
        var pop = $a.popup({
            url: SKIAF.PATH.VIEW_USERS_GROUP_MAP,
            title: SKIAF.i18n.messages['bcm.user.usergroup-info'],
            data : {
                user : $('#userDetail').getData(),
                userGroupMapList : userGroupMapList
            },
            iframe: false,
            movable:true,
            width: 1000,
            height: 600,
            center: true,
            callback : function(res) {
                if(res.data.userId != null){
                    UserDetailModule.getUser(res.data.userId);
                }
            }
        });
        $(pop).addClass('layerpop');
    };

});