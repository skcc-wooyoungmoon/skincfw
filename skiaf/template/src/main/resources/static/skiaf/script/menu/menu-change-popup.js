/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01869
 * description : 메뉴 트리변경 레이어팝업 js
 */
"use strict";
var MenuTreeLayerModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var treeGridObj = {
        autoColumnIndex : true,
        height : 435,
        fitTableWidth: true,
        header : false,
        enableDefaultContextMenu : false,
        disableTextSelection : true,
        defaultSorting:{
            sortingColumn: 2,
            sortingDirection: 'asc'
        },
        columnMapping : [ {
            dragdropColumn : true,
            width : 30
        }, {
            key : "menuName1",
            title : SKIAF.i18n.messages['bcm.menu.menu-name'],
            width : 200,
            treeColumn : true,
            treeColumnHeader : true
        }, {
            key : "menuSortSeq",
            title : SKIAF.i18n.messages['bcm.menu.sort-number'],
            width : 200,
            hidden : true
        }],
        tree : {
            useTree : true,
            idKey : "menuId", // 노드를 지시하는 유일한 값이 저장된 키값
            parentIdKey : "parentMenuId", // 자신의 상위(parent) 노드를 지시하는 ID가 저장된 키값
            expandedKey : "expandedValue", // 데이터가 그리드에 입력되는 시점에 초기 펼쳐짐 여부를 저장하고 있는 키값
            rootNodeParentIdValue : SKIAF.CONSTATNT.ROOT_MENU_ID
        },
        data : []
    };
    
        

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {

        MenuTreeLayerModule.addEvent();

        $("#menuTreeGrid").alopexGrid(treeGridObj);
        
        MenuTreeLayerModule.viewMenuTreeGrid();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        //취소버튼 클릭 이벤트
        $('#btnTreeSaveCancel').on('click', function(e) {
            $a.close({
                type : 'cancel'
            });
        });

        //확인버튼 클릭 이벤트
        $('#btnTreeSave').on('click', function(e) {
            MenuTreeLayerModule.saveMenuTree();
        });
        

        // Drag후 sortSeq를 재정의한다.
        $("#menuTreeGrid").on('rowDragDropEnd', function(e) {
            var evObj = AlopexGrid.parseEvent(e);
            var $grid = evObj.$grid;
            var dragData = evObj.dragDataList[0];
            var parentMenuIdOfDragData = dragData.parentMenuId;
            var siblingNodes = $grid.alopexGrid('dataGet', {"parentMenuId" : parentMenuIdOfDragData});

            siblingNodes.forEach(function(value, i) {
                value.menuSortSeq = i + 1;
                $grid.alopexGrid('dataEdit', value, {'menuId' : value.menuId});
            });
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 트리 그리드 view
     */
    this.viewMenuTreeGrid = function() {
        $a.request(SKIAF.PATH.MENUS, {
            method : 'GET',
            success : function(res) {
                for (var i = 0, len = res.data.length; i < len; i++) {
                    res.data[i].expandedValue = true; //메뉴 모두 펼침
                }
                $('#menuTreeGrid').alopexGrid('dataSet', res.data);
            }
        });
    };

    /**
     * 메뉴 트리 저장
     */
    this.saveMenuTree = function() {
        var tree = $('#menuTreeGrid').alopexGrid('dataGet');

        var updateList = [];
        for (var i = 0; i < tree.length; i++) {
            updateList.push({
                menuId : tree[i].menuId,
                parentMenuId : tree[i].parentMenuId,
                menuSortSeq : tree[i].menuSortSeq
            });
        }

        $a.request(SKIAF.PATH.MENUS_TREE, {
            method : 'PATCH',
            array : updateList,
            success : function(res) {
                $a.close({
                    type : 'confirm'
                });
            }
        });
    };
});