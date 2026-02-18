/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.skiaf.core.aop.AspectUtil;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.MessageDisplayType;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Controller에 대한 Exception 처리하는 Advice
 * 
 * History
 * - 2018. 8. 22. | in01865 | 2차수정.
 * </pre>
 */
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final String SLASH = "/";

    @Autowired
    private MessageComponent messageComponent;

    @Value("${bcm.exception.responses-system-message}")
    private boolean responsesSystemMessage;

    /**
     * Not Found (NotFoundException)에 대한 핸들러
     */
    @ExceptionHandler(NotFoundException.class)
    public Object notFoundHandle(NotFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.notfound"));
        }
        info.setHttpStatus(HttpStatus.NOT_FOUND);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_NOT_FOUND));// 404

        return makeResponse(info, request, response);
    }

    /**
     * Not Found (NoHandlerFoundException)에 대한 핸들러
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object notFoundNoHandlerHandle(NoHandlerFoundException ex, HttpServletRequest request,
            HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.notfound"));
        }
        info.setHttpStatus(HttpStatus.NOT_FOUND);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_NOT_FOUND));// 404

        return makeResponse(info, request, response);
    }

    /**
     * Validation Error (ValidationException, MethodArgumentNotValidException,
     * BindException) 에 대한 핸들러.
     */
    @ExceptionHandler({ ValidationException.class, MethodArgumentNotValidException.class, BindException.class })
    public Object validationHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.validation"));
        }
        info.setHttpStatus(HttpStatus.BAD_REQUEST);
        info.setTemplateName("4xx");

        return makeResponse(info, request, response);
    }

    /**
     * Unauthorized (UnauthorizedException) 에 대한 핸들러.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Object unauthorizedHandle(UnauthorizedException ex, HttpServletRequest request,
            HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.unauthorized"));
        }
        info.setHttpStatus(HttpStatus.UNAUTHORIZED);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_UNAUTHORIZED));// 401

        return makeResponse(info, request, response);
    }

    /**
     * Forbidden (ForbiddenException) 에 대한 핸들러.
     */
    @ExceptionHandler(ForbiddenException.class)
    public Object forbiddenHandle(ForbiddenException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.forbidden"));
        }
        info.setHttpStatus(HttpStatus.FORBIDDEN);
        info.setTemplateName("403");

        return makeResponse(info, request, response);
    }

    /**
     * Business Error (BizException) 에 대한 핸들러.
     */
    @ExceptionHandler(BizException.class)
    public Object bizHandle(BizException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.biz"));
        }

        // 강제 ok설정이면, status code 는 ok로.
        if (info.isForcesOK()) {
            info.setHttpStatus(HttpStatus.OK);
        } else {
            info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }
    
    /**
     * Interface Error (InterfaceException) 에 대한 핸들러.
     */
    @ExceptionHandler(InterfaceException.class)
    public Object interfaceHandle(InterfaceException ex, HttpServletRequest request,
            HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.interface"));
        }
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }    

    /**
     * BaseException 에 대한 핸들러. 정상적이라면 BaseException 타입이 throw되서는 안됨.
     */
    @ExceptionHandler(BaseException.class)
    public Object baseExceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("bcm.common.exception.server"));
        }
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * Exception / RuntimeException 타입에 대한 핸들러.
     */
    @ExceptionHandler({ Exception.class, RuntimeException.class })
    public Object exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ExceptionInfo info = new ExceptionInfo(ex);

        info.setUserMessage(messageComponent.getMessage("bcm.common.exception.server"));
        info.setSystemMessage(ex.getMessage());
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * response를 위한 객체 생성.
     */
    private Object makeResponse(ExceptionInfo info, HttpServletRequest request, HttpServletResponse response) {
        log.error("makeResponse(), {}", ExceptionUtils.getStackTrace(info.getE()));

        response.setStatus(info.getHttpStatus().value());

        MediaType requestMediaType = null;
        if (request.getContentType() != null) {
            requestMediaType = MediaType.valueOf(request.getContentType());
        }

        String apiPath = request.getContextPath() + Path.API + SLASH;
        // /api 로 시작하거나, content-type이 application/json 이면. rest-api
        if (request.getServletPath().startsWith(apiPath)
                || MediaType.APPLICATION_JSON.isCompatibleWith(requestMediaType)) {

            RestResponse restResponse = new RestResponse().userMessage(info.userMessage).code(info.getCode()).displayType(info.getDisplayType());

            // front에 systemMessage를 전달하도록 설정되어있는 경우만.
            if (responsesSystemMessage) {
                restResponse.setSystemMessage(info.getSystemMessage());
            }

            return new ResponseEntity<RestResponse>(restResponse, info.getHttpStatus());
        }
        // 아니면 view
        else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("userMessage", info.getUserMessage());

            // View 에 리턴할 공통 모델을 생성한다.
            mav.addObject("skiaf", AspectUtil.makeCommonModel(null));

            // front에 systemMessage를 전달하도록 설정되어있는 경우만.
            if (responsesSystemMessage) {
                mav.addObject("systemMessage", info.getSystemMessage());
            }

            mav.setViewName("skiaf/view/error/error-" + info.getTemplateName());

            return mav;
        }
    }

    @Getter
    @Setter
    private class ExceptionInfo {
        private Throwable e;
        private Throwable cause;
        private String userMessage;
        private String systemMessage;
        private MessageDisplayType displayType;
        private boolean forcesOK = false;
        private String code;
        private HttpStatus httpStatus;
        private String templateName;

        public ExceptionInfo(Throwable e) {
            this.e = e;
            this.cause = e.getCause();
            if (e instanceof BaseException) {
                BaseException be = (BaseException) e;
                this.userMessage = be.getUserMessage();
                this.systemMessage = be.getSystemMessage();
                this.displayType = be.getDisplayType();
                this.code = be.getCode();
            }
            // @validator에서 뱉는 Exception
            else if (e instanceof MethodArgumentNotValidException) {
                Pair<String, String> msg = setValidationError(((MethodArgumentNotValidException) e).getBindingResult());
                this.userMessage = msg.getFirst();
                this.systemMessage = msg.getSecond();
            }
            // @validator에서 뱉는 Exception
            else if (e instanceof BindException) {
                Pair<String, String> msg = setValidationError(((BindException) e).getBindingResult());
                this.userMessage = msg.getFirst();
                this.systemMessage = msg.getSecond();
            }

            if (e instanceof BizException) {
                this.forcesOK = ((BizException) e).isForcesOK();
            }
            if (cause instanceof BaseException) {
                BaseException be = (BaseException) cause;
                this.userMessage = be.getUserMessage();
                this.systemMessage = be.getSystemMessage();
                this.displayType = be.getDisplayType();
                this.code = be.getCode();
            }
            if (cause instanceof BizException) {
                this.forcesOK = ((BizException) cause).isForcesOK();
            }

        }

        private Pair<String, String> setValidationError(BindingResult br) {
            
            FieldError fe = br.getFieldError();
            String errorDefaultMessage = null;
            String errorAttribute = null;
            if(fe != null) {
                errorDefaultMessage = fe.getDefaultMessage();
                errorAttribute = fe.getCodes()[0];
            }else {
                ObjectError oe = br.getGlobalError();
                errorDefaultMessage = oe.getDefaultMessage();
                errorAttribute = oe.getCode();
            }

            String key = errorDefaultMessage;
            Object[] args = null;

            if (StringUtils.indexOf(key, ',') > -1) {
                String[] errorMessageArr = key.split(",");
                key = errorMessageArr[0].trim();
                args = new Object[errorMessageArr.length - 1];
                System.arraycopy(errorMessageArr, 1, args, 0, errorMessageArr.length - 1);
            }
            
            String errorMessage = messageComponent.getMessage(key, args);  
            if(errorMessage == null) {
                errorMessage = errorDefaultMessage; 
            }

            if (StringUtils.lastIndexOf(errorAttribute, '.') > -1) {
                errorMessage = "[" + errorAttribute.substring(StringUtils.lastIndexOf(errorAttribute, '.') + 1) + "] " + errorMessage;
            }

            return Pair.of(errorMessage, errorAttribute);
        }        
    }

}
