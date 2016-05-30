package com.zurich.mobile.model;

import java.util.List;

/**
 * Created by weixinfei on 16/5/27.
 */
public class GankInfo {


    /**
     * error : false
     * results : [{"_id":"5747be9c67765923298b5c8b","createdAt":"2016-05-27T11:27:24.288Z","desc":"夏天来了宝宝们","publishedAt":"2016-05-27T11:56:22.790Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f49s6i5pg7j20go0p043b.jpg","used":true,"who":"代码家"},{"_id":"5746702e6776594b0d64dc49","createdAt":"2016-05-26T11:40:30.357Z","desc":"5.26","publishedAt":"2016-05-26T11:52:42.430Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f48mxqcvkvj20lt0pyaed.jpg","used":true,"who":"daimajia "},{"_id":"57451a706776594b0bcff794","createdAt":"2016-05-25T11:22:24.607Z","desc":"5.25","publishedAt":"2016-05-25T11:50:54.367Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f47gspphiyj20ia0rf76w.jpg","used":true,"who":"代码家"},{"_id":"5743cde9677659453b01369a","createdAt":"2016-05-24T11:43:37.996Z","desc":"5.24","publishedAt":"2016-05-24T11:56:12.924Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f46bsdcls2j20sg0izac0.jpg","used":true,"who":"daimajia"},{"_id":"57426fbb6776590a0b0fe26d","createdAt":"2016-05-23T10:49:31.552Z","desc":"5.23","publishedAt":"2016-05-23T10:54:25.890Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034jw1f454lcdekoj20dw0kumzj.jpg","used":true,"who":"代码家"},{"_id":"573e6c776776591ca2f31ba5","createdAt":"2016-05-20T09:46:31.535Z","desc":"昨天妹子的正脸","publishedAt":"2016-05-20T10:05:09.959Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f41lxgc3x3j20jh0tcn14.jpg","used":true,"who":"代码家"},{"_id":"573d39ea6776591ca681f8c7","createdAt":"2016-05-19T11:58:34.715Z","desc":"5.19","publishedAt":"2016-05-19T12:09:29.617Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f40k4dyrhhj20iz0sg41b.jpg","used":true,"who":"daimajia"},{"_id":"573be98f6776591c9fd0cd5f","createdAt":"2016-05-18T12:03:27.865Z","desc":"518","publishedAt":"2016-05-18T12:18:37.235Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f3zen8idmkj20dw0kun0i.jpg","used":true,"who":"daimajia"},{"_id":"573a99ee6776591ca681f89f","createdAt":"2016-05-17T12:11:26.506Z","desc":"5.17","publishedAt":"2016-05-17T12:17:17.785Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f3y998rv5uj20m80vxq6c.jpg","used":true,"who":"张涵宇"},{"_id":"573943c06776591ca2f31b55","createdAt":"2016-05-16T11:51:28.480Z","desc":"5.16","publishedAt":"2016-05-16T11:58:08.802Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f3x32bd1hcj20d90k03zx.jpg","used":true,"who":"daimajia"},{"_id":"573542b66776591ca2f31b25","createdAt":"2016-05-13T10:57:58.667Z","desc":"5.13","publishedAt":"2016-05-13T11:08:37.42Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/7a8aed7bjw1f3tkjebzzpj20kg0v7q9h.jpg","used":true,"who":"xiaobei"},{"_id":"5732c06167765974fbfcfa53","createdAt":"2016-05-11T13:17:21.318Z","desc":"5.12","publishedAt":"2016-05-12T12:04:43.857Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f3rdepqtnij21kw2dc1cx.jpg","used":true,"who":"张涵宇"},{"_id":"5732b0bc67765974f5e27edd","createdAt":"2016-05-11T12:10:36.258Z","desc":"511","publishedAt":"2016-05-11T12:12:08.55Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034jw1f3rbikc83dj20dw0kuadt.jpg","used":true,"who":"daimajai"},{"_id":"57315e5d67765974fca83139","createdAt":"2016-05-10T12:06:53.983Z","desc":"510","publishedAt":"2016-05-10T12:14:26.447Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034jw1f3q5semm0wj20qo0hsacv.jpg","used":true,"who":"daimajia"},{"_id":"57300a5d67765974fca83128","createdAt":"2016-05-09T11:56:13.267Z","desc":"5.9","publishedAt":"2016-05-09T12:05:34.903Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f3ozv0wqywj20qo0em0vt.jpg","used":true,"who":"daimajia"},{"_id":"572c146a67765974f885c01e","createdAt":"2016-05-06T11:50:02.319Z","desc":"无版权","publishedAt":"2016-05-06T12:04:47.203Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f3litmfts1j20qo0hsac7.jpg","used":true,"who":"代码家"},{"_id":"572aa3ea67765974fca830f4","createdAt":"2016-05-05T09:37:46.142Z","desc":"5.5","publishedAt":"2016-05-05T12:14:21.156Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f3k9dp8r9qj20dw0jljtd.jpg","used":true,"who":"张涵宇"},{"_id":"5729794967765974fca830e7","createdAt":"2016-05-04T12:23:37.334Z","desc":"5.4","publishedAt":"2016-05-04T12:26:03.894Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bgw1f3j8jt6qn8j20vr15owvk.jpg","used":true,"who":"张涵宇"},{"_id":"5722b27b67765974fbfcf9b9","createdAt":"2016-04-29T09:01:47.962Z","desc":"4.29","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bgw1f3damign7mj211c0l0dj2.jpg","used":true,"who":"张涵宇"},{"_id":"5721791f67765974fbfcf9a8","createdAt":"2016-04-28T10:44:47.43Z","desc":"4.28","publishedAt":"2016-04-28T13:07:51.7Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/7a8aed7bjw1f3c7zc3y3rj20rt15odmp.jpg","used":true,"who":"张涵宇"}]
     */

    private boolean error;
    /**
     * _id : 5747be9c67765923298b5c8b
     * createdAt : 2016-05-27T11:27:24.288Z
     * desc : 夏天来了宝宝们
     * publishedAt : 2016-05-27T11:56:22.790Z
     * source : chrome
     * type : 福利
     * url : http://ww4.sinaimg.cn/large/610dc034jw1f49s6i5pg7j20go0p043b.jpg
     * used : true
     * who : 代码家
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
