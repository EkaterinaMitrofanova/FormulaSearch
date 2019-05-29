package com.cathy.mathsearch.model.base;

public class MathExpression {

    private String name;
    private int range;
    private String topic;
    private String link;
    private String title;

    public MathExpression() {
    }

    public MathExpression(String name, int range, String topic) {
        this.name = name;
        this.range = range;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
