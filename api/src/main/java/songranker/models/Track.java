package songranker.models;

import java.util.List;

public class Track {
    private String track_uri;
    private String title;
    private int eloScore;
    private int numOfMatchesPlayed; // number of times the song has been in an ELO match
    private int trackDuration; // stored in milliseconds
    private int popularityNum; // Spotify's popularity of the track. Based on number of recent streams
    private String spotifyUrl; // link to the full track on Spotify's service
    private String previewUrl; // link to a 30-second track preview in .mp3 format

    private List<Artist> artists; // list of all Artist models who are credited on the track.
    private List<Album> albums; // list of all Album models the track appears on

    public String getTrack_uri() {
        return track_uri;
    }

    public void setTrack_uri(String track_uri) {
        this.track_uri = track_uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEloScore() {
        return eloScore;
    }

    public void setEloScore(int eloScore) {
        this.eloScore = eloScore;
    }

    public int getNumOfMatchesPlayed() {
        return numOfMatchesPlayed;
    }

    public void setNumOfMatchesPlayed(int numOfMatchesPlayed) {
        this.numOfMatchesPlayed = numOfMatchesPlayed;
    }

    public int getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(int trackDuration) {
        this.trackDuration = trackDuration;
    }

    public int getPopularityNum() {
        return popularityNum;
    }

    public void setPopularityNum(int popularityNum) {
        this.popularityNum = popularityNum;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}