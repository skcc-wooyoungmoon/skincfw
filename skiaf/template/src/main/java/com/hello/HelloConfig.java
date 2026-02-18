/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.hello;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <pre>
 * hello 패키지 환경설정 (Entity의 경우 @Profile 로 제어할 수 없기 때문에 Config 제어를 위한 설정)
 * 
 * active 설정이 default가 아닌 경우, Hello Entity를 생성하지 않게 하기 위해 Entity Scan 범위를 변경한다.
 * 이후 hello package가 남아 있는 상태에서 다른 package를 추가하여 사용하는 경우, @EntityScan 및 @EnableJpaRepositories 에도 추가하여 사용한다.
 * 
 * 실제 프로젝트에서는 hello package를 삭제하는 것을 권고한다.
 * 
 * History
 * - 2018. 10. 26. | in01866 | 최초작성.
 * </pre>
 */
@Profile("!default")
@Configuration
@EntityScan(basePackages = {"com.skiaf"})
@EnableJpaRepositories(basePackages = {"com.skiaf"})
public class HelloConfig {

}
