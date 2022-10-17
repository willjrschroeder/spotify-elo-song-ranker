package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.TrackJdbcRepo;
import songranker.models.Result;
import songranker.models.ResultType;
import songranker.models.Track;

import java.util.List;
@Service
public class TrackService {

    @Autowired
    TrackJdbcRepo repository;

    public Result<List<Track>> getTracksByPlaylistUri(String playlistUri) {
        Result result = new Result();

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
}
