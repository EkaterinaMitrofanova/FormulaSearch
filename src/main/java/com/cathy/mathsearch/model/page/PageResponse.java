package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class PageResponse {

    @SerializedName("parse")
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
