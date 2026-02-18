/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.login.domain.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import com.nets.sso.agent.AuthCheck;
import com.nets.sso.agent.AuthCheckLevel;
import com.nets.sso.agent.AuthStatus;
import com.nets.sso.agent.ErrorCode;
import com.nets.sso.agent.ErrorMessage;
import com.nets.sso.agent.SSOConfig;
import com.skiaf.bcm.login.domain.service.dto.LoginUserDTO;
import com.skiaf.bcm.login.domain.service.dto.PasswordChangeUserDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.util.BCMEventLogger;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.RestResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Value("${bcm.password.fail-max-count}")
    private int loginFailMaxCount;
    
    @Value("${bcm.login.use-sso}")
    private boolean isUseSSO;

    @Value("${bcm.password.change-cycle-day}")
    private long passwordChangeCycleDay;
    
    private static Map<String, AuthStatus> SSO_STATUS = new HashMap<>();
    static {
        SSO_STATUS.put(CommonConstant.LOGIN_SSO_SUCCESS, AuthStatus.SSOSuccess);
        SSO_STATUS.put(CommonConstant.LOGIN_SSO_ERROR, AuthStatus.SSOFail);
        SSO_STATUS.put(CommonConstant.LOGIN_SSO_FIRST_ACCESS, AuthStatus.SSOFirstAccess);
        SSO_STATUS.put(CommonConstant.LOGIN_SSO_ACCESS_DENIED, AuthStatus.SSOAccessDenied);
        SSO_STATUS.put(CommonConstant.LOGIN_SSO_UNAVAILABLE, AuthStatus.SSOUnAvaliable);
    }
    
    @Override
    public RestResponse login(LoginUserDTO loginUserDto, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);
        
        Authentication authentication = null;

        /*
         * 로그인 처리 
         */
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginUserDto.getId(), loginUserDto.getPassword());
            authentication = authenticationManager.authenticate(token);
            User loginUser =  SessionUtil.getLoginUser(authentication);
            
            // 사용자 사용여부 판별
            if (!loginUser.isUseYn()) {
                BCMEventLogger.withLogger(log).putLoginEvent("login-check-01", loginUser.getLoginId());
                throw BizException
                        .withUserMessageKey("bcm.login.error.not-use")
                        .withSystemMessage("loginUser's useYn is N")
                        .build();
            }
            // 5회이상 로그인 실패시
            else if (loginUser.getLoginFailCount() >= loginFailMaxCount) {
                BCMEventLogger.withLogger(log).putLoginEvent("login-check-02", loginUser.getLoginFailCount(), loginFailMaxCount);
                throw BizException
                        .withUserMessageKey("bcm.login.error.over-password-try")
                        .withSystemMessage("loginFailCount is over maxCount")
                        .build();
            }
            // 최초 로그인 여부 판별
            else if (!loginUser.isFirstLoginYn()) {
                BCMEventLogger.withLogger(log).putLoginEvent("login-check-03");
                session.setAttribute(CommonConstant.TEMP_LANGUAGE_CODE, loginUserDto.getLanguageCode());
                throw BizException
                        .withUserMessageKey("bcm.login.error.not-password-change")
                        .withSystemMessage("firstLogin is true")
                        .withCode(CommonConstant.LOGIN_REQUIRED_PASSWORD_CHANGE)
                        .build();
            } else {

                // 비밀번호 변경주기 판별
                LocalDateTime lastPwdChgDate = loginUser.getLastPwdChgDtm().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime nowDate = LocalDateTime.now();

                if (lastPwdChgDate.plusDays(passwordChangeCycleDay).isBefore(nowDate)) {
                    BCMEventLogger.withLogger(log).putLoginEvent("login-check-04", lastPwdChgDate, passwordChangeCycleDay, nowDate);
                    session.setAttribute(CommonConstant.TEMP_LANGUAGE_CODE, loginUserDto.getLanguageCode());
                    throw BizException
                            .withUserMessageKey("bcm.login.error.not-password-change")
                            .withSystemMessage("firstLogin is true")
                            .withCode(CommonConstant.LOGIN_REQUIRED_PASSWORD_CHANGE)
                            .build();
                }
            }
            
            token.setDetails(new WebAuthenticationDetails(request));
        } catch (UsernameNotFoundException e) {
            BCMEventLogger.withLogger(log).putLoginEvent("login-check-05", loginUserDto.getId());
            throw BizException
                    .withUserMessageKey("bcm.login.error.fail")
                    .withSystemMessage("loginUser is null")
                    .build();
        } catch (BadCredentialsException e) {
            BCMEventLogger.withLogger(log).putLoginEvent("login-check-06", loginUserDto.getId());
            User loginFailedUser = userService.findByLoginId(loginUserDto.getId());
            if (loginFailedUser != null) {
                // 5회이상 로그인 실패시
                if (loginFailedUser.getLoginFailCount() >= loginFailMaxCount) {
                    BCMEventLogger.withLogger(log).putLoginEvent("login-check-07", loginUserDto.getId(), loginFailedUser.getLoginFailCount(), loginFailMaxCount);
                    throw BizException
                            .withUserMessageKey("bcm.login.error.over-password-try")
                            .withSystemMessage("loginFailCount is over maxCount")
                            .build();
                } else {
                 // 로그인 실패 카운트 증가
                    userService.incrementLoginFailCountByLoginId(loginUserDto.getId());
                }
            }
            throw BizException
                    .withUserMessageKey("bcm.login.error.fail")
                    .withSystemMessage("login failed")
                    .build();
        }
        
        // 정상 로그인
        BCMEventLogger.withLogger(log).putLoginEvent("login-check-08", loginUserDto.getId());

        // 정상 로그인 하였을 경우, 로그인 실패 카운트를 초기화 시킨다.
        userService.initLoginFailCountByLoginId(loginUserDto.getId());
        
        // 언어 설정
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, new Locale(loginUserDto.getLanguageCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return new RestResponse(SessionUtil.getLoginUserInfo(authentication));

    }

    @Override
    public RestResponse ssoLogin(HttpServletRequest request, HttpServletResponse response, boolean isSSOFirstAccess)  {

        SSOConfig.request = request;
        AuthCheck auth = new AuthCheck(request, response);
        AuthStatus status = null;

        // SSO 사용여부에 따른 AuthCheck
        if (isUseSSO) {
            try {
                status = auth.checkLogon(AuthCheckLevel.Medium);
            } catch (Exception e) {
                log.error("ssoLogin", e);
            }
        } else {
            if (isSSOFirstAccess) {
                status = SSO_STATUS.get(request.getParameter("ssoStatus"));
            } else {
                status = SSO_STATUS.get(request.getParameter("nextSsoStatus"));
            }
        }
        
        BCMEventLogger.withLogger(log).putLoginEvent("sso-check-01", status);

        /*
         * SSO 인증 처리 
         */
        // SSO 인증 성공인 경우 ( AuthStatus.SSOSuccess => 0 )
        if (status == AuthStatus.SSOSuccess) {
            LoginUserDTO loginUser = new LoginUserDTO();
            
            if (isUseSSO) {
                loginUser.setId(auth.getSSODomainCookieValue("empNo"));
            } else {
                loginUser.setId(request.getParameter("ssoTestLoginId"));
            }
            loginUser.setPassword(loginUser.getPassword());
            loginUser.setLanguageCode(WebUtils.getCookie(request, CommonConstant.TEMP_LANGUAGE_CODE).getValue());
            
            BCMEventLogger.withLogger(log).putLoginEvent("sso-check-02", loginUser.getId());
            
            return ssoLoginDirect(request, response, loginUser);
        }
        // SSO 미인증인 경우 : SSO 인증 시도 ( AuthStatus.SSOFirstAccess => -1 )
        else if (status == AuthStatus.SSOFirstAccess) {
            if (!isSSOFirstAccess) {
                BCMEventLogger.withLogger(log).putLoginEvent("sso-check-03");
                throw BizException
                        .withUserMessageKey("bcm.login.sso.error.unknown")
                        .withSystemMessage("get AuthStatus.SSOFirstAccess Again")
                        .withCode(CommonConstant.LOGIN_SSO_ERROR)
                        .build();
            } else {
                if (isUseSSO) {
                    BCMEventLogger.withLogger(log).putLoginEvent("sso-check-04");
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.first-access")
                            .withSystemMessage("AuthStatus.SSOFirstAccess")
                            .withCode(CommonConstant.LOGIN_SSO_FIRST_ACCESS)
                            .build();
                } else {
                    return ssoLogin(request, response, false);
                }
            }
        }
        // SSO 인증 실패인 경우 ( AuthStatus.SSOFail => -2 )
        else if (status == AuthStatus.SSOFail) {

            if (auth.errorNumber() != ErrorCode.NO_ERR) {
                if (auth.errorNumber() == ErrorCode.ERR_EXPIRE_TIMEOUT) {
                    BCMEventLogger.withLogger(log).putLoginEvent("sso-check-05");
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.expire-timeout", auth.errorNumber())
                            .withSystemMessage(ErrorMessage.getMessage(auth.errorNumber()))
                            .withCode(CommonConstant.LOGIN_SSO_EXPIRE_TIMEOUT)
                            .build();
                } else {
                    BCMEventLogger.withLogger(log).putLoginEvent("sso-check-06", auth.errorNumber());
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.sso-error", auth.errorNumber())
                            .withSystemMessage(ErrorMessage.getMessage(auth.errorNumber()))
                            .withCode(CommonConstant.LOGIN_SSO_ERROR).build();
                }
            } else {
                String ret = request.getParameter("errorcode");
                BCMEventLogger.withLogger(log).putLoginEvent("sso-check-07", auth.errorNumber(), ret);
                
                if (StringUtils.isBlank(ret)) {
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.unknown")
                            .withSystemMessage("AuthStatus.SSOFail > ErrorCode.NO_ERR && request errorcode is null")
                            .withCode(CommonConstant.LOGIN_SSO_ERROR).build();                    
                }

                if (StringUtils.equals(ret, "-201")) {
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.sso-not-login")
                            .withSystemMessage("request errorcode is {}", ret)
                            .withCode(CommonConstant.LOGIN_SSO_NOT_LOGIN).build();                    
                } else {
                    throw BizException
                            .withUserMessageKey("bcm.login.sso.error.sso-error", ret)
                            .withSystemMessage("request errorcode is {}", ret)
                            .withCode(CommonConstant.LOGIN_SSO_ERROR).build();                    
                }
            }
        }
        // 인증서버 오류인 경우 ( AuthStatus.SSOUnAvaliable => -3 )
        else if (status == AuthStatus.SSOUnAvaliable) {
            BCMEventLogger.withLogger(log).putLoginEvent("sso-check-08");
            throw BizException
                    .withUserMessageKey("bcm.login.sso.error.sso-unknown")
                    .withSystemMessage("AuthStatus.SSOUnAvaliable")
                    .withCode(CommonConstant.LOGIN_SSO_UNAVAILABLE).build();
        }
        // SSO 인증 서버 "접근권한오류"인 경우 ( AuthStatus.SSOAccessDenied => -4 )
        else if (status == AuthStatus.SSOAccessDenied) {
            BCMEventLogger.withLogger(log).putLoginEvent("sso-check-09");
            throw BizException
                    .withUserMessageKey("bcm.login.sso.error.sso-unknown")
                    .withSystemMessage("AuthStatus.SSOAccessDenied")
                    .withCode(CommonConstant.LOGIN_SSO_ACCESS_DENIED).build();
        }

        return null;
    }

    private RestResponse ssoLoginDirect(HttpServletRequest request, HttpServletResponse response, LoginUserDTO loginUserDto) {

        HttpSession session = request.getSession(true);

        Authentication authentication = null;

        /*
         * 로그인 처리 
         */
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginUserDto.getId(), loginUserDto.getId());
            authentication = authenticationManager.authenticate(token);
            User loginUser =  SessionUtil.getLoginUser(authentication);
            
            // 사용자 사용여부 판별
            if (!loginUser.isUseYn()) {
                BCMEventLogger.withLogger(log).putLoginEvent("sso-check-10", loginUser.getLoginId());
                throw BizException
                        .withUserMessageKey("bcm.login.error.not-use")
                        .withSystemMessage("loginUser's useYn is N")
                        .withCode(CommonConstant.LOGIN_SSO_NOT_USE).build();
            }
            
            token.setDetails(new WebAuthenticationDetails(request));
        } catch (Exception e) {
            BCMEventLogger.withLogger(log).putLoginEvent("sso-check-11", loginUserDto.getId());
            throw BizException
                    .withUserMessageKey("bcm.login.sso.error.not-registraion")
                    .withSystemMessage("loginUser is null")
                    .withCode(CommonConstant.LOGIN_SSO_NOT_REGISTRAION).build();
        }
        
        BCMEventLogger.withLogger(log).putLoginEvent("sso-check-12", loginUserDto.getId());
        
        // 언어 설정
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, new Locale(loginUserDto.getLanguageCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return new RestResponse(SessionUtil.getLoginUserInfo(authentication));
    }

    @Override
    public User passwordChange(String userId, PasswordChangeUserDTO passwordChangeUserDTO) {
        BCMEventLogger.withLogger(log).putUserEvent("password-change", userId);
        Boolean firstLoginYn = true;
        return userService.passWordChange(userId, passwordChangeUserDTO.getPrePassword(), passwordChangeUserDTO.getPassword(), firstLoginYn);
    }

}
