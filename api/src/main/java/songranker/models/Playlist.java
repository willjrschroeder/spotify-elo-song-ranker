package songranker.models;

public class Playlist {
    private String playlistUri;
    private String playlistName;
    private String description;
    private String playlistUrl;
    private String playlistImageLink;
    private int appUserId;

    public Playlist(String playlistUri, String playlistName, String description, String playlistUrl, String playlistImageLink, int appUserId) {
        this.playlistUri = playlistUri;
        this.playlistName = playlistName;
        this.description = description;
        this.playlistUrl = playlistUrl;
        this.playlistImageLink = playlistImageLink;
        this.appUserId = appUserId;
    }

    public Playlist(){};

    //TODO: Figure out if we should have an array of Track in this model. When we getTracksByPlaylist, we will be
    // passing a playlist_uri down. We'll use this to search the DB with SQL joins. Don't think we need a Track array
    // in this model to accomplish that.


    public String getPlaylistUri() {
        return playlistUri;
    }

    public void setPlaylistUri(String playlistUri) {
        this.playlistUri = playlistUri;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaylistUrl() {
        return playlistUrl;
    }

    public void setPlaylistUrl(String playlistUrl) {
        this.playlistUrl = playlistUrl;
    }

    public String getPlaylistImageLink() {
        return playlistImageLink;
    }

    public void setPlaylistImageLink(String playlistImageLink) {
        this.playlistImageLink = playlistImageLink;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
