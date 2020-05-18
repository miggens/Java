package com.miggens.siterestapi.models;

import org.springframework.hateoas.EntityModel;

import java.util.List;

public class ContentMetadataEntityModel extends EntityModel<ContentMetadataEntityModel> {

    private String title;
    private String createdOn;
    private String snippet;
    private List<String> imageLinksList;
    private List<String> tagsList;

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

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
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
