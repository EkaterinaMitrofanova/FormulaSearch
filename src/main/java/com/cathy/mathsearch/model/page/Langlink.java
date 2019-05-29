package com.cathy.mathsearch.model.page;

import com.google.gson.annotations.SerializedName;

public class Langlink {

    private String lang;
    private String url;
    private String langname;
    private String autonym;

    @SerializedName("*")
    private String title;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLangname() {
        return langname;
    }

    public void setLangname(String langname) {
        this.langname = langname;
    }

    public String getAutonym() {
        return autonym;
    }

    public void setAutonym(String autonym) {
        this.autonym = autonym;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
