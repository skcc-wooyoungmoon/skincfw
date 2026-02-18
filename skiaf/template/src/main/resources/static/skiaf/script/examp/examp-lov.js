/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * LOV 테스트 화면 // 코드 select 파라메타 var parmInit = { codeGroupId: null,
 * singleSelect: null };
 *  // return data var parmResult = { codeId : null, codeName : null, codeDesc :
 * null, codeSortSeq : null };
 */
var ExampLovModule = $a.page(function() {

    this.init = function(id, param) {
        // 이벤트 설정
        ExampLovModule.addEvent();
    };

    var codeGroupId ="";
    var codeGroupName ="";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        // 코드 그룹
        $('#codeGroupId').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            $('#codeGroupBtn').trigger('click', 'ExampLovModule');
        });

        // 코드 그룹 선택 팝업 이벤트
        $('#codeGroupBtn').on('click', function(e) {
            $a.popup({
                url : SKIAF.PATH.VIEW_CODE_GROUPS_SELECT,
                data : {
                    keyword : $('#codeGroupId').val()
                },
                movable : true,
                iframe : false,
                width : 1000,
                height : 630,
                center : true,
                title : SKIAF.i18n.messages['bcm.code.codegroup.search'],
                callback : function(data) {
                    
                    $('#codeGroupId').val(data.codeGroupId);

                    codeGroupId = data.codeGroupId;
                    codeGroupName = "";                    
                    SKIAF.i18n.langSupportedCodes.forEach(function(langCode, index) {
                        if (langCode == SKIAF.i18n.langCurrentCode) {
                            codeGroupName = data["codeGroupName" + (index + 1)];
                        }
                    });
                }
            });
        });

        // 검색 버튼 엔터
        $('#codeDetailId1').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            $('#codeGroupBtn1').trigger('click', 'ExampLovModule');
        });

        // 검색 버튼 엔터
        $('#codeDetailId1').on('keypress', function(e) {
            if (e.keyCode != 13) {
                return;
            }
            $('#codeGroupBtn1').trigger('click', 'ExampLovModule');
        });

        // 설명
        $('#lovDescBtn').on('click', function(e) {
            var id = '#divDescParent';
            
            if ($(id).css("display") == "none") {
                $(id).show();
            } else {
                $(id).hide();
            }
        });
        
        // 확인
        $('#lovBtn1').on('click', function(e) {
            ExampLovModule.codeGroupBtn1();
        });
        $('#lovBtn3').on('click', function(e) {
            ExampLovModule.codeGroupBtn3();
        });
        $('#lovBtn4').on('click', function(e) {
            ExampLovModule.codeGroupBtn4();
        });
        $('#lovBtn5').on('click', function(e) {
            ExampLovModule.codeGroupBtn5();
        });
        $('#lovBtn6').on('click', function(e) {
            ExampLovModule.codeGroupBtn6();
        });
    };

    // Single 코드값 선택
    this.codeGroupBtn1 = function(e) {
        // 팝업으로 선택한 ID가 아니면 코드명은 null
        if (codeGroupId != $('#codeGroupId').val()) {
            codeGroupName = "";
        }
        
        SKIAF.util.lovPopup({
            codeGroupId : $('#codeGroupId').val(),
            codeGroupName : codeGroupName,
            keyword : $('#codeDetailId1').val()
        }, function(data) {
            $('#codeDetailDataArea1').setData(data[0]);
        });
    };

    //
    this.codeGroupBtn3 = function(e) {
        SKIAF.util.lovPopup({
            codeGroupId : $('#codeGroupId').val(),
            singleSelect : false
        }, function(data) {
            $('#codeDetailDataArea3').setData({
                data : JSON.stringify(data)
            });
        });
    };

    // multiple 코드값 선택 (json data)
    this.codeGroupBtn4 = function(e) {
        SKIAF.util.lovCodeList($('#codeGroupId').val(), function(data) {
            $('#codeDetailDataArea4').setData({
                data : data
            });
        });
    };

    // check Box 생성
    this.codeGroupBtn5 = function(e) {
        SKIAF.util.lovCodeList($('#codeGroupId').val(), function(data) {
            // Alopex 권고 : foreach 로 data-bind 할경우  클릭이 안되는 문제 때문에 삽입
            $('#checkCode').removeAttr('data-converted');
            
            $('#codeDetailDataArea5').show();
            
            $('#codeDetailDataArea5').setData({
                data : data
            });
            
            // Alopex 권고 : foreach 로 data-bind 할경우  클릭이 안되는 문제 때문에 삽입
            $a.convert($('#codeDetailDataArea5'));
        });
    };

    // radio button 생성
    this.codeGroupBtn6 = function(e) {
        SKIAF.util.lovCodeList($('#codeGroupId').val(), function(data) {
            // Alopex 권고 : foreach 로 data-bind 할경우  클릭이 안되는 문제 때문에 삽입
            $('#radioCode').removeAttr('data-converted');
            
            $('#codeDetailDataArea6').show();
       
            $('#codeDetailDataArea6').setData({
                data : data
            });
            
            // Alopex 권고 : foreach 로 data-bind 할경우 클릭이 안되는 문제 때문에 삽입
            $a.convert($('#codeDetailDataArea6'));
        });
    };
});
