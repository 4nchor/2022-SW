package com.libienz.se_2022_closet.startApp_1.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Clothes {
    private String img;
    private ArrayList<String> tag;
    private String ClothesInfo;

    public Clothes(String img, ArrayList<String> tag, String ClothesInfo){
        this.img = img;
        this.tag = tag;
        this.ClothesInfo = ClothesInfo;
    }

    public Clothes (){}

    //getter
    public String getimg(){
        return img;
    }
    public ArrayList<String> gettag(){ return tag; }
    public String getClothesInfo(){
        return ClothesInfo;
    }

    //setter
    public void setimg(String img){
        this.img = img;
    }
    public void settag(ArrayList<String> tag){ this.tag = tag; }
    public void setClothesInfo(String ClothesInfo){
        this.ClothesInfo = ClothesInfo;
    }
}
