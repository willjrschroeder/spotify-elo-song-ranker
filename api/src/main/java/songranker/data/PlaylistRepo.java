package songranker.data;

import songranker.models.Playlist;

import java.util.List;

public interface PlaylistRepo {
    List<Playlist> getPlaylistsByAppUserId(int appUserId);
}
