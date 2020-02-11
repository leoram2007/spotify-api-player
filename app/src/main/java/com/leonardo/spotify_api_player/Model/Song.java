package com.leonardo.spotify_api_player.Model;

/**
 * Model class to Song with 2 attributes
 * */
public class Song {

    private String id;
    private String name;

    public Song(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
