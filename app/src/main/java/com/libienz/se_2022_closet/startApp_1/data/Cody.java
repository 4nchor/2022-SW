package com.libienz.se_2022_closet.startApp_1.data;

import java.util.ArrayList;

public class Cody {
    private String CodyKey;
    private ArrayList<Clothes> CodyComp;
    private ArrayList<String> CodyTag;

    Cody (String CodyKey, ArrayList<Clothes> CodyComp, ArrayList<String> CodyTag) {
        this.CodyKey = CodyKey;
        this.CodyComp = CodyComp;
        this.CodyTag = CodyTag;
    }

    //getter
    public String getCodyKey() { return CodyKey; }
    public ArrayList<Clothes> getCodyComp() { return CodyComp; }
    public ArrayList<String> getCodyTag() { return CodyTag; }

    //setter
    public void setCodyKey(String CodyKey) { this.CodyKey = CodyKey; }
    public void setCodyComp(ArrayList<Clothes> CodyComp) { this.CodyComp = CodyComp; }
    public void setCodyTag(ArrayList<String> CodyTag) { this.CodyTag = CodyTag; }
}