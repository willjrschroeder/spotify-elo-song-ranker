package songranker.models;

import java.util.List;

public class SpotifyData {

    private Playlist playlist;

    private List<Track> tracks;

    public SpotifyData(Playlist playlist, List<Track> tracks) {
        this.playlist = playlist;
        this.tracks = tracks;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }


}
