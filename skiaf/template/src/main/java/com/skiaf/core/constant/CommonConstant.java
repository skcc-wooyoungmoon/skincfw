/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.constant;

public class CommonConstant {
    
    private CommonConstant() {
        throw new IllegalStateException("CommonConstant Class");
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 타임 패턴
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /** 기본 타임 패턴 */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 기본 타임 패턴 milli sec */
    //public static final String TIME_PATTERN_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 공통 코드 그룹
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /** 회사 코드 그룹 아이디 */
    public static final String CODE_GROUP_COMPANY_ID = "CD0001";

    // Article Attachfiles type
    public static final String ATTACHFILES_TYPE_SINGLE = "SINGLE";

    /** 게시글 관리자 권한 ID */
    public static final String BOARD_ADMIN_ROLE = "ABBCM00001";
    
    /** 최상위 메뉴 ID */
    public static final String ROOT_MENU_ID = "ROOT_MENU";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 로그인 RESULT 코드
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String LOGIN_REQUIRED_PASSWORD_CHANGE = "0101"; // 패스워드 변경 필요

    public static final String LOGIN_SSO_SUCCESS = "0200"; // SSO 성공
    public static final String LOGIN_SSO_ERROR = "0201"; // SSO 에러
    public static final String LOGIN_SSO_FIRST_ACCESS = "0202"; // SSO 최초 로그인
    public static final String LOGIN_SSO_EXPIRE_TIMEOUT = "0203"; // SSO 인증토큰 만료
    public static final String LOGIN_SSO_ACCESS_DENIED = "0204"; // SSO 접근 거부
    public static final String LOGIN_SSO_UNAVAILABLE = "0205"; // SSO 장애
    public static final String LOGIN_SSO_NOT_LOGIN = "0206"; // SSO 미로그인
    public static final String LOGIN_SSO_NOT_REGISTRAION = "0207"; // SSO 사용자가 등록되어 있지 않음 (사용자 정보 없음)
    public static final String LOGIN_SSO_NOT_USE = "0208"; // SSO 사용자의 userYn = N

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | 로그인 VALUE
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String RETURN_URL_AFTER_LOGIN = "returnUrlAfterLogin";
    public static final String TEMP_LANGUAGE_CODE = "SKIAF_BCM_TEMP_LANGUAGE_CODE";

}
