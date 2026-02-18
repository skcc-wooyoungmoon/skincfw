/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.repository.AttachFileRepository;
import com.skiaf.bcm.file.domain.service.dto.AttachFileCreateDTO;
import com.skiaf.bcm.file.domain.service.dto.AttachFileUpdateDTO;
import com.skiaf.core.exception.ValidationException;

/**
 * <pre>
 *
 * History
 * - 2018. 7. 23. | in01871 | 최초작성.
 * </pre>
 */
@Service
public class AttachFileServiceImpl implements AttachFileService {

    private static final String USER_MESSAGE_KEY_EMPTY = "bcm.common.EMPTY";
    private static final String SYSTEM_MESSAGE_EMPTY_ATTACH_FILE = "attachFile == null";

    @Autowired
    private AttachFileRepository attachFileRepository;

    @Autowired
    private FileService fileService;

    @Override
    public List<AttachFile> findAll() {

        return attachFileRepository.findAll();
    }

    @Override
    @Transactional
    public AttachFile findOne(String fileId) {

        return attachFileRepository.findOne(fileId);
    }

    @Override
    public AttachFile create(AttachFileCreateDTO attachFileSaveDTO) {

        AttachFile attachFile = fileService.moveSaveLocation(attachFileSaveDTO);
        if (attachFile == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_ATTACH_FILE).build();
        }
        attachFileRepository.save(attachFile);

        return attachFile;
    }

    @Override
    public List<AttachFile> createList(List<AttachFileCreateDTO> attachFileSaveDTOList) {

        List<AttachFile> attachFileList = new ArrayList<>();
        attachFileSaveDTOList.forEach((AttachFileCreateDTO attachFileSaveDTO) -> attachFileList.add(this.create(attachFileSaveDTO)));
        return attachFileList;
    }

    @Override
    public AttachFile delete(String fileId) {

        AttachFile attachFile = this.findOne(fileId);
        if (attachFile == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_ATTACH_FILE).build();
        }
        fileService.moveDeleteLocation(attachFile);
        attachFile.setUseYn(false);

        attachFileRepository.save(attachFile);
        return attachFile;
    }

    @Override
    public List<AttachFile> updateList(String targetId, String targetType, AttachFileUpdateDTO attachFileUpdateDTO) {

        if (attachFileUpdateDTO == null) {
            return Collections.emptyList();
        }
        List<String> deleteFileIdList = attachFileUpdateDTO.getDeleteFileIdList();

        // 파일 삭제 처리
        if (deleteFileIdList != null) {
            this.deleteList(deleteFileIdList);

        }

        // 파일 저장 처리
        List<String> saveFileIdList = attachFileUpdateDTO.getSaveFileIdList();
        if (saveFileIdList != null) {
            List<AttachFileCreateDTO> attachFileSaveDTOList = new ArrayList<>();
            saveFileIdList.forEach((String fileId) -> {
                AttachFileCreateDTO attachFileSaveDTO = new AttachFileCreateDTO();
                attachFileSaveDTO.setFileId(fileId);
                attachFileSaveDTO.setTargetId(targetId);
                attachFileSaveDTO.setTargetType(targetType);
                attachFileSaveDTOList.add(attachFileSaveDTO);
            });

            this.createList(attachFileSaveDTOList);
        }

        return this.findByTargetIdAndTargetType(targetId, targetType);
    }

    @Override
    public List<AttachFile> deleteList(List<String> fileIdList) {

        List<AttachFile> attachFileList = new ArrayList<>();
        fileIdList.forEach((String fileId) -> attachFileList.add(this.delete(fileId)));
        return attachFileList;
    }

    @Override
    public List<AttachFile> deleteList(String targetId, String targetType) {

        List<AttachFile> deleteFileList = new ArrayList<>();

        List<AttachFile> attachFileList = this.findByTargetIdAndTargetType(targetId, targetType);
        attachFileList.forEach((AttachFile attachFile) -> deleteFileList.add(this.delete(attachFile.getFileId())));
        return deleteFileList;
    }

    @Override
    public AttachFile findTopByTargetIdAndTargetType(String targetId, String targetType) {

        return attachFileRepository.findTopByTargetIdAndTargetTypeAndUseYnTrue(targetId, targetType);
    }

    @Override
    public List<AttachFile> findByTargetIdAndTargetType(String targetId, String targetType) {

        return attachFileRepository.findByTargetIdAndTargetTypeAndUseYnTrue(targetId, targetType);
    }
}
