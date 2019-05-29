package com.cathy.mathsearch.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchInfo {

    @SerializedName("totalhits")
    @Expose
    private Integer totalhits;

    public Integer getTotalhits() {
        return totalhits;
    }

    public void setTotalhits(Integer totalhits) {
        this.totalhits = totalhits;
    }
}
