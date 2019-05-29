package com.cathy.mathsearch.model.base;

public class Range {

    private int start;
    private int end;
    private int sentence;

    public Range() {
    }

    public Range(int start, int end, int sentence) {
        this.start = start;
        this.end = end;
        this.sentence = sentence;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSentence() {
        return sentence;
    }

    public void setSentence(int sentence) {
        this.sentence = sentence;
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                ", sentence=" + sentence +
                '}';
    }
}
