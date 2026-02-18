/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.skiaf.bcm.element.domain.service.ElementService;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.constant.Path;
import com.skiaf.core.exception.ForbiddenException;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.UnauthorizedException;
import com.skiaf.core.util.BCMEventLogger;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.util.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *
 * History
 * - 2018. 8. 22. | in01866 | 2차수정.
 * </pre>
 */
@Slf4j
@Aspect
@Component
@Transactional
public class ControllerAspect {

    @Autowired
    ProgramService programService;

    @Autowired
    RoleMapService roleMapService;

    @Autowired
    ElementService elementService;

    @Value("${bcm.login.check}")
    private boolean isLoginCheck;

    @Value("${bcm.role.check}")
    private boolean isRoleCheck;

    @Value("${bcm.home.url}")
    private String bcmHomeUrl;

    /**
     * <pre>
     * BCM Controller AOP
     * </pre>
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public Object bcmControllerAop(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = checkUrlByUserRole();
        if (result != null) {
            return result;
        }

        // 메소드 실행
        result = joinPoint.proceed();

        makeCommon(joinPoint, result);

        return result;
    }

    /**
     * <pre>
     * URl 접근권한 체크
     * </pre>
     */
    private Object checkUrlByUserRole() {

        // 접근권한 체크 여부 (테스트용 플레그)
        if (!isRoleCheck) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String mappingUrl = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();

        boolean isHaveRole = false;

        // 정상 콜이 아닌경우 체크 안함.
        if (StringUtils.isBlank(httpMethod) || StringUtils.isBlank(mappingUrl)) {

            BCMEventLogger.withLogger(log).putRoleEvent("auth-check-01", request.getRequestURI());

            return null;
        }

        /*
         * url 접근 권한 처리
         */
        // 사용자 정보 조회
        UserInfoDTO userInfo = SessionUtil.getLoginUserInfo();

        String currentUrl = request.getServletPath();
        
        /*
         * BCM 예하 페이지들은 로그인이 되어 있어야 한다. (로그인 관련 제외)
         */
        // 로그인 체크가 필수이면서, BCM 예하이면서 로그인이 안되어 있는 경우
        if (isLoginCheck && (userInfo == null && mappingUrl.startsWith(Path.VIEW_BCM))) {

            // 로그인 관련 확인
            if (!(mappingUrl.startsWith(Path.LOGIN_API) || mappingUrl.startsWith(Path.LOGIN_VIEW))) {
                if (request.getQueryString() != null) {
                    currentUrl += "?" + request.getQueryString();
                }
                SessionUtil.getSession().setAttribute(CommonConstant.RETURN_URL_AFTER_LOGIN, currentUrl);

                BCMEventLogger.withLogger(log).putRoleEvent("auth-check-02", mappingUrl);
                throw UnauthorizedException.withUserMessageKey("bcm.common.exception.unauthorized").withSystemMessage("user == null").build();
            }
        }

        // 로그인 상태에서 로그인 화면으로 진입시 메인페이지로 이동
        else if (userInfo != null && Path.VIEW_LOGIN.equals(request.getServletPath())
                && HttpMethod.GET.toString().equals(request.getMethod())) {
            return new ModelAndView(new RedirectView(bcmHomeUrl, true));
        }

        // Program 조회
        Program program = programService.findTopByHttpMethodAndPath(httpMethod, mappingUrl, currentUrl);

        if (program != null) {

            // 해당 URL 사용
            if (program.isUseYn()) {

                // 프로그램에 권한이 있는지 여부 판별
                List<Role> programRoleList = roleMapService.findRoleMapByProgramId(program.getProgramId());
                if (programRoleList != null && !programRoleList.isEmpty()) {

                    if (userInfo == null) {

                        // 로그인 페이지로 이동
                        if (!currentUrl.startsWith(Path.API)) {
                            SessionUtil.getSession().setAttribute(CommonConstant.RETURN_URL_AFTER_LOGIN, currentUrl);
                        }
                        /*
                        else {
                            String host = request.getHeader("host");
                            String referer = request.getHeader("referer");

                            if (!StringUtils.isBlank(host) && !StringUtils.isBlank(referer)) {
                                SessionUtil.getSession().setAttribute(CommonConstant.RETURN_URL_AFTER_LOGIN, referer.substring(referer.indexOf(host) + host.length()));
                            }
                        }*/

                        BCMEventLogger.withLogger(log).putRoleEvent("auth-check-02", mappingUrl);
                        throw UnauthorizedException.withUserMessageKey("bcm.common.exception.unauthorized").withSystemMessage("user == null").build();

                    }

                    List<Role> userRoleList = userInfo.getRoleList();

                    if (userRoleList == null) {
                        BCMEventLogger.withLogger(log).putRoleEvent("auth-check-03", userInfo.getUser().getLoginId());

                        // 사용자 권한이 없으므로, 권한없음 에러
                        throw ForbiddenException.withSystemMessage("userRoleList == null").build();
                    } else {
                        // 사용자 권한에 프로그램 권한이 있는지 체크
                        for (Role programRole : Util.nullToEmpty(programRoleList)) {
                            if (userRoleList.stream().anyMatch(userRole -> userRole.getRoleId().equals(programRole.getRoleId()))) {
                                isHaveRole = true;
                                break;
                            }
                        }
                    }
                } else {
                    isHaveRole = true;
                }
            } else {
                BCMEventLogger.withLogger(log).putRoleEvent("auth-check-04", program.getProgramId(), program.getProgramPath(), program.getHttpMethod());

                // 해당 URL 사용불가로 인한 낫파운드 에러
                throw NotFoundException.withSystemMessage("program useYn is false").build();
            }
        } else {
            isHaveRole = true;
        }

        if (!isHaveRole) {

            BCMEventLogger.withLogger(log).putRoleEvent("auth-check-05", program.getProgramId(), program.getProgramPath(), program.getHttpMethod(),  userInfo.getUser().getLoginId());

            throw ForbiddenException.withSystemMessage("userRoleList != programRoleList").build();
        }

        return null;
    }

    /**
     * <pre>
     * Controller 에서 리턴한 값에 대한 공통 추가 처리
     * </pre>
     */
    private void makeCommon(JoinPoint joinPoint, Object result) {

        if (log.isDebugEnabled()) {
            log.debug("ControllerAspect :: Start ");
        }

        // ModelAndView 타입인지 확인
        if (result instanceof ModelAndView) {
            ModelAndView modelAndView = (ModelAndView) result;
            modelAndView.addObject("skiaf", AspectUtil.makeCommonModel(modelAndView.getModel()));
        }

        // String 타입인지 확인
        if (result instanceof String) {
            Object[] args = joinPoint.getArgs();
            Model model = null;
            for (Object arg : ArrayUtils.nullToEmpty(args)) {
                if (!(arg instanceof Model)) {
                    continue;
                }
                model = (Model) arg;
                model.addAttribute("skiaf", AspectUtil.makeCommonModel(model.asMap()));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("ControllerAspect :: END ");
        }
    }

}
