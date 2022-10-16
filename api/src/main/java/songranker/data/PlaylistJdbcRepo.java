package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.data.mappers.PlaylistMapper;
import songranker.models.Playlist;

import java.util.List;


@Repository
public class PlaylistJdbcRepo implements PlaylistRepo {

    @Autowired
    JdbcTemplate template;
    @Override
    public List<Playlist> getPlaylistsByAppUserId(int appUserId) {
        final String sql = "select * from playlist where app_user_id = ?";

        return template.query(sql, new PlaylistMapper(), appUserId);
    }
}
