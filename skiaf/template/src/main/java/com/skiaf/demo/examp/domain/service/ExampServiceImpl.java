/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.examp.domain.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skiaf.core.util.InterfaceFormater;
import com.skiaf.core.vo.InterfaceFormatDTO;
import com.skiaf.demo.examp.domain.service.dto.ExampFormatDTO;

/**
 * <pre>
 * interface 전문 테스트 서비스 Implement
 *
 * History
 * - 2018. 8. 2. | in01943 | 최초작성.
 * </pre>
 */
@Service
public class ExampServiceImpl implements ExampService {

    @Autowired
    private InterfaceFormater interfaceFormater;

    @Autowired
    ServletContext context; 
    
    @Override
    public ExampFormatDTO readConfigFile(ExampFormatDTO dto) {
        StringBuilder sFileName = new StringBuilder(dto.getConfigFileName());
        //String absolutePath = context.getResourcePaths("aaa");

        if (StringUtils.isBlank(sFileName.toString())) {
            String absolutePath = context.getRealPath("resources" + File.separator + "templates" + File.separator + "skiaf");
            
            absolutePath = absolutePath.replace("webapp" + File.separator, "");
            
            sFileName.append(absolutePath);
            sFileName.append(File.separator + "view");
            sFileName.append(File.separator + "examp");
            sFileName.append(File.separator + "interface-config-sample.dat");

            dto.setConfigFileName(sFileName.toString());
        }

        try {
            dto.setConfigContents(interfaceFormater.readConfigXmlFile(sFileName.toString()));
        } catch (Exception e) {
            dto.setConfigContents(e.toString());
        }

        return dto;
    }

    @Override
    public ExampFormatDTO convertData(ExampFormatDTO dto) {
        StringBuilder returnValue = new StringBuilder();
        String readConfig; // 전문 변환 규칙 xml text 또는 xml 파일 path
        String jsonData; // 수신 받은 buffer에서 JSON으로 변환된 data를 받을 문자열
        StringBuilder readBuffer = new StringBuilder(); // 수신 data를 받아올 buffer (byte단위로 data를 받아 와야 함)
        InterfaceFormatDTO interfaceFormatDTO;

        // 전문 변환용 xml 형식이 파일 인경우
        if ("FILE".equals(dto.getUseConfigType())) {
            readConfig = dto.getConfigFileName();
        } else {
            readConfig = dto.getConfigContents();
        }

        jsonData = dto.getSendJsonData();

        interfaceFormatDTO = new InterfaceFormatDTO();

        try {
            // JSON data를 StringBuilder에 전문 생성
            readBuffer = new StringBuilder(
                    interfaceFormater.convertJsonToBuffer(jsonData, readConfig, interfaceFormatDTO));

            returnValue.setLength(0);
            returnValue.append("전문 Type(" + interfaceFormatDTO.getParsingId());
            returnValue.append(") : " + interfaceFormatDTO.getStatusMessage());
            returnValue.append("\n컬럼수(" + interfaceFormatDTO.getColumnCount());
            returnValue.append("), 총size(" + interfaceFormatDTO.getTotalSize());
            returnValue.append("), 변환건수(" + interfaceFormatDTO.getDataTotalCount());
            returnValue.append("), 변환총size(" + readBuffer.toString().getBytes().length);
            returnValue.append(")");

            dto.setSendStatusCode(Integer.toString(interfaceFormatDTO.getStatusCode())); // 상태 코드
            dto.setSendStatusMessage(returnValue.toString()); // 상태 메시지
            dto.setSendUseConfig(interfaceFormater.getConfigJson(readConfig)); // 사용 변환 환경
            dto.setSendData(readBuffer.toString()); // 변환 data
        } catch (Exception e) {
            dto.setSendStatusMessage(e.toString()); // 상태 메시지
        }

        interfaceFormatDTO = new InterfaceFormatDTO();

        try {
            // 수신된 전문을 JSON 문자열로 변환
            jsonData = interfaceFormater.convertBufferToJson(readBuffer, readConfig, interfaceFormatDTO);

            returnValue.setLength(0);
            returnValue.append("전문 Type(" + interfaceFormatDTO.getParsingId());
            returnValue.append(") : " + interfaceFormatDTO.getStatusMessage());
            returnValue.append("\n컬럼수(" + interfaceFormatDTO.getColumnCount());
            returnValue.append("), 총size(" + interfaceFormatDTO.getTotalSize());
            returnValue.append("), 변환건수(" + interfaceFormatDTO.getDataTotalCount());
            returnValue.append("), 변환총size(" + jsonData.getBytes().length);
            returnValue.append(")");

            dto.setReceiveStatusCode(Integer.toString(interfaceFormatDTO.getStatusCode())); // 상태 코드
            dto.setReceiveStatusMessage(returnValue.toString()); // 상태 메시지
            dto.setReceiveUseConfig(interfaceFormater.getConfigJson(interfaceFormatDTO)); // 사용 변환 환경
            dto.setReceiveData(jsonData); // 변환 data
        } catch (Exception e) {
            dto.setReceiveStatusMessage(e.toString()); // 상태 메시지
        }

        return dto;
    }
}
