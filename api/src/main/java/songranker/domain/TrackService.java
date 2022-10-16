package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.TrackJdbcRepo;
import songranker.models.Result;
import songranker.models.Track;

import java.util.List;
@Service
public class TrackService {

    @Autowired
    TrackJdbcRepo repository;

    public Result<List<Track>> getTracksByPlaylistUri(String playlistUri, int appUserId) {
        Result result = validateUser(appUserId);
        if (!result.isSuccess()) {
            return result;
        }

        //TODO: check if uri is null or blank

        List<Track> tracks = repository.getTracksByPlaylistUri(playlistUri, appUserId);
        result.setPayload(tracks);
        return result;
    }

    private Result validateUser(int appUserId) {
        throw new UnsupportedOperationException();
    }
}
