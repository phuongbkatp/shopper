package com.mcc.fshopper.model;

import java.io.Serializable;

/**
 * Created by Nasir on 3/29/17.
 */

public class OrderModel implements Serializable {

    private String productDetails;
    private String productNote;
    private String shippingMethod;
    private String paymentMethod;
    private String transactionId;

    private UserModel userModel;

    // from product details page
    public OrderModel(String productDetails, String productNote) {
        this.productDetails = productDetails;
        this.productNote = productNote;
    }


    public String getProductDetails() {
        return productDetails;
    }

    public String getProductNote() {
        return productNote;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    // from address page
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public void setProductNote(String productNote) {
        this.productNote = productNote;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
