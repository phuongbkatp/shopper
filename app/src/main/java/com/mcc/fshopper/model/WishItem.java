package com.mcc.fshopper.model;

/**
 * Created by Nasir on 7/11/17.
 */

public class WishItem {
    public String productId;
    public String name;
    public String images;
    public String price;
    public int likeCount;

    public WishItem(String productId, String name, String images, String price, int likeCount){
        this.productId = productId;
        this.name = name;
        this.images = images;
        this.price = price;
        this.likeCount = likeCount;
    }
}
