/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.bcm.file.domain.service.AttachFileService;
import com.skiaf.bcm.file.domain.service.dto.AttachFileCreateDTO;
import com.skiaf.bcm.file.domain.service.dto.AttachFileUpdateDTO;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 *
 * History
 * - 2018. 9. 11. | in01866 | 최초작성.
 * </pre>
 */
@Api(tags = "첨부 파일 관리")
@RestController
public class AttachFileController {

    @Autowired
    private AttachFileService attachFileService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "파일 등록")
    @PostMapping(value = Path.ATTACHFILES_DETAIL)
    public RestResponse create(@PathVariable String fileId, @RequestBody AttachFileCreateDTO attachFileSaveDTO) {

        attachFileSaveDTO.setFileId(fileId);
        return new RestResponse(attachFileService.create(attachFileSaveDTO));
    }

    @ApiOperation(value = "파일 삭제 - 미사용으로 전환")
    @DeleteMapping(value = Path.ATTACHFILES_DETAIL)
    public RestResponse delete(@PathVariable String fileId) {

        return new RestResponse(attachFileService.delete(fileId));
    }

    @ApiOperation(value = "파일 목록 조회")
    @GetMapping(value = Path.ATTACHFILES_TARGET)
    public RestResponse findByTargetIdAndTargetType(@PathVariable String targetId, @PathVariable String targetType) {

        return new RestResponse(attachFileService.findByTargetIdAndTargetType(targetId, targetType));
    }

    @ApiOperation(value = "파일 목록 등록")
    @PostMapping(value = Path.ATTACHFILES)
    public RestResponse createList(@RequestBody List<AttachFileCreateDTO> attachFileSaveDTOList) {

        return new RestResponse(attachFileService.createList(attachFileSaveDTOList));
    }

    @ApiOperation(value = "파일 목록 수정")
    @PostMapping(value = Path.ATTACHFILES_TARGET)
    public RestResponse updateList(@PathVariable String targetId, @PathVariable String targetType,
            @RequestBody AttachFileUpdateDTO attachFileUpdateDTO) {

        return new RestResponse(attachFileService.updateList(targetId, targetType, attachFileUpdateDTO));
    }

    @ApiOperation(value = "파일 목록 삭제 - 미사용으로 전환")
    @DeleteMapping(value = Path.ATTACHFILES)
    public RestResponse deleteList(@RequestBody List<String> fileIdList) {

        return new RestResponse(attachFileService.deleteList(fileIdList));
    }

    @ApiOperation(value = "같은 타입정보로 연결된 파일 목록 삭제 - 미사용으로 전환")
    @DeleteMapping(value = Path.ATTACHFILES_TARGET)
    public RestResponse deleteList(@PathVariable String targetId, @PathVariable String targetType) {

        return new RestResponse(attachFileService.deleteList(targetId, targetType));
    }
}
