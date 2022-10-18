package songranker.data;

import songranker.models.Track;

import java.util.List;

public interface TrackRepo {

    List<Track> getAllTracks(int appUserId);

    List<Track> getTracksByPlaylistUri(String playlistUri);

    boolean updateTrackEloScore(Track updatedTrack);

    List<Track> getTop10Tracks(int appUserId);
}
