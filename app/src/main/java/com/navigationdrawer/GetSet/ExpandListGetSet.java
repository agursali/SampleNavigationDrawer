package com.navigationdrawer.GetSet;

/**
 * Created by AmolGursali on 9/11/2018.
 */

public class ExpandListGetSet
{
    String title;
    int img;

    public ExpandListGetSet() {
    }

    public ExpandListGetSet(String title, int img) {
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
