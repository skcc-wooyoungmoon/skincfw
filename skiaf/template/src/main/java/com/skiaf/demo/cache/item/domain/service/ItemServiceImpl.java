/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item.domain.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.skiaf.demo.cache.item.domain.model.Item;
import com.skiaf.demo.cache.item.domain.repository.ItemRepository;
import com.skiaf.demo.cache.item.domain.service.dto.ItemDTO;

@Service
public class ItemServiceImpl implements ItemService {

    /**
     * keyGenerator를 사용해야 하는 경우
     * 
     * 1. 파라미터가 없을 경우 (기본 key로 사용하면 에러
     * (org.springframework.cache.interceptor.SimpleKey cannot be cast to
     * java.lang.String)
     * 
     * 2. value + 파라미터 조합으로 key를 생성하려할 경우
     */

    @Autowired
    ItemRepository cacheRepository;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(cacheNames = "itemList")
    public List<Item> getItemList() {
        return cacheRepository.findAll();
    }
    
    @Override
    @Cacheable(cacheNames = "item", key = "\"bcm-item-\"+#itemId") 
    public Item getItem(long itemId) {
        return cacheRepository.findOne(itemId);
    }

    @Override
    @CachePut(cacheNames = "item")
    public Item setItem(Item item) {
        return cacheRepository.save(item);
    }
    
    @Override
    public void delItem(long id) {
        cacheRepository.delete(id);
    }
    
    @Override
    @CacheEvict(cacheNames = {"item","itemList"}, allEntries =true)
    public void delItems() {
    }
    
    @Autowired
    ModelMapper modelMapper;

    @Override
    @CachePut(cacheNames = "itemDTOPathKey")
    public Item setItemDTO(ItemDTO ItemDTO, String pathKey) {
        Item item = modelMapper.map(ItemDTO, Item.class);
        return cacheRepository.save(item);
    }

}
