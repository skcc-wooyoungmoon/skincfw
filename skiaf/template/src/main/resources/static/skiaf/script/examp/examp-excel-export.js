/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * 엑셀 export 테스트 화면
 */
var ExampExcelExportModule = $a.page(function() {

    /**
     * 페이지 초기화
     */
    this.init = function(id, param) {
        // 이벤트 설정
        ExampExcelExportModule.addEvent();
        
        // grid setting
        ExampExcelExportModule.gridSetting();
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {
        // 엑셀 테스트
        $('#btnExcel').on('click', function(e) {
            ExampExcelExportModule.execlExportParm();
        });
        $('#btnExcelUrlProgram').on('click', function(e) {
            ExampExcelExportModule.execlExportUrlProgram();
        });

        // 설명 display
        $('#btnDescGrid').on('click', function(e) {
            ExampExcelExportModule.toggle_element("#divDescGrid");
        });
        $('#btnDescUrl').on('click', function(e) {
            ExampExcelExportModule.toggle_element("#divDescUrl");
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

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
     * execl export : Grid 파라메타로 excel export
     */
    this.execlExportParm = function() {
        SKIAF.excelUtil.excelExportGrid({
            gridId : "#programGrid",
            fileName : "Alopex엑셀테스트",
            exportHidden : false,
            useGridColumnWidth : true
        });
    };

    /**
     * execl export
     */
    this.execlExportUrlProgram = function() {
        // excel export URL
        SKIAF.excelUtil.excelExportUrl({
            url : SKIAF.PATH.PROGRAMS,
            method : 'GET',
            data : {
                page : 0,
                size : 10,
                isUnusedInclude : false
            },
            resultListName : "programList",
            columnNames : [ { title : '프로그램 ID', key : 'programId' }, 
                { title : '프로그램명', key : 'programName' }, 
                { title : '유형', key : 'programType' }, 
                { title : '설명', key : 'programDesc' },  
                { title : '메소드', key : 'httpMethod' }, 
                { title : '경로', key : 'programPath' } 
            ]
        });
    };

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
                    key: 'id',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '프로그램명',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'name',
                    align: 'center',
                    tooltip: false
                }, {
                    title: '유형',
                    inlineStyle: {'line-height' : '30px'},
                    key: 'type',
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
                    key: 'method',
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
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "department": "뉴스서비스-목록조회",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "department": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "department": "뉴스서비스-목록조회",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "department": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },{
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "1000",
                    "company": "사용자 메뉴 권한",
                    "department": "뉴스서비스-목록조회",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "linkdate":"2018-07-09 19:23:22",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "2000",
                    "company": "관리자 메뉴 권한",
                    "department": "뉴스서비스-등록",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "3000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-상세조회",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "수석",
                    "href": "#"
                },
                {
                    "id": "ROLE003",
                    "name": "PRO_USER",
                    "code": "4000",
                    "company": "뉴스서비스-등록",
                    "department": "뉴스서비스-수정",
                    "linkdate":"2018-07-09 19:23:22",
                    "type":"SERVICE",
                    "method":"GET",
                    "form":"y",
                    "rank": "과장",
                    "href": "#"
                }
            ]
        };

        $('#programGrid').alopexGrid(linkGridObj);
    };
});
