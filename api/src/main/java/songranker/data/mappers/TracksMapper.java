package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.Album;
import songranker.models.Artist;
import songranker.models.Track;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TracksMapper implements RowMapper<Track> {

    List<Artist> artists;

    List<Album> albums;

    public TracksMapper(List<Artist> artists, List<Album> albums){
        this.artists = artists;
        this.albums = albums;
    }



    @Override
    public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
        String track_uri = rs.getString("track_uri");
        String title = rs.getString("title");
        int elo_score = rs.getInt("elo_score");
        int num_of_matches_played = rs.getInt("num_of_matches_played");
        int track_duration = rs.getInt("track_duration");
        int popularity_num = rs.getInt("popularity_num");
        String spotify_url = rs.getString("spotify_url");
        String preview_url = rs.getString("preview_url");

        return new Track(track_uri, title, elo_score, num_of_matches_played, track_duration, popularity_num, spotify_url, preview_url, artists, albums);
    }
}
