/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * interface 전문 테스트 화면
 */
var ExampFormatModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // 초기 오브젝트
    var formatInit = {
        useConfigType : "",
        configFileName : "",
        configContents : "",
        sendJsonData : "",

        sendStatusCode : "",
        sendStatusMessage : "",
        sendData : "",
        sendUseConfig : "",

        receiveStatusCode : "",
        receiveStatusMessage : "",
        receiveData : "",
        receiveUseConfig : ""
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {
        // 이벤트 설정
        ExampFormatModule.addEvent();

        //ExampFormatModule.readConfigFile(formatInit);
        $('#txaConfigContents').val($('#txaInitConfig').val());
        
        // grid setting
        ExampFormatModule.gridSetting();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {
        // config 파일 read
        $('#btnReadConfig').on('click', function(e) {
            ExampFormatModule.readConfigFile(formatInit);
        });

        // Json data 생성
        $('#btnMakeJsonData').on(
                'click',
                function(e) {
                    $('#txaSendJsonData').val(
                            JSON.stringify($("#programGrid").alopexGrid(
                                    "dataGet", function(data) {
                                        return data._index.row != null;
                                    })));
                });

        // data convert
        $('#btnSend').on('click', function(e) {
            ExampFormatModule.convertData(formatInit);
        });
        $('#btnReceive').on('click', function(e) {
            ExampFormatModule.convertData(formatInit);
        });

        // 설명 display
        $('#btnConfigDesc').on('click', function(e) {
            ExampFormatModule.toggle_element("#divConfigDesc");
        });
        $('#btnJsonDesc').on('click', function(e) {
            ExampFormatModule.toggle_element("#divJsonDesc");
        });
        $('#btnSendDesc').on('click', function(e) {
            ExampFormatModule.toggle_element("#divSendDesc");
        });
        $('#btnReceiveDesc').on('click', function(e) {
            ExampFormatModule.toggle_element("#divReceiveDesc");
        });

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * grid setting
     */
    this.gridSetting = function(id, param) {
        var linkGridObj = {
            autoColumnIndex: true,
            fitTableWidth: true,
            headerRowHeight: 30, /* 컬럼헤더 height */
            message: {
                nodata: '데이터가 없습니다.'
            },
            columnFixUpto: 1,
            height:404, /* 전체 height */
            pager : true,
            paging: {
              perPage : 10,
              pagerCount: 10,
              pagerTotal: true,
              pagerSelect : true
            },
            defaultColumnMapping: {
                resizing: true
            },
            rowOption : {
                styleclass : function(data, rowOption){
                    if(data["code"] === '10002'){
                        return ''
                    } else if(data["code"] === '20002') {
                        return 'highlight-add'
                    } else if(data["code"] === '30002') {
                        return 'highlight-edit'
                    } else if(data["code"] === '40002') {
                        return 'highlight-add highlight-blur'
                    }
                },
                defaultHeight : 30 /* 셀 height */
            },
            rowSelectOption:{
                singleSelect: true
            },
            readonlyRender: false,
            columnMapping: [{
                    title: '선택',
                    inlineStyle: {'line-height' : '30px'},
                    align: 'center',
                    headerStyleclass:'set',
                    selectorColumn : true,
                    resizing : false,
                    width: 50
                },
                {
                    title: '프로그램 ID',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'programId',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '프로그램명',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'programName',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '유형',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'programType',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '프로그램 설명',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'department',
                    width: 170,
                    align: 'center',
                    tooltip: false
                }, {
                    title: '메소드',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'httpMethod',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '경로',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'company',
                    align: 'left',
                    tooltip: false
                }, {
                    title: '사용여부',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'form',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '수정자',
                    inlineStyle: {'line-height' : '30px'},
                    key: ' ',
                    align: 'center',
                    width: 90,
                    tooltip: false
                }, {
                    title: '수정 일시',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'linkdate',
                    align: 'center',
                    width: 170,
                    tooltip: false
                }, {
                    title: '등록자',
                    inlineStyle: {'line-height' : '30px'},
                    key: ' ',
                    align: 'center',
                    width: 90,
                    tooltip: false
                }, {
                    title: '등록 일시',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'linkdate',
                    align: 'center',
                    width: 170,
                    tooltip: false
                }
            ],
            data: [
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "programName": "뉴스서비스-목록조회",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "programName": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "programName": "뉴스서비스-목록조회",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "programName": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },{
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "programName": "뉴스서비스-목록조회",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "programName": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "programId": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "programName": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "programType":"SERVICE",
                    "httpMethod":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                }
            ]
        };

        $('#programGrid').alopexGrid(linkGridObj);
    };
    
    /**
     * 설명 display
     */
    this.toggle_element = function(id) {
        if ($(id).css("display") == "none") {
            $(id).show();
        } else {
            $(id).hide();
        }
    };

    /**
     * config file read
     */
    this.readConfigFile = function(formatInit) {

        formatInit.configFileName = $('#txtConfigFileName').val();

        $a.request('/api/examp-format-readconfig', {
            method : 'POST',
            data : formatInit,
            success : function(res) {

                if (!res.data) {
                    SKIAF.popup.alert(SKIAF.i18n.messages['bcm.common.nodata']);
                    return;
                }

                if ($('#txtConfigFileName').val() == "") {
                    $('#txtConfigFileName').val(res.data.configFileName);
                    // $('#txaInitConfig').val(res.data.configContents);
                }
                $('#txaConfigContents').val(res.data.configContents);
            }
        });
    };

    /**
     * data 변환
     */
    this.convertData = function(formatInit) {

        formatInit.useConfigType = $('#radioUseConfigType:checked').val();
        formatInit.configFileName = $('#txtConfigFileName').val();
        formatInit.configContents = $('#txaConfigContents').val();
        formatInit.sendJsonData = $('#txaSendJsonData').val();

        $('#tab3').setData(formatInit);
        $('#tab4').setData(formatInit);

        if (formatInit.useConfigType == "FILE") {
            if (formatInit.configFileName.length == 0) {
                SKIAF.popup.alert("xml 파일 path가 없습니다.");
                return;
            }
        } else {
            if (formatInit.configContents.length == 0) {
                SKIAF.popup.alert("xml 내용이 없습니다.");
                return;
            }
        }

        if (formatInit.sendJsonData.length == 0) {
            SKIAF.popup.alert("JSON data를 먼저 만들어 주세요.");
            return;
        }

        $a.request('/api/examp-format', {
            method : 'POST',
            data : formatInit,
            success : function(res) {

                if (!res.data) {
                    SKIAF.popup.alert("데이타가 없습니다.");
                    return;
                }

                $('#tab3').setData(res.data);
                $('#tab4').setData(res.data);
            }
        });
    };

});
