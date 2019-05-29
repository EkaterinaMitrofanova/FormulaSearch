package com.cathy.mathsearch.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Continue {

    @SerializedName("sroffset")
    @Expose
    private Integer sroffset;
    @SerializedName("continue")
    @Expose
    private String _continue;

    public Integer getSroffset() {
        return sroffset;
    }

    public void setSroffset(Integer sroffset) {
        this.sroffset = sroffset;
    }

    public String getContinue() {
        return _continue;
    }

    public void setContinue(String _continue) {
        this._continue = _continue;
    }
}
