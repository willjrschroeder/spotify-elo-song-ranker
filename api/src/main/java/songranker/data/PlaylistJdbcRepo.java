package songranker.data;

import org.springframework.stereotype.Repository;
import songranker.models.Playlist;

import java.util.List;


@Repository
public class PlaylistJdbcRepo implements PlaylistRepo {
    @Override
    public List<Playlist> getPlaylistsByAppUserId(int appUserId) {
        throw new UnsupportedOperationException();
    }
}
