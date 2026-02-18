/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.18 - in01866
 * description : 프로그램 요소 공통 처리
 */
"use strict";
var CommonElementModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var hideKey = 'bcm-element-hide';
    var disableKey = 'bcm-element-disable';
    var elementType = Object.freeze({
        visible : 'visible',
        enable : 'enable'
    });
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        CommonElementModule.showElementRole();
        CommonElementModule.enableElementRole();
        CommonElementModule.setElementRole(elementType.enable);
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * show() function 재정의.
     * 프로그램 요소 권한이 있으면 show 처리, 없으면 처리 안함
     */
    this.showElementRole = function() {
        var showFunction = $.fn.show;
        $.fn.show = function () {
            var block = $(this).data(hideKey);
            if (block === 'Y') {
                return this;
            }
            return showFunction.apply(this, Array.prototype.slice.call(arguments));
        };
    };
    
    /**
     * setEnabled() function 재정의
     * 프로그램 요소 권한이 있으면 enabled 처리, 없으면 처리 안함
     */
    this.enableElementRole = function() {
        var setEnabledFunction = $.fn.setEnabled;
        $.fn.setEnabled = function () {
            var block = $(this).data(disableKey);
            var isEnabled = arguments[0] || false;
            if (block === 'Y' && isEnabled) {
                return this;
            }
            return setEnabledFunction.apply(this, Array.prototype.slice.call(arguments));
        };
    };

    /**
     * 프로그램 요소 권한에 따른 접근 처리
     */
    this.setElementRole = function(type) {
        var elementRoleList = SKIAF.elementRole;
        if (!elementRoleList) {
            return;
        }
        if (!Array.isArray(elementRoleList)) {
            return;
        }
        elementRoleList.forEach(function(elementRole, index) {
            if (type == elementType.visible && !elementRole.visibleYn) {
                $(elementRole.elementKey).hide().data(hideKey, 'Y');
            }
            if (type == elementType.enable && !elementRole.enableYn) {
                $(elementRole.elementKey).setEnabled(false);
                $(elementRole.elementKey).data(disableKey, 'Y');
            }
        });
    };


    this.setElementRole(elementType.visible);
});
