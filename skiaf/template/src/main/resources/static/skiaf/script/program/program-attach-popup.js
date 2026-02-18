/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.08.07 - in01866
 * description : 프로그램 첨부파일 팝업
 */
"use strict";
var ProgramAttachModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    var isUploadCompletion = false;
    var fileId = '';
    var isEditMode = false;

    // 첨부파일 허용 확장자
    var acceptType = 'pdf,doc,docx,ppt,pptx';

    // 첨부파일 선택 가능한 확장자
    var acceptTypeWithDot = '.' + acceptType.split(',').join(', .');
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    // Dom Ready
    this.init = function(id, param) {

        // 수정모드
        if (param.attachFile && param.attachFile.fileId) {

            isEditMode = true;

            var file = param.attachFile;
            fileId = file.fileId;
            param.fileFullName = file.originalFileName;

            // 다운로드 영역 표시
            $('#programAttachDownload')
                .show()
                .find('a')
                    .attr('href', SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, file.fileId));
            
            // 삭제버튼 표시
            $('#programAttachDelete').show();

        } else { // 등록 모드
            
            // 업로드 영역 표시
            $('#programAttachUpload').show();
            
            // 저장버튼 표시
            $('#programAttachSave').show();

        }
        
        // 데이터 입력
        SKIAF.console.info('param', param);
        $('#programAttachArea').setData(param);

        // 파일 업로더 설정
        ProgramAttachModule.setFileUpload();
        
        ProgramAttachModule.addEvent();
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {

        // 취소
        $('#programAttachCancel').on('click', function(e) {
            $a.close(false);
        });
        
        // 저장
        $('#programAttachSave').on('click', function(e) {
            
            SKIAF.console.info('programAttachSave', programAttachSave);
            
            if (!isUploadCompletion) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.uploading']);
                return;
            }

            if (!fileId) {
                SKIAF.popup.alert(SKIAF.i18n.messages['bcm.program.valid.select-file']);
                return;
            }
            
            
            var program = $('#programAttachArea').getData();
            var param = {};
            param.targetId = program.programId;
            param.targetType = SKIAF.ENUM.FILE_TYPE.PROGRAM;
            
            // ajax 통신
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ATTACHFILES_DETAIL, fileId), {
                method : 'POST',
                data : param,
                success : function(result) {
                    
                    if (!result.data) {
                        return;
                    }

                    $a.close(true);

                }
            });

        });
        
        // 삭제
        $('#programAttachDelete').on('click', function(e) {
            
            SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'], function() {
                ProgramAttachModule.deleteAttachFile();
            });

        });

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    /**
     * 첨부 파일 삭제
     */
    this.deleteAttachFile = function () {
        
        if (!isEditMode) {
            return;
        }

        if (!fileId) {
            return;
        }
        
        // ajax 통신
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ATTACHFILES_DETAIL, fileId), {
            method : 'DELETE',
            success : function(result) {

                if (!result.data) {
                    return;
                }

                $a.close(true);

            }
        });
    };
    
    /**
     * 파일 업로드 설정
     */
    this.setFileUpload = function() {
        $('#programFileUploader').setOptions({
            url : SKIAF.contextPath + SKIAF.PATH.FILES,
            autoSubmit : true,
            allowedTypes  : acceptType,
            acceptFiles : acceptTypeWithDot,
            showDone : false,
            showDelete : false,
            showCancel : false,
            onSelect : function(files) {
                SKIAF.console.info('onSelect files', files);
                fileId = '';
                isUploadCompletion = false;
            },
            onSuccess : function(files, result, xhr, pd) {
                isUploadCompletion = true;
                
                if (!result.data) {
                    return;
                }
                fileId = result.data.fileId;
                SKIAF.console.info('upload success');

            },
            onError : function(files, status, errMsg, pd, xhr) {

                CommonModule.fileUploadErrorHandle(files, status, errMsg, pd, xhr);
                isUploadCompletion = true;
                SKIAF.console.info('upload fail');
                
                // 에러영역 제거
                $('#programAttachUpload .onefile > div').remove();
            },
            afterUploadAll : function (obj) {
                SKIAF.console.info('afterUploadAll obj', obj);
                if (obj.uploadFileList) {
                    obj.uploadFileList.forEach(function (value, index) {
                        $('#programFileUploader').clearFile(value);                   
                    });
                }
            }
        });
    };
});
