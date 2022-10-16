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

}
