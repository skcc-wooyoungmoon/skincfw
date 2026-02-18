/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.vo.RestResponse;
import com.skiaf.demo.cache.cache.service.CacheService;
import com.skiaf.demo.cache.cache.service.dto.CacheMenualInputDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "데모-캐시-매니저")
@RestController
public class CacheController {

    @Autowired
    CacheService cacheService;

    @ApiOperation(value = "캐시 리스트 조회")
    @GetMapping(value = "/api/demo/cache/cache-manager")
    public RestResponse getCachedCacheList() {
        return new RestResponse(cacheService.getCachedItemList());
    }

    @ApiOperation(value = "전체 캐시 삭제")
    @DeleteMapping(value = "/api/demo/cache/cache-manager")
    public RestResponse delCachedAll() {
        cacheService.delCachedAll();
        return getCachedCacheList();
    }

    @ApiOperation(value = "특정 캐시 삭제")
    @DeleteMapping(value = {"/api/demo/cache/cache-manager/{cacheValue}/{cacheKey}","/api/demo/cache/cache-manager/{cacheValue}"})
    public RestResponse delCachedCache(
            @ApiParam(name = "cacheValue", value = "캐시 네임") @PathVariable("cacheValue") String cacheValue, 
            @ApiParam(name = "cacheKey", value = "캐시 키") @PathVariable(value="cacheKey", required = false) String cacheKey) {
        cacheService.delCachedCache(cacheValue, cacheKey);
        return getCachedCacheList();
    }

    @ApiOperation(value = "캐시 수동 등록")
    @PostMapping(value = "/api/demo/cache/cache-manager")
    public RestResponse addCache(@ApiParam(name = "cache", value = "등록할 아이템") @RequestBody CacheMenualInputDTO cache) {
        return new RestResponse(cacheService.pushCache(cache));
    }

}
