package utils;

import android.app.Application;

public class diaryapi extends Application {

    private String username;
    private String id;
    private String phone;
    private static diaryapi instance;
    public static diaryapi getInstance()
    {
        if(instance==null){
            instance=new diaryapi();
        }
        return instance;
    }
    public diaryapi(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
