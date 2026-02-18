/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.04 - in01869
 * description : 메뉴 상세수정 레이어팝업 js
 */
"use strict";
var MenuDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {

        MenuDetailModule.addEvent();

        MenuDetailModule.setMenuData(param['menu']);
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {
        
        // 메뉴 유형 변경 이벤트 바인딩 
        MenuDetailModule.menuTypeChange();
        
        // [상위메뉴선택] 버튼 클릭
        $('#btnParentMenuSelectByEdit').on('click', function(){
            
            // 자신을 상위메뉴로 선택할 수 없음
            var node = $('#menuTree').getSelectedNode();
            var menuId = node.data.menuId;

            $a.popup({
                title : SKIAF.i18n.messages['bcm.menu.parent-menu-select'],
                url : SKIAF.PATH.VIEW_MENUS_SELECT,
                data : {
                    menuId : menuId
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
                        $('#menuEditDetail').setData(parentData);
                    }
                }
            });
        });
        
        // [프로그램선택] 버튼 클릭
        $('#btnProgramSelectByEdit').on('click', function(){
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
                        $('#menuEditDetail').setData(parentData);
                    }
                }
            }); 
        });
        
        // [저장] 버튼 클릭 이벤트
        $('#saveMenuByEdit').on('click', function(e) {
            MenuDetailModule.saveMenu();
        });
        
        // [취소] 버튼 클릭 이벤트
        $('#saveCancelByEdit').on('click', function(e) {       
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
        
        $menuType.refresh();

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
    this.setMenuData = function(menu) {
        
        // 날짜 포맷 변환
        menu.createDateTxt = SKIAF.util.dateFormat(menu.createDate, 'yyyy-MM-dd HH:mm');
        menu.updateDateTxt = SKIAF.util.dateFormat(menu.updateDate, 'yyyy-MM-dd HH:mm');
        
        // 상위메뉴명
        if(menu.parentMenuId == SKIAF.CONSTATNT.ROOT_MENU_ID){
            menu.parentMenuName = SKIAF.i18n.messages['bcm.menu.parent-menu-root'];
            menu.parentMenuId = SKIAF.CONSTATNT.ROOT_MENU_ID;
        } else {
            menu.parentMenuName = $('#menuTree').getNode(menu.parentMenuId, 'id').menuName1;
            menu.parentMenuId = $('#menuTree').getNode(menu.parentMenuId, 'id').menuId;
        }
        
        // 사용여부
        if(menu.useYn) {
            menu.useYn = 'true';
        } else {
            menu.useYn = 'false';
        }
        
        // 메뉴 유형에 따라 필드 노출 or 비노출
        if(menu.menuType == SKIAF.ENUM.MENU_TYPE.FOLDER) {
            $('#menuEditDetail').find('#programField').hide();
            $('#menuEditDetail').find('#urlField').hide();
            $('#menuEditDetail').find('#openTypeField').hide();
        } else if (menu.menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
            $('#menuEditDetail').find('#programField').show();
            $('#menuEditDetail').find('#urlField').hide();
            $('#menuEditDetail').find('#openTypeField').show();
        } else if(menu.menuType == SKIAF.ENUM.MENU_TYPE.URL) {
            $('#menuEditDetail').find('#programField').hide();
            $('#menuEditDetail').find('#urlField').show();
            $('#menuEditDetail').find('#openTypeField').show();
        } 
        
        // 오픈유형 select option
        var order = [
            SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW,
            SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB,
            SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW ];

        var openTypeOptions = SKIAF.util.setLOVSelectDataOptionWithEnum(SKIAF.ENUM.MENU_OPEN_TYPE, 'bcm.menu.open-type-', order);
        
        $('#openType').setData({options : openTypeOptions, openType : menu.openType});
        $('#menuEditDetail').setData(menu);
        
        // select 디자인 적용
        $('#openTypeByEdit').refresh();
    };
    
    /**
     * 메뉴 저장
     */
    this.saveMenu = function() {
        
        var saveData = $('#menuEditDetail').getData();
        
        if(saveData.program.programId != null) {
            saveData.programId = saveData.program.programId;
        }
        
        if(MenuDetailModule.menuValidate(saveData)) {
            
            //선택한 상위메뉴가 본인 자식인지 체크
            var obj = $('#menuTree').getObject(saveData.menuId, 'id');
            var selectedParentIdIsChildren = false;
            if($(obj).has('ul').length > 0) {
                $(obj).find('ul').each(function(){
                    $(this).find('li').each(function(i){
                        if($(this).attr('id') == saveData.parentMenuId) {
                            selectedParentIdIsChildren = true;
                        }
                    });
                });
            }
            
            if(saveData.program.programId != null) {
                saveData.programId = saveData.program.programId;
            }
            saveData.selectedParentIdIsChildren = selectedParentIdIsChildren;

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_DETAIL, saveData.menuId), {
                method : 'PATCH',
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

        // Validation Check
        if (!$('#menuEditDetail').find('#menuNameRequiredInput').validate()) {
            $('#menuEditDetail').find('#menuNameRequiredInput').validator();
            return false;
        }
        
        if(saveData.menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
            if (!$('#menuEditDetail').find('#programId').validate()) {
                $('#menuEditDetail').find('#programId').validator();
                return false;
            }
            if(saveData.program.programId != null) {
                saveData.programId = saveData.program.programId;
            }
        } else if(saveData.menuType == SKIAF.ENUM.MENU_TYPE.URL) {
            if (!$('#menuEditDetail').find('#urlAddr').validate()) {
                $('#menuEditDetail').find('#urlAddr').validator();
                return false;
            }
        }
        
        if (!$('#menuEditDetail').find('#parentMenuId').validate()) {
            $('#menuEditDetail').find('#parentMenuId').validator();
            return false;
        }
        
        if (!$('#menuEditDetail').find('#menuSortSeq').validate()) {
            $('#menuEditDetail').find('#menuSortSeq').validator();
            return false;
        }

        return true;
    };

});