/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.service.CodeService;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.repository.UserRepository;
import com.skiaf.bcm.user.domain.service.dto.UserGroupListDTO;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.bcm.user.domain.service.dto.UserSearchDTO;
import com.skiaf.bcm.user.domain.service.dto.UserUserGroupMapSaveDTO;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.vo.PageDTO;

import cool.graph.cuid.Cuid;

/**
 * <pre>
 * BCM 사용자 관리 ServiceImpl
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Service
public class UserServiceImpl implements UserService {

    private static final int MIN_DIGITS_MIXING_TWO_TYPES = 10; // 2종류 이상을 조합한 최소 자릿수
    private static final int MIN_DIGITS_MIXING_THREE_TYPES = 8;// 3종류 이상을 조합한 최소 자릿수

    private static final String CHECK_NUMBER = "0123456789";
    private static final String CHECK_SPECIAL_CHAR = "`~!@#$%^&*()_+|-=\\[]{};:'\",.<>/?";

    private static final String BCM_USER_ERROR_EMPTY = "bcm.user.error.empty";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupUserMapService userGroupUserMapService;
    
    @Autowired
    UserGroupService userGroupService;

    @Autowired
    RoleMapService roleMapService;

    @Autowired
    CodeService codeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    /** 패스워드 suffix */
    @Value("${bcm.password.suffix}")
    private String pwSuffix;

    @Override
    public PageDTO<User> findQueryByKeyword(UserSearchDTO search, Pageable pageable) {

        PageDTO<User> result = userRepository.findQueryByKeyword(search, pageable);
        result.setPage(pageable);
        return result;
    }

    @Override
    public void incrementLoginFailCountByLoginId(String loginId) {
        userRepository.incrementLoginFailCountByLoginId(loginId);
    }

    @Override
    public void initLoginFailCountByLoginId(String loginId) {
        userRepository.initLoginFailCountByLoginId(loginId);
    }

    @Override
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Override
    public User findByLoginIdAndSsoYnTrue(String loginId) {
        return userRepository.findByLoginIdAndSsoYnTrue(loginId);
    }

    @Override
    public User findByLoginIdAndSsoYnFalse(String loginId) {
        return userRepository.findByLoginIdAndSsoYnFalse(loginId);
    }

    @Override
    public UserInfoDTO getUserInfoByUser(User user) {

        if (user == null) {
            return null;
        }

        UserInfoDTO result = new UserInfoDTO();
        result.setUser(user);
        List<UserGroup> userGroupList = null;
        List<UserGroupListDTO> userGroupListDTO = new ArrayList<>();

        userGroupList = userGroupUserMapService.findByMapIdUserId(user.getUserId());
        userGroupList.forEach((UserGroup rel) -> userGroupListDTO.add(modelMapper.map(rel, UserGroupListDTO.class)));
        result.setUserGroupList(userGroupListDTO);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        String betweenDate = currentDate.toString(fmt);
        result.setRoleList(roleMapService.findActivationRoleMapByUserId(user.getUserId(), betweenDate));

        return result;
    }

    @Override
    public User update(String userId, User user) {
        
        if(user.isGwIfYn()) {
            throw BizException.withSystemMessage("user gwIfYn == Y").build();
        }

        // 1. userEntity select
        User updateUser = userRepository.findByUserId(userId);

        // 2. null check
        if (user == null || updateUser == null) {
            throw NotFoundException.withUserMessageKey(BCM_USER_ERROR_EMPTY)
                    .withSystemMessage("user == null  || updateUser == null").build();
        }

        // 3. update data set.
        updateUser.setUserName(user.getUserName());
        updateUser.setEmail(user.getEmail());
        updateUser.setCompanyCode(user.getCompanyCode());
        updateUser.setDepartmentName(user.getDepartmentName());
        updateUser.setPositionName(user.getPositionName());
        updateUser.setRegularYn(user.isRegularYn());
        updateUser.setTelNo(user.getTelNo());
        updateUser.setMobileNo(user.getMobileNo());
        updateUser.setUseYn(user.isUseYn());

        // 4. managed Entity save
        updateUser = userRepository.save(updateUser);

        // 5. 성공 결과 return
        return updateUser;
    }

    @Override
    public User create(User user) {
        // 1. CUID 생성
        user.setUserId(Cuid.createCuid());

        // 2. 비번암호화
        if (!user.isSsoYn()) {
            user.setPassword(passwordEncoder.encode(user.getLoginId() + pwSuffix)); // 암호화된 비밀번호
        } else {
            user.setPassword(passwordEncoder.encode(user.getLoginId())); // Spring security 로그인 사용을 위해, 비밀번호를 설정
        }
        // 3. default 값 셋팅
        user.setGwIfYn(false); // 그룹웨어 연동여부
        user.setFirstLoginYn(false); // 비밀번호 변경여부
        user.setLoginFailCount(0); // 로그인 실패횟수
        user.setUseYn(true); // 사용여부
        user.setLastPwdChgDtm(new Date()); // 비밀번호 변경일

        // 4. one more duplicate check
        if (this.duplicateCheck(user.getLoginId())) {
            throw ValidationException.withUserMessageKey("bcm.common.DUPLICATE")
                    .withSystemMessage("this.duplicateCheck(user.getLoginId())").build();
        }
        // 5. save
        User createUser = userRepository.save(user);

        // 6. result return
        return createUser;
    }

    @Override
    public User findOne(String userId) {

        User user = userRepository.findOne(userId);
        if (user == null) {
            throw NotFoundException.withUserMessage(BCM_USER_ERROR_EMPTY)
                    .withSystemMessage(userId + "(userId) is Null").build();
        }

        if (user.getCompanyCode() != null) {
            Code code = codeService.findOne(CommonConstant.CODE_GROUP_COMPANY_ID, user.getCompanyCode());
            if (code != null) {
                user.setCompanyName(code.getCodeName1());
            }
        }

        return user;
    }

    @Override
    public Boolean duplicateCheck(String loginId) {
        Boolean isDuplicate = false;
        User user = userRepository.findByLoginId(loginId);
        if (user != null) {
            isDuplicate = true;
        }
        return isDuplicate;
    }

    @Override
    public User passWordChange(String userId, String prePassword, String newPassword, Boolean firstLoginYn) {

        User user = userRepository.findOne(userId);

        // 0. 사용자 체크
        if (user == null) {
            throw NotFoundException.withUserMessageKey(BCM_USER_ERROR_EMPTY, userId).withSystemMessage("user == null")
                    .build();
        }

        // 1. 비밀번호 변경 가능한 사용자 인지 체크
        if (user.isSsoYn()) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.sso").build();
        }
        
        if(user.isGwIfYn()) {
            throw BizException.withSystemMessage("user gwIfYn == Y").build();
        }

        // 2. 이전패스워드가 맞는지 체크
        if (!passwordEncoder.matches(prePassword, user.getPassword())) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.now-password.wrong").build();
        }

        // 3. 이전 PW 와 변경할 PW가 같은지 여부 판단
        if (prePassword.equals(newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.pre-new.equal").build();
        }

        // 4. 패스워드 조합 정책 체크
        if (!unionRule(newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.union-rule").build();
        }

        // 5. 3자가 추측하기 쉬운 개인정보 포함 체크
        if (!personalInformation(user, newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.personal-infomation").build();
        }

        // 6. 동일한 글자 3번연속 입력 체크
        if (!oneStringRepetition(newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.one-string-repetition").build();
        }

        // 7. 두자이상의 동일문자 두번연속 입력 체크
        if (!twoStringRepetition(newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.two-string-repetition").build();
        }

        // 8. 키보드 연속성 체크
        if (!keyboardContinuity(newPassword)) {
            throw ValidationException.withUserMessageKey("bcm.common.password-change.valid.keyboard-continuity").build();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLoginYn(firstLoginYn);
        user.setLastPwdChgDtm(new Date());

        return userRepository.save(user);
    }

    @Override
    public User passWordReset(String userId) {

        User user = userRepository.findOne(userId);
        if (user == null) {
            throw NotFoundException.withUserMessageKey(BCM_USER_ERROR_EMPTY)
                    .withSystemMessage(userId + "(userId) is Null").build();
        } else if (user.isSsoYn()) {
            throw ValidationException.withUserMessageKey("bcm.common.password-reset.sso")
                    .withSystemMessage(userId + "(userId),  SSO = Y").build();
        } else {
            user.setPassword(passwordEncoder.encode(user.getLoginId() + pwSuffix));
            user.setFirstLoginYn(false);
            user.setLoginFailCount(0);
            user.setLastPwdChgDtm(new Date());
        }

        return userRepository.save(user);
    }

    /**
     * <pre>
     * 2017 SKI 보안가이드 - 패스워드 조합정책
     * </pre>
     */
    private boolean unionRule(String data) {
        boolean hasNumberCH = false;
        boolean hasCharCH = false;
        boolean hasSpecialCharCH = false;

        int dataLength = data.length();

        char temp;
        for (int i = 0, iLen = data.length(); i < iLen; i++) {
            temp = data.charAt(i);

            if (!hasNumberCH) {
                hasNumberCH = hasNumber(temp);
            }

            if (!hasCharCH) {
                hasCharCH = hasChar(temp);
            }

            if (!hasSpecialCharCH) {
                hasSpecialCharCH = hasSpecialChar(temp);
            }
        }

        // 정보통신망법에 의거해 다음 각 목의 문자 종류 중 2종류 이상을 조합하여 최소 10자리 이상 또는 3종류 이상을 조합하여 최소 8자리 이상의 길이로 구성
        // 가. 영문 대문자(26개), 나. 영문 소문자(26개), 다. 숫자(10개), 라. 특수문자(32개)
        return (hasNumberCH && hasCharCH && 
                (
                        (!hasSpecialCharCH && dataLength >= MIN_DIGITS_MIXING_TWO_TYPES) ||
                        (hasSpecialCharCH && dataLength >= MIN_DIGITS_MIXING_THREE_TYPES)
                )
               ) ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean hasNumber(char temp) {
        boolean rtnVal = false;

        for (int j = 0, jLen = CHECK_NUMBER.length(); j < jLen; j++) {
            if (CHECK_NUMBER.charAt(j) == temp) {
                rtnVal = true;
                break;
            }
        }
        return rtnVal;
    }

    private boolean hasChar(char temp) {
        boolean rtnVal = false;

        if (((temp >= 'a') && (temp <= 'z')) || ((temp >= 'A') && (temp <= 'Z'))) {
            rtnVal = true;
        }
        return rtnVal;
    }

    private boolean hasSpecialChar(char temp) {
        boolean rtnVal = false;

        for (int k = 0, kLen = CHECK_SPECIAL_CHAR.length(); k < kLen; k++) {
            if (CHECK_SPECIAL_CHAR.charAt(k) == temp) {
                rtnVal = true;
                break;
            }
        }
        return rtnVal;
    }

    /**
     * <pre>
     * 2017 SKI 보안가이드 - 개인정보 및 아이디와 비슷한 패스워드 사용 금지
     * History
     * - 2018. 8. 20. | in01876 | user 정보는 toUpperCase 를 하지 않아 equals 조건에 필터링 되지 않음. toUpperCase() 추가.
     * </pre>
     */
    private boolean personalInformation(User user, String data) {
        boolean rtnVal = true;

        String loginId  = StringUtils.trimToEmpty(user.getLoginId()).toUpperCase();
        String email    = StringUtils.trimToEmpty(StringUtils.substringBefore(user.getEmail(), "@")).toUpperCase();
        String userName = StringUtils.trimToEmpty(user.getUserName()).toUpperCase();
        String mobileNo = StringUtils.trimToEmpty(user.getMobileNo()).toUpperCase();
        String telNo    = StringUtils.trimToEmpty(user.getTelNo());

        List<String> myInfoList = new ArrayList<>();
        myInfoList.add(loginId);
        if (StringUtils.isNotBlank(email))    { myInfoList.add(email); }
        if (StringUtils.isNotBlank(userName)) { myInfoList.add(userName); }
        if (StringUtils.isNotBlank(mobileNo)) { myInfoList.add(mobileNo); }
        if (StringUtils.isNotBlank(telNo))    { myInfoList.add(telNo); }

        String temp = StringUtils.trim(data).toUpperCase();
//       for (int i = 0, iLen = myInfoList.size(); i < iLen; i++) {
//            for (int j = 0, jLen = temp.length() - myInfoList.get(i).length(); j < jLen + 1; j++) {
//                if (StringUtils.equals(StringUtils.substring(temp, j, j + myInfoList.get(i).length()), myInfoList.get(i))) {
//                    return false;
//                }
//            }
//        }
        for (int i = 0, iLen = myInfoList.size(); i < iLen; i++) {
            if (StringUtils.contains(temp, myInfoList.get(i))) {
                rtnVal = false;
                break;
            }
        }
        return rtnVal;
    }

    /**
     * <pre>
     * 2017 SKI 보안가이드 - 특정 패턴을 갖는 패스워드 사용 금지
     * 동일한 한 글자 연속성(세번이상 중복될 경우)
     * </pre>
     */
    private boolean oneStringRepetition(String data) {
        boolean rtnVal = true;

        String temp1 = "";
        String temp2 = "";
        String temp3 = "";

        for (int i = 0, iLen = data.length() - 2; i < iLen; i++) {
            temp1 = StringUtils.substring(data, i,         i + 1);
            temp2 = StringUtils.substring(data, i + 1,     i + 1 + 1);
            temp3 = StringUtils.substring(data, i + 1 + 1, i + 1 + 1 + 1);

            if (temp1.equals(temp2) && temp2.equals(temp3)) {
                rtnVal = false;
                break;
            }
        }
        return rtnVal;
    }

    /**
     * <pre>
     * 2017 SKI 보안가이드 - 특정 패턴을 갖는 패스워드 사용 금지
     * 두 자 이상의 동일문자 연속성(두 번 이상 중복될 경우)
     * </pre>
     */
    private static boolean twoStringRepetition(String data) {
        int count = 0;
        String temp = "";
        String temps = "";

        for (int i = 0, iLen = data.length() - 2; i <= iLen; i++) {
            count = 0;
            temp = StringUtils.substring(data, i, i + 2);

            for (int j = 0, jLen = data.length() - 1; j < jLen; j++) {
                temps = StringUtils.substring(data, j, j + 2);
                if (temps.equals(temp)) {
                    count++;

                    if (count >= 2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * <pre>
     * 2017 SKI 보안가이드 - 특정 패턴을 갖는 패스워드 사용 금지
     * 키보드  연속성 체크(특수문자 제외)
     * </pre>
     */
    private static boolean keyboardContinuity(String data) {
        String[] keyboardCH = {
                // 숫자 연속성 체크
                "01234567890", "09876543210",

                // 문자 연속성 체크
                "QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM", "POIUYTREWQ", "LKJHGFDSA", "MNBVCXZ", "1QAZ", "2WSX", "3EDC",
                "4RFV", "5TGB", "6YHN", "7UJM", "0OKM", "9IJN", "8UHB", "7YGV", "6TFC", "5RDX", "4ESZ", "ZAQ1", "XSW2",
                "CDE3", "VFR4", "BGT5", "NHY6", "MJU7", "MKO0", "NJI9", "BHU8", "VGY7", "CFT6", "XDR5", "ZSW4" };

        data = data.toUpperCase();
        Pattern p = null;
        Matcher m = null;
        int count = 0;

        for (int i = 0, iLen = keyboardCH.length; i < iLen; i++) {
            for (int j = 0, jLen = keyboardCH[i].length() - 2; j < jLen; j++) {
                count = 0;

                p = Pattern.compile(StringUtils.substring(keyboardCH[i], j, j + 3).toUpperCase());
                m = p.matcher(data);

                for (int k = 0; m.find(k); j = m.end()) {
                    count++;
                    if (count >= 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public List<UserGroup> findUserGroupMap(String userId) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return userGroupService.findByUseYn(sort, true);
    }

    @Override
    public UserUserGroupMapSaveDTO saveUserGroupMap(String userId, UserUserGroupMapSaveDTO userUserGroupMapSaveDTO) {

        if (userUserGroupMapSaveDTO != null) {
            // 사용자 그룹 매핑 삭제
            if (userUserGroupMapSaveDTO.getDeleteUserGroupIds() != null &&
                    !userUserGroupMapSaveDTO.getDeleteUserGroupIds().isEmpty()) {

                String[] deleteArray = userUserGroupMapSaveDTO.getDeleteUserGroupIds().stream().toArray(String[]::new);
                userGroupUserMapService.deleteUserGroupsByUserId(userId, deleteArray);
            }

            // 사용자 그룹 매핑 추가
            if (userUserGroupMapSaveDTO.getAddUserGroupIds() != null &&
                    !userUserGroupMapSaveDTO.getAddUserGroupIds().isEmpty()) {

                String[] addArray =  userUserGroupMapSaveDTO.getAddUserGroupIds().stream().toArray(String[]::new);
                userGroupUserMapService.saveUserGroupsByUserId(userId, addArray);
            }            
        }

        return userUserGroupMapSaveDTO;
    }


}
