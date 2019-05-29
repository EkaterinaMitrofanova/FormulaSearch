package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class Page {

    private String title;
    private long pageid;
//    private long revid;
//    private String displaytitle;

    @SerializedName("text")
    private Text text;

//    @SerializedName("langlinks")
//    private List<Langlink> langlinks;
//
//    @SerializedName("categories")
//    private List<Category> categories;
//
//    @SerializedName("links")
//    private List<Link> links;
//
//    @SerializedName("templates")
//    private List<Template> templates;
//
//    @SerializedName("images")
//    private List<String> images;
//
//    @SerializedName("externallinks")
//    private List<String> externalLinks;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPageid() {
        return pageid;
    }

    public void setPageid(long pageid) {
        this.pageid = pageid;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
