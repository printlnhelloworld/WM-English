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
        public String wordNum;
        public String imgUrl;
        public String resUrl;
    }
}
