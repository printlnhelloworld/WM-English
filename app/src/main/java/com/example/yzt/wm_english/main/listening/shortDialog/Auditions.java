package com.example.yzt.wm_english.main.listening.shortDialog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YZT on 2017/3/13.
 */

public class Auditions {
    @SerializedName("resources")
    public List<Audition> audition;

    public static class Audition{
        public int id;
        public String title;
        public String imgPath;
        public String resUrl;
        public String type;
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

        public void setResUrl(String resUrl) {
            this.resUrl = resUrl;
        }

        public String getResUrl() {
            return resUrl;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getImgPath() {
            return imgPath;
        }
    }
}
