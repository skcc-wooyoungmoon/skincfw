/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpSessionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.skiaf.core.constant.Path;
import com.skiaf.core.security.filter.XSSFilter;
import com.skiaf.core.util.encription.PasswordHashing;


/**
 * <pre>
 * 스프링 시큐리티 설정
 *
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Configuration
@EnableWebSecurity // (debug=true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${server.session.timeout}")
    private int sessionTimeout;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 한글 깨짐 관련.
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
        characterEncodingFilter.setForceEncoding(true);
        http.addFilterBefore(characterEncodingFilter, CsrfFilter.class);

        // XSS필터 설정
        /*
         * XssFilter xssFilter = new XssFilter(); http.addFilterAfter(xssFilter,
         * CharacterEncodingFilter.class);
         */

        /*
         * 시큐리티 설정 - 사용자 인증, 권한체크 기능은 별도의 API(로그인)와 AOP에서 처리함
         *      -> 사용자 인증 : SSO와 ID/PASSWORD 기반 둘다를 처리하기에 용이하지 못함
         *      -> 권한체크 : 권한 처리기능이 DB로 설정처리 + URL 매핑시 pathValue 사용으로 인해
         *              , FilterSecurityInterceptor영역에서 실제 실행될 메소드를 찾을수 없음.
         */
        http
            // http 기반으로 인증
            .httpBasic().and()
            // 권한처리 설정
            .authorizeRequests()
                .antMatchers("/**").permitAll().and()
            // 로그인 설정
                // LoginController에서 로그인 페이지 설정
            // 로그인 프로세스 설정
                // LoginService에서 로그인 프로세스 진행
            //로그아웃 설정
            .logout()
                // 로그아웃 Path 설정
                .logoutRequestMatcher(new AntPathRequestMatcher(Path.LOGOUT, HttpMethod.GET.name()))
                // 로그아웃시 세션 삭제 = true
                .invalidateHttpSession(true)
                // 로그아웃 성공시, 이동할 페이지
                .logoutSuccessUrl(Path.VIEW_LOGIN).and()
            // csrf 설정
            .csrf()
                // csrf 예외 URL 설정
                .requireCsrfProtectionMatcher(new NegatedRequestMatcher(new OrRequestMatcher(
                        // GET mothod의 경우, 데이터 변경 처리 로직이 아니기 때문에 예외처리 한다.
                        new AntPathRequestMatcher("/**", HttpMethod.GET.name()),
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/swagger-ui.html")))).and()
            // cors 사용 설정 (Bean에서 내용 구현)
            .cors().and()
            // iframe 접근 제어 설정( h2에서 오류나는 부분때문에 사용. iframe접근제어를 하지 않도록
            .headers()
                .frameOptions().disable()
            ;
    }

    /**
     * <pre>
     * 패스워드 인코더(단방향) 빈
     * </pre>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordHashing();
    }

    /**
     * <pre>
     * 인증객체 빈
     * </pre>
     *
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * <pre>
     * Cors 빈
     * </pre>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // SSO의 호출은 받도록 설정
        configuration.addAllowedOrigin("http://sso.skcorp.com");
        configuration.addAllowedMethod("GET");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * <pre>
     * Session Timeout 설정 빈
     * </pre>
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        HttpSessionEventPublisher httpSessionEventPublisher = new HttpSessionEventPublisher() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                event.getSession().setMaxInactiveInterval(sessionTimeout);
                super.sessionCreated(event);
            }
        };
        return httpSessionEventPublisher;
    }

    /**
     * <pre>
     * XSSFilter 등록 빈
     * </pre>
     */
    //@Bean  // XSSFilter를 사용한다면, 주석 해제
    public FilterRegistrationBean xssFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new XSSFilter());
        registrationBean.addUrlPatterns("/view/*", "/api/*");

        return registrationBean;
    }

}
