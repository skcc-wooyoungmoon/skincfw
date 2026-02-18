/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 권한 수정 팝업
 */
"use strict";
var ProgramRolePopupModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 프로그램 정보
    var baseProgramId = '';
    var programName = '';
    var defaultProgramPath = '';
    var programType = '';

    // 상세 그리드 컬럼
    var detailColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],  key : 'check',       align : 'center', headerStyleclass :'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.program.id'],     key : 'programId',   align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.name'],   key : 'programName', align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.type'],   key : 'programType', align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.method'], key : 'httpMethod',  align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.path'],   key : 'programPath', align : 'left',   tooltip : true,  width : '200px'}
    ];
    
    // 권한 상세 그리드 컬럼
    var roleMapColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],     key : 'check',    align : 'center', headerStyleclass : 'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.program.role.id'],   key : 'roleId',   align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.role.name'], key : 'roleName', align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.role.desc'], key : 'roleDesc', align : 'left',   tooltip : true,  width : '150px'}
    ];
    
    // 상세 그리드 설정
    var detailGridObj = {
        height: 344,
        autoColumnIndex : true,
        fitTableWidth : true,
        defaultColumnMapping : {
            resizing : true
        },
        rowSelectOption : {
            clickSelect : true
        },
        columnMapping : detailColumns,
        data : []
    };


    // 권한 상세 그리드 설정
    var roleMapGridObj = {
        height: 334,
        autoColumnIndex : true,
        fitTableWidth : true,
        defaultColumnMapping : {
            resizing : true
        },
        rowSelectOption : {
            clickSelect : true
        },
        rowOption : {
            styleclass : function(data, rowOption) {
                if (data._state.added && !data._state.selected) {
                    return 'highlight-add highlight-blur';
                }
                if (data._state.added) {
                    return 'highlight-add';
                } else if (data._state.edited) {
                    return 'highlight-edit';
                }
                return '';
            }
        },
        columnMapping : roleMapColumns,
        data : []
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        
        // 이벤트 등록
        ProgramRolePopupModule.addEvent();

        // 프로그램 상세 그리드
        $('#programDetailPopupGrid').alopexGrid(detailGridObj);

        // 프로그램 권한 그리드
        $('#programRolePopupGrid').alopexGrid(roleMapGridObj);

        if (param) {
            baseProgramId = param.baseProgramId;
        }

        if (param.keyword) {
            $('#programRolePopupArea').setData({
                keyword : param.keyword
            });
        }

        // 프로그램 상세 조회
        ProgramRolePopupModule.programDetail(baseProgramId);

        // 권한 조회
        ProgramRolePopupModule.roleSearch();

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 취소 버튼 클릭
        $('#programRolePopupCancleBtn').on('click', function(e) {

            $a.close(false);
        });
        
        // 저장 버튼 클릭
        $('#programRolePopupSaveBtn').on('click', function(e) {
            var selectedProgram = $('#programDetailPopupGrid').alopexGrid('dataGet', { _state : { selected : true } });
            var selectedRole = $('#programRolePopupGrid').alopexGrid('dataGet', { _state : { selected : true } });
            
            if (selectedProgram.length <= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.role.valid.program']);
                return;
            }
            
            if (selectedRole.length <= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.role.valid.role']);
                return;
            }
            
            // 프로그램 id 와 권한 id 조합의 키 생성
            var programRoleData = {};
            selectedRole.forEach(function(role, index) {
                selectedProgram.forEach(function(program, index) {
                    var id = role.roleId + '/' + program.programId;
                    var originRole = $.extend({}, role);
                    programRoleData[id] = $.extend({}, program, originRole);
                });
            });
            
            // 선택된 데이터 전달
            SKIAF.console.info('programRoleData', programRoleData);
            $a.close(programRoleData);
        });
        
        // 검색 버튼 클릭
        $('#programRolePopupArea .skiaf-ui-search button.search').on('click', function(e) {
            ProgramRolePopupModule.roleSearch();
        });

        // 검색영역 엔터키 이벤트
        $('#programRolePopupArea .skiaf-ui-search input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            ProgramRolePopupModule.roleSearch();
        });
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 프로그램 상세 조회
     */
    this.programDetail = function (baseProgramId) {
        
        if (!baseProgramId) {
            return;
        }
        
        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.PROGRAMS_STARTING_WITH, baseProgramId), {
            method : 'GET',
            success : function(result) {
                
                if (!result.data) {
                    return;
                }

                // 그리드에 데이터 설정
                $('#programDetailPopupGrid').alopexGrid('dataSet', result.data.programList);

            }
        });
    };
    
    /**
     * 프로그램 권한 조회
     */
    this.roleSearch = function() {
        
        var params = {
                isList : true,
                roleType : 'PROGRAM'
        };
        
        var keyword = $('#programRolePopupArea').getData().keyword;
        
        if (keyword) {
            params.keyword = keyword;
        }

        // ajax 통신
        $a.request(SKIAF.PATH.ROLES, {
            method : 'GET',
            data: params,
            success : function(result) {
                
                if (!result.data) {
                    return;
                }

                // 그리드에 데이터 설정
                $('#programRolePopupGrid').alopexGrid('dataSet', result.data);

            }
        });
    };
    

});
