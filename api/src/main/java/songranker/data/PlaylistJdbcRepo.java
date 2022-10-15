package songranker.data;

import songranker.models.Playlist;

import java.util.List;

public class PlaylistJdbcRepo implements PlaylistRepo {
    @Override
    public List<Playlist> getPlaylistsByAppUserId(int appUserId) {
        throw new UnsupportedOperationException();
    }
}
