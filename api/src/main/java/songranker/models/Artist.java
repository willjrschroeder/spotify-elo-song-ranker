package songranker.models;

import java.util.List;
import java.util.Objects;

public class Artist {
    private String artistUri;
    private String artistName;
    private String spotifyUrl;
    private String artistImageLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(artistUri, artist.artistUri) && Objects.equals(artistName, artist.artistName) && Objects.equals(spotifyUrl, artist.spotifyUrl) && Objects.equals(artistImageLink, artist.artistImageLink) && Objects.equals(genres, artist.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistUri, artistName, spotifyUrl, artistImageLink, genres);
    }

    private List<Genre> genres; // List of Genre models associated with the Artist

    public Artist(String artist_uri, String artist_name, String spotify_url, String artist_image_link, List<Genre> genres) {
        this.artistUri = artist_uri;
        this.artistName = artist_name;
        this.spotifyUrl = spotify_url;
        this.artistImageLink = artist_image_link;
        this.genres = genres;
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
