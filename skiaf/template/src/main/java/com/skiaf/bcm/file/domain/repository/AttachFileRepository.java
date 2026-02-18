/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.file.domain.repository;

import java.util.List;

import com.skiaf.bcm.file.domain.model.AttachFile;

/**
 * <pre>
 *
 * History
 * - 2018. 7. 20. | in01871 | 최초작성.
 * </pre>
 */
public interface AttachFileRepository {

    public List<AttachFile> findAll();

    public List<AttachFile> findByTargetIdAndTargetTypeAndUseYnTrue(String targetId, String targetType);

    public AttachFile save(AttachFile attachFile);

    public AttachFile findOne(String fileId);

    public AttachFile findTopByTargetIdAndTargetTypeAndUseYnTrue(String targetId, String targetType);
}
