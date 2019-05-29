package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class Category {

    private String sortkey;

    @SerializedName("*")
    private String name;

    public String getSortkey() {
        return sortkey;
    }

    public void setSortkey(String sortkey) {
        this.sortkey = sortkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
