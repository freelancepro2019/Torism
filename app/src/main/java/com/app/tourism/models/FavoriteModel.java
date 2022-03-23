package com.app.tourism.models;

import java.io.Serializable;

public class FavoriteModel implements Serializable {
    private String id;
    private String user_id;
    private String guide_id;
    private String guide_name;
    private String guide_phone;

    public FavoriteModel() {
    }

    public FavoriteModel(String id, String user_id, String guide_id, String guide_name, String guide_phone) {
        this.id = id;
        this.user_id = user_id;
        this.guide_id = guide_id;
        this.guide_name = guide_name;
        this.guide_phone = guide_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(String guide_id) {
        this.guide_id = guide_id;
    }

    public String getGuide_name() {
        return guide_name;
    }

    public void setGuide_name(String guide_name) {
        this.guide_name = guide_name;
    }

    public String getGuide_phone() {
        return guide_phone;
    }

    public void setGuide_phone(String guide_phone) {
        this.guide_phone = guide_phone;
    }
}
