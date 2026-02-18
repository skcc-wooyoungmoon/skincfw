/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.20 - in01869
 * description : 기본 로그 상세 팝업 화면 js
 */
"use strict";
var SystemLogDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {

        if (param['data'] != null) {
            SystemLogDetailModule.setDetailData(param['data']);
        }

        $('#btnClose').on('click', function() {
            $a.close();
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.setDetailData = function(data) {
        var detailData = {};
        detailData.seq = data.eventId;

        var dt = new Date(data.timestmp);
        detailData.timestamp = SKIAF.util.dateFormatReplace(dt, 'yyyy-MM-dd HH:mm:ss');

        detailData.level = data.levelString;
        detailData.logger = data.loggerName;
        detailData.message = data.formattedMessage;
        detailData.caller = data.callerClass + ' : ' + data.callerMethod + ' : ' + data.callerLine;

        $('#detailArea').setData(detailData);
    };

});