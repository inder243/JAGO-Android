package com.iapp.playdate;

public class childfriendname {

    private String name;
    private String id;
    private String guardian_id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getparent_id() {
        return guardian_id;
    }
    public void setparent_id(String name) {
        this.guardian_id = name;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
