package songranker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import songranker.data.TrackJdbcRepo;
import songranker.models.*;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackServiceTest {

    @Autowired
    TrackService service;

    @MockBean
    TrackJdbcRepo repository;

    private final Genre testGenre1 = new Genre("afrofuturism");
    private final Genre testGenre2 = new Genre("pop");
    private final List<Genre> testGenres1 = List.of(testGenre1, testGenre2);

    private final Album testAlbum1 = new Album("3Ks0eeH0GWpY4AU20D5HPD", "Gemini Rights", new java.sql.Date(2022, Calendar.AUGUST,15), "https://i.scdn.co/image/ab67616d0000b27368968350c2550e36d96344ee", "https://open.spotify.com/album/3Ks0eeH0GWpY4AU20D5HPD");
    private final List<Album> testAlbums1 = List.of(testAlbum1);

    private final Artist testArtist1 = new Artist("57vWImR43h4CaDao012Ofp", "Steve Lacy", "https://open.spotify.com/artist/57vWImR43h4CaDao012Ofp","https://i.scdn.co/image/ab6761610000e5eb09ac9d040c168d4e4f58eb42", testGenres1);
    private final List<Artist> testArtists1 = List.of(testArtist1);

    private final Track testTrack1 = new Track("4k6Uh1HXdhtusDW5y8Gbvy", 1,"Bad Habit", 1000, 0, 232006, 90,
            "https://open.spotify.com/track/4k6Uh1HXdhtusDW5y8Gbvy", "https://p.scdn.co/mp3-preview/b46cf3781e6cbe5bd01b8a81e333899c7f9cdfc5?cid=b055b73f53474f3e931fd58a080ca3cf",
            testArtists1, testAlbums1);

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
        when(repository.updateTrackEloScore(testTrack1)).thenReturn(true);

        Result result = service.updateTrackEloScore(testTrack1);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldUpdateNegativeTrackEloScore() {
        Track badTrack = testTrack1;
        badTrack.setEloScore(-10);

        when(repository.updateTrackEloScore(badTrack)).thenReturn(true);

        Result result = service.updateTrackEloScore(badTrack);

        assertFalse(result.isSuccess());
        assertEquals("[Elo score must be positive]", result.getMessages().toString());
    }


    @Test
    void shouldNotGiveSuccessOnDbFailure() {
        when(repository.updateTrackEloScore(testTrack1)).thenReturn(false);

        Result result = service.updateTrackEloScore(testTrack1);

        assertFalse(result.isSuccess());
        assertEquals("[Error writing ELO score to the database]", result.getMessages().toString());
    }


}