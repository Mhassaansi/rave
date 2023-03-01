package com.fictivestudios.ravebae.presentation.models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotifyTestModel {

    @SerializedName("interest")
    @Expose
    private String interest;
    @SerializedName("spotify_list")
    @Expose
    private List<Spotifyy> spotifyList = null;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public List<Spotifyy> getSpotifyList() {
        return spotifyList;
    }

    public void setSpotifyList(List<Spotifyy> spotifyList) {
        this.spotifyList = spotifyList;
    }

}

class Spotifyy {

    @SerializedName("songName")
    @Expose
    private String songName;
    @SerializedName("artistName")
    @Expose
    private String artistName;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}