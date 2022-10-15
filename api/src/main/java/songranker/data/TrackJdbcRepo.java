package songranker.data;

import songranker.models.Track;

import java.util.List;

public class TrackJdbcRepo implements TrackRepo{
    @Override
    public List<Track> getTracksByPlaylistUri(String playlistUri, int appUserId) {
        throw new UnsupportedOperationException();
    }
}
