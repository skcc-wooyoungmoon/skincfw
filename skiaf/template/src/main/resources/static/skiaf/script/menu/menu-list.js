/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.03 - in01869
 * description : 메뉴 관리 페이지 js
 */
"use strict";
var MenuModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * alopex tree 컴포넌트 setup
     */
    $a.setup('tree', {
        idKey : 'menuId',
        parentIdKey : 'parentMenuId',
        textKey : 'menuName1'
    });

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 메뉴 트리 검색
    var skiafTree = {
        menuIndex : -1,
        treeName  : "",
        keyword   : "",
        inputNode : null,

        init : function (key, mt)
            {
                if (key != null && key != "") this.inputNode = key;
                if (mt  != null && mt  != "") this.treeName = mt;
                this.keyword = this.inputNode.val().toUpperCase();
                this.menuIndex = -1;
            },

        findMenu :  function(){
                if (this.keyword == "")  return 0;
                if (this.menuNode == "") return 0;

                var $m = $(this.treeName);
                var $a = $m.find("a");
                var $l = $m.find("li");

                var found = 0;
                for (var idx=0; idx<$a.length; idx++) {

                    // skip prior items to last found item
                    if (idx <= this.menuIndex ) continue;

                    var nodeVal = $a[idx].innerText;
                    var nodeId  = $l[idx].id;

                    var pos = nodeVal.toUpperCase().indexOf(this.keyword);
                    if (pos >= 0) {
                        found = 1;
                        this.menuIndex = idx; // remember found item's index
                        var node = $m.getNode(nodeId, "id");
                        $m.setSelected(node);
                        MenuModule.viewMenu(nodeId);

                        // scroll to current Node
                        var d = $m[0].parentNode;
                        var y = node.node.offsetTop;
                        if ((d.offsetHeight <= y + 20) || (d.scrollTop > y))
                            node.node.scrollIntoView();
                        break;
                    }

                }

                return found;
             },

        findFirst :  function(){
                var res = this.findMenu();
                if (!res) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.no-equal-menu']);
                    this.inputNode.clear();
                }

            },

        findNext :  function(){
                 var res = this.findMenu();

                 if (!res && this.menuIndex >= 0) {
                     SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.menu.find-first'] , function(target){
                         target.init();
                         target.findMenu();
                     }, this);
                 }
            }

    };

    //메뉴 상세 오브젝트
    var menuDetailData = {};
    
    // 권한 상세 Grid
    var roleInfoGridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        defaultColumnMapping: {
            resizing: true
        },
        height:188, /* 전체 height */ 
        columnMapping: [
            {
                title: SKIAF.i18n.messages['bcm.menu.role-id'],
                key: 'roleId',
                align: 'center',
                width : '120px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-name'],
                key: 'roleName',
                align: 'center',
                width : '140px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-desc'],
                key: 'roleDesc',
                align: 'center',
                width : '140px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-create-by'],
                key: 'updateBy',
                align: 'center',
                width : '90px',
                tooltip: false
            }, {
                title: SKIAF.i18n.messages['bcm.menu.role-create-date'],
                key: 'updateDateTxt',
                align: 'center',
                width : '170px',
                tooltip: false,
                render : function( value, data, render, mapping) {
                    return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd HH:mm');
                }
            }
        ],
        data: []
    };
    

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {
        
        // 이벤트 바인딩
        MenuModule.addEvent();

        // 메뉴 관리 화면 set
        this.viewMenuTree();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 메뉴 [검색] 버튼 클릭
        $('#btnMenuFind').on('click', function () {
            skiafTree.init($('#menuSearchKeyword'), '#menuTree');
            skiafTree.findFirst();
        });
        
        // 메뉴 [검색] 다음 버튼 클릭
        $('#btnMenuNext').on('click', function () {
            skiafTree.findNext();
        });
        
        // 메뉴 [전체 열기] 버튼 클릭 이벤트
        $('#btnMenuTreeExpand').on('click', function(e){
            $('#menuTree').expandAll();
        });
        
        // 메뉴 [전체 닫기] 버튼 클릭 이벤트
        $('#btnMenuTreeCollapse').on('click', function(e){
            $('#menuTree').collapseAll();
        });
        
        // 메뉴 [순서 변경] 버튼 클릭 이벤트
        $('#btnMenuTreeChange').on('click', function(e) {
            MenuModule.popupMenuTreeChange();
        });

        // 메뉴트리 선택 이벤트
        $("#menuTree").on("select", function(e, node) {
            var menuId = node.data.menuId;
            MenuModule.viewMenu(menuId);
        });

        // [메뉴 등록] 버튼 클릭 이벤트
        $('#btnMenuAdd').on('click', function(e) {
            MenuModule.viewMenuAddPopup();
        });
        
        // [메뉴 수정] 버튼 클릭 이벤트
        $('#btnMenuEdit').on('click', function(e) {
            MenuModule.viewMenuEditPopup();
        });
        
        // [권한 수정] 버튼 클릭 이벤트
        $('#btnRoleEdit').on('click', function(e) {
            var node = $('#menuTree').getSelectedNode();
            MenuModule.viewRoleEdit(node.data);
        });
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 트리 view
     */
    this.viewMenuTree = function(selectMenuId) {

        $a.request(SKIAF.PATH.MENUS, {
            method : 'GET',
            success : function(res) {

                if (res.data.length > 0) {
                    
                    $('#menuTree').setDataSource(res.data);
                    $("#menuTree").expandAll();
                    
                    for (var i = 0; i < res.data.length; i++) {                        
                        // 사용여부 "N" class 바인딩
                        if (!res.data[i].useYn) {
                            $('#' + res.data[i].menuId + ' > a').addClass('off');
                        }
                    }
                    
                    // 선택한 메뉴ID 없을경우 트리 최상위 메뉴를 상세에 노출
                    if(selectMenuId == null){                    
                        selectMenuId = $('#menuTree li').attr('id');
                    }
                    
                    // 메뉴 상세 화면
                    MenuModule.viewMenu(selectMenuId);
                    
                } else {
                    // 메뉴가 없을경우
                    $('#menuTreeContent').html('<p>'+ SKIAF.i18n.messages['bcm.menu.menu-empty'] +'</p>');
                    
                    // 메뉴 권한 정보
                    $('#menuRoleField').hide();
                }
            }
        });
    };

    /**
     * 메뉴 상세 View
     */
    this.viewMenu = function(menuId) {
        
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_DETAIL, menuId), {
            method : 'GET',
            success : function(res) {

                // 메뉴 유형에 따라 필드 노출 or 비노출
                if(res.data.menuType == SKIAF.ENUM.MENU_TYPE.FOLDER) {
                    $('#menuDetail').find('#programField').hide();
                    $('#menuDetail').find('#urlField').hide();
                    
                    // 메뉴 권한 정보
                    $('#menuRoleField').hide();
                    
                } else if (res.data.menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
                    $('#menuDetail').find('#programField').show();
                    $('#menuDetail').find('#urlField').hide();
                    
                    // 메뉴 권한 상세 Grid
                    $('#roleInfoGrid').alopexGrid(roleInfoGridObj);
                    MenuModule.setMenuRoleGridData(res.data.menuId);
                    
                    // 메뉴 권한 정보
                    $('#menuRoleField').show();
                    
                } else if(res.data.menuType == SKIAF.ENUM.MENU_TYPE.URL) {
                    $('#menuDetail').find('#programField').hide();
                    $('#menuDetail').find('#urlField').show();
                    
                    // 메뉴 권한 상세 Grid
                    $('#roleInfoGrid').alopexGrid(roleInfoGridObj);
                    MenuModule.setMenuRoleGridData(res.data.menuId);
                    
                    // 메뉴 권한 정보
                    $('#menuRoleField').show();
                } 

                // 메뉴 상세에 setData
                var formatData = MenuModule.setMenuDetailTxtFormat(res.data);
                
                $('#menuDetail').find('td').text(''); //초기화
                $('#menuDetail').setData(formatData);
                
                menuDetailData = formatData; // 가져온 메뉴저장
                
                // 메뉴트리 선택
                var node = $('#menuTree').getNode(menuId, 'id');
                $('#menuTree').setSelected(node);

            }
        });
    };

    /**
     * 메뉴 상세 텍스트 포맷
     */
    this.setMenuDetailTxtFormat = function(data) {

        // 날짜 포맷 변환
        data.createDate = SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd HH:mm');
        data.updateDate = SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd HH:mm');
        
        // 상위메뉴명
        if(data.parentMenuId == SKIAF.CONSTATNT.ROOT_MENU_ID){
            data.parentMenuName = SKIAF.i18n.messages['bcm.menu.parent-menu-root'];
            data.parentMenuId = SKIAF.CONSTATNT.ROOT_MENU_ID;
        } else {
            data.parentMenuName = $('#menuTree').getNode(data.parentMenuId, 'id').menuName1;
            data.parentMenuId = $('#menuTree').getNode(data.parentMenuId, 'id').menuId;
        }

        // 메뉴 유형 한글변환
        var menuType = data.menuType;
        if (menuType == SKIAF.ENUM.MENU_TYPE.FOLDER) {
            data.menuTypeTxt = SKIAF.i18n.messages['bcm.menu.menu-type-folder'];
        } else if (menuType == SKIAF.ENUM.MENU_TYPE.PROGRAM) {
            data.menuTypeTxt = SKIAF.i18n.messages['bcm.menu.menu-type-program'];
            // 오픈 유형 한글변환
            if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-c'];
            } else if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-t'];
            } else if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-p'];
            }
        } else if (menuType == SKIAF.ENUM.MENU_TYPE.URL) {
            data.menuTypeTxt = SKIAF.i18n.messages['bcm.menu.menu-type-url'];                    
            // 오픈 유형 한글변환
            if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-c'];
            } else if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-t'];
            } else if(data.openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW) {
                data.openTypeTxt = SKIAF.i18n.messages['bcm.menu.open-type-p'];
            }
        }

        // 사용여부 변환
        var useYn = data.useYn;
        if (useYn) {
            data.useYn = 'true';
            data.useYnTxt = 'Y';
        } else {
            data.useYn = 'false';
            data.useYnTxt = 'N';
        }
        
        return data;
    };
    
    /**
     * 메뉴 권한 데이터 바인딩 (권한상세, 권한수정 화면)
     */
    this.setMenuRoleGridData = function(menuId, type) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.MENUS_ROLE_MAP, menuId), {
            method : 'GET',
            success : function(res) {
                $('#roleInfoGrid').alopexGrid('dataSet', res.data);
            }
        });
    };

    /**
     * 메뉴 추가 팝업 View
     */
    this.viewMenuAddPopup = function() {
        
        var pop = $a.popup({
            url: SKIAF.PATH.VIEW_MENUS_CREATE,
            title: SKIAF.i18n.messages['bcm.menu.menu-add'],
            iframe: false,
            movable:true,
            width: 1000,
            height: 600,
            center: true,
            callback : function(data) {
                if(data !== null) {
                    if(data.type == 'success') {
                        var addMenuId = data.menuId;
                        MenuModule.viewMenuTree(addMenuId);
                    }
                }
            }
        });
    };
    
    /**
     * 메뉴 수정 팝업 View
     */
    this.viewMenuEditPopup = function() {
        
        var node = $('#menuTree').getSelectedNode();

        if(node.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.modify-menu-select']);
            return false;
        }

        var pop = $a.popup({
            url: SKIAF.PATH.VIEW_MENUS_UPDATE,
            title: SKIAF.i18n.messages['bcm.menu.menu-modify'],
            data : {
                'menu' : node.data
            },
            iframe: false,
            movable:true,
            width: 1000,
            height: 600,
            center: true,
            callback : function(data) {
                if(data !== null) {
                    if(data.type == 'success') {
                        var modifyMenuId = data.menuId;
                        MenuModule.viewMenuTree(modifyMenuId);
                    }
                }
            }
        });
    };
    
    /**
     * 권한 수정 레이어팝업 화면 View
     */
    this.viewRoleEdit = function(menu) {
        
        $a.popup({
            url: SKIAF.PATH.VIEW_MENUS_ROLE,
            title: SKIAF.i18n.messages['bcm.menu.menu-role-info'],
            data : {
                menu : menu
            },
            iframe: false,
            movable:true,
            width: 1000,
            height: 600,
            center: true,
            callback : function(data) {
                if(data !== null) {
                    if(data.type == 'success') {
                        var modifyMenuId = data.menuId;
                        MenuModule.viewMenuTree(modifyMenuId);
                    }
                }
            }
        });
    };

    
    /**
     * 메뉴 트리 순서변경 레이어 팝업
     */
    this.popupMenuTreeChange = function(){
        
        if($("#menuTree").length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.menu.menu-empty']);
            return false;
        }
    
        $a.popup({
            url: SKIAF.contextPath + SKIAF.PATH.VIEW_MENUS_CHANGE,
            title: SKIAF.i18n.messages['bcm.menu.menu-order-change'],
            iframe: false,
            movable:true,
            width: 400,
            height: 600,
            center: true,
            callback : function(data) {
                
                if (data !== null) {
                    if(data.type == 'confirm') {
                        MenuModule.viewMenuTree();
                    }
                }
            }
        });
    };
});