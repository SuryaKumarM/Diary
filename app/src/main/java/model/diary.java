package model;

import com.google.firebase.Timestamp;

public class diary {
    private String about;
    private String story;
    private String imageUrl;
    private String userId;
    private String username;
    private Timestamp timeadded;
    public diary(){

    }

    public diary(String about, String story, String imageUrl, String userId, String username, Timestamp timeadded) {
        this.about = about;
        this.story = story;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.username = username;
        this.timeadded = timeadded;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        this.timeadded = timeadded;
    }
}
