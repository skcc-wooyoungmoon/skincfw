/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.20 - in01869
 * description : 메인화면 js
 */
"use strict";
var MainModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {

        MainModule.addEvent();

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        // 공지사항 더보기 버튼 클릭
        $('#btnNoticeMore').on('click', function(e) {
            location.href = '/view/bcm/boards/notice/articles';
        });

        // FAQ 더보기 버튼 클릭
        $('#btnFaqMore').on('click', function(e) {
            location.href = '/view/bcm/boards/faq/articles';
        });

    };

});