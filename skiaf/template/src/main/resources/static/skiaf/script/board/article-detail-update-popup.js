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
 var ArticleDetailUpdatePopupModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

     // 게시글관리자권한 ID
     var boardAdminRoleId = SKIAF.CONSTATNT.BOARD_ADMIN_ROLE;
     
     var initGetArticleId = "";

     // 게시판용 파일 타입
     var fileType = SKIAF.ENUM.FILE_TYPE.ARTICLE;
     var fileTypeSingle = SKIAF.CONSTATNT.ATTACHFILES_TYPE_SINGLE;
     
     // 파일 첨부 모듈
     var attachFileModule = new AttachFileModule();
     
     // 파일 저장 삭제 처리 완료
     var isFileSave = false;
     var isFileDelete = false;
     
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {
        initGetArticleId = param.articleId;
        
        ArticleDetailUpdatePopupModule.toggleDiv();
        
        // webeditor 생성
        SKIAF.webeditor.setWebeditor('#WebeditorArticleUpdatePopup');
        
        // 이벤트 바인딩
        ArticleDetailUpdatePopupModule.addEvent();

        // 그리드 데이터 바인딩
        ArticleDetailUpdatePopupModule.search(true);

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
        // view 닫기 button
        $('#articleListBtn').on('click', function(e) {
            $a.close();
        });
        
        // view 수정 button
        $('#articleModifyBtn').on('click', function(e) {
            ArticleDetailUpdatePopupModule.search(false);
        });
        
        // 수정 취소 button
        $('#articleCancelBtn').on('click', function(e) {
            ArticleDetailUpdatePopupModule.toggleDiv();
        });
        
        // 수정 삭제 button
        $('#articleDeleteBtn').on('click', function(e) {
            ArticleDetailUpdatePopupModule.deleteMain();
        });

        // 수정 저장 button
        $('#articleSaveBtn').on('click', function(e) {
            ArticleDetailUpdatePopupModule.saveMain();
        });

        // 글 내용 type 
        $('input[name=articleType]').on('change', function(e) {
            SKIAF.webeditor.changeCodeView('#WebeditorArticleUpdatePopup', $('input[name=articleType]:checked').val());
        });

        // 댓글 등록 button
        $('#articleCreateCommentBtn').on('click', function(e) {
            ArticleDetailUpdatePopupModule.createComment();
        });
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * toggle
     */
    this.toggleDiv = function(action) {
        if (action == "update") {
            $('#selectedArticleInfo').hide();
            $('#updateArticleDetail').show();
            $('#selectedArticleInfoBtn').hide();
            $('#updateArticleDetailBtn').show();
        } else {
            $('#selectedArticleInfo').show();
            $('#updateArticleDetail').hide();
            $('#selectedArticleInfoBtn').show();
            $('#updateArticleDetailBtn').hide();
        }
    };

    /**
     * 게시글 검색
     */
    this.search = function(mode) {
        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_DETAIL, initGetArticleId), {
            method : 'GET',
            success : function(res) {

                if (!res.data) {
                    return;
                }

                // button 권한 check
                ArticleDetailUpdatePopupModule.elementVisable("#articleModifyBtn", ArticleDetailUpdatePopupModule.isRoleMyOrAdmin(res.data.createBy));
                ArticleDetailUpdatePopupModule.elementVisable("#articleDeleteBtn", ArticleDetailUpdatePopupModule.isRoleMyOrAdmin(res.data.createBy)); 
                ArticleDetailUpdatePopupModule.elementVisable("#articleSaveBtn", ArticleDetailUpdatePopupModule.isRoleMyOrAdmin(res.data.createBy));                

                // 조회 mode
                if (mode) {
                    ArticleDetailUpdatePopupModule.dataBindAttachfile(res.data.attachFile);
                    ArticleDetailUpdatePopupModule.dataBindComment(res);
                }  
                else {

                    ArticleDetailUpdatePopupModule.toggleDiv("update");
                    
                    // webeditor code setting
                    SKIAF.webeditor.setData('#WebeditorArticleUpdatePopup', res.data.articleContent); 
                    
                    // 게시글 edit mode toggle
                    SKIAF.webeditor.changeCodeView('#WebeditorArticleUpdatePopup', res.data.articleType);
                }
                
                $('input[name=articleBeginDtm]').val(SKIAF.util.makeFormat(res.data.articleBeginDtm, '####-##-##')); 
                $('input[name=articleEndDtm]').val(SKIAF.util.makeFormat(res.data.articleEndDtm, '####-##-##'));                         
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
        var webeditorObjectContent = SKIAF.webeditor.getData('#WebeditorArticleUpdatePopup');

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
                    ArticleDetailUpdatePopupModule.search(true);
                    ArticleDetailUpdatePopupModule.toggleDiv();
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

    /**
     * attch file data bind
     */
    this.dataBindAttachfile = function(attachFile) {
        if (attachFile) {            
            // 댓글 추가 스크립트
            $(attachFile).each(function(index, item) {
                attachFile[index].image = SKIAF.util.getFileImageUrl(item.originalFileName);
                attachFile[index].href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.FILES_DETAIL, item.severFileName); 
            });
        }

        $("#attchfile-area").setData(attachFile);
    };

    /**
     * 댓글 data bind
     */
    this.dataBindComment = function(res) {

        if (res.data.articleType == SKIAF.ENUM.ARTICLE_TYPE.HTML) {
            $('#articleContentHtml').show();
            $('#articleContentText').hide();
            
            res.data.articleContentHtml = res.data.articleContent;
            $('#articleContentHtml').setData(res.data);
        } else {
            $('#articleContentHtml').hide();
            $('#articleContentText').show();

            res.data.articleContentText = res.data.articleContent;
            $('#articleContentText').setData(res.data);
        }

        if (res.data.emgcYn) {
            $('#emergencyNoticeDanger').show();
            $('#emergencyNoticeNomal').hide();
            
            res.data.emergencyNotice = SKIAF.i18n.messages['bcm.board.emergency-notice'];
        } else {
            $('#emergencyNoticeDanger').hide();
            $('#emergencyNoticeNomal').show();

            res.data.emergencyNotice = SKIAF.i18n.messages['bcm.board.normal-notice'];
        }
        
        res.data.createByDate = res.data.createBy + " " + res.data.createDate;
        res.data.articleBeginEndDtm = SKIAF.util.makeFormat(res.data.articleBeginDtm, '####-##-##') + " ~ " + SKIAF.util.makeFormat(res.data.articleEndDtm, '####-##-##');
        res.data.commentCount = res.data.comment.length;

        $('#selectedArticleInfo').setData(res.data);  
        $('#updateArticleDetail').setData(res.data);
        $('#commentAddData').val("");
        
        SKIAF.console.info('res.data ', res.data);
        
        if (res.data.comment.length > 0) {
            var eventButton;
            
            // 댓글 추가 스크립트
            $(res.data.comment).each(function(index, item){
                // event 는 1부터 시작
                index++;
                
                // 댓글 삭제 button
                if (ArticleDetailUpdatePopupModule.isRoleMyOrAdmin(item.createBy) == true) {
                    eventButton = $(".comment-head:eq(" + index + ") .Button:eq(0)");
                    eventButton.on('click', function(e){
                        ArticleDetailUpdatePopupModule.deleteComment(index);
                    });

                    // 댓글 수정 enable button
                    eventButton = $(".comment-head:eq(" + index + ") .Button:eq(1)");
                    eventButton.on('click', function(e){
                        $(this).closest('li').hide();
                        $(this).closest('li').next().show();
                    });

                    // 댓글 취소 button
                    eventButton = $(".comment-edit:eq(" + index + ") .Button:eq(0)");
                    eventButton.on('click', function(e){
                        $(this).closest('li').hide();
                        $(this).closest('li').prev().show();
                    });

                    // 댓글 수정 button
                    eventButton = $(".comment-edit:eq(" + index + ") .Button:eq(1)");
                    eventButton.on('click', function(e){
                        ArticleDetailUpdatePopupModule.updateComment(index);
                    });  
                } else {
                    eventButton = $(".comment-head:eq(" + index + ") .Button").hide();
                }    

                SKIAF.console.info("each index :1: ", index);
            });
        }
    };

    /**
     * 댓글 등록
     */
    this.createComment = function() {

        // Validation Check
        if (!$('#commentAddData').validate()) {
            $('#commentAddData').validator();
            return;
        }

        var saveData = {
                commentCtnt: $('#commentAddData').val()
        }
    
        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.register-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_MAP_COMMENTS, initGetArticleId), {
                method : 'POST',
                data : saveData,
                success : function(res) {
                    ArticleDetailUpdatePopupModule.search(true);
                }
            });
        }, saveData);
    };

    /**
     * 댓글 수정
     */
    this.updateComment = function(index) {

        // Validation Check
        if (!$(".comment-edit:eq(" + index + ") .Textarea").validate()) {
            $(".comment-edit:eq(" + index + ") .Textarea").validator();
            return;
        }

        var saveData = {
                commentId: $(".comment-edit:eq(" + index + ") .Input").val(),
                commentCtnt: $(".comment-edit:eq(" + index + ") .Textarea").val()
            };
    
        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.modify-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.COMMENTS_DETAIL, saveData.commentId), {
                method : 'PUT',
                data : saveData,
                success : function(res) {
                    ArticleDetailUpdatePopupModule.search(true);
                }
            });
        }, saveData);
    };

    /**
     * 댓글 삭제
     */
    this.deleteComment = function(index) {
        var saveData = {
                commentId: $(".comment-edit:eq(" + index + ") .Input").val()
            };

        SKIAF.popup.confirm(SKIAF.i18n.messages['bcm.common.delete-confirm'] , function callback(saveData) {
            $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.COMMENTS_DETAIL, saveData.commentId), {
                method : 'DELETE',
                success : function(res) {
                    ArticleDetailUpdatePopupModule.search(true);
                }
            });
        }, saveData);
    };
});
 