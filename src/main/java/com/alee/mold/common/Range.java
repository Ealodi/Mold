package com.alee.mold.common;

public class Range <T extends Comparable<T>> {
    private T st = null;
    private T ed = null;
    public Range(){};
    public Range(T st,T ed){
        this.st = st;
        this.ed = ed;
    }

    public T getSt() {
        return st;
    }

    public void setSt(T st) {
        this.st = st;
    }

    public T getEd() {
        return ed;
    }

    public void setEd(T ed) {
        this.ed = ed;
    }
}
