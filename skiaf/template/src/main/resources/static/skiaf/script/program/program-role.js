/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 권한 수정 화면
 */
"use strict";
var ProgramRoleEditModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 페이지 정보
    var pageTitle = '';
    var pageUrl = location.pathname;

    // 프로그램 정보
    var programType = '';

    // 상세 그리드 컬럼
    var detailColumns = [
        {title : SKIAF.i18n.messages['bcm.program.id'],         key : 'programId',   align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.name'],       key : 'programName', align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.desc'],       key : 'programDesc', align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.program.method'],     key : 'httpMethod',  align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.path'],       key : 'programPath', align : 'left',   tooltip : true,  width : '200px'},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],      key : 'useYn',       align : 'center', tooltip : false, width : '65px', render : function(value, data) { return value ? 'Y' : 'N';}},
        {title : SKIAF.i18n.messages['bcm.common.update-by'],   key : 'updateBy',    align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.update-date'], key : 'updateDate',  align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.updateDate, 'yyyy-MM-dd hh:mm');}
        },
        {title : SKIAF.i18n.messages['bcm.common.create-by'],   key : 'createBy',    align : 'center', tooltip : false, width : '85px'},
        {title : SKIAF.i18n.messages['bcm.common.create-date'], key : 'createDate',  align : 'center', tooltip : false, width : '135px',
            render : function(value, data) {return SKIAF.util.dateFormat(data.createDate, 'yyyy-MM-dd hh:mm');}
        }
    ];
    
    // 권한 상세 그리드 컬럼
    var roleMapColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],     key : 'check',       align : 'center', headerStyleclass : 'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.program.role.id'],   key : 'roleId',      align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.role.name'], key : 'roleName',    align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.id'],        key : 'programId',   align : 'left',   tooltip : true,  width : '115px'},
        {title : SKIAF.i18n.messages['bcm.program.name'],      key : 'programName', align : 'left',   tooltip : true,  width : '150px'},
        {title : SKIAF.i18n.messages['bcm.program.type'],      key : 'programType', align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.method'],    key : 'httpMethod',  align : 'center', tooltip : true,  width : '100px'},
        {title : SKIAF.i18n.messages['bcm.program.path'],      key : 'programPath', align : 'left',   tooltip : true,  width : '200px'},
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
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : detailColumns,
        data : []
    };


    // 권한 상세 그리드 설정
    var roleMapGridObj = {
        height: 344,
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
                } else if(data._state.edited) {
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

        // 파라메터 셋팅
        ProgramRoleEditModule.setParameter();

        // 이벤트 등록
        ProgramRoleEditModule.addEvent();

        // 프로그램 상세 그리드
        $('#programDetailGrid').alopexGrid(detailGridObj);

        // 프로그램 권한 그리드
        $('#programRoleGrid').alopexGrid(roleMapGridObj);


        // 프로그램 및 권한 조회
        ProgramRoleEditModule.programDetail();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
        // 검색 버튼 클릭
        $('#programRolePopup').on('click', function(e) {
            ProgramRoleEditModule.programPopup();
        });

        // 검색영역 엔터키 이벤트
        $('#programRoleSearchArea input').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }

            ProgramRoleEditModule.programPopup();
        });
        
        // 이전 버튼 클릭
        $('#prevBtn').on('click', function(e) {
            var previousUrl = SKIAF.util.getDelCookies(SKIAF.JS_CONSTANT.PROGRAM_PREVIOUS_URL);
            if (previousUrl) {
                window.location.href = previousUrl;
            } else {
                window.location.href = SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS;
            }
            return;
        });
        
        // 저장 버튼 클릭
        $('#saveBtn').on('click', function(e) {
            ProgramRoleEditModule.saveRole();
        });
        
        // 삭제 버튼 클릭
        $('#deleteBtn').on('click', function(e) {
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'], function() {
                ProgramRoleEditModule.deleteRole();                
            });
        });
        
        // 권한 팝업
        $('#addBtn').on('click', function(e) {
            ProgramRoleEditModule.programRolePopup();
        });
        
        // check 버튼 이벤트
        $('#programRoleGrid').on('dataSelectEnd', function(type, datalist) {
            
            // 그리드 편집 모드 종료
            $('#programRoleGrid').alopexGrid('endEdit');
            $('#programRoleGrid').alopexGrid();
            
            // 버튼 활성화 처리
            ProgramRoleEditModule.checkActiveBtn();
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            SKIAF.console.info('state', e.state);
            ProgramRoleEditModule.setParameter();
            ProgramRoleEditModule.programDetail();
        }, false);
        
        $('#programDetailBtn').on('click', function(e) {
            var $targetChild = $(e.currentTarget).find('span');
            if ($targetChild.hasClass('Chevron-down')) {
                $targetChild.removeClass('Chevron-down').addClass('Chevron-up');
                $('#programDetailArea').show();
                $('#programDetailGrid').alopexGrid();
                
            } else {
                $('#programDetailArea').hide();
                $targetChild.removeClass('Chevron-up').addClass('Chevron-down');
            }

        });
        
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 저장
     */
    this.saveRole = function () {

        var data = $('#programRoleGrid').alopexGrid('dataGet', {
            _state : {
                selected: true,
                added: true
            }
        });

        $a.request(SKIAF.PATH.PROGRAM_ROLE_MAPS, {
            method : 'POST',
            array: data,
            success : function(result) {
                
                ProgramRoleEditModule.programDetail();
                
                // 버튼 활성화 처리
                ProgramRoleEditModule.checkActiveBtn();
            }
        });
    };
    
    /**
     * 삭제
     */
    this.deleteRole = function () {
        var data = $('#programRoleGrid').alopexGrid('dataGet', { _state : { selected : true } });

        $a.request(SKIAF.PATH.PROGRAM_ROLE_MAPS, {
            method : 'DELETE',
            array: data,
            success : function(result) {
                
                ProgramRoleEditModule.programDetail();
                
                // 버튼 활성화 처리
                ProgramRoleEditModule.checkActiveBtn();
            }
        });
    };
    
    /**
     * 프로그램 상세 조회
     */
    this.programDetail = function (isHistory) {

        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        var baseProgramId = $('#programRoleArea').getData().baseProgramId;
        if (!baseProgramId) {
            
            // 프로그램 타입 표시
            var data = {};
            data.baseProgramId = '';
            data.programType = '';

            $('#programRoleArea').setData(data);
            
            // 그리드에 데이터 설정
            $('#programDetailGrid').alopexGrid('dataSet', []);

            // 프로그램 권한 맵핑 정보
            $('#programRoleGrid').alopexGrid('dataSet', []);

            return;
        }

        // 히스토리에 저장
        if (isHistory) {
            var params = {
                    programId : baseProgramId
            };

            // Query String 생성
            var queryString = '?' + SKIAF.util.createParameter(params);

            // 히스토리 푸쉬
            history.pushState(null, pageTitle, pageUrl + queryString);
        }

        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.PROGRAMS_STARTING_WITH, baseProgramId), {
            method : 'GET',
            success : function(result) {
                
                if (!result.data) {
                    return;
                }

                // 프로그램 목록
                var programList = result.data.programList;
                
                // 타입 조회
                if (programList[0] && programList[0].programType) {
                    programType = programList[0].programType.toUpperCase();
                }
                
                // 프로그램 타입 표시
                var data = {};
                data.baseProgramId = baseProgramId;
                data.programType = programType;
                $('#programRoleArea').setData(data);
                
                // 그리드에 데이터 설정
                $('#programDetailGrid').alopexGrid('dataSet', programList);

                // 프로그램 권한 맵핑 정보
                var programRoleMapList = result.data.programRoleMapList;
                $('#programRoleGrid').alopexGrid('dataSet', programRoleMapList);
                
                ProgramRoleEditModule.checkActiveBtn();
            }
        });
    };
    
    /**
     * 프로그램 조회 팝업
     */
    this.programPopup = function() {
        var keyword = $('#programRoleArea').getData().keyword;
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_SELECT,
            data : {
                keyword : keyword,
                programType : SKIAF.JS_CONSTANT.PROGRAM_TYPE_ALL,
                programTypeShow : true
            },
            movable : true,
            iframe : false,
            title : SKIAF.i18n.messages['bcm.program.search'],
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                if (!data) {
                    return;
                }
                if (!data.programId) {
                    return;
                }

                // 전달 받은 프로그램을 입력
                var inputData = $('#programRoleArea').getData();
                inputData.baseProgramId = ProgramRoleEditModule.getBaseProgramId(data.programId);
                inputData.programType = data.programType;
                $('#programRoleArea').setData(inputData);

                ProgramRoleEditModule.programDetail(true);
            }

        });
    };
    
    /**
     * 프로그램 권한 조회 및 추가
     */
    this.programRolePopup = function() {
        
        var baseProgramId = $('#programRoleArea').getData().baseProgramId;
        
        if (!baseProgramId) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.select']);
            return;
        }
        
        $a.popup({
            url : SKIAF.contextPath + SKIAF.PATH.VIEW_PROGRAMS_ROLE_SELECT,
            data : {
                baseProgramId : baseProgramId,
                keyword : ''
            },
            movable: true,
            title: SKIAF.i18n.messages['bcm.program.role.select'],
            iframe : false,
            width : 1000,
            height : 600,
            center : true,
            callback : function(data) {
                if (!data) {
                    return;
                }
                
                // 추가 할 아이템 중복 제거
                // 프로그램 id 와 권한 id 조합의 키 생성
                var roleGridData = $('#programRoleGrid').alopexGrid('dataGet');
                var roleGridDataKey = [];
                roleGridData.forEach(function(value, index) {
                    var id = value.roleId + '/' + value.programId;
                    roleGridDataKey.push(id);
                });
                
                // 같은 키가 있으면 추가 안함
                var isNotAdded = true;
                for (var idx in data) {
                    if (roleGridDataKey.indexOf(idx) >= 0) {
                        continue;
                    }

                    // 추가 및 선택된 데이터로 지정
                    data[idx]._state = {
                            added : true,
                            selected : true
                    };
                    isNotAdded = false;
                    $('#programRoleGrid').alopexGrid('dataAdd', data[idx]);
                }
                
                if (isNotAdded) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.role.valid.mapped']);
                }
                
                // 버튼 활성화 처리
                ProgramRoleEditModule.checkActiveBtn();
            }
        });
    };
    
    /**
     * 저장 또는 삭제 버튼 활성화
     */
    this.checkActiveBtn = function() {

        var editRoleData = $('#programRoleGrid').alopexGrid('dataGet');

        // 기본값은 저장/삭제 버튼 비활성화
        $('#deleteBtn').setEnabled(false).addClass("Confirm");
        $('#saveBtn').setEnabled(false);
        
        // 선택된 데이터 없으면 모두 비활성화
        if (editRoleData.length <= 0) {
            return;
        }

        var isNewList = true;
        var isExistList = true;

        // 추가된 데이터가 하나 이상 있으면 저장버튼 활성화
        for (var i = 0; i < editRoleData.length; i++) {
            if (!editRoleData[i]._state) {
                continue;
            }
            if (!editRoleData[i]._state.selected) {
                continue;
            }
            
            // 추가된 데이터
            if (editRoleData[i]._state.added) {
                $('#saveBtn').setEnabled(true);
                isExistList = false;                
                continue;
            } else {
                isNewList = false;
            }
        }

        // 기존 데이터만 선택되어 있으면, 삭제버튼 활성화
        if (isExistList && !isNewList) {
            $('#deleteBtn').setEnabled(true).addClass("Confirm");
            $('#saveBtn').setEnabled(false);
        }
    };
    
    /**
     * 대표 프로그램 ID 조회
     */
    this.getBaseProgramId = function(programId) {
        if (programId && programId.lastIndexOf('-') >= 0) {
            return programId.substr(0, programId.lastIndexOf('-'));
        }
        return programId;
    };
    
    /**
     * 파라메터 셋팅
     */
    this.setParameter = function() {
        
        // 전달된 프로그램 ID 확인 
        var programId = SKIAF.util.getParameter('programId') ? SKIAF.util.getParameter('programId') : '';

        $('#programRoleArea').setData({
            baseProgramId: ProgramRoleEditModule.getBaseProgramId(programId)
        });

    };
    
});
