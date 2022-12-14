package songranker.models;

import java.util.List;

public class Artist {
    private String artistUri;
    private String artistName;
    private String spotifyUrl;
    private String artistImageLink;

    private List<Integer> eloScore;

    private List<Genre> genres; // List of Genre models associated with the Artist

    public Artist(String artist_uri, String artist_name, String spotify_url, String artist_image_link, List<Integer> eloScore, List<Genre> genres) {
        this.artistUri = artist_uri;
        this.artistName = artist_name;
        this.spotifyUrl = spotify_url;
        this.artistImageLink = artist_image_link;
        this.eloScore = eloScore;
        this.genres = genres;
    }

    public List<Integer> getEloScore() {
        return eloScore;
    }

    public void setEloScore(List<Integer> eloScore) {
        this.eloScore = eloScore;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getArtistUri() {
        return artistUri;
    }

    public void setArtistUri(String artistUri) {
        this.artistUri = artistUri;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getArtistImageLink() {
        return artistImageLink;
    }

    public void setArtistImageLink(String artistImageLink) {
        this.artistImageLink = artistImageLink;
    }
}
