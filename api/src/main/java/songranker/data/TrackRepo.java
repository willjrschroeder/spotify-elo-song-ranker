package songranker.data;

import songranker.models.Track;

import java.util.List;

public interface TrackRepo {
    List<Track> getTracksByPlaylistUri(String playlistUri, int appUserId);
}
