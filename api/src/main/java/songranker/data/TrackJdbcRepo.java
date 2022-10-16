package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.models.Track;

import java.util.List;
@Repository
public class TrackJdbcRepo implements TrackRepo{

    @Autowired
    JdbcTemplate template;
    @Override
    public List<Track> getTracksByPlaylistUri(String playlistUri) {
        throw new UnsupportedOperationException();
    }
}
