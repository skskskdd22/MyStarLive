package com.example.owner.mystarlive;

public class Vod {
    private String vodid, vodtitle, imgURL;

    public String getImgURL(){
        return imgURL;
    }

    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }

    public String getvodid() {
        return vodid;
    }

    public void setvodid(String vodid) {
        this.vodid = vodid;
    }

    public String getvodtitle() {
        return vodtitle;
    }

    public void setvodtitle(String vodtitle) {
        this.vodtitle = vodtitle;
    }
}
