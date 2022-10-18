package songranker.data;

import songranker.models.Track;

import java.util.List;

public interface TrackRepo {

    List<Track> getAllTracks(int appUserId);

    List<Track> getTracksByPlaylistUri(String playlistUri);

    List<Track> getTracksByAppUserId(int appUserId);

    boolean updateTrackEloScore(Track updatedTrack);
}
