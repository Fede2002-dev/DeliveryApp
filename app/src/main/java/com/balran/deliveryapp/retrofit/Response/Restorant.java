
package com.balran.deliveryapp.retrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Restorant implements Serializable {

    @SerializedName("idrestorant")
    @Expose
    private String idrestorant;
    @SerializedName("restorant_name")
    @Expose
    private String restorantName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("restorant_address")
    @Expose
    private String restorantAddress;
    @SerializedName("restorant_photo")
    @Expose
    private String restorantPhoto;
    @SerializedName("restorant_logo")
    @Expose
    private String restorantLogo;
    @SerializedName("restorant_stars")
    @Expose
    private String restorantStars;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Restorant() {
    }

    /**
     * 
     * @param restorantName
     * @param restorantLogo
     * @param idrestorant
     * @param restorantAddress
     * @param userId
     * @param restorantPhoto
     * @param restorantStars
     */
    public Restorant(String idrestorant, String restorantName, String userId, String restorantAddress, String restorantPhoto, String restorantLogo, String restorantStars) {
        super();
        this.idrestorant = idrestorant;
        this.restorantName = restorantName;
        this.userId = userId;
        this.restorantAddress = restorantAddress;
        this.restorantPhoto = restorantPhoto;
        this.restorantLogo = restorantLogo;
        this.restorantStars = restorantStars;
    }

    public String getIdrestorant() {
        return idrestorant;
    }

    public void setIdrestorant(String idrestorant) {
        this.idrestorant = idrestorant;
    }

    public String getRestorantName() {
        return restorantName;
    }

    public void setRestorantName(String restorantName) {
        this.restorantName = restorantName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestorantAddress() {
        return restorantAddress;
    }

    public void setRestorantAddress(String restorantAddress) {
        this.restorantAddress = restorantAddress;
    }

    public String getRestorantPhoto() {
        return restorantPhoto;
    }

    public void setRestorantPhoto(String restorantPhoto) {
        this.restorantPhoto = restorantPhoto;
    }

    public String getRestorantLogo() {
        return restorantLogo;
    }

    public void setRestorantLogo(String restorantLogo) {
        this.restorantLogo = restorantLogo;
    }

    public String getRestorantStars() {
        return restorantStars;
    }

    public void setRestorantStars(String restorantStars) {
        this.restorantStars = restorantStars;
    }

}
