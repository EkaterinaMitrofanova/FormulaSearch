package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class Text {

    @SerializedName("*")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
