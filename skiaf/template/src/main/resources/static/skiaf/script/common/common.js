/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01876
 * description : skiaf 공통 js
 */
"use strict";
var CommonModule = $a.page(function() {
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    var requestCount = 0;
    var body_progress;
    
    var request_id; // $a.request 서비스 ID
    var request_param; // $a.request param
    
    // 첨부파일 허용 확장자
    var acceptType = 'jpg,gif,png,bmp,psd,' +
        'doc,docx,xls,xlsx,ppt,pptx,pdf,hwp,hwpx,txt,rtf,' +
        'zip,alz,egg,rar,7z';

    // 첨부파일 선택 가능한 확장자
    var acceptTypeWithDot = '.' + acceptType.split(',').join(', .');
    
    // 첨부파일 최대 사이즈 10MB
    var maxFileSize = 10485760;
    
    /**
     * alopex ui 기본 설정
     * - 다국어설정 : alopex에서 기본으로 지원하는 다국어로는 'ko'(한국어), 'en'(영어), 'zh'(중국어), 'ja'(일본어) 가 있다.
     */
    $a.setup({
        locale : SKIAF.i18n.langCurrentCode,
/* 'ko'(한국어), 'en'(영어), 'zh'(중국어), 'ja'(일본어) 가 아닌 4번째 언어 추가시, 주석 해제
        language:{
            es : { // 위에서 설정한 locale 값과 동일한 언어 key 사용
                validator :  { // 적용하고자 하는 컴포넌트 key 명을 소문자로 지정합니다.
                    required : "반드시 입력해야 하는 항목입니다.",
                    checkOnlySpace : "공백만을 입력할 수 없습니다.",
                    required_select : "반드시 선택해야 하는 항목입니다.",
                    minlength : "최소 {0}글자 이상 입력하십시오.",
                    maxlength : "최대 {0}글자 까지 입력 가능합니다.",
                    rangelength : "{0}에서 {1} 글자 사이로 입력하십시오.",
                    minblength : "최소 {0}바이트 이상 입력하십시오.",
                    maxblength : "최대 {0}바이트 까지 입력 가능합니다.",
                    rangeblength : "{0}에서 {1} 바이트 사이로 입력하십시오.",
                    min : "최소 입력가능 값은 {0}입니다.",
                    max : "최대 입력가능 값은 {0}입니다.",
                    range : "{0}에서 {1} 사이의 값을 입력해 주십시오.",
                    email : "이메일 형식에 맞게 입력해 주십시오.",
                    url : "url 형식에 맞게 입력해 주십시오.",
                    date : "날짜를 YYYY/MM/DD 또는 YYYY-MM-DD 형식에 맞게 입력해 주십시오.",
                    mindate : "{0} 또는 {0} 이후의 날짜를 입력해 주십시오.",
                    maxdate : "{0} 또는 {0} 이전의 날짜를 입력해 주십시오.",
                    daterange : "{0}에서 {1} 사이의 날짜를 입력해 주십시오.",
                    oneof : "다음중 하나의 값을 입력해 주십시오 : {param}.",
                    number : "실수를 입력해 주십시오.",
                    integer : "정수를 입력해 주십시오.",
                    digits : "숫자만 입력 가능합니다.",
                    alphabet : "알파벳만 입력 가능합니다.",
                    equalTo : "{0} 값만 가능합니다.",
                    numalpha : "숫자 또는 영문자만 입력 가능합니다.",
                    nospace : "스페이스는 입력할 수 없습니다.",
                    hangul : "한글만 입력 가능합니다.",
                    numhan : "숫자 또는 한글만 입력 가능합니다.",
                    phone : "대시(-)가 들어간 전화번호 형태를 입력해 주십시오.",
                    mobile : "대시(-)가 들어간 휴대전화번호 형태를 입력해 주십시오.",
                    decimal : "소숫점 {1}자리를 포함하여 최대 {0}자리까지 허용됩니다."
                },
                multiselect : {
                    noneSelectedText: "선택하세요",
                    checkAllText: '전체선택',
                    uncheckAllText: '전체해제',
                    selectedText: '#개 선택됨',
                },
                fileupload : {
                    dragDropStr : '<div class="fileupload-box">여기에 파일을 끌어다 놓으세요</div>',
                    multiDragErrorStr: '멀티 파일 Drag &amp; Drop 실패입니다.',
                    duplicateErrorStr: '이미 존재하는 파일입니다.',
                    extErrorStr: '허용되지 않는 확장자입니다.허용되는 확장자 : ',
                    sizeErrorStr: '허용 파일 용량을 초과하였습니다. 최대 파일 용량 : ',
                    maxFileCountErrorStr: '허용 파일 갯수를 초과하였습니다. 최대 파일 갯수 : ',
                    uploadErrorStr:'업로드가 실패하였습니다.',
                    uploadStr: '파일 추가',
                    checkAllStr : '전체 선택',
                    unCheckAllStr : '전체 해제',
                    checkedDeleteStr : '선택 삭제',
                }

            }
        }
*/
    });
    
    /**
     * $a.request() API 사용 시 필요한 공통 처리
     */
    $a.request.setup({
        url : function(id, param) {
            request_id = id;
            request_param = param;

            if (request_id == null) {
                return null;
            } else {
                return SKIAF.contextPath + id;
            }
        },
        timeout: 30000,
        before: function(id, option) {    
            
            ++requestCount;
            
            this.requestHeaders["Content-Type"] ="application/json; charset=UTF-8";

            //spring security - csrf_token
            if(option.method != "GET"){
                this.requestHeaders[$("meta[name='_csrf_header']").attr("content")] = $("meta[name='_csrf']").attr("content");
            }
            
            //loading 시작
            body_progress = $('body').progress();
            
        },
        after: function(res) {
            // 통신은 성공이지만, 서버업무오류 조건일 경우,
            // after콜백에서 this.isSuccess = false로 변경해야 success콜백이 아닌, fail콜백이 호출됨
            // if(fail condition ) {
            //      this.isSuccess = false;
            // }
        },
        success: function(res) {
            // 통신이 성공적으로 이루어 진 경우 호출되는 콜백함수
            
        },
        fail: function(res){
            // 통신은 성공적으로 이루어 졌으나, 서버오류가 발생한 경우 호출되는 콜백함수
            // after콜백에서 this.isSuccess = false로 변경해야 success콜백이 아닌, fail콜백이 호출됨

            SKIAF.console.error(res);
            
            /*
             * request fail 일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
             * $a.request 요청시 파라미터에 failConfirmCallback 함수를 추가해준다.
             * ex)
             * 
             * $a.request('/api/articles/'+id, {
                    method : 'GET',
                    success : function(res) {
                        $('#frm').setData(res.data);
                    },
                    //request fail일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
                    failConfirmCallback : function() {
                        console.log('test fail alert confirm');
                    },
                    //request error일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
                    errorConfirmCallback : function() {
                        console.log('test error alert confirm');
                    }
                });
             */
            if(request_param.hasOwnProperty('failConfirmCallback')){
                if(typeof request_param.failConfirmCallback === "function"){
                    // 에러 알림 팝업 호출(callback function 포함)
                    SKIAF.popup.exception(JSON.parse(res.responseText), request_param.failConfirmCallback);
                }
            } else {
                // 에러 알림 팝업 호출
                SKIAF.popup.exception(JSON.parse(res.responseText));
            }
        },
        error: function(errObject) {
            // 통신이 실패한 경우 호출되는 콜백함수

            SKIAF.console.error(errObject);
            
            // 401,403 에러시에 해당 에러 페이지로 이동
            if(errObject.status == 401){
                location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_401;
                return;
            }else if(errObject.status == 403){ 
                // 페이지 이동이 아닌 alert으로 처리
                //location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_403;
                var result = JSON.parse(errObject.response);
                result.meta.userMessage = SKIAF.i18n.messages["bcm.common.exception.forbidden-detail"];
                SKIAF.popup.exception(result);
                return;
            }
            
            
            var errorRes = JSON.parse(errObject.response)
            var code = errorRes.meta.code;
            
            if(code == null){
               
                /*
                 * request error일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
                 * $a.request 요청시 파라미터에 errorConfirmCallback 함수를 추가해준다.
                 * ex)
                 * 
                 * $a.request('/api/articles/'+id, {
                        method : 'GET',
                        success : function(res) {
                            $('#frm').setData(res.data);
                        },
                        //request fail일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
                        failConfirmCallback : function() {
                            console.log('test fail alert confirm');
                        },
                        //request error일 경우 에러알림팝업이 뜨고 '확인'버튼을 누르면 실행되는 callback 함수
                        errorConfirmCallback : function() {
                            console.log('test error alert confirm');
                        }
                    });
                 */
               
                if(request_param.hasOwnProperty('errorConfirmCallback')){
                    if(typeof request_param.errorConfirmCallback === "function"){
                        // 에러 알림 팝업 호출(callback function 포함)
                        SKIAF.popup.exception(JSON.parse(errObject.responseText), request_param.errorConfirmCallback);
                    }
                } else {
                    // 에러 알림 팝업 호출
                    SKIAF.popup.exception(JSON.parse(errObject.responseText));
                }
            }
                        
        },
        last : function(res, status, httpstatus) {
            // 통신성공,실패여부와 관계없이 맨 마지막에 호출되는 콜백함수

            --requestCount;
            //loading 종료
            if(requestCount == 0){
                setTimeout(function(){
                    body_progress.remove();
                }, 300);
            }
        }
    });
    
    /**
     * $a.popup() API 사용 시 필요한 공통 처리
     */
    $a.popup.setup({
        errorCallback : function(res) {
            // 401,403 에러시에 해당 에러 페이지로 이동
            if(res == 401){
                location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_401;
                return;
            }else if(res == 403){
                // 페이지 이동이 아닌 alert으로 처리
                //location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_403;
                var result = JSON.parse(res.response);
                result.meta.userMessage = SKIAF.i18n.messages["bcm.common.exception.forbidden-detail"];
                SKIAF.popup.exception(result);
                return;
            }
        }
    });
    
    /**
     * Alopex UI FileUpload API 사용 시 필요한 공통 처리
     */
    $a.setup('fileupload', {
        locale : SKIAF.i18n.langCurrentCode,
        fileName : 'file',
        showCancel : false,
        showAbort : false,
        showDone : false,
        showCheckedAll : false,
        showUnCheckedAll  : false,
        showDeleteChecked : false,
        allowedTypes  : acceptType,
        acceptFiles : acceptTypeWithDot,
        maxFileSize : maxFileSize,
        beforeUpload : function (xhr) {

            xhr.setRequestHeader($('meta[name="_csrf_header"]').attr('content'), $('meta[name="_csrf"]').attr('content'));

        },
        onError : function(files, status, errMsg, pd, xhr) {

            CommonModule.fileUploadErrorHandle(files, status, errMsg, pd, xhr);
        }
    });
    
    /**
     * AlopexGrid 공통 설정
     */
    AlopexGrid.setup({
        headerRowHeight: 30, /* 컬럼헤더 height */
        rowOption : {
            defaultHeight : 30 /* 셀 height */
        },
        message: {
            nodata: SKIAF.i18n.messages['bcm.common.nodata']
        },
        paging : {
            pagerTotal: function(pageInfo) {
                //총 건수 다국어처리
                return SKIAF.i18n.messages['bcm.common.total-count'] + " : " + pageInfo.dataLength;
            }
        }
    });
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        CommonModule.addEvent();
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.fileUploadErrorHandle = function(files, status, errMsg, pd, xhr) {

        // 취소 버튼 삭제
        if (pd && pd.cancel) {
            pd.cancel.remove();
        }

        // 파일업로드 에러메시지 처리
        if (errMsg) {
            SKIAF.popup.alert(errMsg);
            return;
        }
        
        if (!xhr) {
            return;
        }

        // 401,403 에러시에 해당 에러 페이지로 이동
        if(xhr.status == 401){
            location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_401;
            return;
        }else if(xhr.status == 403){
            // 페이지 이동이 아닌 alert으로 처리
            //location.href = SKIAF.contextPath + SKIAF.PATH.ERROR_403;
            //return;
        }
        
        // 에러 알림 팝업 호출
        SKIAF.popup.exception(JSON.parse(xhr.responseText));

    }
});
