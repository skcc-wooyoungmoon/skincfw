/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skiaf.core.exception.InterfaceException;
import com.skiaf.core.vo.InterfaceFormatDTO;

/**
 * <pre>
 * interface 전문 생성 라이버러리
 * 
 * History
 * - 2018. 8. 2. | in01943 | 최초작성.
 * </pre>
 */
@Service
public class InterfaceFormater {

    private static final String NAME_HEADER = "header";
    private static final String NAME_BIZAPPID = "bizappid";
    private static final String NAME_LOGGING = "logging";
    private static final String NAME_PARSING = "parsing";
    private static final String NAME_VALUE = "value";

    private static final String NAME_METADATA = "metadata";
    private static final String NAME_DATA = "data"; // 고정길이 일 경우 나머지 공간 체울 값(char), default: null
    private static final String NAME_FLD = "fld"; //

    private static final String NAME_SEQ = "seq"; // 순번
    private static final String NAME_ID = "id"; // 컬럼 영문
    private static final String NAME_LENGTH = "length"; // 컬럼길이
    private static final String NAME_NAME = "name"; // 컬럼 한글명
    private static final String NAME_TYPE = "type"; // 컬럼 data 타입
    private static final String NAME_PADDING = "padding"; // 컬럼 좌우 정렬 : R:우츨, L: 좌측, default: L
    private static final String NAME_PADDCHAR = "paddchar"; // 고정길이 일 경우 나머지 공간 체울 값(char), default: null

    private static final String NAME_ROW_SEPARATOR = "row_separator"; // 전문 가변길이인경우 ROW 구분자
    private static final String NAME_COL_SEPARATOR = "col_separator"; // 전문 가변길이인경우 COLUMN 구분자

    private static final String VAL_RIGHT = "R"; // right
    // private static final String VAL_LEFT = "L"; // left
    private static final String VAL_FIXED = "FIXED_PARSE"; // 전문 변환 타입 : 고정길이
    private static final String VAL_VARIABLE = "VARIABLE_PARSE"; // 전문 변환 타입 : 가변길이
    private static final String VAL_XML = "XML_PARSE"; // 전문 변환 타입 : XML

    private static final int VAL_INIT = 0; // 초기화
    private static final int VAL_SUCCESS = 1; // 성공
    private static final int VAL_ERROR = -1; // error

    private static final int VAL_RADIX = 16;
    private static final int VAL_TWO = 2;
    private static final int VAL_FOUR = 4;

    /**
     * @Mehtod : setStatus
     * @Description : 전문정보 초기화/setting
     */
    public void setStatus(InterfaceFormatDTO dto, int nVal, String sMsg) {
        dto.setStatusCode(nVal);
        dto.setStatusMessage(sMsg);

        if (nVal == VAL_INIT) {
            dto.setParsingId(VAL_FIXED);

            dto.setColumnCount(0);
            dto.setTotalSize(0);
            dto.setDataTotalCount(0);

            dto.setConfigList(null);
            dto.setDataList(null);
        }

        if (nVal == VAL_ERROR) {
            dto.setConfigList(null);
            dto.setDataList(null);
        }
    }

    /**
     * @Mehtod : convertJsonToBuffer
     * @Description : Json Data를인터페이스 format으로 변환
     * @param value
     *            : 전송할 JSON data
     * @param value
     *            : 전송할 data 환경 file 명
     * @param value
     *            : (옵션 1) String : JSON 파싱처리할 배열의 이름 : 없을 경우 default "data"
     *              (옵션 2) 기타 전송정보 InterfaceFormatDTO
     * @return : json data
     */
    public String convertJsonToBuffer(String inputJson, String configFileName, Object... objs) {
        InterfaceFormatDTO dto = null;
        String jsonDataHeader = null;
        
        HashMap<String, String> map;
        StringBuilder returnBuffer = new StringBuilder();
        ArrayList<HashMap<String, String>> dataListMap = new ArrayList<>();
        
        for (Object obj : objs) {
            if (obj instanceof InterfaceFormatDTO) {
                dto = (InterfaceFormatDTO)obj;
            } else if (obj instanceof String) {
                jsonDataHeader = (String)obj;
            }
        };

        if (dto == null) dto = new InterfaceFormatDTO();
        if ((jsonDataHeader == null)) {
            jsonDataHeader = NAME_DATA;
            inputJson = "{ " + jsonDataHeader + " : " + inputJson + " } ";
        }
        
        setStatus(dto, VAL_INIT, "");

        int columnLength = 0;

        try {
            setConfigXml(dto, configFileName);

            if (dto == null || dto.getConfigList().isEmpty()) {
                throw InterfaceException.withUserMessage("Interface config file error : file=" + configFileName).build();
            }

            JSONObject jsonObject = new JSONObject(inputJson);
            JSONArray jsonArray = jsonObject.getJSONArray(jsonDataHeader);

            if (jsonArray == null) {
                throw InterfaceException.withUserMessage("json data 오류").build();
            }

            for (int idxArray = 0; idxArray < jsonArray.length(); idxArray++) {

                dto.setDataTotalCount(dto.getDataTotalCount() + 1);
                map = new HashMap<>();

                int idxConfig = 1;
                for (HashMap<String, String> mapConfig : dto.getConfigList()) {
                    // data 전송 건수
                    columnLength = Integer.parseInt(mapConfig.get(NAME_LENGTH));

                    if (dto.getParsingId().equals(VAL_FIXED)) {
                        appendDataFix(returnBuffer, columnLength,
                                jsonArray.getJSONObject(idxArray).getString(mapConfig.get(NAME_ID)),
                                mapConfig.get(NAME_PADDCHAR), mapConfig.get(NAME_PADDING));
                    } else if (dto.getParsingId().equals(VAL_VARIABLE)) {
                        appendDataVariable(dto, returnBuffer,
                                jsonArray.getJSONObject(idxArray).getString(mapConfig.get(NAME_ID)), idxConfig);
                    }

                    idxConfig++;

                    map.put(mapConfig.get(NAME_ID),
                            jsonArray.getJSONObject(idxArray).getString(mapConfig.get(NAME_ID)));
                }

                dataListMap.add(map);
            }

            dto.setDataList(dataListMap);

            // XML인 경우 여기에서 처리
            if (VAL_XML.equals(dto.getParsingId())) {
                writeXmlForJson(dto, returnBuffer, jsonDataHeader);
            }

            setStatus(dto, VAL_SUCCESS, "SUCCESS");

        } catch (Exception e) {
            setStatus(dto, VAL_ERROR, e.getMessage());
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        return returnBuffer.toString();
    }

    /**
     * @Mehtod : convertBufferToJson
     * @Description : buffer에서 config에 있는 format으로 Json data 생성 : 해당 모듈로 Xml 생성 할경우  config 파일 불필요
     * @param value
     *            : (옵션) 기타 전송정보 InterfaceFormatDTO
     * @return : JSON String
     */
    public String convertBufferToJson(StringBuilder buffer, Object... objs) {
        InterfaceFormatDTO dto = null;
        
        for (Object obj : objs) {
            if (obj instanceof InterfaceFormatDTO) {
                dto = (InterfaceFormatDTO)obj;
            }
        };

        if (dto == null) dto = new InterfaceFormatDTO();

        return convertBufferToJson(buffer, VAL_XML, NAME_DATA, dto);        
    }

    /**
     * @Mehtod : convertBufferToJson
     * @Description : buffer에서 config에 있는 format으로 Json data 생성 
     * @param value
     *            : data Array Buffer
     * @param value
     *            : 전송할 data 환경 file 명
     * @param value
     *            : (옵션 1) String : JSON data 배열 이름 : 없을 경우 default "data"
     *              (옵션 2) 기타 전송정보 InterfaceFormatDTO
     * @return : JSON String
     */
    /**
     * @param buffer
     * @param configFileName
     * @param jsonDataHeader
     * @param dto
     * @return
     */
    public String convertBufferToJson(StringBuilder buffer, String configFileName, Object... objs) {
        InterfaceFormatDTO dto = null;
        String jsonDataHeader = null;
        
        for (Object obj : objs) {
            if (obj instanceof InterfaceFormatDTO) {
                dto = (InterfaceFormatDTO)obj;
            } else if (obj instanceof String) {
                jsonDataHeader = (String)obj;
            }
        };

        if (dto == null) dto = new InterfaceFormatDTO();
        if ((jsonDataHeader == null)) jsonDataHeader = NAME_DATA;
        
        try {
            setStatus(dto, VAL_INIT, "");

            // 전문 라이버러리를 이용한 XML전송인 경우 config 파일 포함되어있음
            if (VAL_XML.equals(configFileName)) {
                setConfigParse(dto, buffer);
            } else {
                setConfigXml(dto, configFileName);
            }

            if (dto.getConfigList().isEmpty()) {
                throw InterfaceException.withUserMessage("Interface config file error : file=" + configFileName).build();
            }

            // ArrayList hash map 생성 : 고정길이 parse
            if (VAL_FIXED.equals(dto.getParsingId())) {
                parseFixedData(dto, buffer);
                // ArrayList hash map 생성 : 가변길이 parse
            } else if (VAL_VARIABLE.equals(dto.getParsingId())) {
                parseVariableData(dto, buffer);
                // ArrayList hash map 생성 : XML parse
            } else {
                parseXmlData(dto, buffer);
            }

            setStatus(dto, VAL_SUCCESS, "SUCCESS");
        } catch (Exception e) {
            setStatus(dto, VAL_ERROR, e.getMessage());
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        // JSON Data 생성
        return getJsonForList(dto, jsonDataHeader);
    }

    /**
     * @Mehtod : readConfigXmlFile
     * @Description : file(XML)에서 config(전문 변환) JSON 포멧으로 변환
     * @param value
     *            : config XML fil명
     * @return : JSON 포멧 config data
     */
    public String readConfigXmlFile(String fileName) {
        StringBuilder sReturnValue = new StringBuilder("");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String val;
            while ((val = bufferedReader.readLine()) != null) {
                sReturnValue.append(val + "\n");
            }

        } catch (Exception e) {
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        return sReturnValue.toString();
    }

    /**
     * @Mehtod : getConfigJson
     * @Description : XML file에서 config(전문 변환) JSON 포멧으로 변환
     * @param value
     *            : config XML fil명
     * @return : JSON 포멧 config data
     */
    public String getConfigJson(String configFileName) {
        InterfaceFormatDTO dto = new InterfaceFormatDTO();
        setConfigXml(dto, configFileName);
        return getConfigJson(dto);
    }

    /**
     * @param dto
     * @return
     */
    public String getConfigJson(InterfaceFormatDTO dto) {
        StringBuilder sReturnValue = new StringBuilder();
        try {
            if (dto == null || dto.getConfigList().isEmpty())
                return "";

            sReturnValue.append("{\n");
            sReturnValue.append(" " + getQuoteString(NAME_HEADER));
            sReturnValue.append(" : {\n   " + getQuoteString(NAME_BIZAPPID));
            sReturnValue.append(" : {" + getJsonNameValue(NAME_ID, dto.getBizAppId()));
            sReturnValue.append("," + getJsonNameValue(NAME_NAME, dto.getBizAppName()));
            sReturnValue.append("},\n   " + getQuoteString(NAME_LOGGING));
            sReturnValue.append(" : {" + getJsonNameValue(NAME_ID, dto.getLoggingId()));
            sReturnValue.append("," + getJsonNameValue(NAME_VALUE, dto.getLoggingValue()));
            sReturnValue.append("},\n   " + getQuoteString(NAME_PARSING));
            sReturnValue.append(" : {" + getJsonNameValue(NAME_ID, dto.getParsingId()));
            sReturnValue.append("," + getJsonNameValue(NAME_ROW_SEPARATOR, dto.getRowSeparator()));
            sReturnValue.append("," + getJsonNameValue(NAME_COL_SEPARATOR, dto.getColSeparator()));
            sReturnValue
                    .append("}\n }\n " + getQuoteString(NAME_METADATA) + " : { " + getQuoteString(NAME_FLD) + " : [\n");

            for (int i = 0; i < dto.getConfigList().size(); i++) {
                if (i > 0) {
                    sReturnValue.append(",\n");
                }
                sReturnValue.append("  {");
                sReturnValue.append(getJsonNameValue(NAME_SEQ, dto.getConfigList().get(i).get(NAME_SEQ)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_ID, dto.getConfigList().get(i).get(NAME_ID)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_LENGTH, dto.getConfigList().get(i).get(NAME_LENGTH)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_NAME, dto.getConfigList().get(i).get(NAME_NAME)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_TYPE, dto.getConfigList().get(i).get(NAME_TYPE)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_PADDING, dto.getConfigList().get(i).get(NAME_PADDING)) + ",");
                sReturnValue.append(getJsonNameValue(NAME_PADDCHAR, dto.getConfigList().get(i).get(NAME_PADDCHAR)));
                sReturnValue.append("}");
            }
            sReturnValue.append("\n ]}\n");
            sReturnValue.append("}");

        } catch (Exception e) {
            setStatus(dto, VAL_ERROR, e.toString());
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        return sReturnValue.toString();
    }

    /**
     * @Mehtod : parseFixedData
     * @Description : buffer에서 고정길이 data 추출
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : data buffer
     * @return
     */
    private void parseFixedData(InterfaceFormatDTO dto, StringBuilder buffer) {
        ArrayList<HashMap<String, String>> dataListMap = new ArrayList<>();
        HashMap<String, String> map;

        int idxDataPosion = 0;
        int bufLength = buffer.toString().getBytes().length;

        int columnLength = 0;
        String getData = "";

        while ((idxDataPosion < bufLength)) {
            map = new HashMap<>();
            int idxConfig = 1;
            for (HashMap<String, String> mapConfig : dto.getConfigList()) {

                columnLength = Integer.parseInt(mapConfig.get(NAME_LENGTH));

                getData = getBufferFix(buffer, idxDataPosion, columnLength, mapConfig.get(NAME_PADDCHAR),
                        mapConfig.get(NAME_PADDING));

                // 현재 위치 이동
                idxDataPosion += columnLength;

                map.put(mapConfig.get(NAME_ID), getData);

                if (idxDataPosion >= bufLength) {
                    if (idxConfig != dto.getConfigList().size()) {
                        throw InterfaceException.withUserMessage("data column count error").withSystemMessage("idxConfig={}, idxConfig={}", idxConfig, dto.getConfigList().size()).build();
                    }
                    break;
                }
                idxConfig++;
            }

            dto.setDataTotalCount(dto.getDataTotalCount() + 1);
            dataListMap.add(map);
        }

        dto.setDataList(dataListMap);
    }

    /**
     * @Mehtod : getPaddChar
     * @Description : Padding char return
     * @param value
     *            : char
     * @return : char
     */
    private char getPaddChar(String sPaddChar) {
        char cCh = 0;

        // null인경우
        if (StringUtils.isEmpty(sPaddChar)) {
            cCh = ' ';
            // 헥사인 경우
        } else if (sPaddChar.getBytes().length == VAL_FOUR && "0x".equals(sPaddChar.substring(0, VAL_TWO))) {
            cCh = (char) Integer.parseInt(sPaddChar.substring(VAL_TWO), VAL_RADIX);
            // 그외첫 byte로 setting
        } else {
            cCh = sPaddChar.charAt(0);
        }

        return cCh;
    }

    /**
     * @Mehtod : getBufferFix
     * @Description : buffer에서 고정길이 data 추출
     * @param value
     *            : data buffer
     * @param value
     *            : buffer 현재 위치
     * @param value
     *            : 가져올 길이
     * @param value
     *            : Padding 종류
     * @param value
     *            : Padding 문자
     * @return : 고정길이 문자열
     */
    private String getBufferFix(StringBuilder buffer, int idxDataPosion, int len, String sPaddChar, String sPadding) {
        String sReturnValue = "";
        char cCh = getPaddChar(sPaddChar);

        int dataPosion = idxDataPosion + len;
        int nBufSize = buffer.toString().getBytes().length;

        if (dataPosion > nBufSize) {
            throw InterfaceException.withUserMessage("buffer overflow").withSystemMessage("dataPosion={}, nBufSize={}", dataPosion, nBufSize).build();
        }

        int iPaddingCount = len;
        if (VAL_RIGHT.equals(sPadding)) {
            for (int idxValue = (dataPosion - 1); idxValue >= idxDataPosion; idxValue--) {
                // Padding 문자이면 skip
                if (buffer.toString().getBytes()[idxValue] != cCh)
                    break;

                iPaddingCount--;
            }
        } else {
            for (; idxDataPosion < dataPosion; idxDataPosion++) {
                // Padding 문자이면 skip
                if (buffer.toString().getBytes()[idxDataPosion] != cCh)
                    break;

                iPaddingCount--;
            }
        }

        if (iPaddingCount > 0) {
            byte[] bStr = new byte[iPaddingCount];
            System.arraycopy(buffer.toString().getBytes(), idxDataPosion, bStr, 0, iPaddingCount);
            sReturnValue = new String(bStr);
        }

        return sReturnValue;
    }

    /**
     * @Mehtod : parseVariableData
     * @Description : buffer에서 가변길이 data 추출
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : data buffer
     * @return
     */
    private void parseVariableData(InterfaceFormatDTO dto, StringBuilder buffer) {
        ArrayList<HashMap<String, String>> dataListMap = new ArrayList<>();
        HashMap<String, String> map;

        int dataPosion = 0;
        int iIndex;
        int separatorLength;
        int bufLength = buffer.length();
        int idxConfig;

        String getData = "";

        while (dataPosion < bufLength) {
            map = new HashMap<>();

            idxConfig = 1;
            for (HashMap<String, String> mapConfig : dto.getConfigList()) {

                if (idxConfig < dto.getConfigList().size()) {
                    iIndex = buffer.indexOf(dto.getColSeparator(), dataPosion);
                    separatorLength = dto.getColSeparator().length();
                } else {
                    iIndex = buffer.indexOf(dto.getRowSeparator(), dataPosion);
                    separatorLength = dto.getRowSeparator().length();
                }

                if (iIndex >= bufLength) {
                    throw InterfaceException.withUserMessage(
                            "Column Separator not found column_name=" + mapConfig.get(NAME_ID))
                            .withSystemMessage("iIndex={}, bufLength={}", iIndex, bufLength)
                            .build();
                }

                getData = buffer.substring(dataPosion, iIndex);

                map.put(mapConfig.get(NAME_ID), getData);

                // 현재 위치 이동
                dataPosion = iIndex + separatorLength;

                if (dataPosion >= bufLength && idxConfig != dto.getConfigList().size()) {
                    throw InterfaceException.withUserMessage(
                            "data column count error")
                            .withSystemMessage("idxConfig={}, dto.getConfigList.size={}, last data={}", iIndex, bufLength, getData)
                            .build();
                } else if (dataPosion >= bufLength) {
                    break;
                }

                idxConfig++;
            }

            dto.setDataTotalCount(dto.getDataTotalCount() + 1);

            dataListMap.add(map);
        }

        dto.setDataList(dataListMap);
    }

    /**
     * @Mehtod : parseXmlData
     * @Description : buffer에서 고정길이 data 추출
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : data buffer
     * @return
     */
    private void parseXmlData(InterfaceFormatDTO dto, StringBuilder buffer) {
        ArrayList<HashMap<String, String>> dataListMap = null;
        HashMap<String, String> map;
        InputSource str = null;
        DocumentBuilderFactory dbFactory = null;
        DocumentBuilder dBuilder = null;
        Document doc = null;
        XPath xpath = null;
        String expression;
        NodeList nList;

        try {
            dataListMap = new ArrayList<>();
            str = new InputSource(new StringReader(buffer.toString()));
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(str);
            xpath = XPathFactory.newInstance().newXPath();

            expression = "//*/" + NAME_DATA;
            nList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nList.getLength(); i++) {
                map = new HashMap<>();
                for (HashMap<String, String> mapConfig : dto.getConfigList()) {
                    expression = "//*[@" + NAME_SEQ + "=" + i + "]/" + mapConfig.get(NAME_ID);
                    map.put(mapConfig.get(NAME_ID), xpath.compile(expression).evaluate(doc));
                }

                dto.setDataTotalCount(dto.getDataTotalCount() + 1);
                dataListMap.add(map);
            }

            dto.setDataList(dataListMap);
        } catch (Exception e) {
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }
    }

    /**
     * @Mehtod : writeXmlForJson
     * @Description : Json Data를 XML틔 buffer에 write
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : write할 buffer
     * @param value
     *            : JSON 파싱처리할 배열의 이름 : 없을 경우 default "data"
     * @return
     */
    private void writeXmlForJson(InterfaceFormatDTO dto, StringBuilder dataBuffer, String jsonDataHeader) {
        if (dto.getStatusCode() != VAL_ERROR) {
            dataBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            dataBuffer.append("<root>\n");
            dataBuffer.append(" <" + NAME_HEADER + ">\n");

            for (HashMap<String, String> mapConfig : dto.getConfigList()) {
                dataBuffer.append("  <" + NAME_FLD + " " + NAME_SEQ + "=\"" + mapConfig.get(NAME_SEQ) + "\" ");
                dataBuffer.append(NAME_ID + "=\"" + mapConfig.get(NAME_ID) + "\" ");
                dataBuffer.append(NAME_LENGTH + "=\"" + mapConfig.get(NAME_LENGTH) + "\" ");
                dataBuffer.append(NAME_NAME + "=\"" + mapConfig.get(NAME_NAME) + "\"></" + NAME_FLD + ">\n");
            }
            dataBuffer.append(" </" + NAME_HEADER + ">\n");

            int i = 0;
            for (HashMap<String, String> mapData : dto.getDataList()) {
                i++;
                dataBuffer.append("    <" + NAME_DATA + " " + NAME_SEQ + "=\"" + i + "\">\n");
                for (HashMap<String, String> mapConfig : dto.getConfigList()) {
                    dataBuffer.append(
                            "        " + getXmlNameValue(mapConfig.get(NAME_ID), mapData.get(mapConfig.get(NAME_ID))));
                }
                dataBuffer.append("    </" + jsonDataHeader + ">\n");
            }
            dataBuffer.append("</root>");
        }
    }

    /**
     * @Mehtod : appendDataFix
     * @Description : 고정길이 StringBuilder에 Data 채우기
     * @param value
     *            : data를 settin할 버퍼
     * @param value
     *            : setting할 버퍼 전체길이
     * @param value
     *            : value
     * @param value
     *            : 빈곳 채울 char : 1문자, "", 0x20 형태
     * @param value
     *            : L:left / R:Right
     * @return
     */
    private void appendDataFix(StringBuilder dataBuffer, int len, String str, String sPaddChar, String sPadding) {
        if (str.getBytes().length > len) {
            throw InterfaceException.withUserMessage("data overflow").withSystemMessage("str.getBytes().length={}, str.getBytes().length={}", str.getBytes().length, len).build();
        }

        char cCh = getPaddChar(sPaddChar);

        // 오른쪽 Padding : default 왼쪽
        if (VAL_RIGHT.equals(sPadding)) {
            dataBuffer.append(str);
            for (int i = str.getBytes().length; i < len; i++) {
                dataBuffer.append(cCh);
            }
        } else {
            for (int i = str.getBytes().length; i < len; i++) {
                dataBuffer.append(cCh);
            }
            dataBuffer.append(str);
        }
    }

    /**
     * @Mehtod : appendDataVariable
     * @Description : 가변길이 StringBuilder에 Data 채우기
     * @param value
     *            : data를 settin할 버퍼
     * @param value
     *            : value
     * @return
     */
    private void appendDataVariable(InterfaceFormatDTO dto, StringBuilder returnBuffer, String value, int columnIndex) {

        returnBuffer.append(value);

        // 가변길이 인경우 컬럼 구분자 처리
        if (columnIndex < dto.getConfigList().size()) {
            returnBuffer.append(dto.getColSeparator());
        } else {
            // 가변길이 인경우 Row 구분자 처리
            returnBuffer.append(dto.getRowSeparator());
        }

    }

    /**
     * @Mehtod : getJsonForList
     * @Description : Data ArrayList 에서 config(전문 변환) 포멧으로 JSON Data 생성
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : config XML String
     * @return : JSON 포멧 config data
     */
    private String getJsonForList(InterfaceFormatDTO dto, String jsonDataHeader) {
        StringBuilder retrunJson = new StringBuilder();

        retrunJson.append("{ " + getQuoteString(jsonDataHeader) + " : [\n");

        int idxData = 0;
        int idxConfig = 0;
        for (HashMap<String, String> mapData : dto.getDataList()) {

            retrunJson.append("  {");
            idxConfig = 0;
            for (HashMap<String, String> mapConfig : dto.getConfigList()) {
                ++idxConfig;
                if (idxConfig == dto.getConfigList().size()) {
                    retrunJson.append(getJsonNameValue(mapConfig.get(NAME_ID), mapData.get(mapConfig.get(NAME_ID))));
                } else {
                    retrunJson.append(
                            getJsonNameValue(mapConfig.get(NAME_ID), mapData.get(mapConfig.get(NAME_ID))) + ",");
                }
            }
            retrunJson.append("}");

            ++idxData;
            if (idxData == dto.getDataList().size()) {
                retrunJson.append("\n");
            } else {
                retrunJson.append(",\n");
            }
        }

        retrunJson.append(" ]}");
        return retrunJson.toString();
    }

    /**
     * @Mehtod : setConfigXml
     * @Description : XML 파일/String 에서 config(전문 변환)를 list로 변환
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : 파일 path / XML String
     * @return :
     */
    private void setConfigXml(InterfaceFormatDTO dto, String str) {
        Document doc = null;
        try {
            // config가 파일 path인지 String인지 확인
            if (str.indexOf("<metadata>") >= 0) {
                InputSource sXml = new InputSource(new StringReader(str));
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(sXml);
            } else {
                File fXmlFile = new File(str);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(fXmlFile);
            }

        } catch (Exception e) {
            setStatus(dto, VAL_ERROR, e.toString());
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        xmlConfigParse(dto, doc);
    }

    /**
     * @Mehtod : setConfigParse
     * @Description : xml 전문에서 config 추출
     * @param value
     *            : 전뭄 정보
     * @param value
     *            : data buffer
     * @return
     */
    private void setConfigParse(InterfaceFormatDTO dto, StringBuilder buffer) {
        try {
            HashMap<String, String> map;
            InputSource str = new InputSource(new StringReader(buffer.toString()));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(str);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression;
            NodeList nList;

            // 내부 모듈 전송 사용 인 경우 config 자동 생성
            ArrayList<HashMap<String, String>> configListMap = new ArrayList<>();

            expression = "//*/" + NAME_FLD;
            nList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            dto.setColumnCount(0);
            dto.setTotalSize(0);

            Element eElement = null;
            for (int i = 0; i < nList.getLength(); i++) {
                eElement = (Element) nList.item(i);

                map = new HashMap<>();
                map.put(NAME_ID, eElement.getAttribute(NAME_ID));
                map.put(NAME_SEQ, eElement.getAttribute(NAME_SEQ));
                map.put(NAME_NAME, eElement.getAttribute(NAME_NAME));
                map.put(NAME_LENGTH, eElement.getAttribute(NAME_LENGTH));

                dto.setColumnCount(dto.getColumnCount() + 1);
                dto.setTotalSize(dto.getTotalSize() + Integer.parseInt(eElement.getAttribute(NAME_LENGTH)));

                configListMap.add(map);
            }

            dto.setParsingId(VAL_XML);
            dto.setConfigList(configListMap);
        } catch (Exception e) {
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }
    }

    /**
     * @Mehtod : xmlConfigParse
     * @Description : xml config parse
     * @param value
     *            : interface 정보
     * @param value
     *            : config XML Document
     * @return :
     */
    private void xmlConfigParse(InterfaceFormatDTO dto, Document doc) {
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> configListMap = new ArrayList<>();

        XPath xPath = null;
        try {
            xPath = XPathFactory.newInstance().newXPath();

            // bizappid
            XPathExpression expr = xPath.compile("//*/" + NAME_BIZAPPID);
            NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            if (nList.getLength() > 0) {
                Element eElement = (Element) nList.item(0);

                dto.setBizAppId(eElement.getAttribute(NAME_ID));
                dto.setBizAppName(eElement.getAttribute(NAME_NAME));
            }

            // logging
            expr = xPath.compile("//*/" + NAME_LOGGING);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            if (nList.getLength() > 0) {
                Element eElement = (Element) nList.item(0);

                dto.setLoggingId(eElement.getAttribute(NAME_ID));
                dto.setLoggingValue(eElement.getAttribute(NAME_VALUE));
            }

            // parsing
            expr = xPath.compile("//*/" + NAME_PARSING);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            if (nList.getLength() > 0) {
                Element eElement = (Element) nList.item(0);

                dto.setParsingId(eElement.getAttribute(NAME_ID));
                dto.setRowSeparator(eElement.getAttribute(NAME_ROW_SEPARATOR));
                dto.setColSeparator(eElement.getAttribute(NAME_COL_SEPARATOR));
            }

            expr = xPath.compile("//*/" + NAME_FLD);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nList.getLength(); i++) {
                Element eElement = (Element) nList.item(i);

                dto.setColumnCount(dto.getColumnCount() + 1);
                dto.setTotalSize(dto.getTotalSize() + Integer.parseInt(eElement.getAttribute(NAME_LENGTH)));

                map = new HashMap<>();
                map.put(NAME_SEQ, eElement.getAttribute(NAME_SEQ));
                map.put(NAME_ID, eElement.getAttribute(NAME_ID));
                map.put(NAME_LENGTH, eElement.getAttribute(NAME_LENGTH));
                map.put(NAME_NAME, eElement.getAttribute(NAME_NAME));
                map.put(NAME_TYPE, eElement.getAttribute(NAME_TYPE));
                map.put(NAME_PADDING, eElement.getAttribute(NAME_PADDING));
                map.put(NAME_PADDCHAR, eElement.getAttribute(NAME_PADDCHAR));
                configListMap.add(map);
            }
        } catch (Exception e) {
            setStatus(dto, VAL_ERROR, e.toString());
            throw InterfaceException.withUserMessage(e.getMessage()).withSystemMessage(e.toString()).build();
        }

        dto.setConfigList(configListMap);
    }

    /**
     * @Mehtod : getJsonNameValue
     * @Description : name/value JSON 형식으로 변환
     * @param value
     *            : name
     * @param value
     *            : value
     * @return : JSON 형식 문자열 "name" : "value"
     */
    private String getJsonNameValue(String name, String val) {
        return "\"" + name + "\":\"" + val + "\"";
    }

    /**
     * @Mehtod : getXmlNameValue
     * @Description : name/value XML 형식으로 변환
     * @param value
     *            : name
     * @param value
     *            : value
     * @return : XML 형식 문자열 "<name>value</name>"
     */
    private String getXmlNameValue(String name, String val) {
        if (StringUtils.isEmpty(val))
            return "";

        val = val.replaceAll("&", "&amp;");
        val = val.replaceAll("\"", "&quot;");
        val = val.replaceAll("\'", "&apos;");
        val = val.replaceAll("<", "&lt;");
        val = val.replaceAll(">", "&gt;");

        return "<" + name + ">" + val + "</" + name + ">\n";
    }

    /**
     * @Mehtod : getQuoteString
     * @Description : 문자열에 quote 붙이기
     * @param value
     *            : 문자열
     * @return : "value"
     */
    private String getQuoteString(String val) {
        return "\"" + val + "\"";
    }

}
