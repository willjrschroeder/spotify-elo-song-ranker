package songranker.models;

import java.time.LocalDate;

public class Album {
    private String albumUri;
    private String albumName;
    private LocalDate releaseDate;
    private String albumImageLink;
    private String spotifyUrl;

    public String getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAlbumImageLink() {
        return albumImageLink;
    }

    public void setAlbumImageLink(String albumImageLink) {
        this.albumImageLink = albumImageLink;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }
}
