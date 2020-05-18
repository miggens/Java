package com.miggens.sitebackend.model;

import java.util.List;

public class ArticleContent {

    private String title;
    private String fullContent;
    private String createdOn;
    private String imageLinks;
    private String tags;
    private List<String> fullContentList;
    private List<String> imageLinksList;
    private List<String> tagsList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(String imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append("Article Contents:\n")
                .append("\tTitle: "+this.title+"\n")
                .append("\tTags: "+this.tags+"\n")
                .append("\tImage Links: "+this.imageLinks+"\n")
                .append("\tCreated On: "+this.createdOn+"\n")
                .append("\tFull Content: "+this.fullContent+"\n");

        return sb.toString();
    }
}
