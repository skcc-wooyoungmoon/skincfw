/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skiaf.core.vo.RestResponse;
import com.skiaf.demo.cache.item.domain.model.Item;
import com.skiaf.demo.cache.item.domain.service.ItemService;
import com.skiaf.demo.cache.item.domain.service.dto.ItemDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "데모-캐시-아이템")
@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @ApiOperation(value = "아이템 리스트 조회 + 캐시 등록")
    @GetMapping(value = "/api/demo/cache/items")
    public RestResponse getCacheList() {
        List<Item> itemList = itemService.getItemList();
        return new RestResponse(itemList);
    }
    
    @ApiOperation(value = "아이템 조회 + 캐시 등록")
    @GetMapping(value = "/api/demo/cache/items/{itemId}")
    public RestResponse getCache(@ApiParam(name = "itemId", value = "조회할 아이템 아이디") @PathVariable("itemId") Long itemId) {
        Item item = itemService.getItem(itemId);
        return new RestResponse(item);
    }

    @ApiOperation(value = "아이템 등록/수정 + 캐시 등록/수정")
    @PostMapping(value = "/api/demo/cache/items")
    public RestResponse setCache(@ApiParam(name = "item", value = "등록/수정할 아이템") @RequestBody Item cache) {
        return new RestResponse(itemService.setItem(cache));
    }
    
    @ApiOperation(value = "아이템 DTO 등록/수정(pathKey 추가) + 캐시 등록/수정")
    @PostMapping(value = "/api/demo/cache/items/command/{pathKey}")
    public RestResponse updateCache(
            @ApiParam(name = "pathKey", value = "아이템 아이디") @PathVariable("pathKey") String pathKey,
            @ApiParam(name = "item", value = "등록할 아이템") @RequestBody ItemDTO itemDTO) {
        
        return new RestResponse(itemService.setItemDTO(itemDTO, pathKey));
    }

    @ApiOperation(value = "아이템 삭제")
    @DeleteMapping(value = "/api/demo/cache/items/{itemId}")
    public RestResponse delCache(@ApiParam(name = "itemId", value = "삭제할 아이템 아이디") @PathVariable("itemId") Long itemId) {
        itemService.delItem(itemId);
        return new RestResponse();
    }
    
    @ApiOperation(value = "아이템 캐시 삭제")
    @DeleteMapping(value = "/api/demo/cache/items/command/delete-cache")
    public RestResponse delCaches() {
        itemService.delItems();
        return new RestResponse();
    }
    
}
