package com.quantpower.silverquotes.constant;

import java.text.MessageFormat;

/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class URLS {
    /**
     * 基路径
     */
//    public static final String BASE_SERVER_URL = "http://118.178.121.211/Api/";
    public static final String BASE_SERVER_URL = "http://api.quant-power.com/";


    /**
     * 行情植入JS代码
     * $('.top.div').css({'position':'fixed','width':'100%','z-index':'99999'});\n" +
     * // "$('.top div').css('background-color','#1892f3');
     * <p>
     * <p>
     * 修改高度 $('.top div').css({'height':'44px','line-height':'3.6rem','font-size':'1.5rem'});
     * $('.top .top_fenl').css('height','3.6rem');
     * $('.top_xl ').css('top','3.7rem');
     */
    public static final String OLDJSCODE = "javascript:$('.top .top_back').next().html('行情数据');" +
            "$('.footer').html('').css({'margin':'0','padding':'0'});" +
            "$('.wt_tt').remove();" +
            "$('.top_xl .cr h2').remove();" +
            "$('.top_xl .cr ul').each(function(){$('.top_xl .cr ul').eq(1).remove();});" +
            "$('.top.div').css({'position':'fixed','width':'100%','z-index':'99999'});" +
            "$('.top div').css('background-color','#ffffff');$('.top_back').hide();" +
            "$('.top_xl .cr .cur').remove();" +
            "$('tspan').each(function(){if($(this).text() == 'www.jyh007.com'){$(this).hide();}});" +
            "$('#aabbcc').hide();" +
            "var str;var temp;var temp1;var temp2;" +
            "$('a').each(function(){str = $(this).attr('href');" +
            "temp = str.indexOf('ls');" +
            "temp1 = str.indexOf('xnjj120');" +
            "temp2 = str.indexOf('zgqlxw');" +
            "if(temp>0){$(this).hide();}" +
            "if(temp1>0){$(this).hide();}" +
            "if(temp2>0){$(this).hide();}});" +
            "$('.top div').css({'height':'40px','line-height':'3.6rem','font-size':'1.5rem','margin-bottom':'20px'});" +
            "$('.top .top_fenl').css('height','3.6rem');" +
            "$('.top_xl ').css('top','3.7rem');" +
            "$('.top_xl').css('padding-top','10px');" +
            "$('.main_zst').css({'position':'relative','width':'100%','height':'100%'}).append(\"<div class='lanlzkiddiv'></div>\");\n" +
            "$('.lanlzkiddiv').css({'z-index':'999999','position':'absolute','top':'0','left':'0','width':'100%','height':'100%'});\n" +
            "$('.main_rank').css({'z-index':'999999999'});";


    public static final String JSCODE = "javascript: " +
            "$('.side_menu_box').remove();" +
            " $('.top_xl').remove();" +
            " $('.huadong_box').remove();" +
            " $('.down_app').remove();" +
            " $('.container').css('padding-bottom','0');" +
            " $('.footer').remove();" +
            " $('.totop').remove();" +
            "$('.menu_two_list').css('top','0');" +
            " $('body').append('<style>svg>text:nth-last-child(1){display:none !important}</style>');";


    /**
     * 邀请好友流程图储存本地路径
     */
    public static final String IM_INVITE_PROCESS = "file:///android_asset/back_invite_process.png";
    /**
     * 新闻列表
     */
    public static final String NEWS_NEWS_LIST = "http://api.quant-power.com/News/NewsList";

    /**
     * 行情JS代码
     */
    public static final String MARKET_GETMARKET_JS = MessageFormat.format("{0}Market/getMarketJs", BASE_SERVER_URL);

    /**
     * 资讯详情页相关资讯列表
     */
    public static final String NEWS_NEWSRELEVANT = MessageFormat.format("{0}News/NewsRelevant", BASE_SERVER_URL);
    /**
     * 新闻资讯详情
     */
    public static final String NEWS_NEWSINFO = MessageFormat.format("{0}News/NewsInfo", BASE_SERVER_URL);
    /**
     * 直播房间
     */
    public static final String LIVE_GET_LIVEVIDEO = BASE_SERVER_URL + "Live/getVideoSource";
}
