package com.jeffreyfhow.fakestagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("standard_resolution")
    @Expose
    private Image imgUrlStd;

    @SerializedName("thumbnail")
    @Expose
    private Image imgUrlThumb;

    @SerializedName("low_resolution")
    @Expose
    private Image imgLowRes;

    public Images(Image imgUrlStd, Image imgUrlThumb, Image imgLowRes) {
        this.imgUrlStd = imgUrlStd;
        this.imgUrlThumb = imgUrlThumb;
        this.imgLowRes = imgLowRes;
    }

    public Image getImgUrlStd() {
        return imgUrlStd;
    }

    public Image getImgUrlThumb() {
        return imgUrlThumb;
    }

    public Image getImgLowRes() {
        return imgLowRes;
    }
}
