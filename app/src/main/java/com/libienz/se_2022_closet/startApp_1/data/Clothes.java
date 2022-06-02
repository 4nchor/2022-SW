
package com.libienz.se_2022_closet.startApp_1.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Clothes {
    private String ClothesImg;
    private ArrayList<String> ClothesTag;
    private String ClothesInfo;
    private String ClothesKey;

    public Clothes(String ClothesImg, ArrayList<String> ClothesTag, String ClothesInfo, String ClothesKey){
        this.ClothesImg = ClothesImg;
        this.ClothesTag = ClothesTag;
        this.ClothesInfo = ClothesInfo;
        this.ClothesKey = ClothesKey;
    }

    public Clothes (){}

    //getter
    public String getClothesImg(){
        return ClothesImg;
    }
    public ArrayList<String> getClothesTag(){ return ClothesTag; }
    public String getClothesInfo(){
        return ClothesInfo;
    }
    public String getClothesKey() { return ClothesKey; }

    //setter
    public void setClothesImg(String img){
        this.ClothesImg = img;
    }
    public void setClothesTag(ArrayList<String> tag){ this.ClothesTag = tag; }
    public void setClothesInfo(String ClothesInfo){
        this.ClothesInfo = ClothesInfo;
    }
    public void setClothesKey(String ClothesKey) { this.ClothesKey = ClothesKey; }
}