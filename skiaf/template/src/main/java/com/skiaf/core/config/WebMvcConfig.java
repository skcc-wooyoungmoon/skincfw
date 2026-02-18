/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.nhncorp.lucy.security.xss.XssSaxFilter;
import com.skiaf.bcm.i18n.domain.service.MessageService;
import com.skiaf.core.constant.CommonConstant;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 * Web MVC 설정
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /** 메시지 서비스 */
    @Autowired
    MessageService messageService;

    /** 기본 언어 코드 */
    @Value("${bcm.language.default}")
    private String langDefaultCode;

    /** 사용하는 언어 코드 */
    @Value("${bcm.language.support}")
    private String[] langSupportedCodes;

    /** 언어 설정용 쿠키명 */
    @Value("${bcm.language.cookie.name}")
    private String langCookieName;

    /** 언어 설정용 쿠키 만료기간 */
    @Value("${bcm.language.cookie.maxage}")
    private Integer langCookieMaxAge;

    /** Thymeleaf 캐시 설정 */
    @Value("${bcm.thymeleaf.cache}")
    private boolean isThymeleafCache;

    @Value("${bcm.swagger.enabled}")
    private boolean isSwaggerEnabled;

    /**
     * Resource 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // Swagger UI 경로 설정
        if (isSwaggerEnabled) { // default 와 dev 프로파일에서만 swagger 동작.
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            // 라이브러리 내 리소스 파일 접근 설정
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }

        // 각종 리소스 파일 접근 설정
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
    }

    /**
     * 쿠키를 이용한 Locale 설정
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.forLanguageTag(langDefaultCode));
        cookieLocaleResolver.setCookieName(langCookieName);
        cookieLocaleResolver.setCookieMaxAge(langCookieMaxAge);

        return cookieLocaleResolver;
    }

    /**
     * <pre>
     * LucyXssFilter 등록 빈
     * </pre>
     */
    @Bean
    public LucyXssFilter lucyXssFilter() {
        //LucyXssFilter lucyXssFilter = XssSaxFilter.getInstance("config/lucy-xss-sax.xml");

        // 디버그 주석문 표기 하지 않음.
        LucyXssFilter lucyXssFilter = XssSaxFilter.getInstance("config/lucy-xss-sax.xml",true);
        return lucyXssFilter;
    }

    /**
     * <pre>
     * MessageSource 설정.
     * message값은 db에서 먼저 찾고, 그 다음으로는 messages properties에서 찾는다.
     * </pre>
     */
    @Bean
    public MessageSource messageSource() {

        // 서버 기본 locale 설정
        Locale.setDefault(new Locale(langDefaultCode));

        KeySearchableResourceBundleMessageSource bundleMessageSource = new KeySearchableResourceBundleMessageSource();
        bundleMessageSource.setBasename("messages/messages");

        DatabaseMessageSource dbMesageSource = new DatabaseMessageSource(messageService, langSupportedCodes);
        dbMesageSource.setParentMessageSource(bundleMessageSource);
        return dbMesageSource;
    }

    /**
     * View Resolver 설정 : Thymeleaf
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(isThymeleafCache);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }
    
    /**
     * <pre>
     * JSP ViewResolver 설정 - Multi 설정 가능
     * </pre>
     */
    
    // JSP 설정 가이드
    /*@Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        
        // .jsp 파일은 thymeleaf 파일처럼 resources 안의 경로로 잡을수 없다. 
        // 반드시 webapp > WEB-INF 예하 폴더에 관리 되야 한다.
        viewResolver.setPrefix("/WEB-INF/");
        
        // contoller의 modelAndView 설정하는 viewName은 jsp/*로 시작할 경우에만 jsp view resolover로 인식한다. 
        viewResolver.setViewNames("jsp/*");
        
        // thymeleaf view resolver보다 먼저 동작하도록 order를 설정한다.
        viewResolver.setOrder(1);

        // jstl 사용을 위한 뷰클래스 설정
        viewResolver.setViewClass(JstlView.class);
        
        viewResolver.setSuffix(".jsp");
        
        return viewResolver;
    }*/

    /**
     * 인코딩 필터 설정
     */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    /**
     * DispatcherServlet 설정
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }

    /**
     * ModelMapper 설정
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    /**
     * <pre>
     * ObjectMapper 설정
     * </pre>
     */
    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
        bean.setIndentOutput(true);
        bean.setFailOnUnknownProperties(false);
        bean.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        /*
         * Date format 설정 및 로케이션 설정 (안하면 UTC정보로 client에 전달됨.)
         */
        //bean.setSimpleDateFormat(CommonConstant.TIME_PATTERN_MILLIS);
        bean.setSimpleDateFormat(CommonConstant.TIME_PATTERN);
        bean.setLocale(LocaleContextHolder.getLocale());

        bean.afterPropertiesSet();

        ObjectMapper objectMapper = bean.getObject();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    /**
     * <pre>
     * 메세지 컨버터 설정 (Server -> Client 시 message에 변환시 objectMapper() 사용하도록)
     * </pre>
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    /**
     * <pre>
     * 메세지 컨버터 설정 ( Server -> Client 시 message에 변환시 objectMapper() 사용하도록 )
     * </pre>
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    /**
     * <pre>
     * Favicon 설정
     * </pre>
     */
    @Bean
    public SimpleUrlHandlerMapping faviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/favicon.ico", faviconRequestHandler()));
        return mapping;
    }
    @Bean
    public ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        ClassPathResource classPathResource = new ClassPathResource("static/skiaf/favicon/");
        List<Resource> locations = Arrays.asList(classPathResource);
        requestHandler.setLocations(locations);
        return requestHandler;
    }
}
