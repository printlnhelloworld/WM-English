package com.example.yzt.wm_english.main.listening.video;

import java.util.List;

/**
 * Created by YZT on 2017/3/24.
 */

public class VideoRes {
    public String title;
    public String resUrl;
    public String cover;
    public List<comment> comment;
    public static class comment {
        public String userName;
        public String userIcon;
        public String comment;
        public String commentTime;
        public String praiseNum;
    }

}
