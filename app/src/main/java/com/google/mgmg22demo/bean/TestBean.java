package com.google.mgmg22demo.bean;

/**
 * Created by uniware on 2016/5/26.
 */
public class TestBean {
    private String GH_ID;
    private String NODE_NAME;

    public String getGH_ID() {
        return GH_ID;
    }

    public String getNODE_NAME() {
        return NODE_NAME;
    }

    public TestBean(String GH_ID, String NODE_NAME) {
        this.GH_ID = GH_ID;
        this.NODE_NAME = NODE_NAME;
    }
}
