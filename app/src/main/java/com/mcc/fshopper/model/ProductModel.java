package com.mcc.fshopper.model;

import java.util.ArrayList;

/**
 * Created by ashiq on 11/2/2017.
 */

public class ProductModel {

    private String id;
    private String title;
    private String price;
    private String details;
    private String location;
    private ArrayList<String> imageList;
    private String message;
    private String link;
    private String type;
    private int likeCount;
    private String createDate;
    private String updateDate;
    private String sellerId;

    public ProductModel(String id, String title, String price, String details, String location, ArrayList<String> imageList, String message,
                        String link, String type, int likeCount, String createDate, String updateDate, String sellerId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.details = details;
        this.location = location;
        this.imageList = imageList;
        this.message = message;
        this.link = link;
        this.type = type;
        this.likeCount = likeCount;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.sellerId = sellerId;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }
    public String getSellerId() {
        return sellerId;
    }
}
