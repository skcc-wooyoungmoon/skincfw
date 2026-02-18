/*
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
$a.page(function() {
    // 초기화 함수
    this.init = function(id, param) {
        SKIAF.ui.init();
    };
});
(function($) {
    var module;
    if (!window.SKIAF) {
        window.SKIAF = {};
        module = window.SKIAF.ui = {};
    } else {
        module = window.SKIAF.ui = {};
    }
    module.init = function() {
        module.searchToggle();
        module.inputRemove();
        module.popup();
        module.roleTab(); 
        module.lnb();
        module.loginBox();
        module.customProgress();
    };
    
    module.lnb = function() { //lnb        
        $('.lnb>li').each(function(){
            var num = $(this).find('ul').length;
            if(num > 0){
               $(this).addClass('hasSub');
            }
        });
        $('.lnb>li>ul>li').each(function(){
            var num = $(this).find('ul').length;
            if(num > 0){
               $(this).addClass('hasSub');
            }
        });
        $('.lnb li.active').parents('ul').show();
        $('.lnb li.active').parents('li').addClass('active');
        $('.lnbArea .lnb>li>ul>li.hasSub>a').on('click',function(){
            $(this).parent().siblings('li').removeClass('active');
            $(this).parent().siblings('li').find('ul').slideUp(200);
            $(this).parent().toggleClass('active');
            $(this).next().slideToggle(200);
        });
        $('.lnbArea .lnb>li.hasSub>a').on('click',function(){
            $(this).parent().siblings('li').removeClass('active');
            $(this).parent().siblings('li').find('ul').slideUp(200);
            $(this).parent().toggleClass('active');
            $(this).next().slideToggle(200);
        });
//////////
        $('.gnb > ul > li > a').on('mouseover',function(){
            $(this).parent().siblings('li').removeClass('over').find('ul').slideUp(200);
            $(this).parent().addClass('over');
            $(this).parent().siblings('li');
            $(this).next().slideDown(200);
        });
        $("#header").on('mouseleave',function(){
            $('.navMenu').find('ul').slideUp(200);
            $('.gnb > ul > li').removeClass('over');
            $('.gnb > ul > li > ul > li').removeClass('over');
        });
        $('.gnb > ul > li > ul > li > a').on('mouseover',function(){
            $(this).parent().siblings('li').removeClass('over');
            $(this).parent().addClass('over');
            $(this).parent().siblings('li').find('ul').slideUp(100);
            $(this).next().slideDown(100);
        });
    };
    module.loginBox = function() { //로그인 화면 height
        if( $('.login-box').length > 0 ){
            var cur = $(window).height();
            $(window).on('resize', function() {
                if(cur !== $(window).height()){
                    loginBoxHeight($(window).height());
                }
            });
            loginBoxHeight($(window).height());
        }
        function loginBoxHeight(h) {
            var winHeight = h,
                set = 200;
            if( h > 800){
                set = (winHeight - ($('footer').outerHeight(true)+$('.login-box').outerHeight(true)))/2;
            }else{
                set = (800 - ($('footer').outerHeight(true)+$('.login-box').outerHeight(true)))/2;
            }
            $('.skiaf-wrap-login').css({
                'padding-top' : set+'px',
                'padding-bottom': set+'px'
            });
        }
    };
    module.searchToggle = function() { //상세검색
        var $search = $('.skiaf-ui-search').parent().find('.open'),
            $searchclose = $('.skiaf-ui-searchdetail .close'),
            $searchDiv = $('.skiaf-ui-search').parent().parent().next();
        $search.on('click', open);
        $searchclose.on('click', close);

        function open(e) {
            if ($searchDiv.hasClass('visible')) {
                $search.addClass('close');
                $searchDiv.removeClass('visible');
            } else {
                $search.removeClass('close');
                $searchDiv.addClass('visible');
            }
        }

        function close(e) {
            $searchDiv.removeClass('visible');
        }
    };
    module.inputRemove = function() { //input value 초기화
        var btn = '<button class="Icon Remove-sign remove"></button>';
        var $input = 'input:not([type]), input[type="text"],input[type="password"]';
        
        $(document).on('keyup.inputRemove change.inputRemove', $input, removeToggle);
        $($input).each(function(i) {
            if ($(this).next().hasClass('remove')) {
                if ($(this).val().length > 0) {
                    $(this).next().css('visibility', 'visible');
                } else {
                    $(this).next().css('visibility', 'hidden');
                }
            }
        });

        function removeToggle(e) {
            $('.alopexgrid-search-plugin-section').each(function(i){
                if( $(this).find('.remove').length < 1 ){
                    var btn_remove = $(btn).insertAfter($(this).find('input'));                 
                }
            });
            
            setPos($($input));
        }
        $(document).on('click.inputRemove', '.Icon.remove', remove);

        function remove(e) {
            if ($(this).prev().is('input:not([type]), input[type="text"],input[type="password"]')) {
                $(this).prev().val("");
                $(this).prev().focus();
                $(this).css('visibility', 'hidden');
            }
        }
        var wp = $(window).width();
        $(window).on('resize.inputRemove', function(){
            if(wp !== $(window).width()){
                setPos($($input));
            }
        });
        
        function setPos(input){
            input.each(function(k){
                if ($(this).next().hasClass('remove')) {
                    if ($(this).val().length > 0) {
                        var left = $(this).position().left+$(this).width()+20;
                        if($(this).parent().hasClass('alopexgrid-search-plugin-section')){
                            left = $(this).position().left+$(this).width()+25;
                        }
                        $(this).next().css({
                            'visibility': 'visible',
                            'top': $(this).position().top+'px',
                            'left': left+'px'
                        });

                    } else {
                        $(this).next().css('visibility', 'hidden');
                    }
                }                   
            });
        }
    };
    module.popup = function() { // popup        
        function popPos(){          
            var i;
            var len = Object.keys($a.popup.config).length;           
            for(i = 0; i<len; i++){
                var height = $a.popup.config[Object.keys($a.popup.config)[i]].height;
                if( height > $(window).height() ){
                    $a.popup.config[Object.keys($a.popup.config)[i]].top = 0;
                }
            }
        }
        var hei = $(window).height();
        $(window).on('resize',function(e){
            if($(window).height() !== hei){
                popPos();
            }
        });
        $(document).on('click.pop',function(e){
            popPos();           
        });
    };

    module.roleTab = function() { //권한타입에 따른 tab show/hide
        var $select = $('select.role-select');
        $select.on('change', roleSelect);

        function roleSelect(e) {
            var $div = $('div.role-select'),
                $setdiv = $div.filter("." + $(this).val());
            $div.eq(0).find('.af-tabs-content:eq(0)').data('gird', true);
            $div.hide();
            $setdiv.show();
            if (!$setdiv.find('.af-tabs-content:eq(0)').data('gird')) {
                $setdiv.find('.af-tabs-content:eq(0)').data('gird', true);
                $('#' + $setdiv.find('.af-tabs-content:eq(0) .skiaf-grid>div').attr('id')).alopexGrid();
            }
        }
    };

    module.customProgress = function() { //alopex progress custom
        var createCircleProgress = function() {
            this.progress = document.createElement('div');
            this.progress.style.position = 'absolute';
            this.progress.style.width = '80px'; //이미지 사이즈
            this.progress.style.height = '81px';
            this.progress.style.zIndex = 99991;         
            var size = this.generateSize();
            
            this.progress.style.backgroundImage = 'url(/static/skiaf/images/loading.gif)';
            this.progress.style.left = size.left + $(this.overlay).innerWidth()/2 - 40 + 'px';
            this.progress.style.top = size.top + $(this.overlay).innerHeight()/2 - 40 + 'px';
            $(this.progress).fadeIn(300).insertAfter(this.overlay); //dim
            $(this.progress).text('Loading...').css({
                'background-repeat': 'no-repeat',
                'padding-top': '85px',
                'font-weight': 'bold',
                'text-align': 'center'
            });         
        };
        var resizeCircleProgress = function() { //필요 없을 시 삭제
            var size = this.generateSize();
            this.progress.style.left = size.left + $(this.overlay).innerWidth()/2 - 40 + 'px';
            this.progress.style.top = size.top + $(this.overlay).innerHeight()/2 - 40 + 'px';
        };
        var removeCircleProgress = function() {
            var that = this;
            var dur = this.option.durationOff ? this.option.durationOff :
                this.duration;
            $(this.progress).fadeOut(dur, function() {
                if(that.ptimer) {
                    clearInterval(that.ptimer);
                    that.ptimer = null;
                }
                $(that.progress).remove();
                that.progress = null;
            });
        };
        AlopexOverlay.defaultOption.createProgress = createCircleProgress;
        AlopexOverlay.defaultOption.resizeProgress = resizeCircleProgress;
        AlopexOverlay.defaultOption.removeProgress = removeCircleProgress;

    };
}(jQuery));
