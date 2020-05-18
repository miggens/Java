package com.miggens.siterestapi.models;

import java.util.List;

public class Content extends BaseModel {

    private String title;
    private List<String> fullContentList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getFullContentList() {
        return fullContentList;
    }

    public void setFullContentList(List<String> fullContentList) {
        this.fullContentList = fullContentList;
    }
}
