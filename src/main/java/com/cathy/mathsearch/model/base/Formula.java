package com.cathy.mathsearch.model.base;

import java.util.List;

public class Formula {

    private String name;
    private String title;
    private String link;
    private String topic;
    private List<Term> terms;

    public Formula() {
    }

    public Formula(String name, String title, String link, List<Term> terms, String topic) {
        this.name = name;
        this.title = title;
        this.link = link;
        this.terms = terms;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Formula{" +
                "name='" + name + '\'' +
                ", terms=" + terms +
                '}';
    }
}
