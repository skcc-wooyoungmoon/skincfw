/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.file.domain.service.AttachFileService;
import com.skiaf.bcm.file.domain.service.FileService;
import com.skiaf.core.constant.Path;
import com.skiaf.core.exception.BizException;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.util.Util;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *
 * History
 * - 2018. 9. 11. | in01866 | 최초작성.
 * </pre>
 */
@Api(tags = "파일")
@Slf4j
@RestController
public class FileController {

    private static final String PDF_EXTENSION = "pdf";
    private static final String USER_MESSAGE_KEY_FILE_DOWNLOAD = "bcm.common.EMPTY";
    private static final String USER_MESSAGE_KEY_EMPTY = "bcm.common.EMPTY";
    private static final String SYSTEM_MESSAGE_EMPTY_ATTACH_FILE = "attachFile == null";
    private static final String SYSTEM_MESSAGE_EMPTY_FILE = "file == null";
    private static final String SYSTEM_MESSAGE_NOT_USED_FILE = "!attachFile.isUseYn()";
    private static final String SYSTEM_MESSAGE_FILE_DOWNLOAD = "file download error";

    @Autowired
    private FileService fileService;

    @Autowired
    private AttachFileService attachFileService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "파일 업로드")
    @PostMapping(value = Path.FILES)
    public RestResponse upload(@RequestParam("file") MultipartFile file) {

        String uploadFileName = fileService.uploadFile(file);

        // 업로드 된 파일아이디 전달
        Map<String, String> result = new HashMap<>();
        result.put("fileId", uploadFileName);
        return new RestResponse(result);
    }

    @ApiOperation(value = "파일 다운로드")
    @GetMapping(value = Path.FILES_DETAIL)
    public void download(@RequestHeader(value="User-Agent", required=false) String userAgent,
            @PathVariable String fileId,
            HttpServletRequest request, HttpServletResponse response) {

        // 첨부 파일 조회
        AttachFile attachFile = attachFileService.findOne(fileId);
        if (attachFile == null) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_ATTACH_FILE).build();
        }
        if (!attachFile.isUseYn()) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_NOT_USED_FILE).build();
        }

        // 첨부파일 정보로 파일 조회
        File file = fileService.downloadFile(attachFile.getFilePath() + attachFile.getFileId());

        // 파일이 있는지 확인
        if (file == null || !file.exists() || file.isDirectory()) {
            throw ValidationException.withUserMessageKey(USER_MESSAGE_KEY_EMPTY)
                .withSystemMessage(SYSTEM_MESSAGE_EMPTY_FILE).build();
        }

        // 원본 파일명
        String originalFile = attachFile.getOriginalFileName();

        try (
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream()
        ) {
            // 브라우저에 따라 파일명 인코딩 변경
            boolean ie = Util.isIEBrowser(userAgent);
            if (ie) {
                originalFile = UriUtils.encode(originalFile, StandardCharsets.UTF_8.name());
            } else {
                originalFile = new String(originalFile.getBytes("UTF-8"), StandardCharsets.ISO_8859_1.name());
            }

            // 첨부파일 응답헤더 설정
            if (attachFile.getFileExtension() != null && attachFile.getFileExtension().equalsIgnoreCase(PDF_EXTENSION)) {

                // pdf 파일이면 바로 보기 설정
                response.setContentType(MediaType.APPLICATION_PDF.toString());
                response.setHeader("Content-Disposition", "inline; filename=" + originalFile);

            } else {
                response.setHeader("Content-Disposition", "attachment; filename=" + originalFile);

            }
            response.setHeader("Content-Length", String.valueOf(file.length()));
            FileCopyUtils.copy(fileInputStream, outputStream);

        } catch (Exception e) {

            log.error(SYSTEM_MESSAGE_FILE_DOWNLOAD, e);
            throw BizException
                .withUserMessage(USER_MESSAGE_KEY_FILE_DOWNLOAD)
                .withSystemMessage(SYSTEM_MESSAGE_FILE_DOWNLOAD).build();

        }
    }
}
