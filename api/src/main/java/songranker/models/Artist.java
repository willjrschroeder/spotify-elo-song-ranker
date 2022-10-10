package songranker.models;

public class Artist {
    private String artistUri;
    private String artistName;
    private String spotifyUrl;
    private String artistImageLink;

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
