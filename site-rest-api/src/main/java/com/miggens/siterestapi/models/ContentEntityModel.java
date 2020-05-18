package com.miggens.siterestapi.models;

import org.springframework.hateoas.EntityModel;

import java.util.List;

public class ContentEntityModel extends EntityModel<ContentEntityModel> {

    private String title;
    private String createdOn;
    private String snippet;
    private List<String> fullContentList;
    private List<String> imageLinksList;
    private List<String> tagsList;

    private String errorMessage;

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<String> getFullContentList() {
        return fullContentList;
    }

    public void setFullContentList(List<String> fullContentList) {
        this.fullContentList = fullContentList;
    }

    public List<String> getImageLinksList() {
        return imageLinksList;
    }

    public void setImageLinksList(List<String> imageLinksList) {
        this.imageLinksList = imageLinksList;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }
}
