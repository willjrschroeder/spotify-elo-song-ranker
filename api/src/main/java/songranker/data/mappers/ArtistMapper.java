package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.Artist;
import songranker.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ArtistMapper implements RowMapper<Artist> {

    List<Genre> genres;

    public ArtistMapper(List<Genre> genres){this.genres = genres;}
    public ArtistMapper(){

    }

    @Override
    public Artist mapRow(ResultSet rs, int rowNum) throws SQLException{
        String artist_uri = rs.getString("artist_uri");
        String artist_name = rs.getString("artist_name");
        String spotify_url = rs.getString("spotify_url");
        String artist_image_link = rs.getString("artist_image_link");

        return new Artist(artist_uri, artist_name, spotify_url, artist_image_link, genres);
    }
}
