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
 var ArticleCreatePopupModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

     // URL 파라메타로 받음
     var initGetBoardId = "";
     
     // 게시판용 파일 타입
     var fileType = SKIAF.ENUM.FILE_TYPE.ARTICLE;
     var fileTypeSingle = SKIAF.CONSTATNT.ATTACHFILES_TYPE_SINGLE;

     // 파일 첨부 모듈
     var attachFileModule = new AttachFileModule();
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        initGetBoardId = param.boardId;

        SKIAF.console.info('param:;', param);
        
        $('input[name=articleBeginDtm]').val(SKIAF.util.dateFormatReplace(new Date(), 'yyyy-MM-dd')); 
        $('input[name=articleEndDtm]').val("9999-12-31"); 
        
        // webeditor 생성
        SKIAF.webeditor.setWebeditor('#WebeditorArticleCreatePopup');
        
        // 이벤트 바인딩
        ArticleCreatePopupModule.addEvent();
        
        // 파일 업로드 설정
        if ($("#attachFileType").val() == fileTypeSingle) {
            attachFileModule.setFileUpload($("#fileuploaderA"), '', fileType, {maxFileCount : 1});
        } else {
            attachFileModule.setFileUpload($("#fileuploaderA"), '', fileType);
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
        
        // 저장 button
        $('#articleSaveBtn').on('click', function(e) {
            ArticleCreatePopupModule.saveMain();
        });

        // 글 내용 type 
        $('input[name=articleType]').on('change', function(e) {
            SKIAF.webeditor.changeCodeView('#WebeditorArticleCreatePopup', $('input[name=articleType]:checked').val());
        });

    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

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
        var webeditorObjectContent = SKIAF.webeditor.getData('#WebeditorArticleCreatePopup');

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
        saveData.articleType = $('input[name=articleType]:checked').val();
        saveData.articleBeginDtm = $('input[name=articleBeginDtm]').val().replace(/-/g, '');
        saveData.articleEndDtm =  $('input[name=articleEndDtm]').val().replace(/-/g, '');
        saveData.attachFileUpdateDTO = {
                deleteFileIdList : attachFileModule.getDeleteFileIdList(),
                saveFileIdList : attachFileModule.getSaveFileIdList()                
        };

        // webeditor code getting
        saveData.articleContent = webeditorObjectContent;
       
        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.save-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_CREATE, initGetBoardId), {
                method : 'POST',
                data : saveData,
                success : function(res) {
                    SKIAF.console.info('saveMain success :: res :: ', res);
                    $a.close('save');
                }
            });
        }, saveData);
    };

});
