/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skiaf.AbstractUnitTest;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.program.domain.repository.ProgramRepository;
import com.skiaf.bcm.program.domain.service.ProgramService;
import com.skiaf.bcm.program.domain.service.ProgramServiceImpl;
import com.skiaf.bcm.program.domain.service.dto.ProgramDetailDTO;
import com.skiaf.core.constant.ProgramType;

public class ProgramUnitTests extends AbstractUnitTest {

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void makeBasePath() {
        ProgramServiceImpl programServiceImpl = new ProgramServiceImpl();

        String path = "/api/pie/{id}/like/{fruit}";
        path = programServiceImpl.makeBasePath(path);
        Assert.assertEquals("/api/pie/*/like/*", path);
    }

    @Test
    public void findTopByHttpMethodAndPath() {

        String programId = "PVUNITTEST-01";
        Program testProgram = programRepository.findOne(programId);
        if (testProgram != null) {
            Assert.fail();
        }

        List<ProgramDetailDTO> ProgramSaveDTOList = new ArrayList<>();
        ProgramDetailDTO programSaveDTO = new ProgramDetailDTO();

        programSaveDTO.setProgramId(programId);
        programSaveDTO.setHttpMethod("GET");
        programSaveDTO.setProgramPath("/view/test/{id}");
        programSaveDTO.setProgramName("테스트 화면");
        programSaveDTO.setProgramType(ProgramType.SERVICE.toString());
        programSaveDTO.setProgramDesc("테스트 화면");
        programSaveDTO.setBasePath("/view/test/*");
        ProgramSaveDTOList.add(programSaveDTO);
        programService.create(ProgramSaveDTOList);

        String httpMethod = "get";
        String path1 = "/view/test/{id}";
        String path2 = "/view/test/1";
        Program program = programService.findTopByHttpMethodAndPath(httpMethod, path1, path2);
        if (!program.getProgramId().equals(programId)) {
            Assert.fail();
        }
        programRepository.delete(program);
    }
}

