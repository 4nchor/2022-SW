package com.libienz.se_2022_closet.startApp_1.data;

import java.util.ArrayList;

public class Cody {
    private String CodyKey;
    private ArrayList<String> CodyComp; //코디를 구성하는 의류들의 ClothesKey를 저장하는 ArrayList
    private ArrayList<String> CodyTag;
    private Boolean isFavoriteCody;

    public Cody(String CodyKey, ArrayList<String> CodyComp, ArrayList<String> CodyTag, Boolean isFavoriteCody) {
        this.CodyKey = CodyKey;
        this.CodyComp = CodyComp;
        this.CodyTag = CodyTag;
        this.isFavoriteCody = isFavoriteCody;
    }

    public Cody () {}

    //getter
    public String getCodyKey() { return CodyKey; }
    public ArrayList<String> getCodyComp() { return CodyComp; }
    public ArrayList<String> getCodyTag() { return CodyTag; }
    public Boolean getIsFavoriteCody(){ return isFavoriteCody; }

    //setter
    public void setCodyKey(String CodyKey) { this.CodyKey = CodyKey; }
    public void setCodyComp(ArrayList<String> CodyComp) { this.CodyComp = CodyComp; }
    public void setCodyTag(ArrayList<String> CodyTag) { this.CodyTag = CodyTag; }
    public void setIsFavoriteCody(Boolean isFavoriteCody){ this.isFavoriteCody = isFavoriteCody; }

}