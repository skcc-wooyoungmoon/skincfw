/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.cache.service;

import java.util.List;

import com.skiaf.demo.cache.cache.service.dto.CacheInfoDTO;
import com.skiaf.demo.cache.cache.service.dto.CacheMenualInputDTO;

public interface CacheService {

    /**
     * 전체 캐시를 조회한다.
     * 
     * @return
     */
    public List<CacheInfoDTO> getCachedItemList();

    /**
     * 캐시를 조회한다.
     * 
     * @param key
     * @return
     */
    public CacheInfoDTO getCache(String value, String key);

    /**
     * 전체 캐시를 삭제한다.
     * 
     * @param id
     */
    public void delCachedAll();

    /**
     * <pre>
     * 캐시를 삭제한다.
     * </pre>
     */
    public void delCachedCache(String value, String key);

    /**
     * 캐시 수동 등록
     * 
     * @param cacheDTO
     * @return
     */
    public CacheInfoDTO pushCache(CacheMenualInputDTO cacheDTO);
}
