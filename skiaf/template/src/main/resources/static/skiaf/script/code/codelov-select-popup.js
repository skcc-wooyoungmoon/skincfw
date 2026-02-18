/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866 description : 코드 목록 // 코드 select 파라메타 var
 * parmInit = { codeGroupId: null, singleSelect: null }; // return data var
 * parmResult = { codeId : null, codeName : null, codeDesc : null, codeSortSeq :
 * null };
 * 
 */
"use strict";
var CodeLovModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 코드 수정 그리드 컬럼 정의
    var codeGridColumns = [ 
        {title : SKIAF.i18n.messages['bcm.common.select'],       key : 'check',          align : 'center',  headerStyleclass : 'set', selectorColumn : true, resizing : false, width : '50px', excludeFitWidth : true},
        {title : SKIAF.i18n.messages['bcm.common.seq'],          key : 'codeSortSeq',    align : 'center',  tooltip : true, width : '70px'},
        {title : SKIAF.i18n.messages['bcm.code.code.id'],        key : 'codeId',         align : 'left',    tooltip : true, width : '115px'}
    ];
    SKIAF.i18n.langSupportedCodes.forEach(function (langCode, index) {
        codeGridColumns.push({title : SKIAF.i18n.messages['bcm.code.code.name'] + ' (' + langCode + ')', key : 'codeName' + (index + 1), align : 'left', tooltip : true, width : '150px'});
    });

    var codeGridEditObj = {
        height:270,
        numberingColumnFromZero: false,
        columnFixUpto : 2,
        autoColumnIndex : true,
        fitTableWidth : true,
        endInlineEditByOuterClick : true,
        message : {
            nodata : SKIAF.i18n.messages['bcm.common.nodata']
        },
        defaultColumnMapping : {
            resizing : true,
            sorting: true
        },
        rowSelectOption : {
            clickSelect : true,
            singleSelect : true,
            disableSelectByKey : true
        },
        columnMapping : codeGridColumns,
        data : []
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Dom Ready
    this.init = function(id, parmRequest) {
        if (parmRequest.codeGroupId && parmRequest.codeGroupId.length > 0) {
            // 이벤트 설정
            CodeLovModule.addEvent();

            // 파라메터 셋팅
            CodeLovModule.setParameter(parmRequest);

            // Row single 선택 여부 setting
            if (parmRequest.singleSelect == false) {
                codeGridEditObj.rowSelectOption.singleSelect = false;
            }

            // 그리드 초기화
            $('#lovPopSearchArea').setData({
                keyword : parmRequest.keyword
            });

            // 그리드 초기화
            $('#codeLovGrid').alopexGrid(codeGridEditObj);

            CodeLovModule.findOneCodeGroup(parmRequest);
        }
        else {
            $a.close();
        }
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        // 코드확인 버튼 클릭 이벤트
        $('#popupConfirmBtn').on('click', function(e) {
            // 선택된 데이터 리턴
            var selectedData = $('#codeLovGrid').alopexGrid('dataGet', {
                _state : {
                    selected : true
                }
            });

            if (selectedData.length == 0)
                return;

            var jsonData = [];

            $.each(selectedData, function(index, item) {
                var json = {};
                SKIAF.i18n.langSupportedCodes.forEach(function(langCode, index) {
                    if (langCode == SKIAF.i18n.langCurrentCode) {
                        json['codeName'] = item["codeName" + (index + 1)];
                    }
                });
                json['codeId'] = item.codeId;
                json['codeSortSeq'] = item.codeSortSeq;
                json['codeDesc'] = item.codeDesc;

                jsonData.push(json);
            });

            $a.close(jsonData);
        });

        // 검색 버튼 엔터
        $('#lovPopSearchText').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            CodeLovModule.selectCodeFilter();
        });

        // 코드 취소 버튼 클릭 이벤트
        $('#popupCancleBtn').on('click', function(e) {
            $a.close();
        });

        /**
         * 검색 버튼 클릭
         */
        $('#lovPopSearchBtn').on('click', function(e) {
            CodeLovModule.selectCodeFilter();
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 코드 목록 조회
     */
    this.searchCode = function() {
        // 파라메터 생성
        var params = {};
        var inputData = $('#codeEditArea').getData();
        var codeGroupId = inputData.codeGroupId;

        if (!codeGroupId) {
            return;
        }

        params.codeGroupId = codeGroupId;

        $('#codeLovGrid').alopexGrid('dataSet', []);

        // ajax 통신
        $a.request(SKIAF.PATH.CODES, {
            method : 'GET',
            data : params,
            success : function(result) {
                if (!result.data) {
                    $a.close();
                    return;
                }

                $('#codeLovGrid').alopexGrid('dataSet', result.data);

                // 필터 적용
                CodeLovModule.selectCodeFilter();
            }
        });
    };

    /**
     * 파라메터 셋팅
     */
    this.setParameter = function(parmRequest) {
        CodeLovModule.selectCodeGroupClear();

        // 파라미터 값 매칭
        $('#codeEditArea').setData(parmRequest);
    };

    /**
     * 코드 그룹 조회
     */
    this.findOneCodeGroup = function(parmRequest) {

        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODE_GROUPS_DETAIL, SKIAF.util.encodeUrlAndBase64(parmRequest.codeGroupId)), {
            method : 'GET',
            success : function(result) {
                if (!result.data) {
                    $a.close();
                    return;
                }

                CodeLovModule.selectCodeGroup(result.data);
                CodeLovModule.searchCode();
            }
        });
    };

    /**
     * 선택한 코드 그룹 데이터 입력
     */
    this.selectCodeGroup = function(data) {

        var codeGroupId = data.codeGroupId;
        var codeGroupName = '';
        var codeGroupLang = '';
        SKIAF.i18n.langSupportedCodes.forEach(function(lang, index) {
            if (lang == SKIAF.i18n.langCurrentCode) {
                codeGroupName = data["codeGroupName" + (index + 1)];
                codeGroupLang = lang;
            }
        });

        $('#codeEditArea').setData({
            codeGroupId : codeGroupId,
            codeGroupLang : codeGroupLang,
            codeGroupName : codeGroupName,
            useYnText : data.useYn ? '사용' : '미사용'
        });
    };

    /**
     * 선택한 코드 그룹 초기화
     */
    this.selectCodeGroupClear = function() {
        // 선택한 코드 그룹 정보 삭제
        $('#codeEditArea').setData({
            codeGroupId : '',
            codeGroupName : '',
            codeGroupLang : '',
            keyword : ''
        });
    };

    /**
     * 검색 코드 필터
     */
    this.selectCodeFilter = function() {
        var keyWord = $('#lovPopSearchText').val();

        $("#codeLovGrid").alopexGrid('deleteFilter', 'customFilterName');

        if (keyWord && keyWord.length > 0) {
            $("#codeLovGrid").alopexGrid('setFilter', 'customFilterName', CodeLovModule.lovCodeGridFilter);
        }
    };

    /**
     * 필터 설정
     */
    this.lovCodeGridFilter = function(data) {
        var keyWord = $('#lovPopSearchText').val();
        var ret = false;
        var codeName = "codeName1";

        SKIAF.i18n.langSupportedCodes.forEach(function(lang, index) {
            if (lang == SKIAF.i18n.langCurrentCode) {
                codeName = "codeName" + (index + 1);
            }
        });

        if (data['codeId'].search(keyWord) != -1) {
            ret = true;
        }

        if (data[codeName].search(keyWord) != -1) {
            ret = true;
        }

        return ret;
    };

});
