/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item.domain.service;

import java.util.List;

import com.skiaf.demo.cache.item.domain.model.Item;
import com.skiaf.demo.cache.item.domain.service.dto.ItemDTO;

public interface ItemService {

    public List<Item> getItemList();
    
    public Item getItem(long itemId);

    public Item setItem(Item item);
    
    public void delItem(long id);
    
    public void delItems();
    
    public Item setItemDTO(ItemDTO ItemDTO, String pathKey);

}
