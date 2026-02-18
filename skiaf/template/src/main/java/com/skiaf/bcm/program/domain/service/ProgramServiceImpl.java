/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.reflect.TypeToken;
import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.AttachFileService;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.repository.ProgramRepository;
import com.skiaf.bcm.program.domain.service.dto.ProgramDetailDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramRoleListDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramSearchDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramUpdateDTO;
import com.skiaf.bcm.role.domain.model.ProgramRoleMap;
import com.skiaf.bcm.role.domain.service.dto.ProgramRoleMapDTO;
import com.skiaf.core.constant.FileType;
import com.skiaf.core.exception.NotFoundException;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * BCM 프로그램 Service Implements
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    private static final String USER_MESSAGE_KEY_DUPLICATE = "bcm.common.DUPLICATE";
    private static final String SYSTEM_MESSAGE_DUPLICATE_PROGRAM_DETAIL_DTO_LIST = "this.isDuplicate(ProgramDetailDTOList)";
    private static final String SYSTEM_MESSAGE_DUPLICATE_UPDATE_DTO_LIST = "this.isDuplicate(updateDTOList)";
    private static final String SYSTEM_MESSAGE_ID_VALIDATION = "this.isValidId(programDetailDTO.getProgramId())";

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Program> findAll() {
        return programRepository.findAll();
    }

    @Override
    public Program findOne(String programId) {
        Program program = programRepository.findOne(programId);
        if (program == null) {
            throw NotFoundException.withSystemMessage("program == null").build();
        }
        return program;
    }

    @Override
    public Program findTopByHttpMethodAndPath(String httpMethod, String basePath, String reqeustPath) {
        // 컨트롤러 매칭 URL 기준 프로그램 조회
        String path1 = this.makeBasePath(basePath);
        Program program = new Program();
        program = programRepository.findTopByHttpMethodIgnoreCaseAndBasePathIgnoreCase(httpMethod, path1);

        // 조회 됐는지 확인
        if (program == null) {

            // request URl 기준 프로그램 조회
            program = new Program();
            String path2 = this.makeBasePath(reqeustPath);
            program = programRepository.findTopByHttpMethodIgnoreCaseAndBasePathIgnoreCase(httpMethod, path2);
        }

        if (program == null) {
            return program;
        }

        // 도움말 파일 추가
        AttachFile attachFile = attachFileService.findTopByTargetIdAndTargetType(program.getProgramId(), FileType.PROGRAM.toString());
        if (attachFile != null) {
            program.setAttachFile(attachFile);
        }
        return program;
    }

    @Override
    public boolean existsByProgramIdStartingWith(String programIdPrefix) {

        // 모든 프로그램은 "-" 와 숫자가 마지막에 붙는다.
        programIdPrefix = programIdPrefix + "-";
        return programRepository.existsByProgramIdStartingWith(programIdPrefix);
    }

    @Override
    public ProgramRoleListDTO findByProgramIdStartingWith(String programIdPrefix) {

        // 모든 프로그램은 "-" 와 숫자가 마지막에 붙는다.
        programIdPrefix = programIdPrefix + "-";

        ProgramRoleListDTO programDetailDTO = new ProgramRoleListDTO();

        // 프로그램 목록
        List<Program> programList = programRepository.findByProgramIdStartingWithOrderByUpdateDateDesc(programIdPrefix);
        programDetailDTO.setProgramList(programList);

        // 프로그램 권한 맵 목록
        List<ProgramRoleMapDTO> programRoleMapDTOList = new ArrayList<>();
        programList.forEach((Program program) -> {
            if (program == null) {
                return;
            }

            AttachFile attachFile = attachFileService.findTopByTargetIdAndTargetType(program.getProgramId(), FileType.PROGRAM.toString());
            if (attachFile != null) {
                program.setAttachFile(attachFile);
            }

            if (program.getProgramRoleMap() == null || program.getProgramRoleMap().isEmpty()) {
                return;
            }

            program.getProgramRoleMap().forEach((ProgramRoleMap programRoleMap) -> {
                ProgramRoleMapDTO programRoleMapDTO = modelMapper.map(programRoleMap, ProgramRoleMapDTO.class);
                programRoleMapDTO.setProgramId(programRoleMap.getProgram().getProgramId());
                programRoleMapDTO.setProgramName(programRoleMap.getProgram().getProgramName());
                programRoleMapDTO.setProgramPath(programRoleMap.getProgram().getProgramPath());
                programRoleMapDTO.setProgramType(programRoleMap.getProgram().getProgramType());
                programRoleMapDTO.setHttpMethod(programRoleMap.getProgram().getHttpMethod());
                programRoleMapDTO.setRoleId(programRoleMap.getRole().getRoleId());
                programRoleMapDTO.setRoleName(programRoleMap.getRole().getRoleName());

                programRoleMapDTOList.add(programRoleMapDTO);
            });
        });
        programDetailDTO.setProgramRoleMapList(programRoleMapDTOList);

        return programDetailDTO;
    }

    @Override
    @Transactional
    public List<Program> create(List<ProgramDetailDTO> programDetailDTOList) {

        List<String> programIdList = new ArrayList<>();
        programDetailDTOList.forEach((ProgramDetailDTO programDetailDTO) -> {

            if (!this.isValidId(programDetailDTO.getProgramId())) {
                throw ValidationException.withSystemMessage(SYSTEM_MESSAGE_ID_VALIDATION).build();
            }

            // 존재하는 아이디 인지 확인
            programIdList.add(programDetailDTO.getProgramId());

            // basePath 처리
            String path = programDetailDTO.getProgramPath();
            if (StringUtils.isBlank(path)) {
                throw NotFoundException.withSystemMessage("ProgramDetailDTO.getProgramPath() == null").build();
            }
            programDetailDTO.setBasePath(this.makeBasePath(path));
        });

        // 아이디를 중복으로 입력했는지 확인
        if (this.isDuplicate(programDetailDTOList)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage(SYSTEM_MESSAGE_DUPLICATE_PROGRAM_DETAIL_DTO_LIST).build();
        }

        if (CollectionUtils.isEmpty(programDetailDTOList)) {
            return new ArrayList<Program>();
        }

        // 데이터 검증
        // Base Id 추출
        String firstProgramId = programDetailDTOList.get(0).getProgramId();
        String baseId = firstProgramId;
        if (firstProgramId.lastIndexOf('-') >= 0) {
            baseId = firstProgramId.substring(0, firstProgramId.lastIndexOf('-'));
        }

        // 기존 Base Id로 저장된게 있는지 조회
        List<Program> existProgramList = programRepository.findByProgramIdStartingWithOrderByUpdateDateDesc(baseId);

        // 추가 할 프로그램 Method, Path 중복 검증
        Map<String, ProgramDetailDTO> validCheckMap = new HashMap<>();

        // 기존 저장된 내용과 업데이트 내용을 함께 조사
        existProgramList.forEach((Program existItem) -> validCheckMap.put(existItem.getProgramId(), modelMapper.map(existItem, ProgramDetailDTO.class)));

        programDetailDTOList.forEach((ProgramDetailDTO updateItem) -> {

            // 프로그램이 저장된 적이 있는지 확인, 없으면 업데이트 처리 안함
            if (validCheckMap.containsKey(updateItem.getProgramId())) {
                throw NotFoundException.withSystemMessage("validCheckMap.containsKey(updateItem.getProgramId()").build();
            }
            validCheckMap.put(updateItem.getProgramId(), updateItem);
        });

        // 검사할 목록 생성
        List<ProgramDetailDTO> validCheckList = new ArrayList<>();
        validCheckMap.forEach((k, v) -> validCheckList.add(v));

        // Method, Path 중복 검사
        if (this.isDuplicate(validCheckList)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE).withSystemMessage(SYSTEM_MESSAGE_DUPLICATE_UPDATE_DTO_LIST).build();
        }

        // 저장용 객체 생성
        java.lang.reflect.Type programListType = new TypeToken<List<Program>>() {
            private static final long serialVersionUID = -8215233518745920839L;
        }.getType();

        List<Program> programList = modelMapper.map(programDetailDTOList, programListType);

        return programRepository.save(programList);
    }

    @Override
    public List<Program> update(ProgramUpdateDTO programUpdateDTO) {

        // 저장 결과용
        List<Program> result = null;

        // 프로그램 업데이트 먼저 진행
        List<ProgramDetailDTO> updateList = programUpdateDTO.getUpdateList();

        // 업데이트 목록이 없으면 신규 프로그램만 저장
        if (updateList == null || updateList.isEmpty()) {
            return this.create(programUpdateDTO.getCreateList());
        }

        updateList.forEach((ProgramDetailDTO programDetailDTO) -> {
            String path = programDetailDTO.getProgramPath();
            if (StringUtils.isBlank(path)) {
                throw NotFoundException.withSystemMessage("ProgramDetailDTO.getProgramPath() == null").build();
            }
            programDetailDTO.setBasePath(this.makeBasePath(path));
        });

        // 중복 ID 입력한게 있는지 확인
        if (this.isDuplicate(updateList)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage(SYSTEM_MESSAGE_DUPLICATE_UPDATE_DTO_LIST).build();
        }

        List<Program> programList = null;

        // 업데이트 데이터 검증
        // Base Id 추출
        String firstProgramId = updateList.get(0).getProgramId();
        String baseId = firstProgramId;
        if (firstProgramId.lastIndexOf('-') >= 0) {
            baseId = firstProgramId.substring(0, firstProgramId.lastIndexOf('-'));
        }

        // 기존 Base Id로 저장된게 있는지 조회
        List<Program> existProgramList = programRepository.findByProgramIdStartingWithOrderByUpdateDateDesc(baseId);
        if (existProgramList == null || existProgramList.isEmpty()) {
            throw NotFoundException.withSystemMessage("existProgramList.isEmpty").build();
        }

        // 업데이트 할 프로그램 Method, Path 중복 검증
        Map<String, ProgramDetailDTO> validCheckMap = new HashMap<>();

        // 기존 저장된 내용과 업데이트 내용을 함께 조사
        existProgramList.forEach((Program existItem) -> validCheckMap.put(existItem.getProgramId(), modelMapper.map(existItem, ProgramDetailDTO.class)));

        updateList.forEach((ProgramDetailDTO updateItem) -> {

            // 프로그램이 저장된 적이 있는지 확인, 없으면 업데이트 처리 안함
            if (!validCheckMap.containsKey(updateItem.getProgramId())) {
                throw NotFoundException.withSystemMessage("!validCheckMap.containsKey(updateItem.getProgramId()").build();
            }
            validCheckMap.put(updateItem.getProgramId(), updateItem);
        });

        // 검사할 목록 생성
        List<ProgramDetailDTO> validCheckList = new ArrayList<>();
        validCheckMap.forEach((k, v) -> validCheckList.add(v));

        // Method, Path 중복 검사
        if (this.isDuplicate(validCheckList)) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_DUPLICATE)
                    .withSystemMessage(SYSTEM_MESSAGE_DUPLICATE_UPDATE_DTO_LIST).build();
        }

        // 업데이트 프로그램 저장
        java.lang.reflect.Type programListType = new TypeToken<List<Program>>() {
            private static final long serialVersionUID = -2122072738644840440L;
        }.getType();

        programList = modelMapper.map(updateList, programListType);
        programRepository.save(programList);

        // 신규 프로그램 저장
        result = this.create(programUpdateDTO.getCreateList());

        // 신규 및 업데이트 프로그램 리턴
        result.addAll(programList);
        return result;
    }

    @Override
    public PageDTO<Program> findQueryBySearch(ProgramSearchDTO search, Pageable pageable) {
        return programRepository.findQueryBySearch(search, pageable);
    }

    @Override
    public List<Program> findQueryBySearch(ProgramSearchDTO search, Sort sort) {
        return programRepository.findQueryBySearch(search, sort);
    }

    /**
     * <pre>
     * 아이디에 dash 문자가 한개만 존재하는지 확인
     * </pre>
     */
    @Transactional
    public boolean isValidId(String id) {
        int count = StringUtils.countMatches(id, '-');
        return count == 1;
    }

    /**
     * <pre>
     * 중복 아이디 또는 중복 basePath 가 있는지 체크
     * </pre>
     */
    @Transactional
    public boolean isDuplicate(List<ProgramDetailDTO> programDetailDTOList) {
        List<String> idList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();

        boolean isDuplicate = false;

        String programId = "";
        String basePathWithMethod = "";
        for (ProgramDetailDTO programDetailDTO : programDetailDTOList) {
            programId = programDetailDTO.getProgramId();
            basePathWithMethod = programDetailDTO.getHttpMethod() + programDetailDTO.getBasePath();

            if (idList.contains(programId) || pathList.contains(basePathWithMethod)) {
                isDuplicate = true;
                break;
            }

            idList.add(programId);
            pathList.add(basePathWithMethod);
        }
        return isDuplicate;
    }

    /**
     * <pre>
     * Base Path 생성
     * </pre>
     */
    @Transactional
    public String makeBasePath(String path) {
        if (path == null || StringUtils.isEmpty(path)) {
            return path;
        }
        int preIndex = path.indexOf('{');
        int postIndex = path.indexOf('}');
        if (preIndex < 0 || postIndex < 0) {
            return path;
        }
        path = path.substring(0, preIndex) + '*' + path.substring(postIndex + 1);
        return this.makeBasePath(path);
    }
}
