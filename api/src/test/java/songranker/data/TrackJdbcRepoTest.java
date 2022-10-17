package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import songranker.models.Track;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackJdbcRepoTest {

    @Autowired
    TrackJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    private final String testPlaylistUri = "38cfqZXcGK4KPtDrGUNMkI";

    @BeforeEach
    public void setup(){
        knownGoodState.set();
    }

    @Test
    void shouldGetTracksByPlaylistUri() {

        List<Track> toTest = repo.getTracksByPlaylistUri(testPlaylistUri);

        assertNotNull(toTest);

    }


}