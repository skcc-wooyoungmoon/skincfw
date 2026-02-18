/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.06 - in01876
 * description : 사용자 등록 팝업
 */
"use strict";
var UserCreateModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Alopex setup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    //사용자 초기 오브젝트
    var userData = {};

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    |  Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.init = function(id, param) {

        //초기데이터 조회
        UserCreateModule.initData();

        //이벤트 등록
        UserCreateModule.addEvent();

    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    this.addEvent = function() {

        //로그인 ID가 변경되면 중복체크여부 false
        $('#loginId').on('keyup', function(e) {
            let userData = $('#userCreateForm').getData();
            userData.isDuplicated = false;
            $('#userCreateForm').setData(userData);
            $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-dupl-check']).addClass('Color-danger');
            $('#loginIdMessage').show();
        });

        //취소 버튼
        $('#cancleBtn').on('click', function(e) {
            $a.close();
        });

        //저장 버튼
        $('#saveBtn').on('click', function(e) {

            var createData = $('#userCreateForm').getData();
            if (UserCreateModule.validationCheck(createData)) {
                createData.email = createData.email1 + "@" + createData.email2;
                createData.telNo = createData.telNo1 + "-" + createData.telNo2 + '-' + createData.telNo3;
                createData.mobileNo = createData.mobileNo1 + "-" + createData.mobileNo2 + '-' + createData.mobileNo3;

                $a.request(SKIAF.PATH.USERS, {
                    method : 'POST',
                    data : createData,
                    success : function(res) {
                        $a.close(res);
                    }
                });
            }
        });

        //ssoYN 변경
        $('input[type=radio][name=sso]').on('change', function(e) {
            if ($('#userCreateForm').getData().ssoYn === 'true') {
                $('#loginIdTr th').html(SKIAF.i18n.messages['bcm.user.sso-login-id'] + '<span class="Color-danger">* </span>');
            } else {
                $('#loginIdTr th').html(SKIAF.i18n.messages['bcm.user.login-id'] + '<span class="Color-danger">* </span>');
            }
        });

        //loginId 중복체크
        $('#idDuplicateBtn').on('click', function(e) {
            let userData = $('#userCreateForm').getData();

            if (userData.loginId == null || userData.loginId.length < 6) {
                $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-length']).addClass('Color-danger');
                $('#loginIdMessage').show();
                $('#loginId').find('input').focus();
                return;
            } else if (userData.isDuplicated === 'true') {
                $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-confirm']).removeClass('Color-danger').addClass('Color-success');
                $('#loginIdMessage').show();
                return;
            }

            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.USER_DUPLICATE, userData.loginId), {
                method : 'GET',
                success : function(res) {
                    if (res.data == true) {
                        $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-dupl']).addClass('Color-danger');
                        $('#loginIdMessage').show();
                        $('#loginId').find('input').focus();
                    } else {
                        $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-confirm']).removeClass('Color-danger').addClass('Color-success');
                        $('#loginIdMessage').show();

                        userData.isDuplicated = true;
                        $('#userCreateForm').setData(userData);
                    }
                }
            });
        });

        // 이메일 선택 이벤트
        $('#email1').parent('td').find('select').on('change', function() {
            if ($.trim($(this).val()) != '') {
                $('#email2').val($(this).val());
            }
        });
    };

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     *  초기데이터 조회
     */
    this.initData = function() {
        //회사 목록 조회
        var companyCode = SKIAF.CONSTATNT.CODE_GROUP_COMPANY_ID;
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.CODES_DETAIL_LANG, companyCode), {
            method : 'GET',
            async : false,
            success : function(res) {
                let companyOptions = [{TEXT: SKIAF.i18n.messages['bcm.common.select-option'] ,VALUE:"NONE"}];
                res.data.forEach(function(item) {
                    companyOptions.push({TEXT:item.codeName, VALUE:item.codeId})
                });
                let companyObj = {companyOptions: companyOptions, companyCode : 'NONE'};
                $('#companyTr').setData(companyObj);
            }
        });
    };

    this.validationCheck = function(userData) {
        //1. id 중복체크
        if ($.trim(userData.loginId) == '') {
            $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-dupl-check']).addClass('Color-danger');
            $('#loginIdMessage').show();
            $('#loginId').find('input').focus();
            return false;
        } else {
            $('#loginIdMessage').hide();
        }

        if (userData.isDuplicated !== 'true') {
            $('#loginIdMessage').text(SKIAF.i18n.messages['bcm.user.valid.login-id-dupl-check']).addClass('Color-danger');
            $('#loginIdMessage').show();
            $('#loginId').find('input').focus();
            return false;
        } else {
            $('#loginIdMessage').hide();
        }

        //2. 사용자 이름
        if (userData.userName == null || userData.userName.length < 2) {
            $('#userNameMessage').text(SKIAF.i18n.messages['bcm.user.valid.user-name-length']).addClass('Color-danger');
            $('#userNameMessage').show();
            $('#userName').find('input').focus();
            return false;
        } else {
            $('#userNameMessage').hide();
        }

        //3. 이메일
        if (userData.email1 != null && userData.email2 != null) {
            if (!SKIAF.util.isEmail(userData.email1 + "@" + userData.email2)) {
                $('#email1Message').text(SKIAF.i18n.messages['bcm.user.valid.email-check']).addClass('Color-danger');
                $('#email1Message').show();
                $('#email1').find('input').focus();
                return false;
            } else {
                $('#email1Message').hide();
            }
        } else {
            $('#email1Message').text(SKIAF.i18n.messages['bcm.user.valid.email']).addClass('Color-danger');
            $('#email1Message').show();
            $('#email1').find('input').focus();
            return false;
        }

        //4. 회사구분
        if (userData.companyCode == 'NONE') {
            $('#companyMessage').text(SKIAF.i18n.messages['bcm.user.valid.company-select']).addClass('Color-danger');
            $('#companyMessage').show();
            $('#companyTr').find('select').focus();
            return false;
        } else {
            $('#companyMessage').hide();
        }

        return true;
    };

});