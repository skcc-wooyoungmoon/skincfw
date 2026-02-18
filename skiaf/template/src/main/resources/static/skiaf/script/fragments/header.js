/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
"use strict";
var HeaderModel = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {
        HeaderModel.addEvent();
        //HeaderModel.removeGnbLink();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        $("#logoutBtn").on('click', function(e) {
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.LOGOUT_CONFIRM'], function callback(){$("#logoutForm").submit();});
        });

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*this.removeGnbLink = function() {
        // paas가 아닌 경우, GNB 링크들을 무효화 시킨다.
        if (typeof location.host !== 'undefined' && location.host.indexOf('skiaf.skinnovation.com') < 0) {            
            $('#header a').each(function(index, item){
                // SKIAF(DEMO) 링크는 제외.
                if ($(item).attr('href').indexOf(SKIAF.PATH.VIEW_LOGIN) < 0 && $(item).attr('href').indexOf(SKIAF.PATH.LOGOUT) < 0) {
                    $(item).removeAttr('href');
                }
            });
        }
    }*/
    
});
