/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 *
 */
package com.skiaf.core.security.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.io.ByteStreams;
import com.skiaf.core.constant.Path;

/**
 * <pre>
 * XSS 필터 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public class XSSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String contentType = request.getHeader("Content-Type");

        boolean isAjax = (contentType != null && contentType.toLowerCase().indexOf("application/json") != -1);
        boolean isMultipart = (contentType != null && contentType.toLowerCase().indexOf("multipart/form-data") != -1);

        // 로그인의 requestBoby 파라미터일 경우, 별도의 xss 처리를 하지 않는다.(아이디/ 비밀번호 변조 가능성으로 인해)
        boolean isPassXss = Path.LOGIN.equals(request.getServletPath())
                && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod());

        if (!isAjax || isMultipart) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(new RequestWrapper(request, isPassXss), response);
        }

    }

    public static class RequestWrapper extends HttpServletRequestWrapper {

        private byte[] b;

        public RequestWrapper(HttpServletRequest request, boolean isPassXss) throws IOException {
            super(request);
            InputStream is = super.getInputStream();
            b = ByteStreams.toByteArray(is);
            String requestStringBody = new String(b);
            
            // request body 에 해당 하는 byte[] 을 바꿔주고 getInputStream 2번이 안되는 문제를 해결
            if (StringUtils.isNotEmpty(requestStringBody)) {
                if (isPassXss) {
                    b = requestStringBody.getBytes();
                } else {
                    b = xssFilter(requestStringBody).getBytes();
                }
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bis = new ByteArrayInputStream(b);
            return new ServletInputStreamImpl(bis);
        }

        static class ServletInputStreamImpl extends ServletInputStream {
            private InputStream is;

            public ServletInputStreamImpl(InputStream bis) {
                is = bis;
            }

            public int read() throws IOException {
                return is.read();
            }

            @Override
            public int read(byte[] b) throws IOException {
                return is.read(b);
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 
            }
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return new String[0];
            }

            int count = values.length;
            String[] encodeValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodeValues[i] = xssFilter(values[i]);
            }
            return encodeValues;
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }
            return xssFilter(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null) {
                return null;
            }
            return xssFilter(value);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            Set<String> keySet = parameterMap.keySet();
            Iterator<String> itrator = keySet.iterator();
            Map<String, String[]> cleanMap = new HashMap<>();

            String key = "";
            String[] paramValues = null;
            int count = 0;
            String[] encodedValues = null;
            while (itrator.hasNext()) {
                key = itrator.next();
                paramValues = parameterMap.get(key);
                if (paramValues == null) {
                    cleanMap.put(key, paramValues);
                } else {
                    count = paramValues.length;
                    encodedValues = new String[count];
                    for (int i = 0; i < count; i++) {
                        encodedValues[i] = xssFilter(paramValues[i]);
                    }
                    cleanMap.put(key, encodedValues);
                }
            }

            return cleanMap;
        }

        public String xssFilter(String str) {
            String result = str;
            
            result = XSSFilter.basicEscape(result);      
            result = XSSFilter.keywordReplace(result);  // 필터링 수준에 따라, 선택적으로 적용.

            return result;
        }
    }

    /**
     * <pre>
     * XSS 방어를 위한 기본 escape
     * </pre>
     */
    public static String basicEscape(String str) {
        if (str == null) return null;
        
        String result = str;
        // result = result.replaceAll("&", "&amp;");    // SKI 개발 보안 Guideline V2.1에는 해당되지 않는 처리.
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        result = result.replaceAll("\0"," "); 
        // result = result.replaceAll("#", "&#35;");    // SKI 개발 보안 Guideline V2.1에는 해당되지 않는 처리.
        // result = result.replaceAll("\\(", "&#40;");  // SKI 개발 보안 Guideline V2.1에는 해당되지 않는 처리.
        // result = result.replaceAll("\\)", "&#41;");  // SKI 개발 보안 Guideline V2.1에는 해당되지 않는 처리.
        // result = result.replaceAll("\'", "&apos;");  // 이 처리는 JSON 구조에 영향을 줄 수 있음.
        // result = result.replaceAll("\"", "&quot;");  // 이 처리는 JSON 구조에 영향을 줄 수 있음.        

        return result;        
    }
    
    /**
     * <pre>
     * XSS 방어를 keyword들을 replace
     * keyword 들은 SKI 개발 보안 Guideline V2.1 에 명시되어 있음.
     * </pre>
     */
    public static String keywordReplace(String str) {
        if (str == null) return null;
        
        String keywords = "<script>, %3Cscript, %3Ealert, ja%0Av%0Aa%0As%0Ac%0Aript, JaVaScRiPt, ScRiPt%20%0a%0d, JaVaScRiPt, ScRiPt%20%0a%0d, javascript, vbscript, expression,applet, meta, xml, blink, link, style, script, embed, object, iframe, frame, frameset, ilayer, layer, bgsound, title, base, eval, innerHTML, charset, document, string, create, append, binding, alert, msgbox, refresh, embed, ilayer, applet, cookie, void, href, nabort, @import, \\+ADw, \\+AD4, aim:, %0da=eval, allowscriptaccess, xmlns:html, <html xmlns, xmlns:html=, http-equiv=refresh, http-equiv=refresh, x-scriptlet, echo\\(, 0%0d%0a%00, moz-binding, res://, #exec, background=, &#x, %u0, string.fromcharcode, firefoxurl, <br size=, list-style-image, acunetix_wvs, wvs-xss, lowsrc, dynsrc, behavior, activexobject, microsoft.xmlhttp, clsid:cafeefac-dec7-0000-0000-abcdeffedcba, application/npruntime-scriptable-plugin;deploymenttoolkit, onactivae, onafterprint, onafterupdate, onbefore, onbeforeactivate, onbeforecopy, onbeforecut, onbeforedeactivate, onbeforeeditfocus, onbeforepaste, onbeforeprint, onbeforeunload, onbeforeupdate, onblur,  onbounce, oncellchange, onchange, onclick, oncontextmenu, oncontrolselect, oncopy, oncut, ondataavailable, ondatasetchanged, ondatasetcomplete, ondblclick, ondeactivate, ondrag, ondragend, ondragenter, ondragleave, ondragover, ondragstart, ondrop, onerror,  onerrorupdate, onfilterchange, onfinish, onfocus, onfocusin, onfocusout,onhelp, onkeydown, onkeypress, onkeyup, onlayoutcomplete, onload, onlosecapture, onmousedown, onmouseenter, onmouseleave, onmousemove, onmouseout, onmouseover, onmouseup, onmousewheel, onmove, onmoveend, onmovestart, onpaste, onpropertychange, onreadystatechange, onreset, onresize, onresizeend, onresizestart, onrowenter, onrowexit, onrowsdelete, onrowsinserted, onscroll, onselect, onselectionchange, onselectstart, onstart,onstop, onsubmit, onunload "; // 필터링
        keywords = keywords.replaceAll(" ", "");
        String[] st = keywords.split(",");
        
        String result = str;
        for (String keyword : st) {
            result = result.replaceAll(keyword, "_" + keyword + "_");
        }

        return result;
    }    
}
