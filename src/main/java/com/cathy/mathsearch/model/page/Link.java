package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class Link {

    private int ns;
    private String exists;

    @SerializedName("*")
    private String name;

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
