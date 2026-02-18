/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.program.domain.service.dto.ProgramDetailDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramSearchDTO;
import com.skiaf.bcm.program.domain.service.dto.ProgramUpdateDTO;
import com.skiaf.bcm.role.domain.service.RoleMapService;
import com.skiaf.bcm.role.domain.service.dto.ProgramRoleMapDTO;
import com.skiaf.core.component.MessageComponent;
import com.skiaf.core.constant.Path;
import com.skiaf.core.vo.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * BCM 프로그램 관리 Controller
 *
 * History
 * - 2018. 8. 27. | in01866 | 최초작성.
 * </pre>
 */
@Api(tags = "프로그램 관리")
@RestController
public class ProgramController {

    private static final String BCM_PROGRAM_ASTERISK = "bcm.program.*";

    @Autowired
    private ProgramService programService;

    @Autowired
    private RoleMapService roleMapService;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | VIEW
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @GetMapping(value = Path.VIEW_PROGRAMS)
    public ModelAndView programList() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_PROGRAM_ASTERISK);
        modelAndView.setViewName("skiaf/view/program/program-list");
        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_PROGRAMS_ROLE)
    public ModelAndView programRole() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_PROGRAM_ASTERISK);
        modelAndView.setViewName("skiaf/view/program/program-role");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_PROGRAMS_ROLE_SELECT)
    public ModelAndView programRoleSelect() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skiaf/view/program/program-role-popup");

        return modelAndView;
    }

    @GetMapping(value = Path.VIEW_PROGRAMS_SAVE)
    public ModelAndView programCreate() {

        return new ModelAndView("skiaf/view/program/program-save-popup");
    }

    @GetMapping(value = Path.VIEW_PROGRAMS_ATTACH)
    public ModelAndView programAttach() {

        return new ModelAndView("skiaf/view/program/program-attach-popup");
    }

    @GetMapping(value = Path.VIEW_PROGRAMS_SELECT)
    public ModelAndView programSelectPopup() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MessageComponent.JS_MESSAGES_KEY_PATTERN, BCM_PROGRAM_ASTERISK);
        modelAndView.setViewName("skiaf/view/program/program-select-popup");
        return modelAndView;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | REST API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @ApiOperation(value = "프로그램 목록 조회")
    @GetMapping(value = Path.PROGRAMS)
    public RestResponse findQueryBySearch(ProgramSearchDTO search,
            @PageableDefault(sort = { "updateDate" }, direction = Sort.Direction.DESC) Pageable pageable) {

        if (search.isList()) {
            return new RestResponse(programService.findQueryBySearch(search, pageable.getSort()));
        } else {
            return new RestResponse(programService.findQueryBySearch(search, pageable));
        }
    }

    @ApiOperation(value = "프로그램 등록")
    @PostMapping(value = Path.PROGRAMS)
    public RestResponse create(@RequestBody List<ProgramDetailDTO> programDetailDTOList) {

        return new RestResponse(programService.create(programDetailDTOList));
    }

    @ApiOperation(value = "프로그램 수정")
    @PutMapping(value = Path.PROGRAMS)
    public RestResponse update(@RequestBody ProgramUpdateDTO programDetailDTOList) {

        return new RestResponse(programService.update(programDetailDTOList));
    }

    @ApiOperation(value = "프로그램 조회")
    @GetMapping(value = Path.PROGRAMS_DETAIL)
    public RestResponse findOne(@PathVariable String programId) {

        return new RestResponse(programService.findOne(programId));
    }

    @ApiOperation(value = "프로그램 ID의 시작문자열 동일 여부 체크")
    @GetMapping(value = Path.PROGRAMS_ID_CHECK)
    public RestResponse existByProgramIdStartingWith(@PathVariable String programIdPrefix) {

        return new RestResponse(programService.existsByProgramIdStartingWith(programIdPrefix));
    }

    @ApiOperation(value = "프로그램 ID의 시작문자열이 동일한 프로그램 목록 조회")
    @GetMapping(value = Path.PROGRAMS_STARTING_WITH)
    public RestResponse findByProgramIdStartingWith(@PathVariable String programIdPrefix) {

        return new RestResponse(programService.findByProgramIdStartingWith(programIdPrefix));
    }

    @ApiOperation(value = "프로그램 권한 등록")
    @PostMapping(value = Path.PROGRAM_ROLE_MAPS)
    public RestResponse createRolesByProgramId(@RequestBody List<ProgramRoleMapDTO> programRoleMapDTOList) {

        return new RestResponse(roleMapService.saveRolesByProgramId(programRoleMapDTOList));
    }

    @ApiOperation(value = "프로그램 권한 삭제")
    @DeleteMapping(value = Path.PROGRAM_ROLE_MAPS)
    public RestResponse deleteRolesByProgramId(@RequestBody List<ProgramRoleMapDTO> programRoleMapDTOList) {

        return new RestResponse(roleMapService.deleteRolesByProgramId(programRoleMapDTOList));
    }
}
