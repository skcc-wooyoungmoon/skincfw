/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Mybatis 설정
 * application.properties에 설정된 DB에 따라 mybatis의 MapperLocations 주입을 도와준다.
 * 
 * History
 * - 2018. 9. 19. | in01869 | 최초작성.
 * </pre>
 */
@Slf4j
@Configuration
public class MybatisConfig {

    private static final int DEFAULT_STATEMENT_TIMEOUT = 30;

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private DataSource dataSource;

    @Bean
    public org.apache.ibatis.session.Configuration ibatisConfiguration(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultStatementTimeout(DEFAULT_STATEMENT_TIMEOUT);
        return configuration;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        String databaseProductName = getDatabaseProductName().toString();

        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfiguration(ibatisConfiguration());
        
        if (StringUtils.equalsIgnoreCase(databaseProductName, DatabaseProduct.H2.toString())) {
            
            try {
                Resource[] mapperLocations =  applicationContext.getResources("classpath:mapper/h2/*.xml");
                sessionFactoryBean.setMapperLocations(mapperLocations);
            } catch (IOException e1) {
                log.error("not found h2 DB mapper resources");
            }
        } else if (StringUtils.equalsIgnoreCase(databaseProductName, DatabaseProduct.ORACLE.toString())) {
            try {
                Resource[] mapperLocations =  applicationContext.getResources("classpath:mapper/oracle/*.xml");
                sessionFactoryBean.setMapperLocations(mapperLocations);
            } catch (IOException e1) {
                log.error("not found oracle DB mapper resources");
            }
        } else if (StringUtils.equalsIgnoreCase(databaseProductName, DatabaseProduct.MSSQL.toString())) {
            try {
                Resource[] mapperLocations =  applicationContext.getResources("classpath:mapper/mssql/*.xml");
                sessionFactoryBean.setMapperLocations(mapperLocations);
            } catch (IOException e1) {
                log.error("not found mssql DB mapper resources");
            }
        }

        return sessionFactoryBean.getObject();
    }

    /**
     * <pre>
     * DB connection 설정에 따라 Database Product Name 판단해서 리턴한다.
     * </pre>
     */
    private DatabaseProduct getDatabaseProductName() {
        
        String dataSourceUrl = dataSource.getUrl();

        if(StringUtils.containsIgnoreCase(dataSourceUrl, DatabaseProduct.H2.getName())) {
            return DatabaseProduct.H2;
        } else if (StringUtils.containsIgnoreCase(dataSourceUrl, DatabaseProduct.ORACLE.getName())) {
            return DatabaseProduct.ORACLE;
        } else if (StringUtils.containsIgnoreCase(dataSourceUrl, DatabaseProduct.MSSQL.getName())) {
            return DatabaseProduct.MSSQL;
        }

        return DatabaseProduct.H2;
    }
    
    public enum DatabaseProduct {
        
        H2("h2"), ORACLE("oracle"), MSSQL("sqlserver");
        
        private final String name;

        private DatabaseProduct(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
