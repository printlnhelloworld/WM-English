package com.example.yzt.wm_english.main;

import java.util.List;

/**
 * Created by YZT on 2017/3/2.
 */

public class Mainres {

    public int pageNum;

    public int pageSize;

    public List<Image> images;

    public List<Resource> resources;

    public static class Image{
        public int id;
        public String imgUrl;
        public String resUrl;
    }
    public static class Resource{
        public int id;
        public String title;
        public String elaborate;
        public int wordNum;
        public String imgUrl;
        public String resUrl;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setElaborate(String elaborate) {
            this.elaborate = elaborate;
        }

        public String getElaborate() {
            return elaborate;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setWordNum(int wordNum) {
            this.wordNum = wordNum;
        }

        public int getWordNum() {
            return wordNum;
        }

        public void setResUrl(String resUrl) {
            this.resUrl = resUrl;
        }

        public String getResUrl() {
            return resUrl;
        }
    }
}
