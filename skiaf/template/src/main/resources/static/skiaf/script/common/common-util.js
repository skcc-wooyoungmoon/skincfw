/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/*
 * 공통 javascript 유틸리티
 */
"use strict";
window.SKIAF.variable = {
    logLevel : 1,
    preFix : 'SKIAF_BCM_',
};

window.SKIAF.console = {

    logLevel : 2,

    trace : function() { //level 1
        
        if(this.logLevel <= 1){console.trace.apply(console, Array.prototype.slice.call(arguments));}
    },

    debug : function() { //level 2
        if(this.logLevel <= 2){console.debug.apply(console, Array.prototype.slice.call(arguments));}
    },

    info : function() { //level 3
        if(this.logLevel <= 3){console.info.apply(console, Array.prototype.slice.call(arguments));}
    },

    warn : function() { //level 4
        if(this.logLevel <= 4){console.warn.apply(console, Array.prototype.slice.call(arguments));}
    },

    error : function() { //level 5
        if(this.logLevel <= 5){console.error.apply(console, Array.prototype.slice.call(arguments));}
    }
};

window.SKIAF.popup = {
    /*
     * 알림 팝업 호출
     * 
     *  1. 메세지
     * ex) SKIAF.popup.alert('ID를 입력해주세요.');
     * 
     * 2. 메세지와 콜백함수
     * ex) SKIAF.popup.alert('ID를 입력해주세요.', function(){ alert('확인 버튼을 누르셨습니다.'); });
     * 
     * 3. 메세지와 콜백함수(매개변수 있는경우)
     * ex) SKIAF.popup.alert('ID를 입력해주세요.', function(callbackParam){ alert(callbackParam.message); }, callbackParam);
     * 
     */
    alert : function(message, callbackFunction) {
        
        // 콜백함수의 매개변수
        var callbackParam = Array.prototype.splice.call(arguments, 2);

        var pop = $a.popup({
            title : SKIAF.i18n.messages["bcm.common.alert"],
            url : '/static/skiaf/popup/alert.html',
            data : {
                message : message
            },
            iframe: false,
            movable:true,
            width: 400, //width 필요
            height: 253, //height 필요
            callback : function(data) {
                if(data.type == 'confirm') {
                    if(typeof callbackFunction === "function"){
                        callbackFunction.apply(this, callbackParam);
                    }
                }
            }
        });
        $(pop).addClass('alert');
    },
    /*
     * 확인 팝업 호출
     * 
     * 1. 메세지
     * ex) SKIAF.popup.confirm('저장하시겠습니까?');
     * 
     * 2. 메세지와 콜백함수
     * ex) SKIAF.popup.confirm('저장하시겠습니까?', function callback(){alert('callback!!')});
     * 
     * 3. 메세지와 콜백함수(매개변수 있는경우)
     * ex) SKIAF.popup.confirm('저장하시겠습니까?', function callback(param){alert(param.message);}, callbackParam);
     */
    confirm : function(message, callbackFunction) {

        // 콜백함수의 매개변수
        var callbackParam = Array.prototype.splice.call(arguments, 2);

        var pop = $a.popup({
            title : SKIAF.i18n.messages["bcm.common.confirm"],
            url : '/static/skiaf/popup/confirm.html',
            data : {
                message : message
            },
            iframe: false,
            movable:true,
            width: 400, //width 필요
            height: 253, //height 필요
            callback : function(data) {
                if(data.type == 'confirm') {
                    if(typeof callbackFunction === "function"){
                        callbackFunction.apply(this, callbackParam);
                    }
                }
            }
        });
        $(pop).addClass('alert');
    },
    /*
     * 에러 팝업 호출
     * 
     * 1. 에러 메타(RestResponse) 정보
     * ex) SKIAF.popup.exception({meta : {systemMessage : null, userMessage : '찾지못했습니다.', displayType : 'LAYER_POPUP'}});
     * 
     * 2. 에러 메타(RestResponse) 정보와 콜백함수
     * ex) SKIAF.popup.exception({meta : {systemMessage : 'not found', userMessage : '찾지못했습니다.', displayType : null}}, function callback(){alert('callback!!')});
     * 
     * 3. 에러 메타(RestResponse) 정보와 콜백함수(매개변수 있는경우)
     * ex) SKIAF.popup.exception({ meta : {systemMessage : 'not found', userMessage : '찾지못했습니다.', displayType : null} }, function callback(callbackParam){alert(callbackParam.message);}, callbackParam);
     */
    exception : function(response, callbackFunction) {
        
        // 콜백함수의 매개변수
        var callbackParam = Array.prototype.splice.call(arguments, 2);
        
        var displayType = 'LAYER_POPUP';
        if(response.meta.displayType) {
            displayType = response.meta.displayType;
        }
        
        if(displayType == 'LAYER_POPUP') {

            var pop = $a.popup({
                title : SKIAF.i18n.messages["bcm.common.error"],
                url : '/static/skiaf/popup/exception.html',
                data : {
                    meta : response.meta
                },
                iframe: false,
                movable:true, 
                width: 420, //width 필요
                height: 320, //height 필요
                callback : function(data) {
                    if(data.type == 'confirm') {
                        callbackFunction.apply(this, callbackParam);
                    }
                }
            });
            $(pop).addClass('alert');
        
        }
    }
};

window.SKIAF.util = {
    
    /*
     * 기준일자가 들어있는 달의 말일 구하기
     * ex) getLastDate("2018-10-07") => "31"
     */
    getLastDate : function(dt) {
        var arr = dt.split('-');        
        var lastDate = new Date(arr[0], arr[1], '');
        return lastDate.getDate();
    },
    
    /*
     * 두 날짜 사이의 달 수를 구하기
     * ex) SKIAF.util.getDiffMonth('2018-10-10','2017-12-13'); => "10"
     */
    getDiffMonth : function(date1, date2) {
        var diffDate1 = date1 instanceof Date ? date1 : new Date(date1);
        var diffDate2 = date2 instanceof Date ? date2 : new Date(date2);

        diffDate1 = new Date(diffDate1.getFullYear(), diffDate1.getMonth()+1, diffDate1.getDate());
        diffDate2 = new Date(diffDate2.getFullYear(), diffDate2.getMonth()+1, diffDate2.getDate());
        
        var diff = diffDate2.getTime() - diffDate1.getTime();
        if(diff > 0) {
            diff = ((diffDate2.getFullYear() - diffDate1.getFullYear()) * 12) + (diffDate2.getMonth() - diffDate1.getMonth());
        } else {
            diff = ((diffDate1.getFullYear() - diffDate2.getFullYear()) * 12) + (diffDate1.getMonth() - diffDate2.getMonth());
        }
        return diff;
    },
    
    /*
     * 두 날짜 사이의 일 수를 구하기
     * ex) SKIAF.util.getDiffDate('2018-10-10','2018-12-13'); => "64"
     */
    getDiffDate : function(date1, date2) {
        var diffDate1 = date1 instanceof Date ? date1 : new Date(date1);
        var diffDate2 = date2 instanceof Date ? date2 : new Date(date2);

        diffDate1 = new Date(diffDate1.getFullYear(), diffDate1.getMonth()+1, diffDate1.getDate());
        diffDate2 = new Date(diffDate2.getFullYear(), diffDate2.getMonth()+1, diffDate2.getDate());

        var diff = Math.abs(diffDate2.getTime() - diffDate1.getTime());
        diff = Math.ceil(diff / (1000 * 3600 * 24));
        return diff;
    },
    
    /*
     * 두날짜 사이의 시간차 구하기
     * ex) SKIAF.util.getDiffTime('2018-11-13 09:23:44', '2018-11-12 09:35:33') => "23시간 48분 11초"
     */
    getDiffTime : function(date1, date2) {
        var diffDate1 = date1 instanceof Date ? date1 : new Date(date1.replace('-', '/'));
        var diffDate2 = date2 instanceof Date ? date2 : new Date(date2.replace('-', '/'));
        var time = Math.abs(diffDate2.getTime() - diffDate1.getTime());
        
        var timeGap = new Date(0, 0, 0, 0, 0, 0, time);

        var diffDay = Math.floor(time / (1000 * 60 * 60 * 24));
        var diffHour = timeGap.getHours();
        var diffMin = timeGap.getMinutes();
        var diffSec = timeGap.getSeconds();
        
        var diffTimeStr = ''; 
        if(diffDay > 0) {
            diffTimeStr += (diffDay * 24) + diffHour;
        } else {
            diffTimeStr += diffHour;
        }
        
        diffTimeStr += SKIAF.i18n.messages['bcm.common.hour'] + ' ';
        diffTimeStr += diffMin + SKIAF.i18n.messages['bcm.common.minute'] + ' ';
        diffTimeStr += diffSec + SKIAF.i18n.messages['bcm.common.seconds'];

        return diffTimeStr;
    },
    
    /*
     * 두날짜 사이의 시간차 분으로 구하기
     * ex) SKIAF.util.getDiffMin('2018-11-13 09:23:44', '2018-11-13 09:35:35') => "11분 51초"
     */
    getDiffMin : function(date1, date2) {
        var diffDate1 = date1 instanceof Date ? date1 : new Date(date1.replace('-', '/'));
        var diffDate2 = date2 instanceof Date ? date2 : new Date(date2.replace('-', '/'));
        
        var time = Math.abs(diffDate2.getTime() - diffDate1.getTime());
        var timeGap = new Date(0, 0, 0, 0, 0, 0, time);

        var diffDay = Math.floor(time / (1000 * 60 * 60 * 24));
        var diffHour = timeGap.getHours();
        var diffMin = timeGap.getMinutes();
        var diffSec = timeGap.getSeconds();
        
        var diffMinStr = '';
        if(diffHour > 0) {
            if(diffDay > 0) {
                diffMinStr += (diffDay * 24 * diffHour * 60) + diffMin;
            } else {
                diffMinStr += (diffHour * 60) + diffMin;
            }
        } else {
            diffMinStr += diffMin;    
        }
        
        diffMinStr += SKIAF.i18n.messages['bcm.common.minute'] +' ' + diffSec + SKIAF.i18n.messages['bcm.common.seconds'];

        return diffMinStr;
    },
    
    /*
     * 두날짜 사이의 시간차 초로 구하기
     * ex) SKIAF.util.getDiffSec('2018-11-13 09:34:44', '2018-11-13 09:35:33') => "49초"
     */
    getDiffSec : function(date1, date2) {
        var diffDate1 = date1 instanceof Date ? date1 : new Date(date1.replace('-', '/'));
        var diffDate2 = date2 instanceof Date ? date2 : new Date(date2.replace('-', '/'));
        
        var time = Math.abs(diffDate2.getTime() - diffDate1.getTime());
        var timeGap = new Date(0, 0, 0, 0, 0, 0, time);

        var diffDay = Math.floor(time / (1000 * 60 * 60 * 24));
        var diffHour = timeGap.getHours();
        var diffMin = timeGap.getMinutes();
        var diffSec = timeGap.getSeconds();
        
        var diffSecStr = '';
        if(diffMin > 0) {
            if(diffHour > 0) {
                if(diffDay > 0) {
                    diffSecStr += (diffDay * 24 * diffHour * 60 * diffMin * 60) + diffSec;
                } else {
                    diffSecStr += (diffHour * 60 * diffMin * 60 ) + diffSec;
                }
            } else {
                diffSecStr += (diffMin * 60) + diffSec;    
            }
        } else {
            diffSecStr += diffSec;   
        }
        
        diffSecStr += SKIAF.i18n.messages['bcm.common.seconds'];

        return diffSecStr;
    },
    
    /*
     * 입력값이 특정 형식에 맞는지를 검사한다.
     * '9' : 숫자
     * '#' : 영문
     * ex) checkFormat("12345-12345", "99999-99999") : true   
     *     checkFormat("12345-ABCDE", "99999-99999") : false   
     *     checkFormat("12345-ABCDE", "#####-#####") : true 
     */
    checkFormat : function(txt, fmt) {
        var fmtArr = fmt.split('');
        var txtArr = txt.split('');
        
        if(txtArr.length != fmt.length) {
            return false;
        }
        
        for(var i = 0, len = fmtArr.length; i < len; i++) {
            if(fmtArr[i] == '9') {
               if(!/[0-9]/.test(txtArr[i])) {
                   return false;
               }
            } else if(fmtArr[i] == '#') {
                if(!/[a-zA-Z]/.test(txtArr[i])) {
                    return false;
                }
            } else {
                if(fmtArr[i] != txtArr[i]) {
                    return false;
                }
            }
        }
        
        return true;
    },

    /*
     * 입력값을 특정 형식에 맞게 변환한다.
     * ex) makeFormat("1234512345","#####-#####") =>   "12345-12345"
     */
    makeFormat : function(txt, fmt) {
        var fmtArr = fmt.split('');
        var txtArr = txt.split('');

        var changeStr = '';
        var j = 0;
        for (var i = 0, len = fmtArr.length; i < len; i++) {
            if (fmtArr[i] == '#') {
                if (txtArr[j] != null) {
                    changeStr += txtArr[j];
                    j++;
                }
            } else {
                changeStr += fmtArr[i];
            }
        }
        return changeStr;
    },

    /*
     * 문자열의 특정 문자를 제거한다. ex) removeChar("abcdefg","e") => "abcdfg"
     */
    removeChar : function(txt, chr) {
        var txtArr = txt.split('');

        var changeStr = '';
        for (var i = 0, len = txtArr.length; i < len; i++) {
            if (txtArr[i] != chr) {
                changeStr += txtArr[i];
            }
        }
        return changeStr;
    },

    /*
     * 주민번호 마스킹 값 리턴 ex) getMaskForCtzNum("000000-0000000", "#", 2, 7) =>
     * "00####-##00000"
     */
    getMaskForCtzNum : function(ctz, mask, start, end) {
        return this.getMask(ctz, '######-#######', mask, start, end);
    },

    /*
     * 법인번호 마스킹 값 리턴 ex) getMaskForCorpNum("000000-0000000", "#", 2, 7) =>
     * "00####-##00000"
     */
    getMaskForCorpNum : function(corp, mask, start, end) {
        return this.getMask(corp, '######-#######', mask, start, end);
    },

    /*
     * 사업자번호 마스킹 값 리턴 ex) getMaskForBizNum("000-00-00000", "#", 3, 6) =>
     * "000-##-##000"
     */
    getMaskForBizNum : function(biz, mask, start, end) {
        return this.getMask(biz, '###-##-#####', mask, start, end);
    },

    /*
     * 우편번호 마스킹 값 리턴 ex) getMaskForPostNum("000-000", "#", 3, 5) => "000-###"
     */
    getMaskForPostNum : function(post, mask, start, end) {
        return this.getMask(post, '###-###', mask, start, end);
    },

    /*
     * ‘-‘ 없는 주민번호 마스킹 값 리턴 ex) getNoHypenMaskForCtzNum("0000000000000", "#", 3, 5) =>
     * "000##00000000"
     */
    getNoHypenMaskForCtzNum : function(ctz, mask, start, end) {
        return this.getNoHypenMask(13, ctz, mask, start, end);
    },

    /*
     * ‘-‘ 없는 법인번호 마스킹 값 리턴 ex) getNoHypenMaskForCorpNum("0000000000000", "#", 3, 5) =>
     * "000##00000000"
     */
    getNoHypenMaskForCorpNum : function(corp, mask, start, end) {
        return this.getNoHypenMask(13, corp, mask, start, end);
    },

    /*
     * ‘-‘ 없는 사업자번호 마스킹 값 리턴 ex) getNoHypenMaskForBizNum("0000000000", "#", 3, 5) =>
     * "000##00000"
     */
    getNoHypenMaskForBizNum : function(biz, mask, start, end) {
        return this.getNoHypenMask(10, biz, mask, start, end);
    },

    /*
     * ‘-‘ 없는 우편번호 마스킹 값 리턴 ex) getNoHypenMaskForPostNum("000000", "#", 0, 3) =>
     * "###000"
     */
    getNoHypenMaskForPostNum : function(post, mask, start, end) {
        return this.getNoHypenMask(6, post, mask, start, end);
    },

    /*
     * 널값(비어있는 스트링값)인지를 검사
     */
    isNull : function(value) {
        return (value === undefined || value == null || value == "") ? true : false;
    },

    /*
     * 알파벳만으로 되어 있는지 검사
     */
    isAlpha : function(value) {
        if(this.isNull(value)) return false;
        
        var regExp = /^[a-zA-Z]+$/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 알파벳과 숫자로 되어 있는지 검사
     */
    isAlphaNumber : function(value) {
        if(this.isNull(value)) return false;
        
        var regExp = /^[A-Za-z0-9]*$/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 숫자(0~9)로 되어 있는지 검사
     */
    isDigit : function(value) {
        if(this.isNull(value)) return false;
        
        var regExp = /^[0-9+]*$/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 이메일 형식에 맞는지를 검사
     */
    isEmail : function(value) {
        if(this.isNull(value)) return false;
        
        var regExp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 정수형인지를 검사(마이너스, 소수점 등)
     */
    isNumber : function (value) {
        if(this.isNull(value)) return false;
        
        var regExp = /^[+-]?\d*(\.?\d*)$/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 특수문자가 들어있는지를 체크
     */
    haveSpecialChar : function (value) {
        if(this.isNull(value)) return false;
        
        var regExp = /[^A-Za-z0-9]/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 유효한 시간표기형식(HHMMSS)인지를 검사
     */
    isValidateTime : function(value) {
        if(this.isNull(value)) return false;
        
        var regExp = /(00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23)+(0|1|2|3|4|5)\d+(0|1|2|3|4|5)\d/;
        return (value.match(regExp)) ? true : false;
    },

    /*
     * 주민번호 유효성 체크 ( 000000-0000000 )
     */
    isValidCtzNum : function (jumin) {

        if (jumin.match(/^\d{2}[0-1]\d[0-3]\d-\d{7}$/) == null) {
            return false;
        }

        var chk = 0;
        var i;
        var last_num = jumin.substring(13, 14);
        var chk_num = '234567-892345';

        for (i = 0; i < 13; i++) {
            if (jumin.charAt(i) != '-') {
                chk += (parseInt(chk_num.charAt(i)) * parseInt(jumin.charAt(i)));
            }
        }

        chk = (11 - (chk % 11)) % 10;

        if (chk != last_num) {
            return false;
        }

        return true;
    },

    /*
     * 법인번호 유효성 체크
     */
    isValidCorpNum : function (sRegNo) {
        // var re = /-/g;
        sRegNo = sRegNo.replace('-', '');

        if (sRegNo.length != 13) {
            return false;
        }

        var arr_regno = sRegNo.split("");
        var arr_wt = new Array(1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2);
        var iSum_regno = 0;
        var iCheck_digit = 0;

        for (var i = 0; i < 12; i++) {
            iSum_regno += Number(arr_regno[i]) * Number(arr_wt[i]);
        }

        iCheck_digit = 10 - (iSum_regno % 10);

        iCheck_digit = iCheck_digit % 10;

        if (iCheck_digit != arr_regno[12]) {
            return false;
        }
        return true;
    },

    /*
     * 미성년자 여부 체크 birth : 19981225, 1998-12-25
     */
    getChkMinor : function (birth) {
        var date = new Date();
        var year = date.getFullYear();
        var month = (date.getMonth() + 1);
        var day = date.getDate();
        if (month < 10) {
            month = '0' + month;
        }
        if (day < 10) {
            day = '0' + day;
        }

        var monthDay = month + day;

        birth = birth.replace('-', '').replace('-', '');

        var birthdayy = birth.substr(0, 4);
        var birthdaymd = birth.substr(4, 4);

        var age = monthDay < birthdaymd ? year - birthdayy - 1 : year - birthdayy;

        return (age > 18) ? false : true;
    },

    /*
     * 사용자가 권한이 있는지 여부를 검사 frontend에서 권한검사시, DB를 체크하지 않고 로그인시에 획득된 사용자권한에서 검사하게 함
     */
    hasAuth : function (roleId) {
        
        // 로그인 정보가 있는지 확인
        if (SKIAF.loginUserInfo == null) {
            return false;
        }
        
        // 권한 정보가 있는지 확인
        if (SKIAF.loginUserInfo.roleList == null) {
            return false;
        }
        if (!Array.isArray(SKIAF.loginUserInfo.roleList)) {
            return false;
        }
        
        // 권한 체크
        for (var index in SKIAF.loginUserInfo.roleList) {
            var role = SKIAF.loginUserInfo.roleList[index];
            if (roleId == role.roleId) {
                return true;
            }
        }

        return false;
    },

    /**
     * 선택한 코드그룹의 상세코드 선택 popup 
     */
    lovPopup : function(parm, returnCallback) {
        var codeGroupName = SKIAF.i18n.messages['bcm.common.code'];

        if (!parm || parm.codeGroupId.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.id']);
            return;
        }
        
        // 코드그룹명이 없는 경우 
        if (parm.codeGroupName && parm.codeGroupName.length > 0) {
            codeGroupName = parm.codeGroupName;
        } 

        $a.popup({
            url : SKIAF.PATH.VIEW_CODESLOV_POPUP_SELECT,
            data : parm,
            movable : true,
            iframe : false,
            width : 800,
            height : 520,
            center : true,
            title : '[ ' + codeGroupName + ' ] LOV',
            callback : function(result) {
                returnCallback(result);
            }
        });
    },

    /**
     * 코드그룹 상세코드 가져오기
     */
    lovCodeList : function(codeGroupId, returnCallback) {
        if (!codeGroupId || codeGroupId.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.codegroup.valid.id']);
            return;
        }

        // 파라메터 생성
        var params = {};
        params.codeGroupId = codeGroupId;

        // ajax 통신
        $a.request(SKIAF.PATH.CODES, {
            method : 'GET',
            data : params,
            success : function(result) {
                if (result.data) {
                    var jsonData = [];

                    $.each(result.data, function(index, item) {
                        var json = {};
                        SKIAF.i18n.langSupportedCodes.forEach(function(
                                langCode, index) {
                            if (langCode == SKIAF.i18n.langCurrentCode) {
                                json['codeName'] = item["codeName"
                                        + (index + 1)];
                            }
                        });
                        json['codeId'] = item.codeId;
                        json['codeSortSeq'] = item.codeSortSeq;
                        json['codeDesc'] = item.codeDesc;

                        jsonData.push(json);
                    });

                    returnCallback(jsonData);
                }
            }
        });
    },
    
    /*
     * 공통 함수 : 마스킹 값 리턴 txt : 문자열 fmt : 문자 포맷(###-###) mask : 마스킹 문자 start : 마스킹 시작
     * 인덱스 end : 마스킹 종료 인덱스
     */
    getMask : function (txt, fmt, mask, start, end) {
        if (start > end) {
            return SKIAF.i18n.messages['bcm.util.mask.error.check-index'];
        }

        start = start == null ? 0 : start;
        end = end == null ? txt.length : end;

        var fmtArr = fmt.split('');
        var txtArr = txt.split('');

        var changeStr = '';
        var j = 0;
        for (var i = 0; i < fmtArr.length; i++) {
            if (fmtArr[i] == '#') {
                if (txtArr[j] != null) {

                    if (start <= j && j <= end) {
                        changeStr += mask;
                    } else {
                        changeStr += txtArr[j];
                    }
                }
            } else {
                changeStr += fmtArr[i];
            }
            j++;
        }
        return changeStr;
    },

    /*
     * 공통 함수 : ‘-‘ 없는 마스킹 값 리턴 len : 문자열 자릿수 txt : 문자열 mask : 마스킹 문자 start : 마스킹 시작
     * 인덱스 end : 마스킹 종료 인덱스
     */
    getNoHypenMask : function (len, txt, mask, start, end) {
        if (!this.isDigit(txt)) {
            return SKIAF.i18n.messages['bcm.util.not-hypen-mask.error.not-number'];
        }
        if (len != txt.length) {
            return SKIAF.i18n.messages['bcm.util.not-hypen-mask.error.not-length'];
        }
        if (start > end) {
            return SKIAF.i18n.messages['bcm.util.not-hypen-mask.error.check-index'];
        }

        start = start == null ? 0 : start;
        end = end == null || end > txt.length ? txt.length : end;

        var result = txt.substring(0, start);
        for (var i = 0; i < end - start; i++) {
            result += mask;
        }
        result += txt.substring(end, txt.lengh);
        return result;
    },

    /*
     * string 타입 날짜 포맷 변환
     * ex) dateFormat('2018-07-31 19:05:56.654', 'yyyy-MM-dd') -> '2018-07-31'
     */
    dateFormat : function (str, format){

        if(str == null || $.trim(str) == '') {
            return null;
        }

        var strArray = str.split(' ');
        var d;
        
        if(strArray[0] != null) {
            var dt = strArray[0];

            var year = dt.split('-')[0];
            var month = dt.split('-')[1];
            var day = dt.split('-')[2];

            if(strArray[1] == null) {
                d = new Date(year, month - 1, day);
            } else {
                var tm = strArray[1];

                var hours = tm.split(':')[0];
                var minutes = tm.split(':')[1];
                var seconds;

                if(tm.split(':')[2].indexOf('.') >= 0) {
                    seconds = tm.split(':')[2].substring(0, tm.split(':')[2].indexOf('.'));
                    var milliseconds = tm.split(':')[2].substring(tm.split(':')[2].indexOf('.') + 1);
                    d = new Date(year, month - 1, day, hours, minutes, seconds, milliseconds);
                    
                    
                } else {
                    seconds = tm.split(':')[2];
                    d = new Date(year, month - 1, day, hours, minutes, seconds);
                }
            }
        }

        return SKIAF.util.dateFormatReplace(d, format);
    },
    
    /*
     * Date 타입 날짜 포맷 변환
     * ex) dateFormatReplace(new Date(), 'yyyy-MM-dd') -> '2018-07-31'
     */
    dateFormatReplace : function (d, format){
        
        var weekName = [SKIAF.i18n.messages['bcm.common.time.day-of-week.sunday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.monday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.tuesday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.wednesday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.thursday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.friday'],
            SKIAF.i18n.messages['bcm.common.time.day-of-week.saturday']
        ];
        var h = d.getHours() % 12;
        return format.replace(/(yyyy|yy|MM|dd|E|hh|mm|SSS|ss|a\/p)/gi, function($1) {
            switch ($1) {
                case "yyyy": return d.getFullYear();
                case "yy": return SKIAF.util.padNumLeft((d.getFullYear() % 1000), 2, 0);
                case "MM": return SKIAF.util.padNumLeft((d.getMonth() + 1), 2, 0);
                case "dd": return SKIAF.util.padNumLeft(d.getDate(), 2, 0);
                case "E": return weekName[d.getDay()];
                case "HH": return SKIAF.util.padNumLeft(d.getHours(), 2, 0);
                case "hh": return SKIAF.util.padNumLeft((h ? h : 12), 2, 0);
                case "mm": return SKIAF.util.padNumLeft(d.getMinutes(), 2, 0);
                case "ss": return SKIAF.util.padNumLeft(d.getSeconds(), 2, 0);
                case "SSS": return SKIAF.util.padNumLeft(d.getMilliseconds(), 2, 0);
                case "a/p": return d.getHours() < 12 ? SKIAF.i18n.messages['bcm.common.time.am'] : SKIAF.i18n.messages['bcm.common.time.pm'];
                default: return $1;
            }
        });
    },


    /*
     * String 타입 왼쪽 문자열채우기
     * ex) SKIAF.util.padStrLeft('12', 5, '0') -> '00012'
     */
    padStrLeft : function(str, size, chr){
        if (typeof chr === 'undefined') {
            chr = '0';
        }
        return str.length >= size ? str : new Array(size - str.length + 1).join(chr) + str;
    },

    /*
     * Number 타입 왼쪽 문자열채우기
     * ex) padNumLeft(5, 2, 0) -> '05'
     */
    padNumLeft : function(num, size, padNum){
        if (typeof padNum === 'undefined') {
            padNum = 0;
        }
        var str = num + '';
        return str.length >= size ? str : new Array(size - str.length + 1).join(padNum) + str;
    },

    /*
     * Query String 값 구하기
     */
    getParameter : function (parameterName) {
        var result = null;
        var items = location.search.substr(1).split("&");
        for (var index = 0; index < items.length; index++) {
            var tmp = items[index].split("=");
            if (tmp[0].toUpperCase() === parameterName.toUpperCase()) {
                result = decodeURIComponent(tmp[1]);
                break;
            }
        }
        return result;
    },

    /*
     * Query String 생성
     */
    createParameter : function (obj) {
        var str = [];
        for (var o in obj) {
            if (!obj.hasOwnProperty(o)) {
                continue;
            }
            str.push(encodeURIComponent(o) + "=" + encodeURIComponent(obj[o]));
        }
        return str.join("&");
    },

    /*
     * Path Variable 구하기
     * 0 부터 시작
     *
     * ex) /view/codes/pro 인 Path에서
     * getPathVariable(2);
     * 를 호출하면 'pro' 를 리턴
     */
    getPathVariable : function(index) {
        var ctx = SKIAF.contextPath;
        var path = location.pathname;
        if (ctx.length > 0 && path.indexOf(ctx) >= 0) {
            path = path.substr(ctx.length);
        }
        path = path.substr(1);
        var pathVariable = path.split('/')[index];
        if (!pathVariable) {
            return pathVariable;
        }
        return decodeURIComponent(pathVariable);
    },
    
    /*
     * Path 구하기
     * ex) /view/codes/pro
     */
    getPath : function() {
        var ctx = SKIAF.contextPath;
        var path = location.pathname;
        if (ctx.length > 0 && path.indexOf(ctx) >= 0) {
            path = path.substr(ctx.length);
        }
        path = path.substr(1);
        return decodeURIComponent(path);
    },

    /*
     * 특수 문자 체크
     */
    isExistSpecial : function(str) {
        var special_pattern = /[`~!@#$%^&*|\\\'\";:\/?]/gi;
        return (special_pattern.test(str) == true) ? true : false;
    },
    
    /*
     * 쿠키 조회하면서 삭제하기
     */
    getDelCookies : function(name) {
        var cookie = Cookies.get(name);
        Cookies.remove(name);
        return cookie;
    },

    /*
     * Data 가 변경이 되었는지 확인한다.
     * 원본 키를 기준으로 비교가 안될 경우, 3번째 인자에 비교할 키가 있는 오브젝트를 넣고 비교
     */
    isDataDiff : function(orginalObject, newObject, diffObject) {
        var isDiff = false;
        if (typeof diffObject === 'undefined') {
            diffObject = orginalObject;
        }
        for (var key in diffObject) {
            var type = typeof orginalObject[key];

            // 비교한 타입 지정
            if (type !== 'undefined' && type !== 'boolean' && type !== 'string' && type !== 'number' && orginalObject[key] !== null) {
                continue;
            }

            // 둘다 값이 없으면 비교안함
            if (!orginalObject[key] && !newObject[key]) {
                continue;
            }

            // 값 비교
            if (orginalObject[key] !== newObject[key]) {
                isDiff = true;
                break;
            }
            
        }
        return isDiff;
    },
    
    /*
     * message 값에 arguments 값을 치환합니다.
     * 
     * ex) getMessageWithargs('{0}님  환영합니다.', 'skiaf');
     */
    getMessageWithArgs : function() {
        if (arguments.length <= 0) {
            return '';
        }
        var message = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            var argument = arguments[i];
            var regularEx = new RegExp('\\{' + (i - 1) + '\\}', 'g');
            message = message.replace(regularEx, argument);
            
        }
        return message;
    },
    
    /*
     * Url 인코딩 및 Base64 인코딩을 처리한다.
     */
    encodeUrlAndBase64 : function(value) {
        return btoa(encodeURIComponent(value));
    },
    
    /*
     * Base64 디코딩 및 Url 디코딩 처리한다.
     */
    decodeUrlAndBase64 : function(value) {
        return decodeURIComponent(atob(value));
    },
    
    /*
     * ID 체크 
     */
    checkId : function(value) {
        if (!value) {
            return false;
        }
        var pattern = new RegExp(/^[0-9a-zA-Z._-]*$/);
        var matchValue = value.match(pattern);
        return Array.isArray(matchValue);
    },
    
    /*
     * ID 체크 (-) 제외 
     */
    checkIdWithoutDash : function(value) {
        if (!value) {
            return false;
        }
        var pattern = new RegExp(/^[0-9a-zA-Z._]*$/);
        var matchValue = value.match(pattern);
        return Array.isArray(matchValue);
    },
    
    /*
     * URL 파람값 치환 
     */
    urlWithParams : function(url) {

        try {
            var urlParamLength = (url.match(/{/g) || []).length;
            if (urlParamLength == 0) {
                return url;
            }

            // 가변변수를 ...params로 받고 싶지만 IE에서 에러나서 아래처럼 처리
            var params = new Array();
            for (var i = 1, iLen = arguments.length; i < iLen; i++) {
                params.push(arguments[i]);
            }

            var paramsLength = params.length;
            if (paramsLength != urlParamLength) {
                return null;
            } else {
                var value;
                for(var j = 0, jLen = urlParamLength; j < jLen; j++) {
                    value = url.substring(url.indexOf('{'), url.indexOf('}') + 1);

                    url = url.replace(value, params[j]);
                }
                return url;
            } 
        } catch(err) {
            return null;
        }
    },
    
    /*
     * 파일명으로 image path return
     */
    getFileImageUrl : function(fileName) {

        var rtnFileImg = "ico_file_file.jpg";
        if (fileName) {
            var index = fileName.lastIndexOf('.');

            if (index > -1 ) {
                switch (fileName.substring(index).toLowerCase()) {
                    case ".pptx": rtnFileImg = "ico_file_ppt.jpg"; break;
                    case ".pdf" : rtnFileImg = "ico_file_acro.jpg";break;
                    case ".doc" : rtnFileImg = "ico_file_word.jpg";break;
                    case ".xls" :
                    case ".xlsx": rtnFileImg = "ico_file_exel.jpg";break;
                    default     : rtnFileImg = "ico_file_file.jpg";
                } 
            }            
        }
    
        // 기타 파일
        return "/static/skiaf/images/" + rtnFileImg;
    },
    
    /*
     * Enum값으로 Select Option 설정
     */
    setLOVSelectDataOptionWithEnum : function (enumObject, preMessageKey, order) {
        var result = new Array;
        if(order != null){
            order.forEach(function(value){
                var option = {};
                option['value'] = value;
                option['text'] = SKIAF.i18n.messages[preMessageKey + value.toLowerCase()];
                result.push(option);
            });
        }else{
            $.each(enumObject, function(key){
                var option = {};
                option['value'] = enumObject[key];
                option['text'] = SKIAF.i18n.messages[preMessageKey + enumObject[key].toLowerCase()];
                result.push(option);
            });    
        }
        return result;
    }
};


window.SKIAF.passwordUtil = {
        
        /*
         * 패스워드 조합 정책
         * 영문, 숫자, 특수문자 세 항목의 문자 종류 중 2종류 이상을 조합하여 최소 10자리 이상
         * 또는 3종류 이상을 조합하여 최소 8자리 이상의 길이로 구성되었는지 체크.
         */
        unionRule : function(data) {
            
            let NumberCH = false;
            let CharCH = false;
            let SpecialCharCH = false;
                
            let DataLenght = data.length;
                
            let chNumber ="0123456789";
            let chSpecialChar = "`~!@#$%^&*()_+|-=\\[]{};:'\",.<>/?"; 

            let temp = '';
            for(let i = 0, iLen = data.length; i < iLen; i++) {
                temp = data.charAt(i);
                    
                for(let j = 0, jLen = chNumber.length; j < jLen; j++) {
                    if(chNumber.charAt(j) == temp) {
                        NumberCH = true;
                    }
                }

                if(((temp >='a') && (temp <='z')) || ((temp >='A') && (temp <='Z'))) {
                    CharCH = true;
                }
                    
                for(let k = 0, kLen = chSpecialChar.length; k < kLen; k++) {
                    if(chSpecialChar.charAt(k) == temp) {
                        SpecialCharCH = true;
                    }
                }
            }

            if((NumberCH == true) && (CharCH == true) && (SpecialCharCH == false) && (DataLenght >= 10)) {
                return true;
            } else if((NumberCH == true) && (CharCH == true) && (SpecialCharCH == true) && (DataLenght >= 8)) {
                return true;
            }
            return false;
        },
        
        /*
         * 개인정보 및 아이디와 비슷한 패스워드 사용 금지
         */
        personalInformation : function(userData){
 
            let ID = userData.loginId;
            let UserName = userData.userName;
            let Tel = userData.telNo;
            let Mobile = userData.mobileNo;
            let Email = userData.email ? userData.email.substring(0, userData.email.indexOf('@')) : '';

            let Myinfo = [ID, UserName, Tel, Mobile, Email];

            let temp = '';
            let info = '';
            for(let i = 0, iLen = Myinfo.length; i < iLen; i++) {
                if(Myinfo[i] && Myinfo[i].length > 0) {
                    temp = userData.password.toUpperCase();
                    info = Myinfo[i].toUpperCase();
                    if(temp.indexOf(info) != -1) {
                        return false;
                    }
                }
            }        
            return true;
        },
        
        /*
         * 특정 패턴을 갖는 패스워드 사용 금지
         */
        oneStringRepetition : function(data){
            
            for(let i=0; i < (data.length-2); i++){
                let temp1 = data.substring(i, i+1);
                let temp2 = data.substring(i+1, i+2);
                let temp3 = data.substring(i+2, i+3);
                    
                if(temp1 == temp2){
                    if(temp2 == temp3){
                        return false;
                    }
                }
            }       
            return true;
        },
        
        /*
         * 특정 패턴을 갖는 패스워드 사용 금지
         * 두 자 이상의 동일문자 연속성(두 번 이상 중복될 경우)
         */
        twoStringRepetition : function(data){

            var temp = '';
            var temps = '';
            
            for(let i=0; i < (data.length-2); i++) {
                var count = 0;
                temp = data.substring(i, i+2);
                
                for(let j=0; j < (data.length-1); j++) {
                    temps = data.substring(j, j+2);
                        
                    if(temps == temp) {
                        count++;
                        if(count >= 2) {
                            return false;
                        }
                    }
                }
            }       
            return true;
        },
        
        /*
         * 특정 패턴을 갖는 패스워드 사용 금지
         * 키보드 연속성 체크(특수문자 제외)
         */
        keyboardContinuity : function(data){
            var keyboardCH = [
                // 숫자 연속성 체크
                "01234567890", "09876543210",
                // 문자 연속성 체크
                "QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM", "POIUYTREWQ", "LKJHGFDSA", "MNBVCXZ", "1QAZ", "2WSX", "3EDC",
                "4RFV", "5TGB", "6YHN", "7UJM", "0OKM", "9IJN", "8UHB", "7YGV", "6TFC", "5RDX", "4ESZ", "ZAQ1", "XSW2",
                "CDE3", "VFR4", "BGT5", "NHY6", "MJU7", "MKO0", "NJI9", "BHU8", "VGY7", "CFT6", "XDR5", "ZSW4" ];
            
            var dataUc = data.toUpperCase();

            for (var i = 0; i < keyboardCH.length; i++) {
                
                for (var j = 0; j < keyboardCH[i].length - 2 ; j++) {
                    var count = 0;

                    var p = keyboardCH[i].substring(j, j + 3).toUpperCase();
                    var m = dataUc.match(p);

                    if(m != null) {
                        for(var k = 0; m[k]; j = m.length) {
                            count++;
                            if (count >= 1) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
};

window.SKIAF.loginUtil = {
        
    /*
     * 로그인 성공시 공통 작업
     */
    loginSuccess : function(returnUrlAfterLogin){
        location.replace(SKIAF.contextPath + returnUrlAfterLogin);
        Cookies.remove(SKIAF.CONSTATNT.TEMP_LANGUAGE_CODE);
    }
};

window.SKIAF.webeditor = {
        
    /*
     * webeditor 초기화
     */
    setWebeditor : function(id, param) {
        $(id).webeditor({
            height: 180,
            toolbar: [
                //[groupName, [list of button]]
                ['style', ['style']],
                ['font', ['bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['view',['undo','redo']],
                ['height', ['height']]
            ]
        });
    },

    /*
     * webeditor code return
     */
    setData : function(id, code) {
        $(id).webeditor('code', code);
    },

    /*
     * webeditor code setting
     */
    getData : function(id) {
        return $(id).webeditor('code');
    },

    /**
     * 내용 html/text 변환
     */
    changeCodeView : function(id, type) {

        var isEditType = $(id).webeditor('codeview.isActivated');

        // code view mode : false(HTML), true(TEXT)
        if (isEditType == false && type == SKIAF.ENUM.ARTICLE_TYPE.TEXT) {
            $(id).webeditor('codeview.toggle');
        } else if (isEditType == true && type == SKIAF.ENUM.ARTICLE_TYPE.HTML) {
            $(id).webeditor('codeview.toggle');
        }
    }

};

/**
 * excel Util [엑셀 파라메타 설명] 
 * 
 * 1. URL 호출 Export 
 *  - url (필수) 예) url : '/api/usergroups' 
 *  - method (필수) 예) method : 'GET' 
 *  - resultListName (필수) 호출시 result list 이름 
 *  - data (옵션) URL 호출을 위한 파라메타 예) data : {page: 0, size: 10, isUnusedInclude: false} 
 *  - fileName (옵션) excel 파일명이 없을 경우 default로 SKIAF_excel_yyyyMMddHHmmss 로 정해짐 
 *  - columnNames (필수) 
 *    => 배열 사용시 결과 list와 순차적으로 매핑 됨 예) ['등록자', '수정자', '등록일'] 
 *    => JSON 포맷 사용시 그리드 columns 사용과 동일, title/key 는 필수 사항임 예) {{title : '프로그램 ID', key : 'programId'},{title : '프로그램명', key : 'programName'}}
 * 2. 내부 화면 그리드 ID만으로 후출 - gridId (필수) 예) gridId : "#programGrid"
 * 
 * 3. 내부 화면 그리드 Export 
 *  - gridId (필수) 예) gridId : "#programGrid" 
 *  - fileName (옵션) excel 파일명이 없을 경우 default로 SKIAF_excel_yyyyMMddHHmmss 로 정해짐 
 *  - Alopex Grid excel 옵션 : exportHidden, useGridColumnWidth, exportNewline, filtered, selected 예) exportHidden : true 
 *  
 *  // 초기 오브젝트                          
 *  var excelParm = {                   
 *          url                 : null, 
 *          method              : null, 
 *          data                : null, 
 *          gridId              : null, 
 *          merge               : null, 
 *          fileName            : null, 
 *          columnNames         : null, 
 *          exportHidden        : null, 
 *          useGridColumnWidth  : null, 
 *          exportNewline       : null, 
 *          filtered            : null, 
 *          selected            : null  
 *  }                                   
 *  
 */
window.SKIAF.excelUtil = {
    /**
     * execl export URL
     */
    excelExportUrl : function(parm) {
        var excelParm;
        var gridCreateName     = "SKIAFexcelExportUrlGrid";

        parm.gridId = "#" + gridCreateName;

        // 엑셀 export 할 임시 grid 생성
        if ($(parm.gridId).length == 0) {
            $("div").first().after("<div id='" + gridCreateName + "' style='display: none;'></div>");
        }
      
        SKIAF.popup.confirm("엑셀 download 하시겠습니까 ?", function callback(){
            if (excelParm = SKIAF.excelUtil.setExcelParamater(parm, "URL")) {
                SKIAF.excelUtil.excelExportUrlCall(excelParm);
            }
        });
    },

    /**
     * URL 호출
     */
    excelExportUrlCall : function(parm) {
        // ajax 통신
        $a.request(parm.url, {
            method : parm.method,
            data : parm.data,
            success : function(result) {
                
                if (!result.data) {
                    return;
                }
                
                parm.columnNames = SKIAF.excelUtil.excelTitleParser(parm, result.data);
                $(parm.gridId).alopexGrid({columnMapping : parm.columnNames, data : result.data});
                
                SKIAF.excelUtil.execlExportCoreUrl(parm);
            }
        });
    },

    /**
     * execl export Grid
     */
    excelExportGrid : function(parm){
        var excelParm = {};

        SKIAF.popup.confirm("엑셀 download 하시겠습니까 ?", function callback(){
            if ((excelParm = SKIAF.excelUtil.setExcelParamater(parm, "GRID"))) {
                SKIAF.excelUtil.execlExportCore(excelParm);
            }
        });
    },

    /**
     * execl export URL
     */
    excelTitleParser : function(parm, data){
        var idx = 0;
        var columnTitle = [];

        // 그리드 컬럼이 title='', key='' 형식인경우
        if (typeof parm.columnNames[0] == "object") { return  parm.columnNames; }
        
        $.each(data[0], function(key, value){
            if (typeof value != "object") {    
                var json = {};
                json['title'] = (parm.columnNames[idx] ? parm.columnNames[idx++] : key);
                json['key'] = key;
                columnTitle.push(json); 
            }
        });
        
        return columnTitle;
    },
    
    /**
     * excel 파라메타 check
     */
    setExcelParamater : function(parm, callType) {
        var excelParm = {};
        
        // 그리드 ID 로 호출
        if (typeof parm == "string") {    
            excelParm.gridId = parm;    
        } else {    
            excelParm = parm;
        }

        // 그리드 id check
        if (!excelParm.gridId) { 
            SKIAF.popup.alert("[gridId]는 필수 파라메타 입니다.");
            //SKIAF.popup.alert(SKIAF.i18n.messages['bcm.code.code.valid.id']);
            return false; 
        }

        // 그리드 id 없으면 오류
        if (callType == "URL") { 
            // url check
            if (!excelParm.url) { 
                SKIAF.popup.alert("[url]응 필수 파라메타 입니다.");
                return false; 
            }
            // method check
            if (!excelParm.method) { 
                SKIAF.popup.alert("[method]는 필수 파라메타 입니다.\n[파라메타]\n" + JSON.stringify(parm));
                return false; 
            }
            // data check
            if (excelParm.data) { 
                excelParm.data = parm.data;
            }

            excelParm.url = parm.url;
            excelParm.method = parm.method;
        }
        
        // file name 이 없을 경우 만들어 준다
        if (!excelParm.fileName) {
            excelParm.fileName = "SKIAF_excel_" + SKIAF.util.dateFormatReplace(new Date(), 'yyyyMMddHHmmss');
        }

        return excelParm;
    },

    /**
     * rgid excel export
     */
    execlExportCore : function(parm) {
        var worker = new ExcelWorker({
            excelFileName : parm.fileName,
            palette : [{
                className : 'B_YELLOW',
                backgroundColor: '255,255,0'
            },{
                className : 'F_RED',
                color: '#FF0000'
            }],
            sheetList: [{
                sheetName: 'sheet1',
                $grid: $(parm.gridId)
            }]
        });
        worker.export({
            merge: (parm.merge ? parm.merge : false),
            exportHidden: (parm.exportHidden ? parm.exportHidden : false),
            useGridColumnWidth: (parm.useGridColumnWidth ? parm.useGridColumnWidth : false),
            exportNewline: (parm.exportNewline ? parm.exportNewline : false),
            filtered: (parm.filtered ? parm.filtered : false),
            selected: (parm.selected ? parm.selected : false), 
            callback : {
                preCallback: function(gridList){
                    for(var i= 0; i < gridList.length; i++){
                        if(i == 0 || i == gridList.length -1) gridList[i].alopexGrid('showProgress', {progressText: 'excel export ...'});
                    }
                },
                postCallback: function(gridList){
                    for(var i= 0; i < gridList.length; i++){
                        gridList[i].alopexGrid('hideProgress');
                    }
                }
            }
        });
    },

    /**
     * URL Grid excel export
     */
    execlExportCoreUrl : function(parm) {
        var worker = new ExcelWorker({
            excelFileName : parm.fileName,
            palette : [{
                className : 'B_YELLOW',
                backgroundColor: '255,255,0'
            },{
                className : 'F_RED',
                color: '#FF0000'
            }],
            sheetList: [{
                sheetName: 'sheet1',
                $grid: $(parm.gridId)
            }]
        });
        worker.export();
    }
};
