
package com.balran.deliveryapp.retrofit.Request;

import java.util.List;

import com.balran.deliveryapp.retrofit.Response.Food;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("foods")
    @Expose
    private List<Food> foods = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Email() {
    }

    /**
     * 
     * @param total
     * @param address
     * @param foods
     * @param email
     */
    public Email(String email, String address, String total, List<Food> foods) {
        super();
        this.email = email;
        this.address = address;
        this.total = total;
        this.foods = foods;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

}
