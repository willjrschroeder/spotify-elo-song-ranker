package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.TrackJdbcRepo;
import songranker.models.*;

import java.util.List;
@Service
public class TrackService {

    @Autowired
    TrackJdbcRepo repository;

    public Result<List<Track>> getTracksByPlaylistUri(String playlistUri) {
        Result<List<Track>> result = new Result<>();

        if(playlistUri == null || playlistUri.isBlank()) {
            result.addMessage("Playlist URI is required", ResultType.INVALID);
            return result;
        }

        List<Track> tracks = repository.getTracksByPlaylistUri(playlistUri);
        result.setPayload(tracks);
        return result;
    }

    public Result updateTrackEloScore(Track updatedTrack) {
        Result result = new Result();

        if(updatedTrack.getEloScore() < 0) {
            result.addMessage("Elo score must be positive", ResultType.INVALID);
            return result;
        }

        if(updatedTrack.getTrack_uri() == null || updatedTrack.getTrack_uri().isBlank()) {
            result.addMessage("Track URI is required", ResultType.INVALID);
            return result;
        }

        if(updatedTrack.getAppUserId() <= 0) {
            result.addMessage("User ID is required", ResultType.INVALID);
            return result;
        }

        boolean success = repository.updateTrackEloScore(updatedTrack);

        if (!success) {
            result.addMessage("Error writing ELO score to the database", ResultType.INVALID);
        }

        return result;
    }

    public Result<List<Track>> getTracksByUser(int appUserId) {
        Result<List<Track>> result = new Result<List<Track>>();

        if (appUserId <= 0) {
            result.addMessage("User Id is required", ResultType.INVALID);
            return result;
        }

        List<Track> tracks = repository.getAllTracks(appUserId);
        result.setPayload(tracks);
        return result;
    }

    public Result<List<Artist>> getTop10Artists(int appUserId) {
        Result<List<Artist>> result = new Result<List<Artist>>();

        if (appUserId <=0) {
            result.addMessage("User Id is required", ResultType.INVALID);
            return result;
        }

        List<Artist> artists = repository.getTop10Artists(appUserId);
        result.setPayload(artists);
        return result;
    }

    public Result<List<Genre>> getTop10Genres(int appUserId) {
        Result<List<Genre>> result = new Result<List<Genre>>();

        if (appUserId <=0) {
            result.addMessage("User Id is required", ResultType.INVALID);
            return result;
        }

        List<Genre> genre = repository.getTop10Genres(appUserId);
        result.setPayload(genre);
        return result;
    }

    public Result<List<Track>> getTop10Tracks(int appUserId) {
        Result<List<Track>> result = new Result<List<Track>>();

        if (appUserId <= 0) {
            result.addMessage("User Id is required", ResultType.INVALID);
            return result;
        }

        List<Track> tracks = repository.getTop10Tracks(appUserId);
        result.setPayload(tracks);
        return result;
    }
}
