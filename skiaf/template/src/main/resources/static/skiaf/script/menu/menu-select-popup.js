/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01869
 * description : 상위메뉴선택 레이어팝업 js
 */
"use strict";
var MenuLayerModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    //부모창 메뉴 ID
    var menuId = '';

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {

        if(param['menuId'] != null && param['menuId'] != ''){
            menuId = param['menuId'];
        }
        
        MenuLayerModule.addEvent();

        MenuLayerModule.viewMenuTree();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        //확인버튼 클릭 이벤트
        $('#btnParentMenuSelect').on('click', function(e) {
            // 선택한 상위메뉴
            var selectedNode = $('#layerMenuTree').getSelectedNode();
            
            if(selectedNode.data == null){
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.valid.parent-menu']);
            }else{
                if(selectedNode.data.menuId == menuId){
                    // 선택 불가한 항목(메뉴자신) 선택시
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.valid.no-parent-select-menu']);
                } else {
                    if($('#layerMenuTree #' + selectedNode.data.menuId).find('a').first().hasClass('off')) {
                        // 선택 불가한 항목(폴더가 아닌유형의 메뉴) 선택시
                        SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.valid.no-parent-select-menu']);
                    } else {
                        $a.close({
                            menuId : selectedNode.data.menuId,
                            menuName1 : selectedNode.data.menuName1
                        }); // 데이터를 팝업을 띄운 윈도우의 callback에 전달
                    }
                }
            }
            
        });
        
        //취소버튼 클릭 이벤트
        $('#btnParentMenuCancel').on('click', function(e) {
            $a.close();
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 트리 view
     */
    this.viewMenuTree = function() {

        $a.request(SKIAF.PATH.MENUS, {
            method : 'GET',
            success : function(res) {

                //최상위 상위메뉴없음 로우 추가
                res.data.unshift({menuId : SKIAF.CONSTATNT.ROOT_MENU_ID, parentMenuId : null, menuName1 : SKIAF.i18n.messages['bcm.menu.parent-menu-root'], useYn : true});
                
                $('#layerMenuTree').setDataSource(res.data);
                $("#layerMenuTree").expandAll();
                
                for(var i=0; i<res.data.length; i++){
                    
                    // 메뉴 유형 폴더가 아니 거나, 사용여부 "N" 이거나, 메뉴본인일경우 상위메뉴로 선택 못함(off class 바인딩)
                    if(res.data[i].menuType != SKIAF.ENUM.MENU_TYPE.FOLDER || !res.data[i].useYn || res.data[i].menuId == menuId) {
                        if(res.data[i].menuId != SKIAF.CONSTATNT.ROOT_MENU_ID) {
                            $('#layerMenuTree #' + res.data[i].menuId + ' > a').addClass('off');
                        }
                    }
                }
            }
        });
    };
});