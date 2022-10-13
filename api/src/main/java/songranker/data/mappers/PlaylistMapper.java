package songranker.data.mappers;

import songranker.models.Playlist;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistMapper implements RowMapper<Playlist> {

    @Override
    public Playlist mapRow(ResultSet rs, int rowNum) throws SQLException{
        String playlist_uri = rs.getString("playlist_uri");
        String playlist_name = rs.getString("playlist_name");
        String description = rs.getString("description");
        String playlist_url = rs.getString("playlist_url");
        String playlist_image_link = rs.getString("playlist_image_link");
        int app_user_id = rs.getInt("app_user_id");


        return new Playlist(playlist_uri, playlist_name, description, playlist_url, playlist_image_link, app_user_id);
    }
}
