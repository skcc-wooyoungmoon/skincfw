/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.service.CodeService;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.repository.UserGroupRepository;
import com.skiaf.bcm.user.domain.service.dto.UserGroupSearchDTO;
import com.skiaf.core.constant.CommonConstant;
import com.skiaf.core.exception.ValidationException;
import com.skiaf.core.vo.PageDTO;

/**
 * <pre>
 * 
 * BCM 사용자그룹 관리 ServiceImpl
 * 
 * History
 * - 2018. 7. 19. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    CodeService codeService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    @Override
    public UserGroup create(UserGroup userGroup) {
        // 1. validation
        // 2. one more duplicate check
        if (this.duplicateCheck(userGroup.getUserGroupId())) {
            throw ValidationException.withUserMessageKey("bcm.common.DUPLICATE")
                    .withSystemMessage("this.duplicateCheck(userGroup.getUserGroupId())").build();
        }
        // 3. save
        UserGroup userGroupEntity = userGroupRepository.save(userGroup);
        // 4. result return
        return userGroupEntity;
    }

    @Override
    public UserGroup findOne(String groupId) {

        UserGroup userGroup = userGroupRepository.findOne(groupId);

        if (userGroup.getCompanyCode() != null) {
            Code code = codeService.findOne(CommonConstant.CODE_GROUP_COMPANY_ID, userGroup.getCompanyCode());

            if (code != null) {
                userGroup.setCompanyName(code.getCodeName());
            }
        }

        return userGroup;
    }

    @Override
    public UserGroup update(String id, UserGroup userGroup) {

        // 1. userGroupEntity select
        UserGroup userGroupEntity = userGroupRepository.findByUserGroupId(id);

        // 2. null check
        if (userGroupEntity == null) {
            return null;
        }

        // 3. update data put. ex) groupNm
        userGroupEntity.setUserGroupName(userGroup.getUserGroupName());
        userGroupEntity.setUserGroupDesc(userGroup.getUserGroupDesc());
        userGroupEntity.setUseYn(userGroup.isUseYn());

        // 4. managed Entity save
        userGroupRepository.save(userGroupEntity);

        // 5. 성공 결과 return
        return userGroupEntity;
    }


    @Override
    public Boolean duplicateCheck(String id) {
        Boolean isDuplicate = false;
        UserGroup userGroup = userGroupRepository.findByUserGroupId(id);
        if (userGroup != null) {
            isDuplicate = true;
            
        }
        return isDuplicate;
    }

    @Override
    public PageDTO<UserGroup> findQueryBySearch(UserGroupSearchDTO search, Pageable pageable) {

        PageDTO<UserGroup> result = userGroupRepository.findQueryBySearch(search, pageable);

        for (int i = 0; i < result.getList().size(); i++) {

            // 회사 명
            Code code = codeService.findOne(CommonConstant.CODE_GROUP_COMPANY_ID,
                    result.getList().get(i).getCompanyCode());

            if (code != null) {
                result.getList().get(i).setCompanyName(code.getCodeName());
            }

            // 사용자그룹 별 사용자수

            int userCount = 0;
            if (result.getList() != null) {
                userCount = result.getList().get(i).getUserGroupUserList().size();
            }

            result.getList().get(i).setUserCount(userCount);

        }

        return result;
    }

    @Override
    public List<UserGroup> findByUseYn(Sort sort, boolean useYn) {
        return userGroupRepository.findByUseYn(sort, useYn);
    }

}
