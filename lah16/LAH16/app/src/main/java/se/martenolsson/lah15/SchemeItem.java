package se.martenolsson.lah15;

import java.util.ArrayList;
import java.util.HashMap;

public class SchemeItem {
    private String stage = null;
    private String orgDate = null;
    private String date = null;
    private String artistId = null;
    private String artist = null;

    public String getStage() {
        return stage;
    }
    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getOrgDate() {
        return orgDate;
    }
    public void setOrgDate(String orgDate) {
        this.orgDate = orgDate;
    }

    public String getArtistId() {
        return artistId;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

}
