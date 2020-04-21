package com.tripshots.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class blog_response {

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("items")
    @Expose
    private List<blog_item> items;

    @SerializedName("etag")
    @Expose
    private String etag;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<blog_item> getItems() {
        return items;
    }

    public void setItems(List<blog_item> items) {
        this.items = items;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}
