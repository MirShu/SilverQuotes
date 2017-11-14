package com.quantpower.silverquotes.model;

/**
 * Created by ShuLin on 2017/6/1.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class LiveRoomModel {
    /**
     * code : 0
     * data : {"videosource":"http://ws.streamhls.huya.com/huyalive/17566963-17566963-75449531575042048-252404722-10057-A-1489396940-1_1200/playlist.m3u8"}
     * msg : 请求成功
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * videosource : http://ws.streamhls.huya.com/huyalive/17566963-17566963-75449531575042048-252404722-10057-A-1489396940-1_1200/playlist.m3u8
         */

        private String videosource;

        public String getVideosource() {
            return videosource;
        }

        public void setVideosource(String videosource) {
            this.videosource = videosource;
        }
    }

    /**
     * code : 0
     * data : ["http://hls.yy.com/newlive/16283558_16283558_10057.m3u8?org=huya&appid=0&uuid=38decf3301ca45cdb731f33fab6cdfa4&t=1484552305&tk=b44d5ae62b2d06e3025e262428bc2dc8&uid=0"]
     * msg : 请求成功
     */
}
