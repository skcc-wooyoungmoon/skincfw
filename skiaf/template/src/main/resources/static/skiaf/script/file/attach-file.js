/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.21 - in01866
 * description : 첨부파일 Basic 모듈
 */
"use strict";
var AttachFileModule = function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // 파일 아이디 목록
    var saveFileIdList = [];
    var oldFileIdList = [];
    var deleteFileIdList = [];
    
    // 업로드 완료 체크
    var isUploadCompletion = true;
    
    // 업로드 모듈 타입 체크 (basic, advance)
    var isAdvance = true;
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 파일 조회
     */
    var findFile = function(targetId, targetType, fileObj) {

        if (!targetId) {
            return;
        }
       
        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ATTACHFILES_TARGET, targetId, targetType), {
            method : 'GET',
            success : function(result) {

                if (!result.data) {
                    return;
                }
                
                result.data.forEach(function (value, index) {
                    // 기존 파일 추가
                    oldFileIdList.push(value.fileId);
                    var pd = fileObj.createProgress(value.originalFileName,
                            SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, value.fileId), value.fileSize);
                    
                    // 버튼에 기존 파일 아이디 부여
                    $(pd.download).attr('data-file-id', value.fileId);
                    $(pd.del).first().attr('data-file-id', value.fileId);
                   
                });

            }
        });
    };

    return {
        
        /**
         * 파일 업로드 설정
         */
        setFileUpload : function($target, targetId, targetType, newFileSetting) {
            if (!$target) {
                return;
            }
            
            if ($target.data('selecttype') === 'basic') {
                isAdvance = false;
            }
            
            var fileSetting = {
                url : SKIAF.contextPath + SKIAF.PATH.FILES,
                maxFileCount : 10,
                autoSubmit : true,
                showDownload : true,
                showDelete : true,
                onLoad : function(obj) {
                    // 기존 등록된 파일이 있는지 조회
                    findFile(targetId, targetType, obj);
                },
                onSelect : function(files) {
                    
                    // 파일 업로드 준비
                    SKIAF.console.info('onSelect files', files);
                    isUploadCompletion = false;
                },
                onSuccess : function(files, result, xhr, pd) {
                    SKIAF.console.info('onSucce ssresult :: ', result);
                    SKIAF.console.info('onSuccess xhr :: ', xhr);
                    isUploadCompletion = true;
                    
                    if (!result.data) {
                        return;
                    }
                    
                    var fileId = result.data.fileId;
                    
                    // 신규 등록된 파일은 다운로드 버튼 숨김
                    $(pd.download).remove();
    
                    // Basic 타입인 경우 기존 파일 삭제 처리
                    // Basic 타입인 경우 저장목록 초기화
                    if (!isAdvance) {
                        oldFileIdList.forEach(function (value, index) {
                            if (deleteFileIdList.indexOf(value) < 0) {
                                deleteFileIdList.push(value);           
                            }
                        });
                        saveFileIdList = [];
                    }
                    
                    // 저장목록에 추가
                    if (saveFileIdList.indexOf(fileId) < 0) {
                        saveFileIdList.push(fileId);
                    }
                    SKIAF.console.info('upload success');
    
                },
                onError : function(files, status, errMsg, pd, xhr) {
                    
                    CommonModule.fileUploadErrorHandle(files, status, errMsg, pd, xhr);
                    isUploadCompletion = true;
                    
                    // 파일 등록 제거
                    if (files && Array.isArray(files)) {
                        files.forEach(function (value, index) {
                            $target.clearFile({name : value});
                        });
                    }

                    SKIAF.console.info('upload fail');

                },
                afterUploadAll : function (obj) {
                    SKIAF.console.info('afterUploadAll :: saveFileIdList ::', saveFileIdList);
                },
                downloadCallback : function(fileInfo, pd) {
                    SKIAF.console.info('downloadCallback :: fileInfo ::', fileInfo);
                    SKIAF.console.info('downloadCallback :: pd ::', pd);
                    var fileId = $(pd.download).data('file-id');
                    if (fileId) {
                        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, fileId);
                    }
                    return;
                },
                deleteCallback : function (fileInfo, pd) {
                    SKIAF.console.info('deleteCallback :: fileInfo ::', fileInfo);
                    SKIAF.console.info('deleteCallback :: pd ::', pd);
                    
                    // 기존 파일 삭제인지 확인
                    SKIAF.console.info('deleteCallback :: fileId :: ', $(pd.del).data('file-id'));
                    var fileId = $(pd.del).data('file-id');
                    var oldFileIndex = oldFileIdList.indexOf(fileId);
                    if (oldFileIndex >= 0 && deleteFileIdList.indexOf(fileId) < 0) {
                        deleteFileIdList.push(fileId);
                        SKIAF.console.info('deleteCallback :: deleteFileIdList ::', deleteFileIdList);
                    }
    
                    // 신규 파일 삭제인지 확인
                    if (fileInfo.data && fileInfo.data.fileId) {
                        var newFileIndex = saveFileIdList.indexOf(fileInfo.data.fileId);
                        if (newFileIndex >= 0) {
                            saveFileIdList.splice(newFileIndex, 1);
                        }
                        SKIAF.console.info('deleteCallback :: saveFileIdList ::', saveFileIdList);
                    }
                    
                    return;
                }
            };
    
            if (typeof newFileSetting === 'undefined') {
                $target.setOptions(fileSetting);
            } else {
                $target.setOptions($.extend({}, fileSetting, newFileSetting));
            }
        },
        
        /**
         * 업로드 완료 확인
         */
        isUploadCheck : function() {
            return isUploadCompletion;
        },
        
        /**
         * 파일 저장
         */
        saveFile : function(targetId, targetType, callback) {
    
            SKIAF.console.info('saveFile param ::', param);
            // 파라미터 생성
            var param = {
                deleteFileIdList : deleteFileIdList,
                saveFileIdList : saveFileIdList
                
            };
    
            // ajax 통신
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ATTACHFILES_TARGET, targetId, targetType), {
                method : 'POST',
                data : param,
                success : function(result) {
                    callback(result);
                }
            });
        },
    
        /**
         * 선택한 대상에 연결된 파일 전체 삭제
         */
        deleteFileAll : function(targetId, targetType, callback) {
    
            // ajax 통신
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ATTACHFILES_TARGET, targetId, targetType), {
                method : 'DELETE',
                success : function(result) {
    
                    callback(result);
                }
            });
        },

        /**
         * 신규 등록 파일 ID List
         */
        getSaveFileIdList : function() {
            return saveFileIdList;
        },

        /**
         * 삭제 파일 ID List
         */
        getDeleteFileIdList : function() {
            return deleteFileIdList;
        }
    }
};
