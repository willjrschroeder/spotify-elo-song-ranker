package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.Album;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AlbumMapper implements RowMapper<Album> {

    @Override
    public Album mapRow(ResultSet rs, int rowNum) throws SQLException{
        String album_uri = rs.getString("album_uri");
        String album_name = rs.getString("album_name");
        Date release_date = rs.getDate("release_date");
        String album_image_link = rs.getString("album_image_link");
        String spotify_url = rs.getString("spotify_url");

        return new Album(album_uri, album_name, release_date, album_image_link, spotify_url);
    }


}
