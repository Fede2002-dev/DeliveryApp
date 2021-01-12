
package com.balran.deliveryapp.retrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable {

    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("restorant_name")
    @Expose
    private String restorantName;
    @SerializedName("idRestorant")
    @Expose
    private String idRestorant;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("buys")
    @Expose
    private String buys;
    @SerializedName("food_description")
    @Expose
    private String foodDescription;
    @SerializedName("idfood")
    @Expose
    private String idfood;
    @SerializedName("stock")
    @Expose
    private String stock;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Food() {
    }

    /**
     * 
     * @param foodName
     * @param buys
     * @param foodDescription
     * @param idRestorant
     * @param restorantName
     * @param price
     * @param imageUrl
     * @param idfood
     * @param categoryName
     */
    public Food(String foodName, String categoryName, String restorantName, String idRestorant, String price, String imageUrl, String buys, String foodDescription, String idfood, String stock) {
        super();
        this.foodName = foodName;
        this.categoryName = categoryName;
        this.restorantName = restorantName;
        this.idRestorant = idRestorant;
        this.price = price;
        this.imageUrl = imageUrl;
        this.buys = buys;
        this.foodDescription = foodDescription;
        this.idfood = idfood;
        this.stock = stock;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRestorantName() {
        return restorantName;
    }

    public void setRestorantName(String restorantName) {
        this.restorantName = restorantName;
    }

    public String getIdRestorant() {
        return idRestorant;
    }

    public void setIdRestorant(String idRestorant) {
        this.idRestorant = idRestorant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBuys() {
        return buys;
    }

    public void setBuys(String buys) {
        this.buys = buys;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getIdfood() {
        return idfood;
    }

    public void setIdfood(String idfood) {
        this.idfood = idfood;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
