/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.30 - in01869
 * description : lnb 메뉴 트리 js
 */
"use strict";
var LnbMenuModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {
        LnbMenuModule.getLnbMenuHtml();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 메뉴 유형 프로그램 새창 팝업
     */
    this.windowPopup = function(url) {
        window.open(SKIAF.contextPath + url, "", "width=" + screen.width + ",height=" + screen.height);
    };

    /**
     * 메뉴 유형 URL 새창 팝업
     */
    this.windowPopupUrl = function(url) {
        window.open(url, "", "width=" + screen.width + ",height=" + screen.height);
    };
    
    /**
     * 선택한 메뉴 스타일 적용
     */
    this.selectedMenuAddClass = function(checkPath) {
        var count = 0;
        $('.lnb').find('a').each(function() {
            if ($(this).attr('href').indexOf('view') != -1) {
                // 선택한 메뉴 상위 ul 펼치고 li 스타일 적용
                if ($(this).attr('href') == checkPath) {
                    $(this).closest('ul').attr('style', 'display : block;');
                    $(this).closest('ul').parent('li').addClass("active");
                    $(this).parent('li').addClass("active");
                    count++;
                }
            }
        });
        return count > 0 ? true : false;
    };
    
    /**
     * 선택한 메뉴 체크
     */
    this.selectMenuCheck = function() {
        var path = SKIAF.util.getPath();
        
        if(path.length > 0) {
            var pathArr = path.split('/');

            for(var i = (pathArr.length - 1), iLen = (pathArr.length - 1), pass = true; i >= 0 && pass; i--) {
                var checkPath = '/';
                for(var j = 0, jLen = i; j <= i; j++) {
                    if(j == i) {
                        checkPath += pathArr[j];
                    } else {
                        checkPath += pathArr[j] + '/';
                    }
                }
                
                if(LnbMenuModule.selectedMenuAddClass(checkPath)) {
                    pass = false;
                }
            }
        }
    };

    /**
     * lnb html 생성
     */
    this.getLnbMenuHtml = function() {

        // 다국어 설정에 따른 메뉴명 번호 설정
        var menuNameNumber = SKIAF.i18n.langSupportedCodes.indexOf(SKIAF.i18n.langCurrentCode) + 1;

        $a.request(SKIAF.PATH.MENUS_TREE,{
            method : 'GET',
            success : function(res) {
                var menuList = res.data;

                var LnbMenuHtmlLinkTypes = {
                    Folder : function() {
                        return $('<a href="javascript:void(0);"></a>');
                    },
                    Program : function(openType, aUrl) {
                        if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW) {
                            return $('<a href="' + aUrl + '" target="_self"></a>');
                        } else if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB) {
                            return $('<a href="' + aUrl + '" target="_blank"></a>');
                        } else if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW) {
                            return $('<a href="javascript:void(0);" onclick="LnbMenuModule.windowPopup(\'' + aUrl + '\')"></a>');
                        }
                    },
                    Url : function(openType, aUrl) {
                        if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.CURRENT_WINDOW) {
                            return $('<a href="' + aUrl + '" target="_self"></a>');
                        } else if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_TAB) {
                            return $('<a href="' + aUrl + '" target="_blank"></a>');
                        } else if (openType == SKIAF.ENUM.MENU_OPEN_TYPE.NEW_WINDOW) {
                            return $('<a href="javascript:void(0);" onclick="LnbMenuModule.windowPopupUrl(\'' + aUrl + '\')"></a>');
                        }
                    }
                };

                var menu = '';
                var $li = '';
                var $a = '';

                var child = '';
                var $childUl = '';
                var $childLi = '';
                var $childA = '';

                var grandChild = '';
                var $grandChildUl = '';
                var $grandChildLi = '';
                var $grandChildA = '';

                for (var i = 0, iLen = menuList.length; i < iLen; i++) {
                    menu = menuList[i];

                    $li = $('<li>');
                    $a = '';

                    switch(menu.menuType) {
                    case SKIAF.ENUM.MENU_TYPE.FOLDER : $a = LnbMenuHtmlLinkTypes.Folder();break;
                    case SKIAF.ENUM.MENU_TYPE.PROGRAM: $a = LnbMenuHtmlLinkTypes.Program(menu.openType, menu.program.programPath);break;
                    case SKIAF.ENUM.MENU_TYPE.URL    : $a = LnbMenuHtmlLinkTypes.Url(menu.openType, menu.urlAddr);break;
                    default:
                    }
                    // 다국어 메세지 등록이 안되어있을 경우 default 언어인 menuName1 이 노출된다.
                    $a.text(menu['menuName' + menuNameNumber] || menu['menuName1']);

                    if (menu.children.length > 0) {

                        $childUl = $('<ul style="display: none;">');

                        for (var j = 0, jLen = menu.children.length; j < jLen; j++) {
                            child = menu.children[j];

                            $childLi = $('<li>');
                            $childA = '';

                            switch(child.menuType) {
                            case SKIAF.ENUM.MENU_TYPE.FOLDER : $childA = LnbMenuHtmlLinkTypes.Folder();break;
                            case SKIAF.ENUM.MENU_TYPE.PROGRAM: $childA = LnbMenuHtmlLinkTypes.Program(child.openType, child.program.programPath);break;
                            case SKIAF.ENUM.MENU_TYPE.URL    : $childA = LnbMenuHtmlLinkTypes.Url(child.openType, child.urlAddr);break;
                            default:
                            }
                            $childA.text(child['menuName' + menuNameNumber] || child['menuName1']);

                            if (child.children.length > 0) {
                                grandChild = '';
                                $grandChildUl = $('<ul style="display: none;">');
                                $grandChildLi = '';
                                $grandChildA = '';

                                for (var z = 0, zLen = child.children.length; z < zLen; z++) {
                                    grandChild = child.children[z];

                                    $grandChildLi = $('<li>');
                                    $grandChildA = '';

                                    switch(grandChild.menuType) {
                                    case SKIAF.ENUM.MENU_TYPE.FOLDER : $grandChildA = LnbMenuHtmlLinkTypes.Folder();break;
                                    case SKIAF.ENUM.MENU_TYPE.PROGRAM: $grandChildA = LnbMenuHtmlLinkTypes.Program(grandChild.openType, grandChild.program.programPath);break;
                                    case SKIAF.ENUM.MENU_TYPE.URL    : $grandChildA = LnbMenuHtmlLinkTypes.Url(grandChild.openType, grandChild.urlAddr);break;
                                    default:
                                    }
                                    $grandChildA.text(grandChild['menuName' + menuNameNumber] || grandChild['menuName1']);
                                    
                                    $grandChildLi.append($grandChildA);
                                    $grandChildUl.append($grandChildLi);
                                }

                                $childLi.append($childA);
                                $childLi.append($grandChildUl);
                            } else {
                                //grandChild 없을 때
                                $childLi.append($childA);
                            }
                            $childUl.append($childLi);
                        }
                        $li.append($a);
                        $li.append($childUl);
                    } else {
                        //child 없을 때
                        $li.append($a);
                    }

                    $('.lnb').append($li);
                }

                // lnb 생성하고 이벤트 바인딩
                SKIAF.ui.lnb();

                // lnb 선택한 메뉴 스타일 적용
                LnbMenuModule.selectMenuCheck();
            }
        });
    };

});