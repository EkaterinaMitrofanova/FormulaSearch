package com.cathy.mathsearch.model.base;

public class Term {

    private String name;
    private String symbol;
    private String text;
    private int range;

    public Term(String name, String symbol, String text, int range) {
        this.name = name;
        this.symbol = symbol;
        this.text = text;
        this.range = range;
    }

    public Term() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "Term{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", text='" + text + '\'' +
                ", range=" + range +
                '}';
    }
}
