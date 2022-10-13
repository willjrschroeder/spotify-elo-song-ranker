package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException{
        int genre_id = rs.getInt("genre_id");
        String genre_name = rs.getString("genre_name");

        return new Genre(genre_id, genre_name);
    }
}
