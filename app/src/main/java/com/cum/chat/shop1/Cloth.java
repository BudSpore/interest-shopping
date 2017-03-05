package com.cum.chat.shop1;

import android.graphics.Bitmap;

/**
 * Created by 2-1Ping on 2017/2/16.
 */

public class Cloth {
    private Bitmap img;
    private String title;

    public Cloth(Bitmap img, String title) {
        this.img = img;
        this.title = title;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
