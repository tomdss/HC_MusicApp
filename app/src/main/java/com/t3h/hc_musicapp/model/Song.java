package com.t3h.hc_musicapp.model;

public class Song {
    private String name,path,album,singer;
    private int duration;

    public Song(String name, String path, String album, String singer, int duration) {
        this.name = name;
        this.path = path;
        this.album = album;
        this.singer = singer;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getSinger() {
        return singer;
    }

    public int getDuration() {
        return duration;
    }
}
