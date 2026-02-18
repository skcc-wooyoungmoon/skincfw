/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 공통
 */
"use strict";
var ProgramCommonModule = $a.page(function() {
    
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
     * 프로그램 페이지 조회
     */
    this.search = function (params, isHistory, callback) {
        
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
        $a.request(SKIAF.PATH.PROGRAMS, {
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
    
    /**
     * Base Path 추출
     */
    this.makeBasePath = function(value) {
        var preIndex = value.indexOf('{');
        var postIndex = value.indexOf('}');
        if (preIndex < 0) {
            return value;
        }
        if (postIndex < 0) {
            return value;
        }
        value = value.substr(0, preIndex) + '*' + value.substr(postIndex + 1);
        return this.makeBasePath(value);
    };
    
    /**
     * 프로그램 아이디 자동 부여
     */
    this.generateId = function(gridData, defaultProgramId) {
        var selectedIndex = 1;
        gridData.forEach(function(data, index) {
            if (!data.programId) {
                return;
            }
            if (!data._state) {
                return;
            }
            
            // 추가했지만 선택해제 한 경우 아이디 부여 안함
            if (data._state.added && !data._state.selected) {
                return;
            }
            
            // 기존 데이터가 있는경우 기존 아이디 다음 순번부터 부여
            if (!data._state.added) {
                var idIndex = data.programId.substr(data.programId.lastIndexOf("-") + 1);
                if (parseInt(idIndex) >= selectedIndex) {
                    selectedIndex = parseInt(idIndex) + 1;
                }
            } else {
                data.programId = defaultProgramId + '-' + SKIAF.util.padNumLeft(selectedIndex++, 2);                
            }
        });
        return gridData;
    };
    
    /**
     * 추가할 프로그램 아이디 생성
     */
    this.generateNextId = function(gridData, defaultProgramId) {
        var selectedIndex = 1;
        gridData.forEach(function(data, index) {
            if (!data.programId) {
                return;
            }
            if (!data._state) {
                return;
            }
            
            // 추가했지만 선택해제 한 경우 아이디 부여 안함
            if (data._state.added && !data._state.selected) {
                return;
            }
            
            // 기존 데이터가 있는경우 기존 아이디 다음 순번부터 부여
            if (!data._state.added) {
                var idIndex = data.programId.substr(data.programId.lastIndexOf("-") + 1);
                if (parseInt(idIndex) >= selectedIndex) {
                    selectedIndex = parseInt(idIndex) + 1;
                }
            } else {
                selectedIndex++;
            }
        });
        return defaultProgramId + '-' + SKIAF.util.padNumLeft(selectedIndex, 2);
        
    };

    /**
     * 데이터 유효성 검사
     */
    this.validation = function(data) {
        var isValidate = true;
        var basePath;
        var pathTest;

        var pathCheck = [];
        for (var i = 0, len = data.length; i < len; i++) {
            
            var program = data[i];
            // 추가는 했으나 선택해제한 아이템은 체크안함
            if (program._state && program._state.added === true && program._state.selected === false) {
                continue;
            }
            
            // 필수값 체크
            // 프로그램 ID
            if (!program.programId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.id']);
                isValidate = false;
                break;
            }
            
            // 프로그램 이름
            if (!program.programName) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.name']);
                isValidate = false;
                break;
            }
            
            // 프로그램 Method
            if (!program.httpMethod) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.method']);
                isValidate = false;
                break;
            }
            
            // 프로그램 Path
            if (!program.programPath) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.path']);
                isValidate = false;
                break;
            }
            
            // 중복 Path 가 있는지 체크
            basePath = ProgramCommonModule.makeBasePath(program.programPath);
            pathTest = program.httpMethod.toUpperCase() + basePath.toUpperCase();
            if (pathCheck.indexOf(pathTest) >= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.method-path']);
                isValidate = false;
                break;
            }
            pathCheck.push(pathTest);
        }

        return isValidate;
    };
});
