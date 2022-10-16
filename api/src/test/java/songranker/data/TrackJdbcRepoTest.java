package songranker.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TrackJdbcRepoTest {

    @Autowired
    TrackJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    private final String testPlaylistUri = "";

    @Test
    void shouldGetTracksByPlaylistUri() {
    }
}