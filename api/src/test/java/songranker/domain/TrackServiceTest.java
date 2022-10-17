package songranker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import songranker.data.TrackJdbcRepo;
import songranker.models.Result;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackServiceTest {

    @Autowired
    TrackService service;

    @MockBean
    TrackJdbcRepo repository;

    @Test
    void shouldNotGetTracksWithNullOrBlankPlaylistUri() {
        Result result = service.getTracksByPlaylistUri(null);

        assertFalse(result.isSuccess());
        assertEquals("[Playlist URI is required]", result.getMessages().toString());

        Result result2 = service.getTracksByPlaylistUri("");

        assertFalse(result2.isSuccess());
        assertEquals("[Playlist URI is required]", result2.getMessages().toString());
    }

    @Test
    void shouldUpdateTrackEloScore() {
        when(repository.updateTrackEloScore(10)).thenReturn(true);

        Result result = service.updateTrackEloScore(10);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldUpdateNegativeTrackEloScore() {
        when(repository.updateTrackEloScore(-10)).thenReturn(true);

        Result result = service.updateTrackEloScore(-10);

        assertFalse(result.isSuccess());
        assertEquals("[Elo score must be positive]", result.getMessages().toString());
    }

    @Test
    void shouldUpdateNullTrackEloScore() {
        Result result = service.updateTrackEloScore(null);

        assertFalse(result.isSuccess());
        assertEquals("[Elo score must be included]", result.getMessages().toString());
    }

    @Test
    void shouldNotGiveSuccessOnDbFailure() {
        when(repository.updateTrackEloScore(10)).thenReturn(false);

        Result result = service.updateTrackEloScore(10);

        assertFalse(result.isSuccess());
        assertEquals("[Error writing ELO score to the database]", result.getMessages().toString());
    }
}