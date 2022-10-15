package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import songranker.data.PlaylistJdbcRepo;
import songranker.models.Playlist;
import songranker.models.Result;

import java.util.List;

public class PlaylistService {

    @Autowired
    PlaylistJdbcRepo repository;

    public Result<List<Playlist>> getPlaylistsByAppUserId(int appUserId) {
        Result result = validateUser(appUserId);
        if (!result.isSuccess()) {
            return result;
        }

        List<Playlist> playlists = repository.getPlaylistsByAppUserId(appUserId);
        result.setPayload(playlists);
        return result;
    }

    private Result validateUser(int appUserId) {
        throw new UnsupportedOperationException();
    }
}
