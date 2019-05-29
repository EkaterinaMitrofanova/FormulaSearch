package com.cathy.mathsearch.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Query {

    @SerializedName("searchinfo")
    @Expose
    private SearchInfo searchinfo;
    @SerializedName("search")
    @Expose
    private List<Search> search = null;

    public SearchInfo getSearchinfo() {
        return searchinfo;
    }

    public void setSearchinfo(SearchInfo searchinfo) {
        this.searchinfo = searchinfo;
    }

    public List<Search> getSearch() {
        return search;
    }

    public void setSearch(List<Search> search) {
        this.search = search;
    }
}
