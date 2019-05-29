package com.cathy.mathsearch.model.base;

import java.util.regex.Pattern;

public class Request {

    private Pattern pattern;
    private String request;

    public Request(Pattern pattern, String request) {
        this.pattern = pattern;
        this.request = request;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
