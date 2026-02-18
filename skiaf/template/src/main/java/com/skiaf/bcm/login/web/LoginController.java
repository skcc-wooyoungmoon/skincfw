/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.login.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.nets.sso.agent.AuthCheck;
import com.nets.sso.agent.SSOConfig;
import com.skiaf.bcm.code.domain.service.dto.CodeDetailDTO;
import com.skiaf.bcm.login.domain.service.LoginService;
import com.skiaf.bcm.login.domain.service.dto.LoginUserDTO;
import com.skiaf.bcm.login.domain.service.dto.PasswordChangeUserDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.UserService;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.constant.Path;
import com.skiaf.core.controller.AbstractBCMController;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <pre>
 * 로그인 컨트롤러
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Api(tags = "로그인")
@RestController
public class LoginController extends AbstractBCMController {

    @Value("${bcm.login.use-sso}")
    private boolean isUseSSO;

    @Value("${bcm.domain.base-url}")
    private String baseUrl;

    @Value("${server.context-path}")
    private String contextPath;

    @Value("${bcm.login.sso-login-url}")
    private String ssoRedirectLoginUrl;

    @Value("${bcm.home.url}")
    private String bcmHomeUrl;

    @Value("${bcm.login.use-sso-login}")
    private boolean useSsoLoginBtn;
    
    @Value("${bcm.language.support}")
    private String languageSupport;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageComponent messageComponent;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 로그인 화면
     * </pre>
     */
    @GetMapping(value = Path.VIEW_LOGIN)
    public ModelAndView loginView() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/login/login-form");
        modelAndView.addObject("isUseSSO", isUseSSO);
        modelAndView.addObject("useSsoLoginBtn", useSsoLoginBtn);
        List<CodeDetailDTO> languageSupportList = new ArrayList<>();
        for(String language : languageSupport.split(",")) {
            CodeDetailDTO code = new CodeDetailDTO();
            code.setCodeId(language);
            code.setCodeName(messageComponent.getMessage("bcm.common.lanuage."+language));
            languageSupportList.add(code);
        }
        modelAndView.addObject("languageSupportList", languageSupportList);
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.login.sso.error.*");
        
        setReturnUrlAfterLogin(modelAndView);

        return modelAndView;
    }

    /**
     * <pre>
     * SSO 처리중 화면 (redirect 처리 페이지)
     * </pre>
     */
    @GetMapping(value = Path.VIEW_SSO_LOGIN_PROCESS)
    public ModelAndView ssoLoginView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/login/sso-login-process");
        setReturnUrlAfterLogin(modelAndView);

        return modelAndView;
    }

    /**
     * <pre>
     * SSO 세션 만료시 이동 화면 (redirect 페이지)
     * </pre>
     */
    @GetMapping(value = Path.VIEW_SSO_LOGOFF)
    public ModelAndView ssoLogoffView() throws Exception {

        ModelAndView modelAndView = new ModelAndView();

        // http://sso.skcorp.com/SSO/AuthWeb/Logoff.aspx?returnURL= + 돌아올 URL +
        // &ssosite= + 사이트 도메인
        String navigateUrl = SSOConfig.logoffPage() 
                    + "?" + SSOConfig.returnURLTagName() + "="
                    + URLEncoder.encode(baseUrl + contextPath + Path.VIEW_SSO_LOGIN_PROCESS, StandardCharsets.UTF_8.name())
                    + "&" + SSOConfig.REQUESTSSOSITEPARAM + "=" + SSOConfig.siteDomain();
        modelAndView.setView(new RedirectView(navigateUrl, true));
        return modelAndView;
    }

    /**
     * <pre>
     * SSO 최초 로그인 시 이동 화면 (redirect 페이지)
     * </pre>
     */
    @GetMapping(value = Path.VIEW_TRY_SSO)
    public void trySsoView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AuthCheck auth = new AuthCheck(request, response);
        auth.trySSO(baseUrl + contextPath + Path.VIEW_SSO_LOGIN_PROCESS);
    }

    /**
     * <pre>
     * 로그인 에러시 화면
     * </pre>
     */
    @GetMapping(value = Path.VIEW_LOGIN_ERROR)
    public ModelAndView loginErrorView() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/login/login-error");

        modelAndView.addObject("ssoRedirectLoginUrl", ssoRedirectLoginUrl + "?sFlag=em&returnURL="
                + URLEncoder.encode(baseUrl + contextPath + Path.SSO_LOGIN, StandardCharsets.UTF_8.name()));
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.login.sso.error.*");

        return modelAndView;
    }

    /**
     * <pre>
     * 비밀번호 변경 화면
     * </pre>
     */
    @GetMapping(value = Path.VIEW_CHANGE_PASSWORD)
    public ModelAndView changePasswordView(@PathVariable String loginId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/login/password-change-form");

        User loginUser = userService.findByLoginIdAndSsoYnFalse(loginId);
        modelAndView.addObject("loginUser", loginUser);
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, "bcm.login.password-change.*");

        setReturnUrlAfterLogin(modelAndView);
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 로그인 처리
     * </pre>
     */
    @ApiOperation(value = "로그인")
    @PostMapping(value = Path.LOGIN)
    public RestResponse login(
            @ApiParam(name = "loginUser", value = "로그인 정보") @Validated @RequestBody LoginUserDTO loginUser,
            @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        return loginService.login(loginUser, request, response);
    }

    /**
     * <pre>
     * SSO 인증 처리
     * </pre>
     */
    @ApiOperation(value = "SSO 인증")
    @GetMapping(value = Path.SSO_LOGIN)
    public RestResponse ssoLogin(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        return loginService.ssoLogin(request, response, true);
    }

    /**
     * <pre>
     * 비밀번호 변경 처리
     * </pre>
     */
    @ApiOperation(value = "비밀번호 변경")
    @PatchMapping(value = Path.PASSWORD_CHANGE)
    public RestResponse changePassword(@ApiIgnore HttpSession session, @ApiIgnore HttpServletRequest request,
            @ApiIgnore HttpServletResponse response,
            @ApiParam(name = "userId", required = true, value = "사용자 ID", example = "skiaf_admin-cuid") @PathVariable String userId,
            @ApiParam(name = "passwordChangeValue", required = true, value = "현재/변경 비밀번호 값") @RequestBody @Validated PasswordChangeUserDTO passwordChangeUserDTO) {

        User user = loginService.passwordChange(userId, passwordChangeUserDTO);

        String lanaugeCode = (String) session.getAttribute(CommonConstant.TEMP_LANGUAGE_CODE);
        session.removeAttribute(CommonConstant.TEMP_LANGUAGE_CODE);
        if (StringUtils.isBlank(lanaugeCode)) {
            lanaugeCode = LocaleContextHolder.getLocale().toString();
        }

        LoginUserDTO loginUser = new LoginUserDTO();
        loginUser.setId(user.getLoginId());
        loginUser.setPassword(passwordChangeUserDTO.getPassword());
        loginUser.setLanguageCode(lanaugeCode);

        return loginService.login(loginUser, request, response);
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Function
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 로그인 후 이동 페이지 설정
     * </pre>
     */
    private void setReturnUrlAfterLogin(ModelAndView modelAndView) {
        String returnUrlAfterLogin = (String) SessionUtil.getSession()
                .getAttribute(CommonConstant.RETURN_URL_AFTER_LOGIN);
        if (returnUrlAfterLogin != null) {
            SessionUtil.getSession().removeAttribute(CommonConstant.RETURN_URL_AFTER_LOGIN);
        } else {
            returnUrlAfterLogin = bcmHomeUrl;
        }

        modelAndView.addObject(CommonConstant.RETURN_URL_AFTER_LOGIN, returnUrlAfterLogin);
    }

}
