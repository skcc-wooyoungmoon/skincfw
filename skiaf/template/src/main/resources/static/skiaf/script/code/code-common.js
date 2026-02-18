/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 코드 공통
 */
"use strict";
var CodeCommonModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 페이지 정보
    var pageTitle = '';
    var pageUrl = location.pathname;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 코드 그룹 페이지 조회
     */
    this.search = function(params, isHistory, callback) {
        
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 히스토리에 저장
        if (isHistory) {
            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);
            // 히스토리 푸쉬
            history.pushState(null, pageTitle, pageUrl + queryString);
        }

        // 그리드 페이지에서 서버 페이지로 값 변환
        if (params && typeof params.page !== 'undefined') {
            params.page = params.page - 1;
        }

        // ajax 통신
        $a.request(SKIAF.PATH.CODE_GROUPS, {
            method : 'GET',
            data : params,
            success : function(result) {

                if (!result.data) {
                    return;
                }

                callback(result);

            }
        });
    };

});
