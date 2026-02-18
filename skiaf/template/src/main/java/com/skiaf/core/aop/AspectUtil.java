/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.thymeleaf.util.MapUtils;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.element.domain.service.ElementService;
import com.skiaf.bcm.element.domain.service.dto.ElementRoleListDTO;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.ArticleType;
import com.skiaf.core.constant.BoardAttachFileType;
import com.skiaf.core.constant.BoardType;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.constant.FileType;
import com.skiaf.core.constant.MenuOpenType;
import com.skiaf.core.constant.MenuType;
import com.skiaf.core.constant.Path;
import com.skiaf.core.constant.ProgramType;
import com.skiaf.core.constant.RoleType;
import com.skiaf.core.util.SessionUtil;
import com.skiaf.core.util.Util;

/**
 * <pre>
 * AOP간 공통 사용 유틸
 *
 * History
 * - 2018. 9. 11. | in01868 | 최초작성.
 * </pre>
 */
@Component
public class AspectUtil {

    private static MessageComponent messageComponent;
    @Autowired
    public void setMessageComponent(MessageComponent messageComponent) {
        AspectUtil.messageComponent = messageComponent;
    }

    private static ProgramService programService;
    @Autowired
    public void setProgramService(ProgramService programService) {
        AspectUtil.programService = programService;
    }

    private static ElementService elementService;
    @Autowired
    public void setElementService(ElementService elementService) {
        AspectUtil.elementService = elementService;
    }

    private static String bcmHomeUrl;
    @Value("${bcm.home.url}")
    public void setBcmHomeUrl(String bcmHomeUrl) {
        AspectUtil.bcmHomeUrl = bcmHomeUrl;
    }

    private static String bcmUIHomeUrl;
    @Value("${bcm.ui.home.url}")
    public void setBcmUIHomeUrl(String bcmUIHomeUrl) {
        AspectUtil.bcmUIHomeUrl = bcmUIHomeUrl;
    }

    private static List<String> langSupportedCodeList;
    @Value("#{'${bcm.language.support}'.split(',')}")
    public void setLangSupportedCodeList(List<String> langSupportedCodeList) {
        AspectUtil.langSupportedCodeList = langSupportedCodeList;
    }

    private static String langDefaultCode;
    @Value("${bcm.language.default}")
    public void setLangDefaultCode(String langDefaultCode) {
        AspectUtil.langDefaultCode = langDefaultCode;
    }
    
    private static String bcmDomain;
    @Value("${bcm.domain.base-url}")
    public void setDomain(String bcmDomain) {
        AspectUtil.bcmDomain = bcmDomain;
    }
    
    /**
     * <pre>
     * View 에 리턴할 공통 모델을 생성한다.
     * </pre>
     */
    public static Map<String, Object> makeCommonModel(Map<String, Object> attrs) {

        Map<String, Object> modelMap = new HashMap<>();

        // BCM 홈 URL
        modelMap.put("bcmHomeUrl", bcmHomeUrl);
        modelMap.put("bcmUIHomeUrl", bcmUIHomeUrl);
        modelMap.put("bcmDomain", bcmDomain);

        modelMap.put("i18n", makeI18nModel(attrs));

        modelMap.put("constant", convertConstToMap(CommonConstant.class));
        modelMap.put("path", convertConstToMap(Path.class));

        Map<String, Object> enumMap = new HashMap<>();
        enumMap.put("ARTICLE_TYPE", convertEnumToMap(ArticleType.class));
        enumMap.put("FILE_TYPE", convertEnumToMap(FileType.class));
        enumMap.put("MENU_TYPE", convertEnumToMap(MenuType.class));
        enumMap.put("MENU_OPEN_TYPE", convertEnumToMap(MenuOpenType.class));
        enumMap.put("PROGRAM_TYPE", convertEnumToMap(ProgramType.class));
        enumMap.put("ROLE_TYPE", convertEnumToMap(RoleType.class));
        enumMap.put("BOARD_TYPE", convertEnumToMap(BoardType.class));
        enumMap.put("BOARD_ATTACH_FILE_TYPE", convertEnumToMap(BoardAttachFileType.class));
        modelMap.put("enum", enumMap);

        // 프로그램 도움말 아이디
        Map<String, Object> programInfo = new HashMap<>();

        if (attrs == null) {
            modelMap.put("loginUserInfo", null);
            modelMap.put("elementRole", new Object());
        } else {
            // 로그인 사용자 정보 추가
            UserInfoDTO userInfoDTO = SessionUtil.getLoginUserInfo();
            modelMap.put("loginUserInfo", userInfoDTO);

            /*
             * 프로그램 요소 처리
             */
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String mappingUrl = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            String httpMethod = request.getMethod();

            // 등록된 프로그램이 있는지 확인
            Program program = programService.findTopByHttpMethodAndPath(httpMethod, mappingUrl, request.getServletPath());


            if (program != null && program.isUseYn()) {

                /*
                 * 프로그램 도움말 조회
                 */
                if (program.getAttachFile() != null) {
                    programInfo.put("fileId", program.getAttachFile().getFileId());
                }


                /*
                 * 프로그램 요소 조회
                 */

                // 등록된 프로그램 요소가 있는지 확인
                ElementRoleListDTO elementRoleListDTO = elementService.findByProgramProgramId(program.getProgramId());
                List<ElementRoleMapDTO> elementRoleMapDTOList = elementRoleListDTO.getElementRoleMapList();
                List<Element> elementList = elementRoleListDTO.getElementList();

                List<Role> userRoleList = new ArrayList<>();

                // 사용자 정보 조회
                if (userInfoDTO != null) {

                    // 사용자의 권한 조회
                    userRoleList = userInfoDTO.getRoleList();
                    if (userRoleList == null) {
                        userRoleList = new ArrayList<>();
                    }
                }

                // 권한에 따른 프로그램 요소 표시 및 활성화 여부 결정
                for (ElementRoleMapDTO elementRoleMapDTO : Util.nullToEmpty(elementRoleMapDTOList)) {
                    boolean isRole = userRoleList.stream().anyMatch(userRole -> userRole.getRoleId().equals(elementRoleMapDTO.getRoleId()));

                    boolean useYn = false;

                    Optional<Element> elementFirst = elementList.stream()
                            .filter(element -> element.getElementSeq() == elementRoleMapDTO.getElementSeq())
                            .findFirst();

                    if (elementFirst.isPresent()) {
                        useYn = elementFirst.get().isUseYn();
                    }

                    // 권한이 있으면 Visible, Enable 처리
                    if (isRole) {
                        elementRoleMapDTO.setEnableYn(true);
                        elementRoleMapDTO.setVisibleYn(true);
                    } else {
                        // Enable 설정 되어있으면 비활성화
                        elementRoleMapDTO.setEnableYn(!elementRoleMapDTO.isEnableYn());

                        // Visible 설정 되어있으면 비활성화
                        elementRoleMapDTO.setVisibleYn(!elementRoleMapDTO.isVisibleYn());
                    }

                    // 사용하지 않은 요소이면 접근 허용
                    if (!useYn) {
                        elementRoleMapDTO.setEnableYn(true);
                        elementRoleMapDTO.setVisibleYn(true);
                    }

                }

                modelMap.put("elementRole", elementRoleMapDTOList);

            } else {
                modelMap.put("elementRole", new ArrayList<>());
            }

        }

        // 프로그램 도움말
        modelMap.put("program", programInfo);

        return modelMap;
    }

    /**
     * <pre>
     * javascript SKIAF.i18n 객체로 전달할 Map 생성.
     * </pre>
     */
    private static Map<String, Object> makeI18nModel(Map<String, Object> attrs) {
        Map<String, Object> i18n = new HashMap<>();

        String[] keyPatterns = new String[] { "bcm.common.*" }; // bcm.common.*에 대해서는 기본 추가.
        Object programIds = null;
        if (!MapUtils.isEmpty(attrs) && attrs.containsKey(MessageComponent.JS_MESSAGES_KEY_PATTERN)) {
            keyPatterns = ArrayUtils.add(keyPatterns, (String) attrs.get(MessageComponent.JS_MESSAGES_KEY_PATTERN));
            programIds = attrs.get(MessageComponent.JS_MESSAGES_PROGRAM_IDS);
        }
        i18n.put("messages", messageComponent.filterWith(keyPatterns, programIds));

        String langCurrentCode = LocaleContextHolder.getLocale().toString();
        i18n.put("langCurrentCode", langCurrentCode);
        i18n.put("langCurrentCodeName", messageComponent.getMessage("bcm.common.lanuage." + langCurrentCode));

        i18n.put("langDefaultCode", langDefaultCode);
        i18n.put("langSupportedCodes", langSupportedCodeList);

        return i18n;
    }

    /**
     * <pre>
     * static / enum상수 클래스를 Map형태로 변경
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, String> convertConstToMap(Class cls) {

        Map<String, String> constantMap = new HashMap<>();

        Constants contants = new Constants(cls);
        Set<String> constantNames = contants.getNames("");
        constantNames.forEach((String name) -> constantMap.put(name, contants.asString(name)));

        return constantMap;
    }

    /**
     * <pre>
     * enum 상수 클래스를 Map형태로 변경
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, String> convertEnumToMap(Class cls) {

        Map<String, String> constantMap = new HashMap<>();

        Field[] fields = cls.getFields();
        for (Field field : fields) {
            try {
                ReflectionUtils.makeAccessible(field);
                Object obj = field.get(null);
                Method method = obj.getClass().getDeclaredMethod("getName");
                constantMap.put(field.getName(), (String) method.invoke(obj));
            } catch (NoSuchMethodException e) {
                constantMap.put(field.getName(), field.getName());
            } catch (Exception e) {}
        }

        return constantMap;
    }
}
