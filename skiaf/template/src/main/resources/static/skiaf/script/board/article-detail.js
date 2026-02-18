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
 var ArticleDetailModule = $a.page(function() {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

     // 게시글관리자권한 ID
     var boardAdminRoleId = SKIAF.CONSTATNT.BOARD_ADMIN_ROLE;
     
     var initGetBoardId = SKIAF.util.getPathVariable(3);
     var initGetArticleId = SKIAF.util.getPathVariable(5);
     
    // 페이지 정보
    var pageUrl = location.pathname;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Init
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.init = function(id, param) {

        // 이벤트 바인딩
        ArticleDetailModule.addEvent();
        
        // 그리드 데이터 바인딩
        ArticleDetailModule.search();
        
    };    
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | ADD Event
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    
    this.addEvent = function() {
        // 목록 button
        $('#articleListBtn').on('click', function(e) {
            ArticleDetailModule.callBoardList();
        });
        
        // 수정 button
        $('#articleModifyBtn').on('click', function(e) {
            ArticleDetailModule.modifyMain();
        });

        // 등록 button
        $('#articleCreateBtn').on('click', function(e) {
            ArticleDetailModule.createComment();
        });
        
        // 브라우저 뒤로가기 이벤트
        window.addEventListener('popstate', function(e) {
            // 브라우저 뒤로가기 후 최초 검색
            ArticleDetailModule.search(true);
        }, false);
    };
    
    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Functions
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 게시글 검색
     */
    this.search = function(isHistory) {
        if (typeof isHistory === 'undefined') {
            isHistory = false;
        }

        // 히스토리에 저장
        if (isHistory) {
            // 히스토리 푸쉬
            history.pushState(null, '', pageUrl);
        }

        $a.request(SKIAF.util.urlWithParams(SKIAF.PATH.ARTICLES_DETAIL, initGetArticleId), {
            method : 'GET',
            success : function(res) {
                
                if (!res.data) {
                    return;
                }

                // button 권한 check
                ArticleDetailModule.elementVisable("#articleModifyBtn", ArticleDetailModule.isRoleMyOrAdmin(res.data.createBy));
            
                ArticleDetailModule.dataBindAttachfile(res.data.attachFile);                
                ArticleDetailModule.dataBindComment(res);
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
        $('#commentAddData').val("");

        if (res.data.comment.length > 0) {
            var eventButton;

            // 댓글 추가 스크립트
            $(res.data.comment).each(function(index, item){
                // event 는 1부터 시작
                index++;

                SKIAF.console.info("item.createBy :: ", item.createBy);
                // 권한 있는 경우
                if (ArticleDetailModule.isRoleMyOrAdmin(item.createBy) == true) {
                    // 댓글 삭제 button
                    eventButton = $(".comment-head:eq(" + index + ") .Button:eq(0)");
                    eventButton.on('click', function(e){
                        ArticleDetailModule.deleteComment(index);
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
                        ArticleDetailModule.updateComment(index);
                    }); 
                } else {
                    eventButton = $(".comment-head:eq(" + index + ") .Button").hide();
                }                                       
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
                    // 이렇게 하면 webeditor setting에서 오류 발생 ArticleDetailModule.search();
                    ArticleDetailModule.callThisPage();
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
                 // 이렇게 하면 webeditor setting에서 오류 발생 ArticleDetailModule.search();
                    ArticleDetailModule.callThisPage();
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
                    ArticleDetailModule.search();
                }
            });
        }, saveData);
    };

    /**
     * 댓글 main 수정
     */
    this.modifyMain = function() {
        $a.popup({
        url : SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_ARTICLES_SAVE_POPUP_DETAIL, initGetBoardId, initGetArticleId), 
            title : $('#boardName').text(),
            data : {
                boardId: initGetBoardId,
                articleId: initGetArticleId
            },
            iframe : false,
            width : 1000,
            height : 685,
            center : true,
            callback : function(data) {
                if (data != null) {
                    if (data == "delete") {
                        ArticleDetailModule.callBoardList();
                    } else {
                        ArticleDetailModule.search();
                    }
                }
            }
        });
    };

    /**
     * link 목록
     */
    this.callBoardList = function() {
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_BOARD_ARTICLES, initGetBoardId);
    };

    /**
     * this page 호출
     */
    this.callThisPage = function() {
        window.location.href = SKIAF.contextPath + SKIAF.util.urlWithParams(SKIAF.PATH.VIEW_BOARD_ARTICLES_DETAIL, initGetBoardId, initGetArticleId);
    };
});