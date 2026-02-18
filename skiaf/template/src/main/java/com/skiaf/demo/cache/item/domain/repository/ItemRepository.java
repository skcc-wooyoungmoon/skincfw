/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.item.domain.repository;

import java.util.List;

import com.skiaf.demo.cache.item.domain.model.Item;

public interface ItemRepository {

    Item findOne(Long id);

    Item save(Item mobile);

    void delete(Long id);

    List<Item> findAll();

}
