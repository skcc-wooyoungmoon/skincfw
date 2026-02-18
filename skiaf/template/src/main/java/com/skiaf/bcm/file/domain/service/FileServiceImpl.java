/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.dto.AttachFileCreateDTO;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.util.Util;

import cool.graph.cuid.Cuid;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *
 * History
 * - 2018. 7. 23. | in01871 | 최초작성.
 * </pre>
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private static final String SKIAF = "skiaf";
    private static final String USER_MESSAGE_KEY_FIKE_SEND = "bcm.common.FILE_SEND";
    private static final String USER_MESSAGE_KEY_FILE_EXT = "bcm.common.FILE_EXT";
    private static final String USER_MESSAGE_KEY_EMPTY = "bcm.common.EMPTY";
    private static final String SYSTEM_MESSAGE_FILE_EXT = "allowExtension.indexOf(fileExtension) < 0";
    private static final String SYSTEM_MESSAGE_EMPTY_ATTACH_FILE_SAVE_DTO = "attachFileSaveDTO == null";
    private static final String SYSTEM_MESSAGE_FOLDER_NOT_FOUND = "folder == null";
    private static final String SYSTEM_MESSAGE_FILE_NOT_FOUND = "folder == null";

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    @Value("${bcm.attach-file.dir.temp}")
    private String tempRootPath;

    @Value("${bcm.attach-file.dir.save}")
    private String saveRootPath;

    @Value("${bcm.attach-file.dir.delete}")
    private String deleteRootPath;

    @Value("${bcm.attach-file.temp.clear-ago}")
    private int tempClearDayAgo;

    @Value("#{'${bcm.attach-file.allowed-extensions}'.split(',')}")
    private List<String> allowExtensions;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String uploadFile(MultipartFile file) {

        // 파일이 있는지 확인
        if (file == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_FILE_NOT_FOUND).build();
        }

        // 파일명 생성
        String filename = StringUtils.getFilename((StringUtils.cleanPath(file.getOriginalFilename())));

        // 파일 확장자 검사
        String fileExtension = this.getExtension(filename);
        if (allowExtensions.indexOf(fileExtension.toLowerCase()) < 0) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_FILE_EXT)
                .withSystemMessage(SYSTEM_MESSAGE_FILE_EXT).build();
        }

        // 파일 아이디 생성
        String cuid = Cuid.createCuid();

        try {

            // 불필요 기존 임시 파일 제거
            this.cleanTempFile(tempClearDayAgo);

            // 임시 저장위치 생성
            File tempDir = new File(getTempFilePath() + File.separator + cuid);
            if (!tempDir.isDirectory()) {
                tempDir.mkdirs();
            }

            // 임시 저장 위치에 파일 저장
            String savePath = tempDir.getAbsolutePath() + File.separator + filename;
            file.transferTo(new File(savePath));

            log.info("temporary file uploaded [file id : " + cuid + "] : " + savePath);

        } catch (IllegalStateException e) {
            log.error("temporary file upload failed", e);
            throw BizException
                    .withUserMessage(USER_MESSAGE_KEY_FIKE_SEND)
                    .withSystemMessage("file upload fail : IllegalStateException").build();

        } catch (IOException e) {
            log.error("temporary file upload failed", e);
            throw BizException
                    .withUserMessage(USER_MESSAGE_KEY_FIKE_SEND)
                    .withSystemMessage("file upload fail : IOException").build();
        }

        return cuid;
    }

    @Override
    public AttachFile moveSaveLocation(AttachFileCreateDTO attachFileSaveDTO) {

        // 파일정보 검사
        if (attachFileSaveDTO == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_ATTACH_FILE_SAVE_DTO).build();
        }

        // 임시 저장된 파일 경로
        File tempDir = new File(getTempFilePath() + File.separator + attachFileSaveDTO.getFileId());

        // 임시 저장된 경로에서 하위 첫번째 파일 가져오기
        File[] listOfFiles = tempDir.listFiles();
        if (listOfFiles[0] == null || !listOfFiles[0].exists() || listOfFiles[0].isDirectory()) {
            throw BizException.withSystemMessage(SYSTEM_MESSAGE_FOLDER_NOT_FOUND).build();
        }
        File tempFile = listOfFiles[0];

        // 저장할 위치
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String saveDir = File.separator + format.format(now);
        File dir = new File(getSaveFilePath() + saveDir);

        // 존재 하지 않으면 생성
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        // 파일 정보 조사
        String originalFileName = tempFile.getName();
        String fileExtension = this.getExtension(tempFile);
        Long fileSize = tempFile.length();

        // 임시 파일을 지정된 위치로 이동
        String savePath = dir.getAbsolutePath() + File.separator + attachFileSaveDTO.getFileId();

        // 파일 이동
        if (!tempFile.renameTo(new File(savePath))) {

            log.error("failed to move temporary file to save path [file id : " + attachFileSaveDTO.getFileId() + "] : " + savePath);

            // 실패시 처리
            throw BizException
                .withUserMessage(USER_MESSAGE_KEY_FIKE_SEND)
                .withSystemMessage("tempFile.renameTo(new File(savePath)").build();
        }


        log.info("moved temporary file to save path [file id : " + attachFileSaveDTO.getFileId() + "] : " + savePath);

        AttachFile attachFile = modelMapper.map(attachFileSaveDTO, AttachFile.class);
        attachFile.setOriginalFileName(originalFileName);
        attachFile.setFileExtension(fileExtension);
        attachFile.setFileSize(fileSize);
        attachFile.setFilePath(saveDir + File.separator);
        attachFile.setSeverFileName(attachFileSaveDTO.getFileId());

        return attachFile;
    }

    @Override
    public AttachFile moveDeleteLocation(AttachFile attachFile) {

        // 파일정보 검사
        if (attachFile == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_ATTACH_FILE_SAVE_DTO).build();
        }

        // 저장된 파일 경로
        File saveFile = new File(getSaveFilePath() + attachFile.getFilePath() + attachFile.getFileId());

        // 저장된 경로에서 파일 가져오기
        if (saveFile == null || !saveFile.exists() || saveFile.isDirectory()) {
            throw BizException.withSystemMessage(SYSTEM_MESSAGE_FOLDER_NOT_FOUND).build();
        }

        // 삭제 디렉토리 위치
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String deleteDir = File.separator + format.format(now);
        File dir = new File(getDeleteFilePath() + deleteDir);

        // 존재 하지 않으면 생성
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        // 저장된 파일을 지정된 위치로 이동
        String deletePath = dir.getAbsolutePath() + File.separator + attachFile.getFileId();
        if (!saveFile.renameTo(new File(deletePath))) {

            log.error("failed to move saved file to delete path [file id : " + attachFile.getFileId() + "] : " + deletePath);

            // 실패시 처리
            throw BizException
                .withUserMessage(USER_MESSAGE_KEY_FIKE_SEND)
                .withSystemMessage("saveFile.renameTo(new File(deletePath))").build();
        }

        log.info("moved save file to delete path [file id : " + attachFile.getFileId() + "] : " + deletePath);

        // 삭제 정보 입력
        attachFile.setFilePath(deleteDir + File.separator);
        attachFile.setSeverFileName(attachFile.getFileId());

        return attachFile;
    }

    @Override
    public File downloadFile(String filePathWithName) {

        // 전달할 파일 위치 조회
        File file = new File(getSaveFilePath() + filePathWithName);
        if(!file.exists()) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }

        return file;
    }


    @Override
    public void cleanTempFile(int clearDayAgo) {
        // Calendar 생성
        Calendar calandar = Calendar.getInstance();
        long todayMillis = calandar.getTimeInMillis();
        // long oneDayMillis = (long) 24 * 60 * 60 * 1000;
        long oneDayMillis = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

        Calendar fileCalandar = Calendar.getInstance();
        Date fileDate = null;

        // Temp 파일 경로
        File path = new File(this.getTempFilePath());
        File[] listOfFiles = path.listFiles();
        if (listOfFiles == null) {
            return;
        }

        for (int i = 0; i < listOfFiles.length; i++) {

            if (!listOfFiles[i].exists()) {
                continue;
            }

            // 파일의 마지막 수정시간 가져오기
            fileDate = new Date(listOfFiles[i].lastModified());

            // 현재시간과 파일 수정시간 시간차 계산(단위 : 밀리 세컨드)
            fileCalandar.setTime(fileDate);
            long diffMillis = todayMillis - fileCalandar.getTimeInMillis();

            // 날짜로 계산
            int diffDay = (int) (diffMillis / oneDayMillis);

            // 지난 파일 삭제
            if (diffDay >= clearDayAgo) {
                try {
                    FileUtils.forceDelete(listOfFiles[i]);
                    log.info("temporary file removed : " + listOfFiles[i].getAbsolutePath());
                } catch (IOException e) {
                    log.info("failed to remove temporary file : " + listOfFiles[i].getAbsolutePath());
                }

            }
        }
    }

    /**
     * <pre>
     * 확장자를 구한다.
     * </pre>
     */
    private String getExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf('.');
        if (lastIndexOf < 0) {
            return "";
        }
        return name.substring(lastIndexOf + 1);
    }

    /**
     * <pre>
     * 확장자를 구한다.
     * </pre>
     */
    private String getExtension(String fileName) {

        int lastIndexOf = fileName.lastIndexOf('.');
        if (lastIndexOf < 0) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

    /**
     * <pre>
     * 임시 경로
     * </pre>
     */
    private String getTempFilePath() {

        if (StringUtils.isEmpty(tempRootPath)) {
            return System.getProperty(JAVA_IO_TMPDIR) + File.separator + SKIAF;
        }
        return Util.removeLastSeperator(tempRootPath);
    }

    /**
     * <pre>
     * 저장 경로
     * </pre>
     */
    private String getSaveFilePath() {

        if (StringUtils.isEmpty(saveRootPath)) {
            return System.getProperty(JAVA_IO_TMPDIR);
        }
        return Util.removeLastSeperator(saveRootPath);
    }

    /**
     * <pre>
     * 삭제 경로
     * </pre>
     */
    private String getDeleteFilePath() {

        if (StringUtils.isEmpty(deleteRootPath)) {
            return System.getProperty(JAVA_IO_TMPDIR);
        }
        return Util.removeLastSeperator(deleteRootPath);
    }

}
