/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.20 - in01869
 * description : 이벤트 로그 상세 팝업 화면 js
 */
"use strict";
var EventLogDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {

        if (param['data'] != null) {
            EventLogDetailModule.setDetailData(param['data']);
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

        detailData.eventGroup = data.eventGroup;
        detailData.eventType = data.eventType;
        detailData.loginId = data.loginId;
        detailData.logger = data.loggerName;
        detailData.message = data.formattedMessage;

        $('#detailArea').setData(detailData);
    };
});