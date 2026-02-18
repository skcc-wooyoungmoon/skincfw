/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 권한 선택 팝업
 */
"use strict";
var ProgramSaveModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 수정 모드
    var isEditMode = false;
    
    // 대표 프로그램 경로
    var defaultProgramPath = '';
    
    // 초기 프로그램 ID
    var initProgramId = '';
    
    // 편집 가능 컬럼
    var editColumn = {
        programId : '',
        programName : '',
        programDesc : '',
        httpMethod : '',
        programPath : '',
        useYn : ''
    };
    
    // 최대 추가 갯수
    var maxAddCnt = 9999;
    
    // 등록 그리드 컬럼
    var gridColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],  key : 'check',       align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: 50},
        {title : SKIAF.i18n.messages['bcm.program.id'],     key : 'programId',   align : 'center', tooltip : false},
        {title : SKIAF.i18n.messages['bcm.program.name'],   key : 'programName', align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 128}}},
        {title : SKIAF.i18n.messages['bcm.program.desc'],   key : 'programDesc', align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 2000}}},
        {title : SKIAF.i18n.messages['bcm.program.method'], key : 'httpMethod',  align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 10}}},
        {title : SKIAF.i18n.messages['bcm.program.path'],   key : 'programPath', align : 'left',   tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 1000}}},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],  key : 'useYn',       align : 'center', tooltip : false,
            render : function(value, data) { return value ? 'Y' : 'N'; },
            editable: {type: 'radio', rule: [{value: true, text: 'Y'}, {value: false, text: 'N'}]},
            value: function (value, data) {
                if (typeof value === 'undefined') {
                    return true;
                }
                if (value === 'true') {
                    return true;
                } else if (value === 'false') {
                    return false;
                }
                return value;
            }
        },
    ];
    
    // 도움말 그리드 컬럼
    var gridHelpColumns = [
        {title : SKIAF.i18n.messages['bcm.common.select'],  key : 'check',       align : 'center', headerStyleclass:'set', selectorColumn : true, resizing : false, width: 50},
        {title : SKIAF.i18n.messages['bcm.program.id'],     key : 'programId',   align : 'center', tooltip : false},
        {title : SKIAF.i18n.messages['bcm.program.name'],   key : 'programName', align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 128}}},
        {title : SKIAF.i18n.messages['bcm.program.desc'],   key : 'programDesc', align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 2000}}},
        {title : SKIAF.i18n.messages['bcm.program.method'], key : 'httpMethod',  align : 'center', tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 10}}},
        {title : SKIAF.i18n.messages['bcm.program.path'],   key : 'programPath', align : 'left',   tooltip : false, defaultValue: '', editable : {type : 'text', attr : {maxlength : 1000}}},
        {title : SKIAF.i18n.messages['bcm.common.use-yn'],  key : 'useYn',       align : 'center', tooltip : false,
            render : function(value, data) { return value ? 'Y' : 'N'; },
            editable: {type: 'radio', rule: [{value: true, text: 'Y'}, {value: false, text: 'N'}]},
            value: function (value, data) {
                if (typeof value === 'undefined') {
                    return true;
                }
                if (value === 'true') {
                    return true;
                } else if (value === 'false') {
                    return false;
                }
                return value;
            }
        },
        {title : '도움말', key: 'helpAttach', align: 'center', tooltip: false, width: 120, render : {type : "btn"}}
    ];

    // 프로그램 타입
    var currentType = SKIAF.ENUM.PROGRAM_TYPE.VIEW;

    // 그리드 설정
    var gridObj = {
        autoColumnIndex: true,
        fitTableWidth: true,
        rowInlineEdit : true,
        rowInlineEditOption : {
            startEvent : 'click'
        },
        endInlineEditByOuterClick : true,
        defaultColumnMapping : {
            resizing : true
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
        readonlyRender : false,
        columnMapping : gridColumns,
        data : []
    };

    var gridHelpObj = {
            autoColumnIndex: true,
            fitTableWidth: true,
            rowInlineEdit : true,
            rowInlineEditOption : {
                startEvent : 'click'
            },
            endInlineEditByOuterClick : true,
            defaultColumnMapping : {
                resizing : true
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
            renderMapping: {
                'btn': {
                    renderer: function(value, data, render, mapping) {
                        if (data._state.added) {
                            return '';
                        } 
                        
                        var isAttach = false;
                        if (data.attachFile && data.attachFile.fileId) {
                            isAttach = true;
                        }
                        
                        if (isAttach) {
                            return '<button class="program-attach Button small detail primery-b" data-program-id="' + data.programId + '">수정</button>';
                        } else {
                            return '<button class="program-attach Button small create" data-program-id="' + data.programId + '">등록</button>';
                        }
                    }
                }
            },
            readonlyRender : false,
            columnMapping : gridHelpColumns,
            data : []
        };
    
    var basProgramIdLabel = SKIAF.i18n.messages['bcm.program.base-id'];
    var idCheckMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.check'], basProgramIdLabel);
    var idSuccessMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.success'], basProgramIdLabel);
    var idDuplicationMsg = SKIAF.util.getMessageWithArgs(SKIAF.i18n.messages['bcm.common.id.duplication'], basProgramIdLabel);
    var idValidationMsg = SKIAF.i18n.messages['bcm.program.valid.id-rule'];

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {
        SKIAF.console.info('ProgramSaveModule init');
        
        // 타입 조회 ('SERVICE' 또는 'VIEW')

        ProgramSaveModule.addEvent();

        isEditMode = param.editMode;
        
        // 편집 모드시 처리
        if (isEditMode && param.program) {
            initProgramId = param.program.programId;
            ProgramSaveModule.programDetail(initProgramId);
            
            // 편집모드 테이블로 변경
            $('#viewCreateTable, #serviceCreateTable').hide();
            $('#viewEditTable, #serviceEditTable').show();
        } else {
            // 등록모드 테이블로 변경
            $('#viewCreateTable, #serviceCreateTable').show();
            $('#viewEditTable, #serviceEditTable').hide();
        }

        // 그리드 초기화
        // 편집 모드 뷰 타입인 경우 도움말 컬럼 추가
        if (isEditMode) {
            $('#serviceGrid').alopexGrid(gridObj);
            $('#viewGrid').alopexGrid(gridHelpObj);

        } else {
            $('#serviceGrid').alopexGrid(gridObj);
            $('#viewGrid').alopexGrid(gridObj);

        }
        
        /*$('#serviceGrid').alopexGrid(gridObj);
        $('#viewGrid').alopexGrid(gridObj);*/

        // 프로그램 타입에 따른 화면 변경
        if (param.program && param.program.programType) {
            currentType = param.program.programType;          
        }
        if (currentType == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
            $('.type input[value=' + currentType + ']').trigger('click');
        }
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {

        // 아이디 입력 변경 이벤트
        $('#viewProgramIdInput, #serviceProgramIdInput').on('change', function(e) {
            
            // 선택된 타입의 아이디만 체크
            $('#' + currentType.toLowerCase() + 'ProgramIdCheckMsg')
                .removeClass('Color-confirm')
                .addClass('Color-danger')
                .text(idCheckMsg);
        });
        
        // service check 버튼 이벤트
        $('#serviceGrid').on('dataSelectEnd', function(e) {
            
            // 체크박스 체크 해제시 그리드 데이터 원래대로 처리
            ProgramSaveModule.changeSelectHandler(e);

            // 아이디 재정의
            $('#serviceGrid').alopexGrid();
            ProgramSaveModule.generateId.service();
            
            // 저장버튼 활성화 여부
            ProgramSaveModule.checkSaveBtn(e.currentTarget.id);
            
        }).on('dataChanged', ProgramSaveModule.changeEventDataHandler);
        
        // view check 버튼 이벤트
        $('#viewGrid').on('dataSelectEnd', function(e) {
            
            // 체크박스 체크 해제시 그리드 데이터 원래대로 처리
            ProgramSaveModule.changeSelectHandler(e);
            
            // 아이디 새로 부여
            $('#viewGrid').alopexGrid();
            ProgramSaveModule.generateId.view();

            // 저장버튼 활성화 여부
            ProgramSaveModule.checkSaveBtn(e.currentTarget.id);

        }).on('dataChanged', ProgramSaveModule.changeEventDataHandler);
        
        // Service 아이디 중복 체크 Event
        $('#serviceCheckBtn, #viewCheckBtn').on('click', function(e) {
            // 그리드 편집 모드 종료
            $('#serviceGrid, #viewGrid').alopexGrid('endEdit');
            
            var $checkBtn = $(e.currentTarget);
            var testId = $checkBtn.parent().find('input').val();
            
            // 선택된 타입의 아이디만 체크
            // Validation ID Check
            if (!$('#' + currentType.toLowerCase() + 'ProgramIdInput').validate()) {
                $('#' + currentType.toLowerCase() + 'ProgramIdInput').validator();
                return;
            }
            
            if (!SKIAF.util.checkIdWithoutDash(testId)) {
                $('#' + currentType.toLowerCase() + 'ProgramIdCheckMsg')
                    .removeClass('Color-confirm')
                    .addClass('Color-danger')
                    .text(idValidationMsg).show();
                return;
            }
            
            ProgramSaveModule.existsId(testId);
        });
        
        // Service 메소드 추가 버튼 이벤트
        $('#serviceMethodAddBtn').on('click', function(e) {
            
            var serviceData = isEditMode ? $('#serviceEditTable').getData() : $('#serviceCreateTable').getData();
            var programId = serviceData.programId;
            var programName = serviceData.programName;
            
            // Validation Check
            if (!isEditMode && !$('#serviceCreateTable').validate()) {
                $('#serviceCreateTable').validator();
                return;
            }
            
            // id check
            var isIdConfirm = $('#serviceProgramIdCheckMsg').hasClass('Color-confirm');
            if (!isEditMode && !isIdConfirm) {
                $('#serviceProgramIdCheckMsg').show();
                return;
            }
            
            var methodAddList = $('#serviceMethodAddArea').getData().addService;
            if (!methodAddList) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.add-method']);
                return;
            }
            if (methodAddList.length <= 0) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.add-method']);
                return;
            }

            for (var index in methodAddList) {
                var method = methodAddList[index];
                var serviceGridData = $('#serviceGrid').alopexGrid('dataGet');
                
                if (serviceGridData.length >= maxAddCnt) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.id-max-cnt']);
                    return;
                }

                // data 추가
                var data = {};
                var programPath = serviceData.programPath || defaultProgramPath || '';

                if (method == 'PUT' || method == 'PATCH' || method == 'DELETE' || method == 'GET') {
                    if (programPath && programPath.charAt(programPath.length - 1) != '/') {
                            programPath += '/';
                    }
                    programPath += '{id}';
                }
                if (method == 'GET-LIST') {
                    method = 'GET';
                }

                data.programId = ProgramCommonModule.generateNextId(serviceGridData, programId);
                data.programName = programName;
                data._state = {};
                data._state.added = true;
                data._state.selected = true;
                
                data.httpMethod = method;
                data.programPath = programPath;
                
                // 그리드 적용
                $('#serviceGrid').alopexGrid('dataAdd', data);

            }
        });
        
        // View 추가 버튼 이벤트
        $('#viewAddBtn').on('click', function(e) {
            
            var viewData = isEditMode ? $('#viewEditTable').getData() : $('#viewCreateTable').getData();
            var programId = viewData.programId;
            var programName = viewData.programName;

            // Validation Check
            if (!isEditMode && !$('#viewCreateTable').validate()) {
                $('#viewCreateTable').validator();
                return;
            }
            
            // id check
            var isIdConfirm = $('#viewProgramIdCheckMsg').hasClass('Color-confirm');
            if (!isEditMode && !isIdConfirm) {
                $('#viewProgramIdCheckMsg').show();
                return;
            }
            
            var data = {};
            var viewGridData = $('#viewGrid').alopexGrid('dataGet');
            if (viewGridData.length >= maxAddCnt) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.id-max-cnt']);
                return;
            }
            // data 추가
            data.programId = ProgramCommonModule.generateNextId(viewGridData, programId);
            data.programName = programName;
            data._state = {};
            data._state.added = true;
            data._state.selected = true;
            
            // 그리드 적용
            $('#viewGrid').alopexGrid('dataAdd', data);
        });
        
        // 타입 선택 이벤트
        $('.typebox .type').on('click', 'input[type="radio"]', function(e, triggerBy) {
            if (triggerBy == 'ProgramSaveModule') {
                return;
            }
            var $target = $(e.currentTarget);
            currentType = $target.val();

            if (currentType == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
                $('#serviceArea').show();
                $('#viewArea').hide();
                $('#serviceGrid').alopexGrid();
            } else {
                $('#serviceArea').hide();
                $('#viewArea').show();
                $('#viewGrid').alopexGrid();
            }

            // view 영역과 service 영역에 있는 프로그램 유형 라디오 버튼 동기화
            $('.type input[value=' + currentType + ']').trigger('click', 'ProgramSaveModule');
        });
        
        // 저장 이벤트
        $('#saveBtn').on('click', function(e) {
            
            // 그리드 편집 모드 종료
            $('#serviceGrid, #viewGrid').alopexGrid('endEdit');

            var editData = [];
            var createData = [];
            var gridData = [];
            
            // 타입별 저장 데이터 처리
            if (currentType == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
                gridData = $('#serviceGrid').alopexGrid('dataGet');

            } else {
                gridData = $('#viewGrid').alopexGrid('dataGet');
            }

            gridData.forEach(function(data, index) {
                if (!data._state) {
                    return;
                }
                if (!data._state.selected) {
                    return;
                }
                data.programType = currentType;

                if (data._state.added) {
                    createData.push(data);
                } else if (data._state.edited) {
                    editData.push(data);
                }
            });
            
            // Validation 체크
            if (!ProgramCommonModule.validation(gridData)) {
                return;
            }

            SKIAF.console.info('createData', createData);
            SKIAF.console.info('editData', editData);

            if (isEditMode) {
                ProgramSaveModule.update(createData, editData);
            } else {
                ProgramSaveModule.create(createData);
            }
        });

        // 취소 버튼
        $('#cancleBtn').on('click', function(e) {
            $a.close(false);
        });
        
        // 도움말 버튼
        $("#viewGrid").on('click', function(e) {

            // 버튼을 클릭했는지 확인
            if (!$(e.target).hasClass('program-attach')) {
                return
            }
            var mapping = AlopexGrid.parseEvent(e).mapping;
            if (!mapping) {
                return;
            }
            if (mapping.key != 'helpAttach') {
                return;
            }

            var data = AlopexGrid.parseEvent(e).data;
            if (!data) {
                return;
            }
            
            var title = SKIAF.i18n.messages['bcm.program.help.create'];
            if (data.attachFile && data.attachFile.fileId) {
                title = SKIAF.i18n.messages['bcm.program.help.edit'];
            }

            $a.popup({
                url : '/view/bcm/programs/popups/attach',
                data : data,
                movable : true,
                iframe : false,
                width : 700,
                height : 300,
                center : true,
                title : title,
                callback : function(data) {
                    SKIAF.console.info('attach add:: ', data);
                    if (data && initProgramId) {
                        ProgramSaveModule.programDetail(initProgramId);
                    }
                }
            });
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 아이디 중복 체크
     */
    this.existsId = function (id) {
        
        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.PROGRAMS_ID_CHECK, id), {
            method : 'GET',
            success : function(result) {
                var isDuplicate = result.data;

                // ID 중복
                if (result.data) {
                    $('#' + currentType.toLowerCase() + 'ProgramIdCheckMsg')
                        .removeClass('Color-confirm')
                        .addClass('Color-danger')
                        .text(idDuplicationMsg).show();

                } else {
                    $('#' + currentType.toLowerCase() + 'ProgramIdCheckMsg')
                        .removeClass('Color-danger')
                        .addClass('Color-confirm')
                        .text(idSuccessMsg).show();
                    
                    // 프로그램 아이디 재정의
                    if (currentType == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
                        ProgramSaveModule.generateId.service();
                    } else {
                        ProgramSaveModule.generateId.view();
                    }
                }
            }
        });
    };
    
    /**
     * 저장
     */
    this.create = function (data) {
        if (!Array.isArray(data)) {
            return;
        }
        
        if (data.length <= 0) {
            return;
        }
        
        $a.request(SKIAF.PATH.PROGRAMS, {
            method : 'POST',
            array: data,
            success : function(result) {
                $a.close(true);
            }
        });
    };
    

    /**
     * 수정
     */
    this.update = function (createData, editData) {
        
        $a.request(SKIAF.PATH.PROGRAMS, {
            method : 'PUT',
            data: {
                createList: createData,
                updateList: editData
            },
            success : function(result) {
                
                $a.close(true);
            }
        });
    };
    
    /**
     * 프로그램 상세 조회
     */
    this.programDetail = function (programId) {

        var baseProgramId = programId;
        if (programId && programId.lastIndexOf('-') >= 0) {
            baseProgramId = programId.substr(0, programId.lastIndexOf('-'));
        }
        
        // 화면에 표시할 기준 아이디 정보
        var displayValue = {
                baseProgramId: baseProgramId
        };
        $('#detailArea').setData(displayValue);
        $('#editArea').setData(displayValue);

        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.PROGRAMS_STARTING_WITH, baseProgramId), {
            method : 'GET',
            success : function(result) {
                
                if (!result.data) {
                    return;
                }

                // 프로그램 목록
                var programList = result.data.programList;
                var programName = '';
                var programType = '';
                
                // 프로그램 이름
                if (programList[0] && programList[0].programName) {
                    programName = programList[0].programName;                    
                }
                
                // 타입 조회
                if (programList[0] && programList[0].programType) {
                    programType = programList[0].programType.toUpperCase();
                }
                
                // default Path 
                if (programList[0] && programList[0].basePath) {
                    var path = programList[0].basePath;
                    if (path.lastIndexOf('/*') > 0) {
                        defaultProgramPath = path.substr(0, path.lastIndexOf('/*'));                        
                    } else {
                        defaultProgramPath = path;
                    }
                }
                
                var programData = {
                        programId : baseProgramId,
                        programType : programType,
                        programName : programName
                }
                
                var serviceList = JSON.parse(JSON.stringify(programList));
                var viewList = JSON.parse(JSON.stringify(programList));
                
                // 그리드에 데이터 설정
                if (currentType == SKIAF.ENUM.PROGRAM_TYPE.SERVICE) {
                    $('#serviceGrid').alopexGrid('dataSet', serviceList);
                    $('#serviceArea').setData(programData);
                } else {
                    $('#viewGrid').alopexGrid('dataSet', viewList);
                    $('#viewArea').setData(programData);
                }

            }
        });
    };
    
    /**
     * 프로그램 아이디 부여
     */
    this.generateId = {
        service: function() {
            // Service
            var serviceData = $('#serviceGrid').alopexGrid('dataGet');
            
            var programId =  $('.typebox.service').getData().programId;
            serviceData = ProgramCommonModule.generateId(serviceData, programId);

            $('#serviceGrid').alopexGrid('dataSet', serviceData);

        },
        view : function() {
            // View
            var viewData = $('#viewGrid').alopexGrid('dataGet');
            
            var programId =  $('.typebox.view').getData().programId;
            viewData = ProgramCommonModule.generateId(viewData, programId);
            $('#viewGrid').alopexGrid('dataSet', viewData);

        }
    };
    
    /**
     * 프로그램 저장 버튼 처리
     */
    this.checkSaveBtn = function(targetId) {
        var $grid = $('#' + targetId);
        var isSave = false;
        var selectedData = $grid.alopexGrid('dataGet', { _state : { selected : true } });
        
        for (var idx in selectedData) {
            var data = selectedData[idx];
            if (!data._state.added && !data._state.edited) {
                continue;
            }
            isSave = true;
            break;
        }
        
        if (isSave) {
            $('#saveBtn').setEnabled(true);
        } else {
            $('#saveBtn').setEnabled(false);
        }
    };

    /**
     * 체크박스 체크 해제시 그리드 데이터 원래대로 처리 
     */
    this.changeSelectHandler = function(e) {
        $('#serviceGrid, #viewGrid').alopexGrid('endEdit');
        
        var dataList = AlopexGrid.parseEvent(e).datalist;
        if (!dataList) {
            return;
        }
        
        // 선택 해제시 기존의 데이터로 변경
        dataList.forEach(function(dataObj, index) {
            var state = dataObj._state;
            
            // 선택 된 아이템이면 처리안함
            if (state.added || state.selected) {
                return;
            }
            var indexInfo = {
                    _index: dataObj._index
            }
            $(e.currentTarget).alopexGrid('dataRestore', indexInfo);

        });

    };
    
    /**
     * 그리드 데이터 변경시 체크박스 체크 처리
     */
    this.changeEventDataHandler = function(e) {
        var dataList = AlopexGrid.parseEvent(e).datalist;
        var eventType = AlopexGrid.parseEvent(e).type;
        if (!dataList) {
            return;
        }
        if (!eventType) {
            return;
        }
        if (eventType != 'edit') {
            ProgramSaveModule.checkSaveBtn(e.currentTarget.id);
            return;
        }
        SKIAF.console.info('dataList', dataList);
        dataList.forEach(function(dataObj, index) {
            var state = dataObj._state;
            var chageData = {};
            var indexInfo = {
                    _index: dataObj._index
            }
            
            // 추가된 데이터인지 확인
            if (state.added) {
                if (!state.selected) {
                    
                    // 데이터 자동 체크 처리
                    chageData = {
                            _state: {
                                selected: true
                            }
                    };
                    $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
                }
                
                return;
            }
            
            var isDiff = SKIAF.util.isDataDiff(dataObj._original, dataObj, editColumn);
            
            // 원본 데이터와 차이가 있는지 확인
            if (isDiff && !state.selected) {
                
                // 데이터 자동 체크 처리
                chageData = {
                        _state: {
                            selected: true
                        }
                };
                $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
            
            } else if (!isDiff && (state.selected || state.edited)) {

                // 데이터 자동 체크해제 처리
                chageData = {
                        _state: {
                            selected: false,
                            edited: false
                        }
                };
                
                $(e.currentTarget).alopexGrid('dataEdit', chageData, indexInfo);
            }

            ProgramSaveModule.checkSaveBtn(e.currentTarget.id);
        });
    };
});
