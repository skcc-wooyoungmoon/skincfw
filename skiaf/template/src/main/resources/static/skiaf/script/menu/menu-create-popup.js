/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.04 - in01869
 * description : 메뉴 등록 레이어팝업 js
 */
"use strict";
var MenuCreateModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    //메뉴 추가시 메뉴ID 중복체크 여부
    var menuIdDuplChkYn = false;
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        $('#menuId').validator();
        
        MenuCreateModule.addEvent();

        MenuCreateModule.setMenuData();
        
        // 오픈유형 select option
        var order = [
            SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW,
            SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB,
            SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW ];

        $('#openType').setData({
            options : SKIAF.util.setLOVSelectDataOptionWithEnum(SKIAF.ENUM.MENU_OPEN_TYPE, 'bcm.menu.open-type-', order),
            openType : SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {
        
        // 메뉴 유형 변경 이벤트 바인딩 
        MenuCreateModule.menuTypeChange();
        
        // 메뉴ID [중복체크] 버튼 클릭 이벤트
        $('#btnDuplChk').on('click',function(e) {
            var menuId = $('#menuId').val();
            MenuCreateModule.menuIdDuplChk(menuId);
        });
        
        //메뉴 ID가 변경되면 중복체크여부 false
        $('#menuId').on('keyup', function(e){
            menuIdDuplChkYn = false;
            $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id-dupl-check']).addClass('Color-danger').removeClass('Color-confirm');
        });
        
        // [상위메뉴선택] 버튼 클릭
        $('#btnParentMenuSelectByAdd').on('click', function(){

            $a.popup({
                title : SKIAF.i18n.messages['bcm.menu.parent-menu-select'],
                url : SKIAF.PATH.VIEW_MENUS_SELECT,
                data : {
                    menuId : ''
                },
                iframe : false,
                movable:true,
                width : 400,
                height : 600,
                center : true,
                callback : function(data) {
                    
                    if (data !== null) {
                        var parentData = {
                            parentMenuId : data.menuId,
                            parentMenuName : data.menuName1
                        };
                        $('#menuAddDetail').setData(parentData);
                    }
                }
            });
        });
        
        // [프로그램선택] 버튼 클릭
        $('#btnProgramSelectByAdd').on('click', function(){
            $a.popup({
                title : SKIAF.i18n.messages['bcm.menu.program-search'],
                url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_SELECT,
                data : {
                    programType : 'VIEW'  
                },
                iframe : false,
                width : 1000,
                height : 600,
                center : true,
                callback : function(data) {
                    if (data !== null) {

                        var parentData = {
                            program : {
                                programId : data.programId
                            }
                        };
                        $('#menuAddDetail').setData(parentData);
                    }
                }
            }); 
        });
        
        // [저장] 버튼 클릭 이벤트
        $('#saveMenu').on('click', function(e) {
            MenuCreateModule.saveMenu();
        });
        
        // [취소] 버튼 클릭 이벤트
        $('#saveCancel').on('click', function(e) {       
            $a.close({
                type : 'cancel'
            });
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 유형 변경 이벤트
     */
    this.menuTypeChange = function() {
        
        var $menuType = $('td.menu-type input');

        $menuType.on('change', typeShow);
        
        function typeShow(e) {
            var idx = $(this).filter('[type="radio"]:checked').parent().index(),
                $tr = $(this).closest('table').find('tr.menu-type');

            if (idx === 1) {
                $('#urlAddr').val('');
                $tr.filter('.url').hide();
                $tr.filter('.program').show();
                $tr.filter('.openType').show();
            } else if (idx === 2) {
                $('#programId').val('');
                $tr.filter('.program').hide();
                $tr.filter('.url').show();
                $tr.filter('.openType').show();
            }else{
                $('#programId').val('');
                $('#urlAddr').val('');
                $tr.filter('.program').hide();
                $tr.filter('.url').hide();
                $tr.filter('.openType').hide();
            }
        }
    };
    
    /**
     * 메뉴 추가 기본 데이터 셋
     */
    this.setMenuData = function() {
        
        var defaultAddMenu = {
            menuType : SKIAF.ENUM.MENU_TYPE.FOLDER,
            useYn : 'true'
        };
        
        $('#menuAddDetail').setData(defaultAddMenu);
    };
    
    /**
     * 메뉴ID 중복체크
     */
    this.menuIdDuplChk = function(menuId) {
        if ($.trim(menuId) == '') {
            $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id']).addClass('Color-danger').removeClass('Color-confirm');
        } else {
            
            if(menuId != SKIAF.CONSTATNT.ROOT_MENU_ID && !SKIAF.util.checkId(menuId)){
                $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.common.id.validation']).addClass('Color-danger').removeClass('Color-confirm');
                return false;
            }
            
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_DUPLICATE, menuId), {
                method : 'GET',
                success : function(res) {
                    if (res.data) {
                        $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id-dupl']).addClass('Color-danger').removeClass('Color-confirm');
                    } else {
                        $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id-confirm']).addClass('Color-confirm').removeClass('Color-danger');
                        menuIdDuplChkYn = true;
                    }
                }
            });
        }
    };
    
    /**
     * 메뉴 저장
     */
    this.saveMenu = function() {
        
        var saveData = $('#menuAddDetail').getData();
        
        if(saveData.program.programId != null) {
            saveData.programId = saveData.program.programId;
        }
        
        if(MenuCreateModule.menuValidate(saveData)) {

            $a.request(SKIAF.PATH.MENUS, {
                method : 'POST',
                data : saveData,
                success : function(res) {
                    $a.close({
                        type : 'success',
                        menuId : saveData.menuId
                    });
                }
            });
        }
    };
    
    /**
     * 메뉴 vaidation
     */
    this.menuValidate = function(saveData) {
        
        if(saveData.menuId == null || $.trim(saveData.menuId) == ''){
            $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id']).addClass('Color-danger').removeClass('Color-confirm');
            return false;
        } else {
            if(!menuIdDuplChkYn){
                $('#duplChkMessage').text(SKIAF.i18n.messages['bcm.menu.valid.menu-id-dupl-check']).addClass('Color-danger').removeClass('Color-confirm');
                return false;
            }
        }

        // Validation Check menuAddDetail
        if (!$('#menuAddDetail').find('#menuNameRequiredInput').validate()) {
            $('#menuAddDetail').find('#menuNameRequiredInput').validator();
            return false;
        }
        
        if(saveData.menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
            if (!$('#menuAddDetail').find('#programId').validate()) {
                $('#menuAddDetail').find('#programId').validator();
                return false;
            }
            if(saveData.program.programId != null) {
                saveData.programId = saveData.program.programId;
            }
        } else if(saveData.menuType == SKIAF.ENUM.MENU_TYPE.URL) {
            if (!$('#menuAddDetail').find('#urlAddr').validate()) {
                $('#menuAddDetail').find('#urlAddr').validator();
                return false;
            }
        }
        
        if (!$('#menuAddDetail').find('#parentMenuId').validate()) {
            $('#menuAddDetail').find('#parentMenuId').validator();
            return false;
        }
        
        if (!$('#menuAddDetail').find('#menuSortSeq').validate()) {
            $('#menuAddDetail').find('#menuSortSeq').validator();
            return false;
        }

        return true;
    };

});