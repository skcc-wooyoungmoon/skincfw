/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.element.domain.repository.ElementRepository;
import com.skiaf.bcm.element.domain.service.dto.ElementDetailDTO;
import com.skiaf.bcm.element.domain.service.dto.ElementRoleListDTO;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.role.domain.model.ElementRoleMap;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;
import com.skiaf.core.exception.NotFoundException;

/**
 * <pre>
 * 
 * BCM 프로그램요소 관리 ServiceImpl
 * 
 * History
 * - 2018. 8. 09. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Service
public class ElementServiceImpl implements ElementService {

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Element> findAll() {

        return elementRepository.findAll();
    }

    @Override
    public Element create(String programId, ElementDetailDTO elementDetailDTO) {

        Element element = modelMapper.map(elementDetailDTO, Element.class);
        Program program = new Program();
        program.setProgramId(programId);
        element.setProgram(program);

        return elementRepository.save(element);
    }

    @Override
    public Element findOne(Long elementSeq) {

        Element element = elementRepository.findByElementSeq(elementSeq);

        if (element == null) {
            throw NotFoundException.withSystemMessage("element == null").build();
        }

        return element;
    }

    @Override
    public Element update(String programId, Long elementSeq, ElementDetailDTO elementDetailDTO) {

        Element elementEntity = elementRepository.findByProgramProgramIdAndElementSeqOrderByUpdateDateDesc(programId, elementSeq);

        if (elementEntity == null) {
            throw NotFoundException.withSystemMessage("element == null").build();
        }

        elementEntity.setElementDesc(elementDetailDTO.getElementDesc());
        elementEntity.setUseYn(elementDetailDTO.isUseYn());

        elementRepository.save(elementEntity);

        return elementEntity;
    }

    @Override
    public ElementRoleListDTO findByProgramProgramId(String programId) {

        ElementRoleListDTO elementRoleListDTO = new ElementRoleListDTO();

        List<Element> elementList = elementRepository.findByProgramProgramIdOrderByUpdateDateDesc(programId);
        elementRoleListDTO.setElementList(elementList);

        List<ElementRoleMapDTO> elementRoleMapDTOList = new ArrayList<>();
        elementList.forEach((Element element) -> {

            if (element == null || element.getElementRoleMap() == null || element.getElementRoleMap().isEmpty()) {
                return;
            }

            element.getElementRoleMap().forEach((ElementRoleMap elementRoleMap) -> {
                ElementRoleMapDTO elementRoleMapDTO = modelMapper.map(elementRoleMap, ElementRoleMapDTO.class);
                elementRoleMapDTO.setElementSeq(elementRoleMap.getElement().getElementSeq());
                elementRoleMapDTO.setElementKey(elementRoleMap.getElement().getElementKey());
                elementRoleMapDTO.setElementDesc(elementRoleMap.getElement().getElementDesc());
                elementRoleMapDTO.setRoleId(elementRoleMap.getRole().getRoleId());
                elementRoleMapDTO.setRoleName(elementRoleMap.getRole().getRoleName());
                elementRoleMapDTO.setRoleDesc(elementRoleMap.getRole().getRoleDesc());
                elementRoleMapDTO.setVisibleYn(elementRoleMap.isVisibleYn());
                elementRoleMapDTO.setEnableYn(elementRoleMap.isEnableYn());

                elementRoleMapDTOList.add(elementRoleMapDTO);
            });
        });

        elementRoleListDTO.setElementRoleMapList(elementRoleMapDTOList);
        elementRoleListDTO.setProgram(programService.findOne(programId));

        return elementRoleListDTO;
    }

    @Override
    public Boolean duplicateElement(String programId, String elementKey) {

        Boolean isDuplicate = false;

        List<Element> element = elementRepository.findByProgramProgramIdOrderByUpdateDateDesc(programId);

        for (int i = 0, iLen = element.size(); i < iLen; i++) {
            if (StringUtils.equals(element.get(i).getElementKey(), elementKey)) {
                isDuplicate = true;
                break;
            }
        }

        return isDuplicate;
    }
}
