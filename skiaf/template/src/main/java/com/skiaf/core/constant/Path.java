/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.constant;

/**
 * <pre>
 * 각종 view 및 api에 대한 url path 정의
 *
 * History
 * - 2018. 7. 18. | in01876 | 최초작성.
 * </pre>
 */
public class Path {

    private Path() {
        throw new IllegalStateException("Path Class");
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Prefix
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // API Prefix
    public static final String API                                      = "/api";

    // VIEW Prefix
    public static final String VIEW                                     = "/view";
    public static final String VIEW_BCM                                 = VIEW + "/bcm";

    // RestAPI 상세
    public static final String API_DETAIL                               = "/{id}";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Popup
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Popup Prefix
    public static final String VIEW_POPUPS                              = VIEW_BCM + "/popups";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Login, Main
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String VIEW_ROOT                                = "/";

    public static final String LOGIN_VIEW                               = VIEW_BCM + "/login";
    public static final String LOGIN_API                                = API + "/login";

    public static final String VIEW_LOGIN                               = VIEW_BCM + "/login/login-form";
    public static final String VIEW_SSO_LOGIN_PROCESS                   = VIEW_BCM + "/login/sso-login-process";
    public static final String VIEW_SSO_LOGOFF                          = VIEW_BCM + "/login/sso-logoff";
    public static final String VIEW_TRY_SSO                             = VIEW_BCM + "/login/try-sso";
    public static final String VIEW_LOGIN_ERROR                         = VIEW_BCM + "/login/login-error";
    public static final String VIEW_CHANGE_PASSWORD                     = VIEW_BCM + "/login/change-password/{loginId}";

    public static final String LOGIN                                    = API + "/login/login";
    public static final String SSO_LOGIN                                = API + "/login/sso-login";
    public static final String PASSWORD_CHANGE                          = API + "/login/command/change-password/{userId}";
    public static final String LOGOUT                                   = API + "/logout";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Error
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String ERROR_401                               = VIEW + "/error/401";
    public static final String ERROR_403                               = VIEW + "/error/403";
    //public static final String ERROR_404                               = VIEW + "error/404";
    //public static final String ERROR_500                               = VIEW + "error/500";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | [User], [UserGroup], [UserGroup <-> UserMap]
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String VIEW_USERS_DETAIL                        = VIEW_BCM + "/users/{userId}";
    public static final String VIEW_USERS_SEARCH                        = VIEW_BCM + "/users/popups/search";
    public static final String VIEW_USERS_CREATE                        = VIEW_BCM + "/users/popups/create";
    public static final String VIEW_USERS_UPDATE                        = VIEW_BCM + "/users/popups/update";
    public static final String VIEW_USERS_PWCHANGE                      = VIEW_BCM + "/users/popups/pwchange";
    public static final String VIEW_USERS_GROUP_MAP                     = VIEW_BCM + "/users/popups/userGroup";

    public static final String USERS                                    = API + "/users";
    public static final String USERS_DETAIL                             = API + "/users/{userId}";
    public static final String USERS_DETAIL_PWRESET                     = API + "/users/{userId}/commands/password-reset";
    public static final String USERS_DETAIL_FAILRESET                   = API + "/users/{userId}/commands/login-fail-count-reset";
    public static final String USERS_DETAIL_PWCHANGE                    = API + "/users/{userId}/commands/password-change";
    public static final String USERS_DETAIL_PWCHANGE_MY                 = API + "/users/commands/my-password-change";
    public static final String USERS_DETAIL_INFO                        = API + "/users/{userId}/commands/detail-with-mappings";
    public static final String USERS_DETAIL_INFO_MY                     = API + "/users/commands/my-detail-with-mappings";
    public static final String USER_DUPLICATE                           = API + "/users/{loginId}/commands/duplicate-check";
    public static final String USERS_GROUP_MAP                          = API + "/users/{userId}/groupMap";

    public static final String VIEW_USER_GROUPS                         = VIEW_BCM + "/usergroups";
    public static final String VIEW_USER_GROUPS_DETAIL                  = VIEW_BCM + "/usergroups/{userGroupId}";
    public static final String VIEW_USER_GROUPS_SEARCH                  = VIEW_BCM + "/usergroups/popups/select";
    public static final String VIEW_USER_GROUPS_CREATE                  = VIEW_BCM + "/usergroups/popups/create";
    public static final String VIEW_USER_GROUPS_UPDATE                  = VIEW_BCM + "/usergroups/popups/update";

    public static final String USER_GROUPS                              = API + "/usergroups";
    public static final String USER_GROUPS_DETAIL                       = API + "/usergroups/{userGroupId}";
    public static final String USER_GROUPS_DUPLICATE                    = API + "/usergroups/{userGroupId}/commands/duplicate-check";


    public static final String USER_GROUP_USERS_ROLE_MAPS               = API + "/usergroups/{userGroupId}/map/users";
    public static final String USER_GROUPS_USER_ROLE_MAPS               = API + "/usergroups/{userId}/map/userGroups";

//    public static final String USER_GROUP_USER_MAPS                = API + "/usergroup/user/maps";
//    public static final String USER_GROUP_USER_MAPS_DETAIL        = USER_GROUP_USER_MAPS + "/{userId}/{userGroupId}";
//    public static final String USER_GROUP_USER_MAPS_USER            = USER_GROUP_USER_MAPS + "/{userId}";
//    public static final String USER_GROUP_USER_MAPS_USER_GROUP    = USER_GROUP_USER_MAPS + "/{userGroupId}";
//    public static final String USER_GROUP_USER_MAPS_USER            = USER_GROUP_USER_MAPS + "/userId";
//    public static final String USER_GROUP_USER_MAPS_USER_GROUP    = USER_GROUP_USER_MAPS + "/userGroupId";
//    public static final String USER_GROUP_USER_MAPS_DUPLICATE        = USER_GROUP_USER_MAPS + "/duplicate" + "/{userId}/{userGroupId}";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Role
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public static final String ROLES                                    = API + "/roles";
    public static final String ROLES_DETAIL                             = API + "/roles/{roleId}";
    public static final String ROLES_DETAIL_INFO                        = API + "/roles/{roleId}/commands/detail-with-mappings";
    public static final String ROLES_DUPLICATE                          = API + "/roles/{roleId}/commands/duplicate-check";
    public static final String ROLES_MAP_USERGROUPS                     = API + "/roles/{roleId}/map/usergroups";
    public static final String ROLES_MAP_USERS                          = API + "/roles/{roleId}/map/users";

    public static final String VIEW_ROLES                               = VIEW_BCM + "/roles";
    public static final String VIEW_ROLES_SEARCH                        = VIEW_BCM + "/roles/popups/select";
    public static final String VIEW_ROLES_CREATE                        = VIEW_BCM + "/roles/popups/create";
    public static final String VIEW_ROLES_EDIT                          = VIEW_BCM + "/roles/popups/edit";
    public static final String VIEW_ROLES_DETAIL                        = VIEW_BCM + "/roles/forms/map";


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Board, Article, Comment, AttachFile
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String BOARDS                                   = API + "/boards";
    public static final String BOARDS_DETAIL                            = API + "/boards/{boardId}";
    public static final String BOARDS_DUPLICATE                         = API + "/boards/{boardId}/commands/duplicate-check";

    public static final String ARTICLES                                 = API + "/articles";
    public static final String ARTICLES_CREATE                          = API + "/articles/{boardId}}popups/create";
    public static final String ARTICLES_DETAIL                          = API + "/articles/{articleId}";
    public static final String ARTICLES_MAP_COMMENTS                    = API + "/articles/{articleId}/map/comments";

    public static final String COMMENTS                                 = API + "/articles/comments";
    public static final String COMMENTS_DETAIL                          = API + "/articles/comments/{commentsId}";

    public static final String ATTACHFILES                              = API + "/attachfiles";
    public static final String ATTACHFILES_DETAIL                       = API + "/attachfiles/{fileId}";
    public static final String ATTACHFILES_TARGET                       = API + "/attachfiles/{targetId}/{targetType}/commands/list-match";

    public static final String FILES                                    = API + "/files";
    public static final String FILES_DETAIL                             = API + "/files/{fileId}";
    
    public static final String VIEW_BOARDS                              = VIEW_BCM + "/boards";
    public static final String VIEW_BOARDS_CREATE                       = VIEW_BCM + "/boards/popups/create";
    public static final String VIEW_BOARDS_UPDATE                       = VIEW_BCM + "/boards/popups/update";
    
    public static final String VIEW_ARTICLES_UPDATE_POPUP_DETAIL        = VIEW_BCM + "/boards/{boardId}/articles/{articleId}/popups/update";
    public static final String VIEW_ARTICLES_SAVE_POPUP_DETAIL          = VIEW_BCM + "/boards/{boardId}/articles/{articleId}/popups";
    public static final String VIEW_BOARD_ARTICLES_DETAIL               = VIEW_BCM + "/boards/{boardId}/articles/{articleId}";
    public static final String VIEW_ARTICLES_CREATE_POPUP               = VIEW_BCM + "/boards/{boardId}/articles/popups/create";
    public static final String VIEW_BOARD_ARTICLES                      = VIEW_BCM + "/boards/{boardId}/articles";


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Menu
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String MENUS                                    = API + "/menus";
    public static final String MENUS_TREE                               = API + "/menus/tree";
    public static final String MENUS_DETAIL                             = API + "/menus/{menuId}";
    public static final String MENUS_DUPLICATE                          = API + "/menus/{menuId}/commands/duplicate-check";
    public static final String MENUS_ROLE_MAP                           = API + "/menus/{menuId}/map/roles";

    public static final String VIEW_MENUS                               = VIEW_BCM + "/menus";
    public static final String VIEW_MENUS_CREATE                        = VIEW_BCM + "/menus/popups/create";
    public static final String VIEW_MENUS_UPDATE                        = VIEW_BCM + "/menus/popups/update";
    public static final String VIEW_MENUS_ROLE                          = VIEW_BCM + "/menus/popups/role";
    public static final String VIEW_MENUS_SELECT                        = VIEW_BCM + "/menus/popups/select";
    public static final String VIEW_MENUS_CHANGE                        = VIEW_BCM + "/menus/popups/change";


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Code
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    // Code Group
    public static final String CODE_GROUPS                              = API + "/codegroups";
    public static final String CODE_GROUPS_DETAIL                       = API + "/codegroups/{codeGroupIdBase64}";
    
    public static final String VIEW_CODE_GROUPS_CREATE                  = VIEW_BCM + "/codegroups/forms/create";
    public static final String VIEW_CODE_GROUPS_SELECT                  = VIEW_BCM + "/codegroups/popups/select";

    // Code
    public static final String CODES                                    = API + "/codes";
    public static final String CODES_DETAIL_LANG                        = API + "/codes/{codeGroupId}/commands/of-current-lang";

    public static final String VIEW_CODES                               = VIEW_BCM + "/codes";
    public static final String VIEW_CODES_CREATE                        = VIEW_BCM + "/codes/forms/create";
    public static final String VIEW_CODESLOV_POPUP_SELECT               = VIEW_BCM + "/code/codelov/popup/select";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Program
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String PROGRAMS                                 = API + "/programs";
    public static final String PROGRAMS_DETAIL                          = API + "/programs/{programId}";
    public static final String PROGRAMS_STARTING_WITH                   = API + "/programs/{programIdPrefix}/commands/list-starting-with";
    public static final String PROGRAMS_ID_CHECK                        = API + "/programs/{programIdPrefix}/commands/starts-with";

    // 프로그램 권한 맵
    public static final String PROGRAM_ROLE_MAPS                        = API + "/programs/commands/save-program-role-maps";

    public static final String VIEW_PROGRAMS                            = VIEW_BCM + "/programs";
    public static final String VIEW_PROGRAMS_SAVE                       = VIEW_BCM + "/programs/popups/save";
    public static final String VIEW_PROGRAMS_SELECT                     = VIEW_BCM + "/programs/popups/select";
    public static final String VIEW_PROGRAMS_ATTACH                     = VIEW_BCM + "/programs/popups/attach";
    public static final String VIEW_PROGRAMS_ROLE_SELECT                = VIEW_BCM + "/programs/popups/role";
    public static final String VIEW_PROGRAMS_ROLE                       = VIEW_BCM + "/programs/forms/role";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Message
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String MESSAGES                                 = API + "/messages";
    public static final String MESSAGES_DETAIL                          = API + "/messages/{messageKeyBase64}";

    public static final String VIEW_MESSAGES                            = VIEW_BCM + "/messages";
    public static final String VIEW_MESSAGES_SAVE                       = VIEW_BCM + "/messages/popups/save";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Element
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String VIEW_ELEMENT                             = VIEW_BCM + "/elements";
    public static final String VIEW_ELEMENT_CREATE                      = VIEW_BCM + "/elements/popups/create";
    public static final String VIEW_ELEMENT_UPDATE                      = VIEW_BCM + "/elements/popups/update";
    public static final String VIEW_ELEMENT_ROLE                        = VIEW_BCM + "/elements/role/{programId}";
    public static final String VIEW_ELEMENT_ROLE_POPUP                  = VIEW_BCM + "/elements/popups/select";

    public static final String ELEMENT                                  = API + "/elements/programs/{programId}";
    public static final String ELEMENT_DETAIL                           = API + "/elements/{elementSeq}/programs/{programId}";
    public static final String ELEMENT_DUPLICATE                        = API + "/elements/{elementKey}/programs/{programId}/commands/duplicate-check";

    public static final String ELEMENT_ROLE_MAPS                        = API + "/elements/programs/{programId}/map/roles";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Log
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public static final String LOGS                                     = API + "/logs";

    public static final String VIEW_SYSTEM_LOGS                         = VIEW_BCM + "/systemLogs";
    public static final String VIEW_SYSTEM_LOGS_POPUPS_DETAIL           = VIEW_BCM + "/systemLogs/popups/detail";
    public static final String VIEW_EVENT_LOGS                          = VIEW_BCM + "/eventLogs";
    public static final String VIEW_EVENT_LOGS_POPUPS_DETAIL            = VIEW_BCM + "/eventLogs/popups/detail";

}
