package com.tripshots.model;

public class Post {

    String title, description, travel_story, image_url;

    public Post(String title, String description, String travel_story, String image_url) {
        this.title = title;
        this.description = description;
        this.travel_story = travel_story;
        this.image_url = image_url;
    }



    public Post(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTravel_story() {
        return travel_story;
    }

    public void setTravel_story(String travel_story) {
        this.travel_story = travel_story;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
