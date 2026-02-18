/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.thymeleaf.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 공통 유틸리티
 *
 * History
 * - 2018. 7. 17. | in01866 | 최초작성.
 * </pre>
 */
@Slf4j
public class Util {

    private static final int LENGTH_OF_CTZ_NUM             = 13;
    private static final int FRONT_NUMBER_OF_CTZ_NUM       = 6;
    private static final int BACK_NUMBER_OF_CTZ_NUM        = 7;
    private static final int LAST_DIGIT_OF_CTZ_NUM         = 13;
    private static final int CTZ_NUM_VALIDATION_NUM_ELEVEN = 11;
    private static final int CTZ_NUM_VALIDATION_NUM_TEN    = 10;

    private static final int LENGTH_OF_CORP_NUM          = 13;
    private static final int CORP_NUM_VALIDATION_NUM_ONE = 1;
    private static final int CORP_NUM_VALIDATION_NUM_TWO = 2;
    private static final int CORP_NUM_VALIDATION_NUM_TEN = 10;

    private Util() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * <pre>
     * 입력값을 특정 형식에 맞게 변환한다.
     * ex) makeFormat("1234512345", "#####-#####") => "12345-12345"
     * </pre>
     */
    public static String makeFormat(String value, String format) {
        StringBuilder builder = new StringBuilder();
        int valueIdx = 0;

        // Format loop
        for (int formatIdx = 0, max = format.length(); formatIdx < max; formatIdx++) {

            // '#' 이면 value값 적용, 아니면 format 값 적용
            if (format.charAt(formatIdx) == '#') {
                builder.append(value.charAt(valueIdx));
                valueIdx++;
            } else {
                builder.append(format.charAt(formatIdx));
            }
        }

        return builder.toString();
    }

    /**
     * <pre>
     * 문자열의 특정 문자를 제거한다. ex) removeChar("abcdefg","e") => "abcdfg"
     * </pre>
     */
    public static String removeChar(String value, String target) {
        return value.replaceAll(target, "");
    }

    /**
     * <pre>
     * 주민번호 유효성 체크
     * </pre>
     */
    public static Boolean isValidCtzNum(String value) {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMdd");

        try {
            // 앞자리 유효성 체크
            LocalDate.parse(StringUtils.substring(value, 0, FRONT_NUMBER_OF_CTZ_NUM), fmt);

            // '-' 체크
            if (value.charAt(FRONT_NUMBER_OF_CTZ_NUM) != '-') {
                return false;
            }

            // 뒷자리 유효성 체크
            if (!Pattern.matches("^\\d{7}$", StringUtils.substring(value, BACK_NUMBER_OF_CTZ_NUM))) {
                return false;
            }

            int lastNum = NumberUtils.toInt(StringUtils.substring(value, LAST_DIGIT_OF_CTZ_NUM, LAST_DIGIT_OF_CTZ_NUM + 1));
            String chkNum = "234567-892345";
            int chk = 0;

            for (int i = 0; i < LENGTH_OF_CTZ_NUM; i++) {
                if (value.charAt(i) != '-') {
                    chk += Character.getNumericValue(chkNum.charAt(i)) * Character.getNumericValue(value.charAt(i));
                }
            }

            chk = (CTZ_NUM_VALIDATION_NUM_ELEVEN - (chk % CTZ_NUM_VALIDATION_NUM_ELEVEN)) % CTZ_NUM_VALIDATION_NUM_TEN;

            return (chk == lastNum);
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * <pre>
     * 법인번호 번호 체크
     * </pre>
     */
    public static Boolean isValidCorpNum(String value) {
        value = value.replaceAll("-", "");

        if (value.length() != LENGTH_OF_CORP_NUM) {
            return false;
        }

        String[] valueSplit = value.split("");
        int[] validationNo = { CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO,
                                CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO,
                                CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO,
                                CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO,
                                CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO,
                                CORP_NUM_VALIDATION_NUM_ONE, CORP_NUM_VALIDATION_NUM_TWO };
        int iSumNo = 0;
        int iCheckDigit = 0;

        for (int i = 0, iMax = LENGTH_OF_CORP_NUM - 1; i < iMax; i++) {
            iSumNo += NumberUtils.toInt(valueSplit[i]) * validationNo[i];
        }

        iCheckDigit = CORP_NUM_VALIDATION_NUM_TEN - (iSumNo % CORP_NUM_VALIDATION_NUM_TEN);

        iCheckDigit = iCheckDigit % CORP_NUM_VALIDATION_NUM_TEN;

        return (iCheckDigit == NumberUtils.toInt(valueSplit[LENGTH_OF_CORP_NUM - 1]));
    }

    /**
     * <pre>
     * 리스트/컬렉션 null 체크 (for each 사용시 체크용)
     * </pre>
     *
     * @param item List/Collection 객체
     */
    @SuppressWarnings("unchecked")
    public static <T extends Iterable<?>> T nullToEmpty(T item) {
        if (item == null) {
            return (T) Collections.emptyList();
        }
        return item;
    }

    /**
     * <pre>
     * Base64 디코딩 및 Url 디코딩 처리한다.
     * </pre>
     */
    public static String decodeUrlAndBase64(String value) {

        String result = new String(Base64.getDecoder().decode(value));
        try {
            result = URLDecoder.decode(result.replace("+", "%2B"),  StandardCharsets.UTF_8.name()).replace("%2B", "+");
        } catch (UnsupportedEncodingException e) {
            log.error("url decode error", e);
        }

        return result;
    }

    /**
     * <pre>
     * 'User-Agent' 요청 헤더정보 값으로 IE 브라우저인지 확인
     * </pre>
     */
    public static boolean isIEBrowser(String userAgent) {

        if (StringUtils.isEmpty(userAgent)) {
            return false;
        }
        return (userAgent.indexOf("MSIE") >= 0 || userAgent.indexOf("Trident") >= 0);
    }

    /**
     * <pre>
     * 마지막 문자가 Path Separator이면 제거
     * </pre>
     */
    public static String removeLastSeperator(String path) {

        if (StringUtils.isEmpty(path)) {
            return path;
        }
        int lastPathIndex = path.lastIndexOf(File.separator);
        if (path.length() - 1 != lastPathIndex) {
            return path;
        }

        return path.substring(0, lastPathIndex);
    }
}
