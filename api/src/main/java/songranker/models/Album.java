package songranker.models;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

public class Album {
    private String albumUri;
    private String albumName;
    private Date releaseDate;
    private String albumImageLink;
    private String spotifyUrl;

    public Album(String album_uri, String album_name, Date release_date, String album_image_link, String spotify_url) {
        this.albumUri = album_uri;
        this.albumName = album_name;
        this.releaseDate = release_date;
        this.albumImageLink = album_image_link;
        this.spotifyUrl = spotify_url;
    }

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
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
