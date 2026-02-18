/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.model.CodeGroup;
import com.skiaf.bcm.code.domain.model.CodeId;
import com.skiaf.bcm.code.domain.repository.CodeRepository;
import com.skiaf.bcm.code.domain.service.dto.CodeDetailDTO;
import com.skiaf.bcm.code.domain.service.dto.CodeUpdateDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.MessageDisplayType;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;

/**
 * <pre>
 * BCM 코드 Service Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Service
@Transactional
public class CodeServiceImpl implements CodeService {

    private static final int LANGUAGE_INDEX_ZERO = 0;
    private static final int LANGUAGE_INDEX_ONE  = 1;
    private static final int LANGUAGE_INDEX_TWO  = 2;
/* 4번째 언어 추가시, 주석 해제
    private static final int LANGUAGE_INDEX_THREE  = 3;
*/

    private static final String USER_MESSAGE_KEY_DUPLICATE = "bcm.common.DUPLICATE";
    private static final String USER_MESSAGE_KEY_EMPTY     = "bcm.common.EMPTY";

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    CodeGroupService codeGroupService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MessageComponent messageComponent;

    @Override
    public Code findOne(String codeGroupId, String codeId) {
        Code code = codeRepository.findOne(new CodeId(codeGroupId, codeId));
        if(code != null) {
            code.setCodeName(this.findCodeNameByCurrentLanguage(code));
        }
        return code;
    }

    @Override
    public List<Code> createList(List<CodeDetailDTO> codeDetailDTOList, String codeGroupId) {

        // 저장할 아이디 목록
        List<String> codeIdList = new ArrayList<>();
        codeDetailDTOList.forEach((CodeDetailDTO codeDetailDTO) -> {

            // 아이디 있는지 체크
            if (StringUtils.isEmpty(codeDetailDTO.getCodeId())) {
                throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                    .withSystemMessage("codeId isEmpty")
                    .withDisplayType(MessageDisplayType.LAYER_POPUP)
                    .build();
            }

            // 아이디 중복 체크
            if (codeIdList.contains(codeDetailDTO.getCodeId())) {
                throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage("this.isDuplicate(codeDetailDTOList)")
                    .withDisplayType(MessageDisplayType.LAYER_POPUP)
                    .build();
            }
            codeIdList.add(codeDetailDTO.getCodeId());
        });

        // 존재하는 아이디가 있는지 확인
        List<Code> existCodeList = codeRepository.findByCodeIdIn(codeIdList);
        if (existCodeList != null && !existCodeList.isEmpty()) {
            // 이미 존재하는 아이디 있음
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                .withSystemMessage("existCodeList.size() > 0")
                .withDisplayType(MessageDisplayType.LAYER_POPUP)
                .build();
        }

        // 코드 그룹 조회
        CodeGroup codeGroup = codeGroupService.findOne(codeGroupId);

        // 저장
        List<Code> codeList = new ArrayList<>();
        codeDetailDTOList.forEach((CodeDetailDTO codeDetailDTO) -> {
            Code code = modelMapper.map(codeDetailDTO, Code.class);
            code.setCodeGroup(codeGroup);
            codeList.add(code);
        });
        return codeRepository.save(codeList);
    }

    @Override
    public List<Code> updateList(CodeUpdateDTO codeUpdateDTO) {

        // 리턴 객체
        List<Code> result = new ArrayList<>();

        // 업데이트 목록
        List<CodeDetailDTO> updateList = codeUpdateDTO.getUpdateList();

        // 저장할 아이디 목록
        List<String> codeIdList = new ArrayList<>();
        updateList.forEach((CodeDetailDTO codeDetailDTO) -> {

            // 아이디 있는지 체크
            if (StringUtils.isEmpty(codeDetailDTO.getCodeId())) {
                throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                    .withSystemMessage("codeId isEmpty")
                    .withDisplayType(MessageDisplayType.LAYER_POPUP)
                    .build();
            }

            // 아이디 중복 체크
            if (codeIdList.contains(codeDetailDTO.getCodeId())) {
                throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage("this.isDuplicate(codeDetailDTO)")
                    .withDisplayType(MessageDisplayType.LAYER_POPUP)
                    .build();
            }
            codeIdList.add(codeDetailDTO.getCodeId());
        });

        String codeGroupId = codeUpdateDTO.getCodeGroupId();
        CodeGroup codeGroup = codeGroupService.findOne(codeGroupId);

        // 업데이트 할 코드가 모두 존재하는지 확인
        List<Code> existCodeList = codeRepository.findByCodeIdIn(codeIdList);
        if (existCodeList.size() != updateList.size()) {
            throw NotFoundException
                .withSystemMessage("existCodeList.size() != updateList.size()")
                .withDisplayType(MessageDisplayType.LAYER_POPUP)
                .build();
        }

        // 수정
        List<Code> codeList = new ArrayList<>();

        updateList.forEach((CodeDetailDTO codeDetailDTO) -> {
            Code code = modelMapper.map(codeDetailDTO, Code.class);
            code.setCodeGroup(codeGroup);
            codeList.add(code);
        });
        result.addAll(codeRepository.save(codeList));

        // 저장 처리
        result.addAll(this.createList(codeUpdateDTO.getSaveList(), codeUpdateDTO.getCodeGroupId()));

        return result;
    }

    @Override
    public List<Code> findAll() {
        return codeRepository.findAll();
    }

    @Override
    public void delete(Code code) {
        codeRepository.delete(code);
    }

    @Override
    public List<Code> findByCodeGroupDetail(String codeGroupId) {
        List<Code> resultCodeList = new ArrayList<>();

        // 사용중인 코드 그룹에서 코드 그룹 조회
        CodeGroup codeGroup = codeGroupService.findByCodeGroupIdAndUseYn(codeGroupId, true);
        if (codeGroup == null) {
            return resultCodeList;
        }

        // 코드 그룹의 하위 코드 목록 조회
        List<Code> codeList = codeGroup.getCodeList();
        if (codeList == null) {
            return resultCodeList;
        }

        // 현재 언어 기준 코드값 조회
        codeList.forEach((Code code) -> {
            // 사용여부 체크
            if (!code.isUseYn()) {
                return;
            }
            code.setCodeName(this.findCodeNameByCurrentLanguage(code));
            resultCodeList.add(code);
        });

        // 정렬 순번에 따라 오름차순 정렬
        resultCodeList.sort((code1, code2) -> code1.getCodeSortSeq() - code2.getCodeSortSeq());

        return resultCodeList;
    }

    @Override
    public List<CodeDetailDTO> findQueryByCodeGroupId(String codeGroupId, Sort sort) {
        // 코드 페이지 조회
        List<Code> codeList = codeRepository.findQueryByCodeGroupId(codeGroupId, sort);

        List<CodeDetailDTO> codeDetailDTOList = new ArrayList<>();

        // 코드 그룹 아이디 입력
        codeList.forEach((Code code) -> {
            if (code.getCodeGroup() == null) {
                return;
            }

            CodeDetailDTO codeDetailDTO = modelMapper.map(code, CodeDetailDTO.class);
            codeDetailDTO.setCodeGroupId(code.getCodeGroup().getCodeGroupId());
            codeDetailDTOList.add(codeDetailDTO);
        });

        return codeDetailDTOList;
    }

    @Override
    public String findCodeNameByCurrentLanguage(Code code) {
        int index = messageComponent.getLanguageIndex();
        String codeName = "";

        switch (index) {
        case LANGUAGE_INDEX_ZERO: codeName = code.getCodeName1();    break;
        case LANGUAGE_INDEX_ONE:  codeName = code.getCodeName2();    break;
        case LANGUAGE_INDEX_TWO:  codeName = code.getCodeName3();    break;
/* 4번째 언어 추가시, 주석 해제
        case LANGUAGE_INDEX_THREE: codeName = code.getCodeName4();    break;
*/
        default:                  codeName = "";
        }

        return codeName;
    }

    /**
     * <pre>
     * 코드 중복 확인
     * </pre>
     */
    public boolean isDuplicate(List<CodeDetailDTO> codeDetailDTOList) {
        List<String> id = new ArrayList<>();
        boolean isDuplicate = false;
        String codeId = "";
        for (CodeDetailDTO codeDetailDTO : codeDetailDTOList) {
            codeId = codeDetailDTO.getCodeId();
            if (id.contains(codeId)) {
                isDuplicate = true;
                break;
            }
            id.add(codeId);
        }
        return isDuplicate;
    }
}
