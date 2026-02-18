/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.repository.CodeGroupRepository;
import com.skiaf.bcm.code.domain.service.dto.CodeSearchDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 코드 그룹 Service Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Service
@Transactional
public class CodeGroupServiceImpl implements CodeGroupService {

    private static final int LANGUAGE_INDEX_ZERO = 0;
    private static final int LANGUAGE_INDEX_ONE  = 1;
    private static final int LANGUAGE_INDEX_TWO  = 2;
/* 4번째 언어 추가시, 주석 해제
    private static final int LANGUAGE_INDEX_THREE  = 3;
*/

    private static final String USER_MESSAGE_KEY_EMPTY = "bcm.common.EMPTY";
    private static final String USER_MESSAGE_KEY_DUPLICATE = "bcm.common.DUPLICATE";

    @Autowired
    CodeGroupRepository codeGroupRepository;

    @Autowired
    CodeService codeService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MessageComponent messageComponent;

    @Override
    public CodeGroup findOne(String codeGroupId) {

        CodeGroup codeGroup = codeGroupRepository.findOne(codeGroupId);
        if (codeGroup == null) {
            return null;
        }

        codeGroup.setCodeGroupName(this.findCodeGroupNameByCurrentLanguage(codeGroup));
        return codeGroup;
    }

    @Override
    public CodeGroup findByCodeGroupIdAndUseYn(String codeGroupId, boolean useYn) {
        CodeGroup codeGroup = codeGroupRepository.findByCodeGroupIdAndUseYn(codeGroupId, useYn);
        if (codeGroup == null) {
            return null;
        }
        codeGroup.setCodeGroupName(this.findCodeGroupNameByCurrentLanguage(codeGroup));
        return codeGroup;
    }

    @Override
    public CodeGroup update(String codeGroupId, CodeGroup codeGroup) {

        // 저장된 데이터 있는지 조회
        CodeGroup selectCodeGroup = this.findOne(codeGroupId);
        if (selectCodeGroup == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                    .withSystemMessage("selectCodeGroup == null").build();
        }

        // 저장
        codeGroup.setCodeGroupId(codeGroupId);
        return codeGroupRepository.save(codeGroup);
    }

    @Override
    public List<CodeGroup> findAll() {
        return codeGroupRepository.findAll();
    }

    @Override
    public CodeGroup create(CodeGroup codeGroup) {
        if (codeGroup == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                    .withSystemMessage("codeGroup == null").build();
        }
        String codeGroupId = codeGroup.getCodeGroupId();
        if (StringUtils.isEmpty(codeGroupId)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                    .withSystemMessage("codeGroupId == null").build();
        }

        CodeGroup selectCodeGroup = this.findOne(codeGroupId);
        if (selectCodeGroup != null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage("selectCodeGroup != null").build();
        }
        return codeGroupRepository.save(codeGroup);
    }

    @Override
    public PageDTO<CodeGroup> findQueryBySearch(CodeSearchDTO search, Pageable pageable) {
        return codeGroupRepository.findQueryBySearch(search, pageable);
    }

    @Override
    public String findCodeGroupNameByCurrentLanguage(CodeGroup codeGroup) {
        int index = messageComponent.getLanguageIndex();
        String codeGroupName = "";

        switch (index) {
        case LANGUAGE_INDEX_ZERO:  codeGroupName = codeGroup.getCodeGroupName1();    break;
        case LANGUAGE_INDEX_ONE:   codeGroupName = codeGroup.getCodeGroupName2();    break;
        case LANGUAGE_INDEX_TWO:   codeGroupName = codeGroup.getCodeGroupName3();    break;
/* 4번째 언어 추가시, 주석 해제
        case LANGUAGE_INDEX_THREE: codeGroupName = codeGroup.getCodeGroupName4();    break;
*/
        default:                  codeGroupName = "";
        }

        return codeGroupName;
    }
}
