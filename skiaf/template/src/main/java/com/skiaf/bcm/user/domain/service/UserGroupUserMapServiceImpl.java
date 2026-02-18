/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skiaf.bcm.code.domain.model.Code;
import com.skiaf.bcm.code.domain.service.CodeService;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;
import com.skiaf.bcm.user.domain.model.UserGroupUserMap;
import com.skiaf.bcm.user.domain.model.UserGroupUserMapId;
import com.skiaf.bcm.user.domain.repository.UserGroupRepository;
import com.skiaf.bcm.user.domain.repository.UserGroupUserMapRepository;
import com.skiaf.bcm.user.domain.repository.UserRepository;
import com.skiaf.core.constant.CommonConstant;

/**
 * <pre>
 * 
 * BCM 사용자그룹 매핑 ServiceImpl
 * 
 * History
 * - 2018. 7. 24. | in01876 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Service
public class UserGroupUserMapServiceImpl implements UserGroupUserMapService {

    @Autowired
    UserGroupUserMapRepository userGroupUserMapRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    CodeService codeService;

    @Override
    public List<UserGroup> findByMapIdUserId(String userId) {

        List<UserGroupUserMap> userGroupUserMapList = userGroupUserMapRepository.findByMapIdUserId(userId);

        List<UserGroup> userGroupList = new ArrayList<>();

        userGroupUserMapList.forEach((UserGroupUserMap item) -> {
            UserGroup userGroup = item.getUserGroup();
            userGroup.setUserMapDt(item.getCreateDate());

            userGroupList.add(userGroup);
        });

        return userGroupList;

    }

    @Override
    public List<User> findByMapIdUserGroupId(String userGroupId) {

        List<UserGroupUserMap> userGroupUserMapList = userGroupUserMapRepository.findByMapIdUserGroupId(userGroupId);

        List<User> userList = new ArrayList<>();

        userGroupUserMapList.forEach((UserGroupUserMap item) -> {
            if (item.getUser().getCompanyCode() != "") {
                Code code = codeService.findOne(CommonConstant.CODE_GROUP_COMPANY_ID, item.getUser().getCompanyCode());

                if (code != null) {
                    item.getUser().setCompanyName(code.getCodeName());
                }
            }

            userList.add(item.getUser());
        });

        return userList;
    }

    @Override
    @Transactional
    public List<User> saveUsersByUserGroupId(String userGroupId, String[] userIdList) {

        List<UserGroupUserMap> userGroupUserMapList = new ArrayList<>();
        UserGroup userGroup = userGroupRepository.findOne(userGroupId);

        UserGroupUserMap userGroupUserMap = null;
        User user = null;
        for (int i = 0, iLen = userIdList.length; i < iLen; i++) {
            user = userRepository.findOne(userIdList[i]);
            if (user != null) {
                userGroupUserMap = new UserGroupUserMap();
                userGroupUserMap.setMapId(new UserGroupUserMapId(userGroup.getUserGroupId(), user.getUserId()));
                userGroupUserMap.setUserGroup(userGroup);
                userGroupUserMap.setUser(user);

                userGroupUserMapList.add(userGroupUserMap);
            }
        }

        userGroupUserMapList = userGroupUserMapRepository.save(userGroupUserMapList);

        List<User> userList = new ArrayList<>();

        userGroupUserMapList.forEach((UserGroupUserMap item) -> userList.add(item.getUser()));

        return userList;
    }

    @Override
    public Boolean deleteUsersByUserGroupId(String userGroupId, String[] userIdList) {

        List<UserGroupUserMap> userGroupUserMapList = new ArrayList<>();
        UserGroup userGroup = userGroupRepository.findOne(userGroupId);

        UserGroupUserMap userGroupUserMap = null;
        User user = null;
        for (int i = 0; i < userIdList.length; i++) {
            user = userRepository.findOne(userIdList[i]);
            if (user != null) {
                userGroupUserMap = new UserGroupUserMap();
                userGroupUserMap.setMapId(new UserGroupUserMapId(user.getUserId(), userGroup.getUserGroupId()));
                userGroupUserMap.setUserGroup(userGroup);
                userGroupUserMap.setUser(user);

                userGroupUserMapList.add(userGroupUserMap);                
            }
        }

        userGroupUserMapRepository.deleteInBatch(userGroupUserMapList);
        return true;
    }

    @Override
    public List<UserGroup> saveUserGroupsByUserId(String userId, String[] userGroupIdList) {
        
        List<UserGroupUserMap> userGroupUserMapList = new ArrayList<>();
        User user = userRepository.findOne(userId);

        UserGroupUserMap userGroupUserMap = null;
        UserGroup userGroup = null;
        
        for (int i = 0; i < userGroupIdList.length; i++) {
            userGroup = userGroupRepository.findOne(userGroupIdList[i]);
            if (userGroup != null) {
                userGroupUserMap = new UserGroupUserMap();
                userGroupUserMap.setMapId(new UserGroupUserMapId(userGroup.getUserGroupId(), user.getUserId()));
                userGroupUserMap.setUserGroup(userGroup);
                userGroupUserMap.setUser(user);

                userGroupUserMapList.add(userGroupUserMap);
            }
        }

        userGroupUserMapList = userGroupUserMapRepository.save(userGroupUserMapList);

        List<UserGroup> userGroupList = new ArrayList<>();

        userGroupUserMapList.forEach((UserGroupUserMap item) -> userGroupList.add(item.getUserGroup()));

        return userGroupList;
    }
    

    @Override
    public boolean deleteUserGroupsByUserId(String userId, String[] userGroupIdList) {
        List<UserGroupUserMap> userGroupUserMapList = new ArrayList<>();
        User user = userRepository.findOne(userId);

        UserGroupUserMap userGroupUserMap = null;
        UserGroup userGroup = null;
        for (int i = 0; i < userGroupIdList.length; i++) {
            userGroup = userGroupRepository.findOne(userGroupIdList[i]);
            if (userGroup != null) {
                userGroupUserMap = new UserGroupUserMap();
                userGroupUserMap.setMapId(new UserGroupUserMapId(user.getUserId(), userGroup.getUserGroupId()));
                userGroupUserMap.setUserGroup(userGroup);
                userGroupUserMap.setUser(user);

                userGroupUserMapList.add(userGroupUserMap);                
            }
        }

        userGroupUserMapRepository.deleteInBatch(userGroupUserMapList);
        return true;
    }
}
