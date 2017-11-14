package com.quantpower.silverquotes.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class NewsModel implements Serializable {
    private static final long serialVersionUID = -5992280568359860471L;
    /**
     * "id": "123",
     * "news_title": "学者：中国企业走出去 非商业风险或决定投资成败",
     * "news_time": "2016-11-25 14:41:01",
     * "news_by": "第一财经",
     * "news_img": "http://imgcdn.yicai.com/uppics/slides/2016/11/636156791710489336.jpg@200w_150h_1e_1c"
     * news_content
     * collstate
     * <p>
     * "id": "150784",
     * "news_title": "“顺风车”消失！OPEC下半年减产难度加大？",
     * "news_time": "2017-05-17 19:51:44",
     * "news_by": "",
     * "news_img": "http://upload.fx678.com/upload/ht/20170517/sl_2017051719120021.jpg",
     * <p>
     * "news_group_type": "9",
     * "news_readcount": "2.4k",
     * "news_time_show": "2小时前",
     * "news_type": "原油"
     */
    private String news_group_type;
    private String news_readcount;

    public String getNews_group_type() {
        return news_group_type;
    }

    public void setNews_group_type(String news_group_type) {
        this.news_group_type = news_group_type;
    }

    public String getNews_readcount() {
        return news_readcount;
    }

    public void setNews_readcount(String news_readcount) {
        this.news_readcount = news_readcount;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    private String news_type;

    private int id;
    private String news_title;
    private String news_time;
    private String news_time_show;
    private String news_by;
    private String news_img;
    private String news_content;
    private String news_tips;
    private String collstate;
    private String description;
    private String location;
    private String webview;
    private String jquery;
    private String news_detail_url;
    private String menu_url;

    public String getNews_detail_url() {
        return news_detail_url;
    }

    public void setNews_detail_url(String news_detail_url) {
        this.news_detail_url = news_detail_url;
    }


    public String getWebview() {
        return webview;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public void setWebview(String webview) {
        this.webview = webview;
    }

    public String getJquery() {
        return jquery;
    }

    public void setJquery(String jquery) {
        this.jquery = jquery;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollstate() {
        return collstate;
    }

    public void setCollstate(String collstate) {
        this.collstate = collstate;
    }

    public String getNews_tips() {
        return news_tips;
    }

    public void setNews_tips(String news_tips) {
        this.news_tips = news_tips;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }


    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
    }

    public String getNews_by() {
        return news_by;
    }

    public void setNews_by(String news_by) {
        this.news_by = news_by;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }


    public String getNews_time_show() {
        return news_time_show;
    }

    public void setNews_time_show(String news_time_show) {
        this.news_time_show = news_time_show;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * @param result
     * @return
     */
    public static NewsModel parse(String result) {
        NewsModel userModel = new NewsModel();
        try {
            userModel = JSON.parseObject(result, NewsModel.class);
        } catch (Exception e) {
            String error = e.getMessage();

        }
        return userModel;
    }
}
