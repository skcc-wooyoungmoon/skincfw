/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 * create by 2018.09.11 - in01943
 * description : 게시글 리스트
 */
"use strict";
 var ArticleDetailSavePopupModule = $a.page(function() {
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

     // 게시글관리자권한 ID
     var boardAdminRoleId = SKIAF.CONSTATNT.BOARD_ADMIN_ROLE;
     
     // URL 파라메타로 받음
     var initGetArticleId = "";
     
     // 게시판용 파일 타입
     var fileType = SKIAF.ENUM.FILE_TYPE.ARTICLE;
     var fileTypeSingle = SKIAF.CONSTATNT.ATTACHFILES_TYPE_SINGLE;
     
     // 파일 첨부 모듈
     var attachFileModule = new AttachFileModule();
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        initGetArticleId = param.articleId;

        // webeditor 생성
        SKIAF.webeditor.setWebeditor('#WebeditorArticleDetailSavePopup');
        
        // 이벤트 바인딩
        ArticleDetailSavePopupModule.addEvent();
        
        // 그리드 데이터 바인딩
        ArticleDetailSavePopupModule.search(true);
        
        // 파일 업로드 설정
        if ($("#attachFileType").val() == fileTypeSingle) {
            attachFileModule.setFileUpload($("#fileuploaderA"), initGetArticleId, fileType, {maxFileCount : 1});
        } else {
            attachFileModule.setFileUpload($("#fileuploaderA"), initGetArticleId, fileType);
        }
    };
    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        // 취소 button
        $('#articleCancelBtn').on('click', function(e) {
            $a.close();
        });
        
        // 삭제 button
        $('#articleDeleteBtn').on('click', function(e) {
            ArticleDetailSavePopupModule.deleteMain();
        });
        
        // 저장 button
        $('#articleSaveBtn').on('click', function(e) {
            ArticleDetailSavePopupModule.saveMain();
        });

        // 글 내용 type 
        $('input[name=articleType]').on('change', function(e) {
            SKIAF.webeditor.changeCodeView('#WebeditorArticleDetailSavePopup', $('input[name=articleType]:checked').val());
        });

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 게시글 검색
     */
    this.search = function(isHistory) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_DETAIL, initGetArticleId), {
            method : 'GET',
            success : function(res) {

                if (!res.data) {
                    return;
                }

                $('#updateArticleDetail').setData(res.data);
                $('input[name=articleBeginDtm]').val(SKIAF.util.makeFormat(res.data.articleBeginDtm, '####-##-##')); 
                $('input[name=articleEndDtm]').val(SKIAF.util.makeFormat(res.data.articleEndDtm, '####-##-##')); 

                // button 권한 check
                ArticleDetailSavePopupModule.elementVisable("#articleDeleteBtn", ArticleDetailModule.isRoleMyOrAdmin(res.data.createBy));
                ArticleDetailSavePopupModule.elementVisable("#articleSaveBtn", ArticleDetailModule.isRoleMyOrAdmin(res.data.createBy));                
            
                // webeditor code setting
                SKIAF.webeditor.setData('#WebeditorArticleDetailSavePopup', res.data.articleContent);

                // 게기글 edit mode toggle
                SKIAF.webeditor.changeCodeView('#WebeditorArticleDetailSavePopup', res.data.articleType);
            }
        });
    };

    /**
     * admin role/본인 인지 확인 
     */
    this.isRoleMyOrAdmin = function(userId) {
        var visable = (userId == SKIAF.loginUserInfo.user.loginId ? true : false);

        // admin 권한 있는지 check
        if (visable == false) {
            $(SKIAF.loginUserInfo.roleList).each(function(index, item){
                if (item['roleId'] == boardAdminRoleId) {
                    visable = true;
                    return;
                }   
            });
        }

        return visable;
    }; 

    /**
     * element show/hide
     */
    this.elementVisable = function(id, visable) {
        if (visable == true) {
            $(id).show();
        } else {
            $(id).hide();
        }
    };

    /**
     * 게시글 저장
     */
    this.saveMain = function(index) {

        // Validation Check
        if (!$('#updateArticleDetail').validate()) {
            $('#updateArticleDetail').validator();
            return;
        }

        // Validation Check
        if ($('input[name=articleType]:checked').length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.board.valid.content-type']);
            return;
        }
        
        // Validation Check
        var webeditorObjectContent = SKIAF.webeditor.getData('#WebeditorArticleDetailSavePopup');

        if (!webeditorObjectContent || webeditorObjectContent.length == 0) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.board.valid.content']);
            return;
        }

        // 업로드 진행중인지 확인
        if (!attachFileModule.isUploadCheck()) {
            SKIAF.popup.alert(SKIAF.i18n.messages['bcm.board.valid.uploading']);
            return;
        }

        var saveData = $('#updateArticleDetail').getData();

        saveData.emgcYn = saveData.emgcYn[0] ? true : false;
        saveData.articleId = initGetArticleId;
        saveData.articleType = $('input[name=articleType]:checked').val();
        saveData.articleBeginDtm = $('input[name=articleBeginDtm]').val().replace(/-/g, '');
        saveData.articleEndDtm =  $('input[name=articleEndDtm]').val().replace(/-/g, '');
        saveData.attachFileUpdateDTO = {
                deleteFileIdList : attachFileModule.getDeleteFileIdList(),
                saveFileIdList : attachFileModule.getSaveFileIdList()                
        };

        // webeditor code getting
        saveData.articleContent = webeditorObjectContent;

        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.modify-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_DETAIL, saveData.articleId), {
                method : 'PUT',
                data : saveData,
                success : function(res) {
                    $a.close('save');
                }
            });
        }, saveData);

    };

    /**
     * 게시글 삭제
     */
    this.deleteMain = function() {
        var saveData = {
                articleId: initGetArticleId
        };

        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_DETAIL, initGetArticleId), {
                method : 'DELETE',
                success : function(res) {
                   $a.close('delete');
                }
            });
        }, saveData);
    };
    
});
